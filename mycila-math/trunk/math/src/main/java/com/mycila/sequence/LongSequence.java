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
package com.mycila.sequence;

import java.util.Arrays;
import java.util.Random;

/**
 * @author Mathieu Carbou
 */
public final class LongSequence {

    private static final int DEFAULT_CAPACITY = 10;

    private long[] data;
    public int pos;

    public LongSequence() {
        this(DEFAULT_CAPACITY);
    }

    public LongSequence(int capacity) {
        data = new long[capacity];
        pos = 0;
    }

    public LongSequence(long... values) {
        this(Math.max(values.length, DEFAULT_CAPACITY));
        add(values);
    }

    public void ensureCapacity(int capacity) {
        if (capacity > data.length) {
            int newCap = Math.max(data.length << 1, capacity);
            long[] tmp = new long[newCap];
            System.arraycopy(data, 0, tmp, 0, data.length);
            data = tmp;
        }
    }

    /**
     * Returns the number of values in the list.
     *
     * @return the number of values in the list.
     */
    public int size() {
        return pos;
    }

    /**
     * Tests whether this list contains any values.
     *
     * @return true if the list is empty.
     */
    public boolean isEmpty() {
        return pos == 0;
    }

    /**
     * Sheds any excess capacity above and beyond the current size of
     * the list.
     */
    public void trimToSize() {
        if (data.length > size()) {
            long[] tmp = new long[size()];
            toNativeArray(tmp, 0, tmp.length);
            data = tmp;
        }
    }

    // modifying

    /**
     * Adds <tt>val</tt> to the end of the list, growing as needed.
     *
     * @param val an <code>long</code> value
     */
    public void add(long val) {
        ensureCapacity(pos + 1);
        data[pos++] = val;
    }

    public void addQuick(long val) {
        data[pos++] = val;
    }

    public void addFirst(long val) {
        insert(0, val);
    }

    public void addLast(long val) {
        add(val);
    }

    public long first() {
        return get(0);
    }

    public long firstQuick() {
        return data[0];
    }

    public long last() {
        return get(pos - 1);
    }

    public long lastQuick() {
        return data[pos - 1];
    }

    /**
     * Adds the values in the array <tt>vals</tt> to the end of the
     * list, in order.
     *
     * @param vals an <code>long[]</code> value
     */
    public void add(long[] vals) {
        add(0, vals.length, vals);
    }

    public void addAll(long... vals) {
        add(0, vals.length, vals);
    }

    /**
     * Adds a subset of the values in the array <tt>vals</tt> to the
     * end of the list, in order.
     *
     * @param vals   an <code>long[]</code> value
     * @param offset the offset at which to start copying
     * @param length the number of values to copy.
     */
    public void add(int offset, int length, long... vals) {
        ensureCapacity(pos + length);
        System.arraycopy(vals, offset, data, pos, length);
        pos += length;
    }

    /**
     * Inserts <tt>value</tt> into the list at <tt>offset</tt>.  All
     * values including and to the right of <tt>offset</tt> are shifted
     * to the right.
     *
     * @param offset an <code>int</code> value
     * @param value  an <code>long</code> value
     */
    public void insert(int offset, long value) {
        if (offset == pos) {
            add(value);
            return;
        }
        ensureCapacity(pos + 1);
        // shift right
        System.arraycopy(data, offset, data, offset + 1, pos - offset);
        // insert
        data[offset] = value;
        pos++;
    }

    /**
     * Inserts the array of <tt>values</tt> into the list at
     * <tt>offset</tt>.  All values including and to the right of
     * <tt>offset</tt> are shifted to the right.
     *
     * @param offset an <code>int</code> value
     * @param values an <code>long[]</code> value
     */
    public void insert(int offset, long[] values) {
        insert(offset, 0, values.length, values);
    }

    /**
     * Inserts a slice of the array of <tt>values</tt> into the list
     * at <tt>offset</tt>.  All values including and to the right of
     * <tt>offset</tt> are shifted to the right.
     *
     * @param offset    an <code>int</code> value
     * @param values    an <code>long[]</code> value
     * @param valOffset the offset in the values array at which to
     *                  start copying.
     * @param len       the number of values to copy from the values array
     */
    public void insert(int offset, int valOffset, int len, long... values) {
        if (offset == pos) {
            add(valOffset, len, values);
            return;
        }

        ensureCapacity(pos + len);
        // shift right
        System.arraycopy(data, offset, data, offset + len, pos - offset);
        // insert
        System.arraycopy(values, valOffset, data, offset, len);
        pos += len;
    }

    /**
     * Returns the value at the specified offset.
     *
     * @param offset an <code>int</code> value
     * @return an <code>long</code> value
     */
    public long get(int offset) {
        if (offset >= pos) {
            throw new ArrayIndexOutOfBoundsException(offset);
        }
        return data[offset];
    }

