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
