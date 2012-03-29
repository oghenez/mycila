/**
 * Copyright (C) 2010 Mycila <mathieu.carbou@gmail.com>
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

package com.mycila.jms.test;


import com.mycila.jms.JMSClient
import com.mycila.jms.SimpleClient
import com.mycila.jms.tool.LightBroker
import com.mycila.junit.concurrent.ConcurrentJunitRunner
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import java.util.concurrent.atomic.AtomicInteger
import javax.jms.ExceptionListener
import javax.jms.JMSException
import javax.swing.JButton
import org.apache.activemq.ActiveMQConnectionFactory
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertTrue
import static org.junit.Assert.fail

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@RunWith(ConcurrentJunitRunner)
class JMSClientTest {

    static AtomicInteger counts = new AtomicInteger(0)
    static LightBroker broker = new LightBroker(JMSClientTest.simpleName, "rendezvous://" + JMSClientTest.simpleName)

    JMSClient client1 = new SimpleClient(
            new ActiveMQConnectionFactory("discovery:(rendezvous://" + JMSClientTest.simpleName + ")"),
            'client-' + counts.incrementAndGet(),
            {JMSException exception -> exception.printStackTrace()} as ExceptionListener)

    JMSClient client2 = new SimpleClient(
            new ActiveMQConnectionFactory("discovery:(rendezvous://" + JMSClientTest.simpleName + ")"),
            'client-' + counts.incrementAndGet(),
            {JMSException exception -> exception.printStackTrace()} as ExceptionListener)

    String dest = UUID.randomUUID().toString()

    @Test
    void client_Ids() {
        assertTrue client1.clientId.startsWith("client-")
        assertTrue client2.clientId.startsWith("client-")
    }

    @Test
    void printMetaData() {
        println client1.metaData
    }

    @Test
    void test_publish_subscribe() {
        def listener = new MockedListener()
        client2.subscribe "topic:${dest}", listener
        def message = client1.createMessage("hello1")
        message.responseListener = listener
        message.send "topic:${dest}"
        def received = listener.message
        assertEquals "hello1", received.body
        assertEquals message.conversationId, received.conversationId
    }

    @Test
    void test_publish_listen() throws Exception {
        def message = client1.createMessage("hello1")
        message.send "queue:${dest}"
        def received = client2.receive("queue:${dest}")
        assertEquals "hello1", received.body
        assertEquals message.conversationId, received.conversationId
    }

    @Test
    void test_publish_listen_timeout() {
        try {
            client2.receive "queue:${dest}", 1, TimeUnit.SECONDS
            fail()
        } catch (e) {
        }
    }

    @Test
    void test_request_response() {
        def listener = new MockedListener(response: "RESP")
        client2.subscribe "queue:${dest}", listener
        def message = client1.createMessage("hello1")
        def received = message.getResponse("queue:${dest}")
        assertEquals "RESP", received.body
        assertEquals message.conversationId, received.conversationId
    }

    @Test
    void test_map_message() {
        client1.createMessage(["prop1": 5, "prop2": "val"]).send "queue:${dest}"
        def message = client2.receive("queue:${dest}")
        assertEquals 5, message.body.prop1
        assertEquals "val", message.body.prop2
    }

    @Test
    void test_byte_message() {
        client1.createMessage("mat".getBytes()).send "queue:${dest}"
        def message = client2.receive("queue:${dest}")
        byte[] b = message.body;
        assertEquals "mat", new String(b)
    }

    @Test
    void test_object_message() {
        client1.createMessage(new JButton("ok")).send "queue:${dest}"
        def message = client2.receive("queue:${dest}")
        assertTrue message.body instanceof JButton
        assertEquals "ok", message.body.text
    }

    @Test
    void test_reply() {
        client1.createMessage("hello1").send "queue:${dest}"
        def message = client2.receive("queue:${dest}")
        println message
        assertFalse message.replyExpected
        try {
            message.createReply("")
            fail()
        } catch (e) {
            assertEquals "Cannot reply to this message: it does not expect any reply", e.message
        }
    }

    @Test
    void test_reply_topic() {
        def listener = new MockedListener(response: "RESP")
        client2.subscribe "topic:${dest}", listener
        def message = client1.createMessage("hello1").getResponse("topic:${dest}")
    }

    @Test
    void test_reply_topic_timeout() {
        try {
            client1.createMessage("hello1").getResponse("topic:${dest}", 1, TimeUnit.SECONDS)
        } catch (e) {
            assertTrue e instanceof TimeoutException
        }
    }

    @Test
    void test_subscribe() {
        def listener = new MockedListener()
        client2.subscribe "topic:${dest}", listener

        def message = client1.createMessage("hello1")
        message.send "topic:${dest}"

        assertEquals "hello1", listener.message.body
    }

    @Test
    void test_subscribe_listener_and_reply() {
        client1.subscribe "topic:${dest}", new MockedListener(response: "RESP")

        def listener = new MockedListener()
        def message = client2.createMessage("hello1")
        message.responseListener = listener
        message.send "topic:${dest}"

        assertEquals "RESP", listener.message.body
    }

    @Test
    void test_selector() {
        def listener1 = new MockedListener();
        def listener2 = new MockedListener();
        client2.subscribe "topic:${dest}", listener1
        client2.subscribe "topic:${dest}", "cpu > 80 and server = 'jboss'", listener2

        def message = client1.createMessage("hello1", ["cpu": 83, "server": "glassfish"])
        message.send "topic:${dest}"

        assertEquals "hello1", listener1.message.body
        try {
            listener2.message
            fail()
        } catch (e) {
        }

        listener1.reset()
        listener2.reset()

        message = client1.createMessage("hello2", ["cpu": 83, "server": "jboss"])
        message.send "topic:${dest}"

        assertEquals "hello2", listener1.message.body
        assertEquals 3, listener1.message.properties.size()
    }

    @Test
    void test_properties() {
        def message = client1.createMessage("hello1", [
                "int": Integer.MAX_VALUE,
                "bool": true,
                "short": Short.MAX_VALUE,
                "string": "toto",
                "long": Long.MAX_VALUE,
                "byte": Byte.MAX_VALUE,
                "float": Float.MAX_VALUE,
                "double": Double.MAX_VALUE,
                "char": Character.valueOf((char) 'c')])
        message.setProperty "other", ["hello"]
        message.send "queue:${dest}"
        def resp = client2.receive("queue:${dest}")
        assertEquals 11, resp.properties.size()
        assertEquals Integer.MAX_VALUE, resp.getPropertyAsLong("int")
        assertEquals true, resp.getPropertyAsBoolean("bool")
        assertEquals Short.MAX_VALUE, resp.getPropertyAsShort("short")
        assertEquals "toto", resp.getPropertyAsString("string")
        assertEquals Long.MAX_VALUE, resp.getPropertyAsLong("long")
        assertEquals Byte.MAX_VALUE, resp.getPropertyAsByte("byte")
        assertEquals Float.MAX_VALUE, resp.getPropertyAsFloat("float"), 0.1
        assertEquals Double.MAX_VALUE, resp.getPropertyAsDouble("double"), 0.1
        assertEquals Character.valueOf((char) 'c'), resp.getProperty("char")
        assertEquals("hello", resp.getProperty("other")[0])
        org.junit.Assert.assertNull resp.getPropertyAsBoolean("inexisting")
        org.junit.Assert.assertNull resp.getPropertyAsByte("inexisting")
        org.junit.Assert.assertNull resp.getPropertyAsDouble("inexisting")
        org.junit.Assert.assertNull resp.getPropertyAsFloat("inexisting")
        org.junit.Assert.assertNull resp.getPropertyAsInt("inexisting")
        org.junit.Assert.assertNull resp.getPropertyAsLong("inexisting")
        org.junit.Assert.assertNull resp.getPropertyAsShort("inexisting")
        org.junit.Assert.assertNull resp.getPropertyAsString("inexisting")
    }

    @BeforeClass
    static void startLightBroker() {
        broker.start()
    }

    @AfterClass
    static void stopLightBroker() {
        broker.stop()
    }

    @Before
    void before() {
        client1.start();
        client2.start();
    }

    @After
    void after() {
        client1.stop();
        client2.stop();
    }

}