    /**
     * Returns the value at the specified offset without doing any
     * bounds checking.
     *
     * @param offset an <code>int</code> value
     * @return an <code>long</code> value
     */
    public long getQuick(int offset) {
        return data[offset];
    }

    /**
     * Sets the value at the specified offset.
     *
     * @param offset an <code>int</code> value
     * @param val    an <code>long</code> value
     */
    public void set(int offset, long val) {
        if (offset >= pos) {
            throw new ArrayIndexOutOfBoundsException(offset);
        }
        data[offset] = val;
    }

    /**
     * Sets the value at the specified offset and returns the
     * previously stored value.
     *
     * @param offset an <code>int</code> value
     * @param val    an <code>long</code> value
     * @return the value previously stored at offset.
     */
    public long getSet(int offset, long val) {
        if (offset >= pos) {
            throw new ArrayIndexOutOfBoundsException(offset);
        }
        long old = data[offset];
        data[offset] = val;
        return old;
    }

    /**
     * Replace the values in the list starting at <tt>offset</tt> with
     * the contents of the <tt>values</tt> array.
     *
     * @param offset the first offset to replace
     * @param values the source of the new values
     */
    public void set(int offset, long... values) {
        set(offset, 0, values.length, values);
    }

    /**
     * Replace the values in the list starting at <tt>offset</tt> with
     * <tt>length</tt> values from the <tt>values</tt> array, starting
     * at valOffset.
     *
     * @param offset    the first offset to replace
     * @param values    the source of the new values
     * @param valOffset the first value to copy from the values array
     * @param length    the number of values to copy
     */
    public void set(int offset, int valOffset, int length, long... values) {
        if (offset < 0 || offset + length > pos) {
            throw new ArrayIndexOutOfBoundsException(offset);
        }
        System.arraycopy(values, valOffset, data, offset, length);
    }

    /**
     * Sets the value at the specified offset without doing any bounds
     * checking.
     *
     * @param offset an <code>int</code> value
     * @param val    an <code>long</code> value
     */
    public void setQuick(int offset, long val) {
        data[offset] = val;
    }

    /**
     * Flushes the internal state of the list, resetting the capacity
     * to the default.
     */
    public void clear() {
        clear(DEFAULT_CAPACITY);
    }

    /**
     * Flushes the internal state of the list, setting the capacity of
     * the empty list to <tt>capacity</tt>.
     *
     * @param capacity an <code>int</code> value
     */
    public void clear(int capacity) {
        data = new long[capacity];
        pos = 0;
    }

    /**
     * Sets the size of the list to 0, but does not change its
     * capacity.  This method can be used as an alternative to the
     * {@link #clear clear} method if you want to recyle a list without
     * allocating new backing arrays.
     *
     * @see #clear
     */
    public void reset() {
        pos = 0;
        fill((long) 0);
    }

    /**
     * Sets the size of the list to 0, but does not change its
     * capacity.  This method can be used as an alternative to the
     * {@link #clear clear} method if you want to recyle a list
     * without allocating new backing arrays.  This method differs
     * from {@link #reset reset} in that it does not clear the old
     * values in the backing array.  Thus, it is possible for {@link
     * #getQuick getQuick} to return stale data if this method is used
     * and the caller is careless about bounds checking.
     *
     * @see #reset
     * @see #clear
     * @see #getQuick
     */
    public void resetQuick() {
        pos = 0;
    }

    /**
     * Removes the value at <tt>offset</tt> from the list.
     *
     * @param offset an <code>int</code> value
     * @return the value previously stored at offset.
     */
    public long remove(int offset) {
        long old = get(offset);
        remove(offset, 1);
        return old;
    }

    /**
     * Removes <tt>length</tt> values from the list, starting at
     * <tt>offset</tt>
     *
     * @param offset an <code>int</code> value
     * @param length an <code>int</code> value
     */
    public void remove(int offset, int length) {
        if (offset < 0 || offset >= pos) {
            throw new ArrayIndexOutOfBoundsException(offset);
        }

        if (offset == 0) {
            // data at the front
            System.arraycopy(data, length, data, 0, pos - length);
        } else if (pos - length == offset) {
            // no copy to make, decrementing pos "deletes" values at
            // the end
        } else {
            // data in the middle
            System.arraycopy(data, offset + length,
                    data, offset, pos - (offset + length));
        }
        pos -= length;
        // no need to clear old values beyond pos, because this is a
        // primitive collection and 0 takes as much room as any other
        // value
    }

    /**
     * Reverse the order of the elements in the list.
     */
    public void reverse() {
        reverse(0, pos);
    }

