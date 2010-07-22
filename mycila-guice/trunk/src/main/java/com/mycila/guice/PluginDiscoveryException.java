/**
 * Copyright (C) 2010 mycila.com <mathieu.carbou@gmail.com>
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

package com.mycila.guice;

import java.lang.annotation.Annotation;
import java.util.Arrays;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class PluginDiscoveryException extends PluginException {
    private static final long serialVersionUID = 1;

    public PluginDiscoveryException(Throwable cause, Class<? extends Annotation> pluginAnnotation, String... packages) {
        super("Error when scanning for plugin annotated by @" + pluginAnnotation.getName() + " in packages " + Arrays.toString(packages) + " : " + cause.getMessage(), cause);
    }

    public PluginDiscoveryException(String msg, Throwable cause) {
        super(msg, cause);
    }
}