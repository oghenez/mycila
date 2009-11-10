package com.mycila.event.api;

import com.mycila.event.api.annotation.Reference;
import com.mycila.event.api.ref.Reachability;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@RunWith(JUnit4.class)
public final class ReachabilityTest {

    @Test
    public void test() throws Exception {
        assertEquals(Reachability.of(new Object()), Reachability.HARD);
        assertEquals(Reachability.of(new Subscriber() {
            @Override
            public void onEvent(Event event) throws Exception {
            }
        }), Reachability.HARD);
        assertEquals(Reachability.of(new S()), Reachability.WEAK);
    }

    @Reference(Reachability.WEAK)
    private static class S implements Subscriber {
        @Override
        public void onEvent(Event event) throws Exception {
        }
    }
}