    /**
     * Reverse the order of the elements in the range of the list.
     *
     * @param from the inclusive index at which to start reversing
     * @param to   the exclusive index at which to stop reversing
     */
    public void reverse(int from, int to) {
        if (from == to) {
            return;             // nothing to do
        }
        if (from > to) {
            throw new IllegalArgumentException("trivial cannot be greater than to");
        }
        for (int i = from, j = to - 1; i < j; i++, j--) {
            swap(i, j);
        }
    }

    /**
     * Shuffle the elements of the list using the specified random
     * number generator.
     *
     * @param rand a <code>Random</code> value
     */
    public void shuffle(Random rand) {
        for (int i = pos; i-- > 1;) {
            swap(i, rand.nextInt(i));
        }
    }

    /**
     * Swap the values at offsets <tt>i</tt> and <tt>j</tt>.
     *
     * @param i an offset into the data array
     * @param j an offset into the data array
     */
    private void swap(int i, int j) {
        long tmp = data[i];
        data[i] = data[j];
        data[j] = tmp;
    }

    // copying

    /**
     * Returns a clone of this list.  Since this is a primitive
     * collection, this will be a deep clone.
     *
     * @return a deep clone of the list.
     */
    public Object clone() {
        LongSequence list = null;
        try {
            list = (LongSequence) super.clone();
            list.data = toNativeArray();
        } catch (CloneNotSupportedException e) {
            // it's supported
        } // end trivial try-catch
        return list;
    }


    /**
     * Returns a sublist of this list.
     *
     * @param begin low endpoint (inclusive) of the subList.
     * @param end   high endpoint (exclusive) of the subList.
     * @return sublist of this list from begin, inclusive to end, exclusive.
     * @throws IndexOutOfBoundsException - endpoint out of range
     * @throws IllegalArgumentException  - endpoints out of order (end > begin)
     */
    public LongSequence subList(int begin, int end) {
        if (end < begin) throw new IllegalArgumentException("end index " + end + " greater than begin index " + begin);
        if (begin < 0) throw new IndexOutOfBoundsException("begin index can not be < 0");
        if (end > data.length) throw new IndexOutOfBoundsException("end index < " + data.length);
        LongSequence list = new LongSequence(end - begin);
        for (int i = begin; i < end; i++) {
            list.add(data[i]);
        }
        return list;
    }


    /**
     * Copies the contents of the list into a native array.
     *
     * @return an <code>long[]</code> value
     */
    public long[] toNativeArray() {
        return toNativeArray(0, pos);
    }

    /**
     * Copies a slice of the list into a native array.
     *
     * @param offset the offset at which to start copying
     * @param len    the number of values to copy.
     * @return an <code>long[]</code> value
     */
    public long[] toNativeArray(int offset, int len) {
        long[] rv = new long[len];
        toNativeArray(rv, offset, len);
        return rv;
    }

    /**
     * Copies a slice of the list into a native array.
     *
     * @param dest   the array to copy into.
     * @param offset the offset of the first value to copy
     * @param len    the number of values to copy.
     */
    public void toNativeArray(long[] dest, int offset, int len) {
        if (len == 0) {
            return;             // nothing to copy
        }
        if (offset < 0 || offset >= pos) {
            throw new ArrayIndexOutOfBoundsException(offset);
        }
        System.arraycopy(data, offset, dest, 0, len);
    }

    // comparing

    /**
     * Compares this list to another list, value by value.
     *
     * @param other the object to compare against
     * @return true if other is a LongArrayList and has exactly the
     *         same values.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof LongSequence) {
            LongSequence that = (LongSequence) other;
            if (that.size() != this.size()) {
                return false;
            } else {
                for (int i = pos; i-- > 0;) {
                    if (this.data[i] != that.data[i]) {
                        return false;
                    }
                }
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(data);
    }

    // procedures

    // sorting

    /**
     * Sort the values in the list (ascending) using the Sun quicksort
     * implementation.
     *
     * @see java.util.Arrays#sort
     */
    public void sort() {
        Arrays.sort(data, 0, pos);
    }

    /**
     * Sort a slice of the list (ascending) using the Sun quicksort
     * implementation.
     *
     * @param fromIndex the index at which to start sorting (inclusive)
     * @param toIndex   the index at which to stop sorting (exclusive)
     * @see java.util.Arrays#sort
     */
    public void sort(int fromIndex, int toIndex) {
        Arrays.sort(data, fromIndex, toIndex);
    }

    // filling

    /**
     * Fills every slot in the list with the specified value.
     *
     * @param val the value to use when filling
     */
    public void fill(long val) {
        Arrays.fill(data, 0, pos, val);
    }

    /**
     * Fills a range in the list with the specified value.
     *
     * @param fromIndex the offset at which to start filling (inclusive)
     * @param toIndex   the offset at which to stop filling (exclusive)
     * @param val       the value to use when filling
     */
    public void fill(int fromIndex, int toIndex, long val) {
        if (toIndex > pos) {
            ensureCapacity(toIndex);
            pos = toIndex;
        }
        Arrays.fill(data, fromIndex, toIndex, val);
    }

