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

import javax.jms.ConnectionMetaData;
import javax.jms.JMSException;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.TreeSet;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class JMSMetaDataImpl implements JMSMetaData {

    final ConnectionMetaData metaData;
    final Collection<String> jmsx;

    JMSMetaDataImpl(ConnectionMetaData metaData) {
        this.metaData = metaData;
        try {
            TreeSet<String> set = new TreeSet<String>();
            Enumeration<String> props = metaData.getJMSXPropertyNames();
            while (props.hasMoreElements())
                set.add(props.nextElement());
            jmsx = Collections.unmodifiableSortedSet(set);
        } catch (JMSException e) {
            throw new JMSClientException(e);
        }
    }

    @Override
    public String getJMSVersion() {
        try {
            return metaData.getJMSVersion();
        } catch (JMSException e) {
            throw new JMSClientException(e);
        }
    }

    @Override
    public String getJMSMinorVersion() {
        try {
            return "" + metaData.getJMSMinorVersion();
        } catch (JMSException e) {
            throw new JMSClientException(e);
        }
    }

    @Override
    public String getJMSMajorVersion() {
        try {
            return "" + metaData.getJMSMajorVersion();
        } catch (JMSException e) {
            throw new JMSClientException(e);
        }
    }

    @Override
    public String getJMSProviderName() {
        try {
            return metaData.getJMSProviderName();
        } catch (JMSException e) {
            throw new JMSClientException(e);
        }
    }

    @Override
    public String getProviderVersion() {
        try {
            return metaData.getProviderVersion();
        } catch (JMSException e) {
            throw new JMSClientException(e);
        }
    }

    @Override
    public String getProviderMajorVersion() {
        try {
            return "" + metaData.getProviderMajorVersion();
        } catch (JMSException e) {
            throw new JMSClientException(e);
        }
    }

    @Override
    public String getProviderMinorVersion() {
        try {
            return "" + metaData.getProviderMinorVersion();
        } catch (JMSException e) {
            throw new JMSClientException(e);
        }
    }

    @Override
    public Collection<String> getJMSXPropertyNames() {
        return jmsx;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder()
                .append("JMS Version: ").append(getJMSVersion())
                .append("\nJMS Provider Name: ").append(getJMSProviderName())
                .append("\nJMS Provider Version: ").append(getProviderVersion())
                .append("\nJMS Extended Properties:");
        for (String s : jmsx)
            sb.append("\n - ").append(s);
        return sb.toString();
    }
}
