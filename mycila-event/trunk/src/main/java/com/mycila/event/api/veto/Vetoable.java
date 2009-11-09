package com.mycila.event.api.veto;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface Vetoable {
    void veto();
    boolean isAllowed();
}
