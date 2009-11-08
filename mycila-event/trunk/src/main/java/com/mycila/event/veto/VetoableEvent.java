package com.mycila.event.veto;

import com.mycila.event.topic.Topic;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface VetoableEvent<E> extends Vetoable {
    Topic topic();
    E event();
}
