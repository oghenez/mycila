/**
 * Copyright (C) 2010 Mathieu Carbou <mathieu.carbou@gmail.com>
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

package tmp.spring;

/**
 * Thrown by the <code>JmxAttributeSource</code> when it encounters
 * incorrect metadata on a managed resource or one of its methods.
 *
 * @author Rob Harrop
 * @since 1.2
 */
public class InvalidMetadataException extends JmxException {

    /**
     * Create a new <code>InvalidMetadataException</code> with the supplied
     * error message.
     *
     * @param msg the detail message
     */
    public InvalidMetadataException(String msg) {
        super(msg);
    }

}