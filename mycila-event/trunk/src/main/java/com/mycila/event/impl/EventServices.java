package com.mycila.event.impl;

import com.mycila.event.api.EventService;
import com.mycila.event.api.error.ErrorHandlerProvider;
import com.mycila.event.api.error.ErrorHandlers;
import static com.mycila.event.api.util.Ensure.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class EventServices {

    private EventServices() {
    }

    public static EventService newEventService() {
        return new DefaultEventService(ErrorHandlers.rethrowErrorsWhenFinished());
    }

    public static EventService newEventService(ErrorHandlerProvider errorHandlerProvider) {
        return new DefaultEventService(notNull(errorHandlerProvider, "ErrorHandlerProvider"));
    }

}
