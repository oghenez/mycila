package com.mycila.event.impl;

import com.mycila.event.api.util.ref.Reachability;
import com.mycila.event.api.util.ref.Referencable;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@RunWith(JUnit4.class)
public final class IdentityRefIterableTest {

    @Test
    public void test() {
        IdentityRefIterable<Referencable> list = new IdentityRefIterable<Referencable>();
        for (int i = 0; i < 20; i++) {
            list.add(new WeakObject());
            list.add(new HardObject());
        }
        System.gc();
        System.gc();
        System.gc();
        int count = 0;
        for (Referencable referencable : list) {
            System.out.println(referencable);
            count++;
        }
        assertEquals(count, 20);
        assertEquals(list.size(), 20);
    }

    private static class WeakObject implements Referencable {
        static int count = 0;
        final int c;
        private WeakObject() {
            c = count++;
        }

        @Override
        public Reachability reachability() {
            return Reachability.WEAK;
        }

        @Override
        public String toString() {
            return "WeakObject-" + c;
        }
    }

    private static class HardObject implements Referencable {
        static int count = 0;
        final int c;
        private HardObject() {
            c = count++;
        }

        @Override
        public Reachability reachability() {
            return Reachability.HARD;
        }

        @Override
        public String toString() {
            return "HardObject-" + c;
        }
    }
}
