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
package com.mycila.distribution;

/**
 * @author Mathieu Carbou
 */
public final class Item<T> {

    private final T value;
    private int count;

    private Item(T value) {
        this.value = value;
    }

    public T value() {
        return value;
    }

    public int increment() {
        return ++count;
    }

    public Item<T> reset() {
        count = 0;
        return this;
    }

    public int count() {
        return count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return count == item.count && value.equals(item.value);
    }

    @Override
    public int hashCode() {
        int result = value.hashCode();
        result = 31 * result + count;
        return result;
    }

    @Override
    public String toString() {
        return value + ":" + count;
    }

    public static <T> Item<T> from(T value) {
        return new Item<T>(value);
    }
}
