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

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

/**
 * Interface that allows infrastructure components to provide their own
 * <code>ObjectName</code>s to the <code>MBeanExporter</code>.
 * <p/>
 * <p><b>Note:</b> This interface is mainly intended for internal usage.
 *
 * @author Rob Harrop
 * @see tmp.spring.export.MBeanExporter
 * @since 1.2.2
 */
public interface SelfNaming {

    /**
     * Return the <code>ObjectName</code> for the implementing object.
     *
     * @throws javax.management.MalformedObjectNameException
     *          if thrown by the ObjectName constructor
     * @see javax.management.ObjectName#ObjectName(String)
     * @see javax.management.ObjectName#getInstance(String)
     * @see tmp.spring.support.ObjectNameManager#getInstance(String)
     */
    ObjectName getObjectName() throws MalformedObjectNameException;

}