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

package tmp.spring.annot;

/**
 * JDK 1.5+ method-level annotation used to provide metadata about operation
 * parameters, corresponding to a <code>ManagedOperationParameter</code> attribute.
 * Used as part of a <code>ManagedOperationParameters</code> annotation.
 *
 * @author Rob Harrop
 * @see tmp.spring.annot.ManagedOperationParameters#value
 * @see tmp.spring.export.metadata.ManagedOperationParameter
 * @since 1.2
 */
public @interface ManagedOperationParameter {

    public abstract String name();

    public abstract String description();

}