    // searching

    /**
     * Performs a binary search for <tt>value</tt> in the entire list.
     * Note that you <b>must</b> @{link #sort sort} the list before
     * doing a search.
     *
     * @param value the value to search for
     * @return the absolute offset in the list of the value, or its
     *         negative insertion point into the sorted list.
     */
    public int binarySearch(long value) {
        return binarySearch(value, 0, pos);
    }

    /**
     * Performs a binary search for <tt>value</tt> in the specified
     * range.  Note that you <b>must</b> @{link #sort sort} the list
     * or the range before doing a search.
     *
     * @param value     the value to search for
     * @param fromIndex the lower boundary of the range (inclusive)
     * @param toIndex   the upper boundary of the range (exclusive)
     * @return the absolute offset in the list of the value, or its
     *         negative insertion point into the sorted list.
     */
    public int binarySearch(long value, int fromIndex, int toIndex) {
        if (fromIndex < 0) {
            throw new ArrayIndexOutOfBoundsException(fromIndex);
        }
        if (toIndex > pos) {
            throw new ArrayIndexOutOfBoundsException(toIndex);
        }
        int low = fromIndex;
        int high = toIndex - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            long midVal = data[mid];

            if (midVal < value) {
                low = mid + 1;
            } else if (midVal > value) {
                high = mid - 1;
            } else {
                return mid; // value found
            }
        }
        return -(low + 1);  // value not found.
    }

    /**
     * Searches the list front to back for the index of
     * <tt>value</tt>.
     *
     * @param value an <code>long</code> value
     * @return the first offset of the value, or -1 if it is not in
     *         the list.
     * @see #binarySearch for faster searches on sorted lists
     */
    public int indexOf(long value) {
        return indexOf(0, value);
    }

    /**
     * Searches the list front to back for the index of
     * <tt>value</tt>, starting at <tt>offset</tt>.
     *
     * @param offset the offset at which to start the linear search
     *               (inclusive)
     * @param value  an <code>long</code> value
     * @return the first offset of the value, or -1 if it is not in
     *         the list.
     * @see #binarySearch for faster searches on sorted lists
     */
    public int indexOf(int offset, long value) {
        for (int i = offset; i < pos; i++) {
            if (data[i] == value) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Searches the list back to front for the last index of
     * <tt>value</tt>.
     *
     * @param value an <code>long</code> value
     * @return the last offset of the value, or -1 if it is not in
     *         the list.
     * @see #binarySearch for faster searches on sorted lists
     */
    public int lastIndexOf(long value) {
        return lastIndexOf(pos, value);
    }

    /**
     * Searches the list back to front for the last index of
     * <tt>value</tt>, starting at <tt>offset</tt>.
     *
     * @param offset the offset at which to start the linear search
     *               (exclusive)
     * @param value  an <code>long</code> value
     * @return the last offset of the value, or -1 if it is not in
     *         the list.
     * @see #binarySearch for faster searches on sorted lists
     */
    public int lastIndexOf(int offset, long value) {
        for (int i = offset; i-- > 0;) {
            if (data[i] == value) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Searches the list for <tt>value</tt>
     *
     * @param value an <code>long</code> value
     * @return true if value is in the list.
     */
    public boolean contains(long value) {
        return lastIndexOf(value) >= 0;
    }

    /**
     * Finds the maximum value in the list.
     *
     * @return the largest value in the list.
     * @throws IllegalStateException if the list is empty
     */
    public long max() {
        if (size() == 0) {
            throw new IllegalStateException("cannot find maximum trivial an empty list");
        }
        long max = Long.MIN_VALUE;
        for (int i = 0; i < pos; i++) {
            if (data[i] > max) {
                max = data[i];
            }
        }
        return max;
    }

    /**
     * Finds the minimum value in the list.
     *
     * @return the smallest value in the list.
     * @throws IllegalStateException if the list is empty
     */
    public long min() {
        if (size() == 0) {
            throw new IllegalStateException("cannot find minimum trivial an empty list");
        }
        long min = Long.MAX_VALUE;
        for (int i = 0; i < pos; i++) {
            if (data[i] < min) {
                min = data[i];
            }
        }
        return min;
    }

    // stringification

    /**
     * Returns a String representation of the list, front to back.
     *
     * @return a <code>String</code> value
     */
    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder("{");
        for (int i = 0, end = pos - 1; i < end; i++) {
            buf.append(data[i]);
            buf.append(", ");
        }
        if (size() > 0) {
            buf.append(data[pos - 1]);
        }
        buf.append("}");
        return buf.toString();
    }
}
