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

package com.mycila.jdbc.query;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public final class Row {

    public final int index;
    public final List<Cell> cells;
    private final Mapper mapper;

    Row(int index, int cellNumber, Mapper mapper) {
        this.mapper = mapper;
        this.index = index;
        this.cells = new ArrayList<Cell>(cellNumber);
    }

    public Cell cell(int columnIndex) {
        return cells.get(columnIndex);
    }

    public Cell cell(String name) {
        if (name == null)
            throw new IllegalArgumentException("Cell name missing");
        name = name.toUpperCase();
        for (Cell cell : cells) {
            if (cell.column.header.name.equals(name))
                return cell;
        }
        throw new NoSuchElementException("Cell named " + name);
    }

    @Override
    public String toString() {
        return "Row " + index;
    }

    public Cell uniqueCell() {
        if (cells.size() != 1)
            throw new NoSuchElementException("No unique cell returned: got " + cells.size() + " cell(s)");
        return cells.get(0);
    }

    <T> T map(Class<T> type, Reflect reflect) {
        if (cells.size() == 1)
            return uniqueCell().as(type);
        T t = mapper.instanciate(type);
        for (Cell cell : cells) {
            Field f = reflect.findBest(cell.column.header.name);
            if (f != null) {
                try {
                    if (!f.isAccessible())
                        f.setAccessible(true);
                    f.set(t, cell.as(f.getType().isPrimitive() ? ClassUtils.getWrapper(f.getType()) : f.getType()));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
        }
        return t;
    }

    public <T> Row convert(Class<T> type, Converter<T> converter) {
        mapper.customConverters.put(type, converter);
        return this;
    }

    public <T> T map(Class<T> type) {
        return map(type, Reflect.access(type));
    }
}
