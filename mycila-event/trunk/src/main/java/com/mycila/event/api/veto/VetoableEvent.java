package com.mycila.event.api.veto;

import com.mycila.event.api.topic.Topic;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface VetoableEvent<E> extends Vetoable {
    Topic topic();
    E event();
}
