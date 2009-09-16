package com.mycila.sandbox.collection;

import static com.mycila.sandbox.collection.Wrapper.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import static java.util.Arrays.*;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class WrapperTest {
    public static void main(String... args) throws MalformedURLException {
        Map<String, List<Integer>> numbers = new HashMap<String, List<Integer>>();

        numbers = wrap(numbers, ArrayList.class);

        numbers.get("squares").addAll(asList(1, 4, 9, 16));
        numbers.get("primes").addAll(asList(2, 3, 5, 7));

        System.out.println(numbers);

        Map<String, Map<URL, Integer>> categoriesAndRanks = wrap(new LinkedHashMap<String, Map<URL, Integer>>(), IdentityHashMap.class);
        categoriesAndRanks.get("blogs").put(new URL("http://blog.mycila.com"), 5);
        categoriesAndRanks.get("blogs").put(new URL("http://thecodersbreakfast.net/"), 5);

        System.out.println(categoriesAndRanks);

        Map<Integer, StringBuilder> names = wrap(new TreeMap<Integer, StringBuilder>(), StringBuilder.class);
        names.get(3).append("This is number '").append(3).append("'.");
        names.get(10).append("This is number '").append(10).append("'.");
        names.get(0).append("This is number '").append(0).append("'.");

        System.out.println(names);
    }
}
