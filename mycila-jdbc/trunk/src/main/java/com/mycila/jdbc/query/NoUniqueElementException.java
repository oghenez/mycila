package com.mycila.jdbc.query;

import java.util.NoSuchElementException;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class NoUniqueElementException extends NoSuchElementException {
    public NoUniqueElementException(String s) {
        super(s);
    }
}
