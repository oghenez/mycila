package com.mycila.testing.plugins.jetty.locator;

import java.io.File;

import com.google.common.base.Predicate;

class ExistsFilePredicate
        implements Predicate<File> {
    
    public boolean apply(
            final File input)
    {
        final boolean exists = (input != null)
                ? input.exists()
                : false;
        return exists;
    }
    
}