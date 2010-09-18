package com.mycila.event.internal;

import com.mycila.event.Dispatcher;
import com.mycila.event.Topic;
import com.mycila.event.annotation.Multiple;
import com.mycila.event.annotation.Publish;
import com.mycila.event.annotation.Request;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static com.mycila.event.internal.Ensure.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class PublisherInterceptor implements MethodInterceptor {
    private final Map<Signature, Publisher<Object>> publisherCache = new HashMap<Signature, Publisher<Object>>();
    private final Map<Signature, Requestor<Object[], Object>> requestorCache = new HashMap<Signature, Requestor<Object[], Object>>();
    private final Object delegate;

    public PublisherInterceptor(Dispatcher dispatcher, final Class<?> c) {
        Iterable<Method> allMethods = ClassUtils.getAllDeclaredMethods(c, false);
        // find publishers
        for (Method method : ClassUtils.filterAnnotatedMethods(allMethods, Publish.class)) {
            hasSomeArgs(method);
            Publish annotation = method.getAnnotation(Publish.class);
            Publisher<Object> publisher = Publishers.createPublisher(dispatcher, Topic.topics(annotation.topics()));
            publisherCache.put(new Signature(method), publisher);
        }
        // find requestors
        for (Method method : ClassUtils.filterAnnotatedMethods(allMethods, Request.class)) {
            Request annotation = method.getAnnotation(Request.class);
            Requestor<Object[], Object> requestor = Publishers.createRequestor(dispatcher, Topic.topic(annotation.topic()), annotation.timeout(), annotation.unit());
            requestorCache.put(new Signature(method), requestor);
        }
        delegate = !c.isInterface() ? null : new Object() {
            @Override
            public String toString() {
                return c.getName() + "$$byMycila@" + Integer.toHexString(hashCode());
            }
        };
    }

    @SuppressWarnings({"unchecked"})
    public Object invoke(MethodInvocation invocation) throws Throwable {
        MethodSignature methodSignature = MethodSignature.of(invocation.getMethod());
        Publisher<Object> publisher = publisherCache.get(methodSignature);
        if (publisher != null)
            return handlePublishing(publisher, invocation);
        Requestor<Object[], Object> requestor = requestorCache.get(methodSignature);
        if (requestor != null)
            return requestor.request(invocation.getArguments());
        return delegate == null ?
                invocation.proceed() :
                invocation.getMethod().invoke(delegate, invocation.getArguments());
    }

    private static Object handlePublishing(Publisher<Object> publisher, MethodInvocation invocation) {
        boolean requiresSplit = invocation.getMethod().isAnnotationPresent(Multiple.class);
        for (Object arg : invocation.getArguments()) {
            if (!requiresSplit)
                publisher.publish(arg);
            else if (arg.getClass().isArray())
                for (Object event : (Object[]) arg)
                    publisher.publish(event);
            else if (arg instanceof Iterable)
                for (Object event : (Iterable) arg)
                    publisher.publish(event);
            else
                publisher.publish(arg);
        }
        return null;
    }
}
