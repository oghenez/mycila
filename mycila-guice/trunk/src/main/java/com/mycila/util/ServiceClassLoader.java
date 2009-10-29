package com.mycila.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.ServiceConfigurationError;

/**
 * @author Mark Reinhold
 * @author Mathieu Carbou <mathieu.carbou@gmail.com>
 * @param <S> The type of the service to be loaded by this loader
 */
public final class ServiceClassLoader<S> implements Iterable<Class<S>> {

    private static final String PREFIX = "META-INF/services/";
    private Class<S> service;
    private ClassLoader loader;
    private LinkedHashMap<String, Class<S>> providers = new LinkedHashMap<String, Class<S>>();
    private LazyIterator lookupIterator;

    public void reload() {
        providers.clear();
        lookupIterator = new LazyIterator(service, loader);
    }

    private ServiceClassLoader(Class<S> svc, ClassLoader cl) {
        service = svc;
        loader = cl;
        reload();
    }

    private static void fail(Class service, String msg, Throwable cause) throws ServiceConfigurationError {
        throw new ServiceConfigurationError(service.getName() + ": " + msg, cause);
    }

    private static void fail(Class service, String msg) throws ServiceConfigurationError {
        throw new ServiceConfigurationError(service.getName() + ": " + msg);
    }

    private static void fail(Class service, URL u, int line, String msg) throws ServiceConfigurationError {
        fail(service, u + ":" + line + ": " + msg);
    }

    private int parseLine(Class service, URL u, BufferedReader r, int lc, List<String> names) throws IOException, ServiceConfigurationError {
        String ln = r.readLine();
        if (ln == null) return -1;
        int ci = ln.indexOf('#');
        if (ci >= 0) ln = ln.substring(0, ci);
        ln = ln.trim();
        int n = ln.length();
        if (n != 0) {
            if ((ln.indexOf(' ') >= 0) || (ln.indexOf('\t') >= 0))
                fail(service, u, lc, "Illegal configuration-file syntax");
            int cp = ln.codePointAt(0);
            if (!Character.isJavaIdentifierStart(cp))
                fail(service, u, lc, "Illegal provider-class name: " + ln);
            for (int i = Character.charCount(cp); i < n; i += Character.charCount(cp)) {
                cp = ln.codePointAt(i);
                if (!Character.isJavaIdentifierPart(cp) && (cp != '.'))
                    fail(service, u, lc, "Illegal provider-class name: " + ln);
            }
            if (!providers.containsKey(ln) && !names.contains(ln))
                names.add(ln);
        }
        return lc + 1;
    }

    private Iterator<String> parse(Class service, URL u) throws ServiceConfigurationError {
        InputStream in = null;
        BufferedReader r = null;
        ArrayList<String> names = new ArrayList<String>();
        try {
            in = u.openStream();
            r = new BufferedReader(new InputStreamReader(in, "utf-8"));
            int lc = 1;
            while ((lc = parseLine(service, u, r, lc, names)) >= 0) ;
        } catch (IOException x) {
            fail(service, "Error reading configuration file", x);
        } finally {
            try {
                if (r != null) r.close();
                if (in != null) in.close();
            } catch (IOException y) {
                fail(service, "Error closing configuration file", y);
            }
        }
        return names.iterator();
    }

    private class LazyIterator implements Iterator<Class<S>> {
        Class<S> service;
        ClassLoader loader;
        Enumeration<URL> configs = null;
        Iterator<String> pending = null;
        String nextName = null;

        private LazyIterator(Class<S> service, ClassLoader loader) {
            this.service = service;
            this.loader = loader;
        }

        public boolean hasNext() {
            if (nextName != null) {
                return true;
            }
            if (configs == null) {
                try {
                    String fullName = PREFIX + service.getName();
                    if (loader == null)
                        configs = ClassLoader.getSystemResources(fullName);
                    else
                        configs = loader.getResources(fullName);
                } catch (IOException x) {
                    fail(service, "Error locating configuration files", x);
                }
            }
            while ((pending == null) || !pending.hasNext()) {
                if (!configs.hasMoreElements()) {
                    return false;
                }
                pending = parse(service, configs.nextElement());
            }
            nextName = pending.next();
            return true;
        }

        @SuppressWarnings({"unchecked"})
        public Class<S> next() {
            if (!hasNext())
                throw new NoSuchElementException();
            String cn = nextName;
            nextName = null;
            try {
                Class<S> p = (Class<S>) Class.forName(cn, true, loader);
                providers.put(cn, p);
                return p;
            } catch (ClassNotFoundException x) {
                fail(service,
                        "Provider " + cn + " not found");
            } catch (Throwable x) {
                fail(service,
                        "Provider " + cn + " could not be instantiated: " + x,
                        x);
            }
            throw new Error();        // This cannot happen
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    public Iterator<Class<S>> iterator() {
        return new Iterator<Class<S>>() {
            Iterator<Map.Entry<String, Class<S>>> knownProviders = providers.entrySet().iterator();

            public boolean hasNext() {
                return knownProviders.hasNext() || lookupIterator.hasNext();
            }

            public Class<S> next() {
                if (knownProviders.hasNext())
                    return knownProviders.next().getValue();
                return lookupIterator.next();
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public static <S> ServiceClassLoader<S> load(Class<S> service, ClassLoader loader) {
        return new ServiceClassLoader<S>(service, loader);
    }

    public static <S> ServiceClassLoader<S> load(Class<S> service) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        return ServiceClassLoader.load(service, cl);
    }

    public static <S> ServiceClassLoader<S> loadInstalled(Class<S> service) {
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        ClassLoader prev = null;
        while (cl != null) {
            prev = cl;
            cl = cl.getParent();
        }
        return ServiceClassLoader.load(service, prev);
    }

    public String toString() {
        return "com.mycila.util.ServiceClassLoader[" + service.getName() + "]";
    }

}