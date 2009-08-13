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
package com.mycila.cards;

/**
 * @author Mathieu Carbou
 */
public final class HandFilters {

    private static final HandFilter ALL = new HandFilter() {
        public boolean accept(Hand hand) {
            return true;
        }
    };

    private static final HandFilter NONE = new HandFilter() {
        public boolean accept(Hand hand) {
            return false;
        }
    };

    private HandFilters() {
    }

    public static HandFilter all() {
        return ALL;
    }

    public static HandFilter none() {
        return NONE;
    }

}
