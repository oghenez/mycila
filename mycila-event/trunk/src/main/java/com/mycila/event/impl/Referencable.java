package com.mycila.event.impl;

import com.mycila.event.api.Reachability;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
interface Referencable {
    Reachability reachability();
}
