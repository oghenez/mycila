package com.mycila.event.api.message;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface MessageResponse<P, R> {
    P getParameter();
    void replyError(Exception error);
    void reply(R reply);
}
