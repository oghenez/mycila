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

package test;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class Main {
    public static void main(String[] args) {
        System.out.println(Main.class.getResource("/test/Main.class"));
        System.out.println(Main.class.getResource("/org/objectweb/asm/ClassVisitor.class"));
    }
}
