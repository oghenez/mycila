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

package com.mycila.jms;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class JMSXProperty {

    private JMSXProperty() {
    }

    /* custom */
    public static final String FromClientID = "FromClientID";

    /* JMXS vendors */
    public static final String UserID = "JMSXUserID";
    public static final String AppID = "JMSXAppID";
    public static final String ProducerTXID = "JMSXProducerTXID";
    public static final String ConsumerTXID = "JMSXConsumerTXID";
    public static final String RcvTimestamp = "JMSXRcvTimestamp";
    public static final String DeliveryCount = "JMSXDeliveryCount";
    public static final String State = "JMSXState";
    public static final String GroupID = "JMSXGroupID";
    public static final String GroupSeq = "JMSXGroupSeq";

}