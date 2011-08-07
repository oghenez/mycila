import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Stage;
import com.mycila.inject.jsr250.Jsr250;
import com.mycila.inject.jsr250.Jsr250Injector;

public class MycilaDestroyOrderTest {

    @Singleton
    public static class Repository {
        private boolean closed = false;

        @PreDestroy
        public void close() {
            closed = true;
        }

        public void writeApplicationStatus(String message) {
            if (closed) {
                throw new IllegalStateException("Repository closed!");
            }
        }
    }

    @Singleton
    public static class Service {
        @Inject
        private Repository repository;

        @PreDestroy
        public void destroy() {
            repository.writeApplicationStatus("Closing application");
        }
    }

    @Test
    public void testDestroyOrder() {

        // This test fails because Mycila destroys singletons in the order they are bound

        Jsr250Injector injector = Jsr250.createInjector(Stage.PRODUCTION, new AbstractModule() {
            @Override
            protected void configure() {
                bind(Repository.class);
                bind(Service.class);
            }
        });

        injector.destroy();
    }
}
