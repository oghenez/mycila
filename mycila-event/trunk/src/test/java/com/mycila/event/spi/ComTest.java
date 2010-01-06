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
import com.mycila.event.api.ErrorHandlers;
import com.mycila.event.api.Event;
import com.mycila.event.api.Subscriber;
import com.mycila.event.api.annotation.AnnotationProcessor;
import com.mycila.event.api.annotation.Request;
import com.mycila.event.api.annotation.Subscribe;
import com.mycila.event.api.message.MessageRequest;
import com.mycila.event.api.message.MessageResponse;
import com.mycila.event.api.message.Messages;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.FileNotFoundException;

import static com.mycila.event.api.topic.Topics.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@RunWith(JUnit4.class)
public final class ComTest {

    @Test
    public void test_subscribe_strong() throws Exception {
        Dispatcher dispatcher = Dispatchers.asynchronousUnsafe(ErrorHandlers.rethrow());

        dispatcher.subscribe(only("system/df"), MessageResponse.class, new Subscriber<MessageResponse<String, Integer>>() {
            public void onEvent(Event<MessageResponse<String, Integer>> event) throws Exception {
                String folder = event.getSource().getParameter();
                System.out.println("df request on folder " + folder);
                // call df -h <folder>
                if ("inexisting".equals(folder))
                    event.getSource().replyError(new FileNotFoundException("df did not found folder: " + folder));
                else
                    event.getSource().reply(45);
            }
        });

        // through annotations
        AnnotationProcessor processor = AnnotationProcessors.create(dispatcher);
        DF df = processor.proxy(DF.class);

        System.out.println(df.getSize("root"));
        System.out.println(df.getSize("notFound"));

        // manually
        MessageRequest<Integer> req1 = Messages.createRequest("home");
        MessageRequest<Integer> req2 = Messages.createRequest("inexisting");
        dispatcher.publish(topic("system/df"), req1);
        dispatcher.publish(topic("system/df"), req2);

        System.out.println(req1.getResponse());
        System.out.println(req2.getResponse());
    }

    static abstract class DF {
        @Request(topic = "system/du")
        abstract Integer getSize(String folder);

        @Subscribe(topics = "system/du", eventType = MessageResponse.class)
        void duRequest(Event<MessageResponse<String, Integer>> event) {
            String folder = event.getSource().getParameter();
            System.out.println("du request on folder " + folder);
            // call du -h <folder>
            if ("notFound".equals(folder))
                event.getSource().replyError(new FileNotFoundException("du did not found folder: " + folder));
            else
                event.getSource().reply(40);
        }
    }

}
