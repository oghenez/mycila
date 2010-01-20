/**
 * Copyright (C) 2009 Mathieu Carbou <mathieu.carbou@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mycila.event.spi;

import com.mycila.event.api.Dispatcher;
import com.mycila.event.api.DispatcherException;
import com.mycila.event.api.ErrorHandlers;
import com.mycila.event.api.Event;
import com.mycila.event.api.Subscriber;
import com.mycila.event.api.annotation.AnnotationProcessor;
import com.mycila.event.api.annotation.Answers;
import com.mycila.event.api.annotation.Request;
import com.mycila.event.api.annotation.Subscribe;
import com.mycila.event.api.message.MessageRequest;
import com.mycila.event.api.message.MessageResponse;
import com.mycila.event.api.message.Messages;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.FileNotFoundException;
import java.util.concurrent.TimeUnit;

import static com.mycila.event.api.topic.Topics.*;
import static org.junit.Assert.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@RunWith(JUnit4.class)
public final class ComTest {

    static boolean throwExcepiton = false;

    @Test
    public void test_args() throws Exception {
        Dispatcher dispatcher = Dispatchers.synchronousSafe(ErrorHandlers.rethrow());
        AnnotationProcessor processor = AnnotationProcessors.create(dispatcher);

        DU du = processor.proxy(DU.class);

        assertEquals(30, du.mult(5, 6));
        //assertEquals(15, du.add(1, 2, 3, 4, 5));
    }

    @Test
    public void test() throws Exception {
        Dispatcher dispatcher = Dispatchers.synchronousSafe(ErrorHandlers.rethrow());

        dispatcher.subscribe(only("system/df"), MessageResponse.class, new Subscriber<MessageResponse<Integer>>() {
            public void onEvent(Event<MessageResponse<Integer>> event) throws Exception {
                String folder = (String) event.getSource().getParameters()[0];
                System.out.println("df request on folder " + folder);
                // call df -h <folder>
                if ("inexisting".equals(folder))
                    event.getSource().replyError(new FileNotFoundException("df did not found folder " + folder));
                else
                    event.getSource().reply(45);
            }
        });

        // through annotations
        AnnotationProcessor processor = AnnotationProcessors.create(dispatcher);
        DU du = processor.proxy(DU.class);
        DU2 du2 = processor.proxy(DU2.class);

        System.out.println(Integer.toHexString(du.hashCode()));
        System.out.println(Integer.toHexString(du2.hashCode()));
        System.out.println(du);
        System.out.println(du2);

        assertEquals(40, du.getSize("root").intValue());
        try {
            du.getSize("notFound");
            fail();
        } catch (Exception e) {
            assertEquals("java.io.FileNotFoundException: du did not found folder notFound", e.getMessage());
        }

        assertEquals(40, du2.getSize("root").intValue());
        try {
            du2.getSize("notFound");
            fail();
        } catch (Exception e) {
            assertEquals("java.io.FileNotFoundException: du did not found folder notFound", e.getMessage());
        }

        assertEquals(111, du.rm("root"));
        try {
            du.rm("err");
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof DispatcherException);
            assertTrue(e.getCause() instanceof FileNotFoundException);
            assertEquals("java.io.FileNotFoundException: rm did not found folder err", e.getMessage());
        }

        assertEquals(222, du.rm());
        try {
            throwExcepiton = true;
            du.rm();
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof DispatcherException);
            assertTrue(e.getCause() instanceof FileNotFoundException);
            assertEquals("java.io.FileNotFoundException: rm2 did not found folder", e.getMessage());
        }

        // manually
        MessageRequest<Integer> req1 = Messages.createRequest("home");
        MessageRequest<Integer> req2 = Messages.createRequest("inexisting");
        dispatcher.publish(topic("system/df"), req1);
        dispatcher.publish(topic("system/df"), req2);

        assertEquals(45, req1.getResponse(5, TimeUnit.SECONDS).intValue());
        try {
            req2.getResponse();
            fail();
        } catch (Exception e) {
            assertEquals("java.io.FileNotFoundException: df did not found folder inexisting", e.getMessage());
        }
    }

    static abstract class DU {
        @Request(topic = "system/du", timeout = 5, unit = TimeUnit.SECONDS)
        abstract Integer getSize(String folder);

        @Subscribe(topics = "system/du", eventType = MessageResponse.class)
        void duRequest(Event<MessageResponse<Integer>> event) {
            String folder = (String) event.getSource().getParameters()[0];
            System.out.println("du request on folder " + folder);
            // call du -h <folder>
            if ("notFound".equals(folder))
                event.getSource().replyError(new FileNotFoundException("du did not found folder " + folder));
            else
                event.getSource().reply(40);
        }

        @Request(topic = "system/rm")
        abstract int rm(String folder);

        @Answers(topics = "system/rm")
        int rmRequest(String folder) throws FileNotFoundException {
            System.out.println("rm request on folder " + folder);
            if ("err".equals(folder))
                throw new FileNotFoundException("rm did not found folder " + folder);
            else return 111;
        }

        @Request(topic = "system/rm2")
        abstract int rm();

        @Answers(topics = "system/rm2")
        int rmRequest() throws FileNotFoundException {
            System.out.println("rm2 request on folder");
            if (throwExcepiton)
                throw new FileNotFoundException("rm2 did not found folder");
            else return 222;
        }

        @Request(topic = "system/mult")
        abstract int mult(int p1, int p2);

        @Answers(topics = "system/mult")
        int multRequest(int p1, int p2) {
            return p1 * p2;
        }

        @Request(topic = "system/add")
        abstract int add(int... p);

        @Answers(topics = "system/add")
        int addRequest(int... p) {
            int c = 0;
            for (int i : p) c += i;
            return c;
        }
    }

    interface DU2 {
        @Request(topic = "system/du", timeout = 5, unit = TimeUnit.SECONDS)
        Integer getSize(String folder);
    }

}
