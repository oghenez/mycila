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
final class Query {

    final StringBuilder sb = new StringBuilder();

    private Query() {
    }

    public Query header(JMSHeader header) {
        sb.append(header);
        return this;
    }

    public Query eq(String val) {
        sb.append("='").append(val).append("'");
        return this;
    }

    public String build() {
        return sb.toString();
    }

    @Override
    public String toString() {
        return build();
    }

    public static Query q() {
        return new Query();
    }
}
