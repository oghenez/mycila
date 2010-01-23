package service;

import com.google.inject.Inject;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class Service1 implements Service {
    @Inject
    Dao dao;

    @Override
    public Dao get() {
        return dao;
    }
}
