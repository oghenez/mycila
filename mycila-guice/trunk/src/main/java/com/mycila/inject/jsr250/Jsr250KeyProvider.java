/**
 * Copyright (C) 2010 Mathieu Carbou <mathieu.carbou@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mycila.inject.jsr250;

import com.google.inject.Key;
import com.google.inject.name.Names;
import com.mycila.inject.injector.AnnotatedMember;
import com.mycila.inject.injector.KeyProviderSkeleton;

import javax.annotation.Resource;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Jsr250KeyProvider extends KeyProviderSkeleton<Resource> {
    @Override
    public Key<?> getKey(AnnotatedMember<?, Resource> member) {
        String name = member.getAnnotation().name();
        return name.length() == 0 ? super.getKey(member) : Key.get(member.getType(), Names.named(name));
    }
}
