package com.mycila.combination;

import com.mycila.math.Factorial;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author Mathieu Carbou
 */
public final class PermutationSet<T> implements Iterable<List<T>> {

    final Factoradic factoradic;
    final List<T> objects;
    final long max;

    PermutationSet(Factoradic factoradic, List<T> objects) {
        this.factoradic = factoradic;
        this.objects = objects;
        this.max = Factorial.splitRecursive(objects.size());
    }

    public List<T> get(int index) {
        final List<T> objs = new ArrayList<T>(objects);
        final List<T> elements = new ArrayList<T>(objects.size());
        int[] digits = factoradic.digits(index);
        for (int i = 0, max = digits.length; i < max; i++)
            elements.add(objs.remove(digits[i]));
        return elements;
    }

    @Override
    public Iterator<List<T>> iterator() {
        return new Iterator<List<T>>() {
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < max;
            }

            @Override
            public List<T> next() {
                if (index == max) throw new NoSuchElementException();
                return get(index++);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Remove not supported");
            }
        };
    }
}
