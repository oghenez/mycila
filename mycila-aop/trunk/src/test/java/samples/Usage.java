package samples;

import com.mycila.aop.ProxyConfig;
import org.aopalliance.intercept.ConstructorInterceptor;
import org.aopalliance.intercept.ConstructorInvocation;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class Usage {
    public static void main(String[] args) {

        MyIf myIf = (MyIf) ProxyConfig.create()
                .addConstructorInterceptor(new ConstructorInterceptor() {
                    @Override
                    public Object construct(ConstructorInvocation invocation) throws Throwable {
                        System.out.println("ctor");
                        return invocation.proceed();
                    }
                })
                .addMethodInterceptor(new MethodInterceptor() {
                    @Override
                    public Object invoke(MethodInvocation invocation) throws Throwable {
                        System.out.println("add m1");
                        return "m1 " + invocation.proceed();
                    }
                })
                .addMethodInterceptor(new MethodInterceptor() {
                    @Override
                    public Object invoke(MethodInvocation invocation) throws Throwable {
                        System.out.println("add m2");
                        return "m2 " + invocation.proceed();
                    }
                })
                .addInterface(MyIf.class)
                .buildProxy()
                .getProxyConstructor()
                .newProxyInstance();

        System.out.println(myIf.intercept());

    }

    static interface MyIf {
        String intercept();
    }

    static class MyClass {
        String intercept() {
            return "real";
        }
    }

    static abstract class MyAbstract {
        abstract String intercept();
    }
}
