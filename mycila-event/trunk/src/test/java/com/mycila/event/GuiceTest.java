package com.mycila.event;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.mycila.event.annotation.Publish;
import com.mycila.event.annotation.Subscribe;
import com.mycila.event.annotation.Veto;
import com.mycila.event.guice.MycilaEventGuice;
import com.mycila.event.guice.MycilaEventGuiceModule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@RunWith(JUnit4.class)
public final class GuiceTest implements Module {

    Publisher<String> publisher;

    @Override
    public void configure(Binder binder) {
        binder.bind(GuiceTest.class).toInstance(this);
        MycilaEventGuice.bindPublisher(binder, MyCustomPublisher.class).in(Singleton.class);
    }

    @Test
    public void test() throws Exception {
        Injector injector = Guice.createInjector(this, new MycilaEventGuiceModule() {
            @Override
            @Provides
            @Singleton
            protected Dispatcher dispatcher(ErrorHandler errorHandler) {
                return Dispatchers.synchronousUnsafe(errorHandler);
            }
        });
        injector.getInstance(MyCustomPublisher.class).send("A", "cut", "message", "containing", "bad words");
        injector.getInstance(GuiceTest.class).publisher.publish("Hello world !");
    }

    @Subscribe(topics = "a/topic/path", eventType = String.class)
    void subscribe(Event<String> event) {
        System.out.println("Got: " + event);
    }

    @Veto(topics = "a/topic/path", eventType = String.class)
    void vetoer(VetoableEvent<String> vetoableEvent) {
        if (vetoableEvent.event().source().contains("bad words"))
            vetoableEvent.veto();
    }

    @Publish(topics = "a/topic/path")
    void publisher(Publisher<String> publisher) {
        System.out.println("Publisher injected");
        publisher.publish("Hello from publisher !");
        this.publisher = publisher;
    }

    static interface MyCustomPublisher {
        @Publish(topics = "a/topic/path")
        void send(String... messages);
    }
}
