/**
 * Copyright (C) 2009 Mathieu Carbou <mathieu.carbou@gmail.com>
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
package euler;

import com.mycila.Format;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Mathieu Carbou
 */
final class RunALL {
    @SuppressWarnings({"RedundantArrayCreation"})
    public static void main(String[] args) throws Exception {
        for (int i = 1; i < 300; i++) {
            String num = Format.leftPad(i, 3, '0');
            Class c = null;
            try {
                c = Class.forName("euler.Problem" + num);
            } catch (ClassNotFoundException e) {
                Thread.sleep(500);
                System.err.println("\n\nPROBLEM " + num + " is MISSING !!!");
                return;
            }
            System.out.println("==================== EULER PROBLEM: " + num + " ====================");
            try {
                c.getMethod("main", new Class<?>[]{String[].class}).invoke(null, new Object[]{new String[0]});
            } catch (InvocationTargetException e) {
                if (e.getTargetException() instanceof RuntimeException) throw (RuntimeException) e.getTargetException();
                throw new RuntimeException(e.getTargetException().getMessage(), e.getTargetException());
            }
        }
    }
}
