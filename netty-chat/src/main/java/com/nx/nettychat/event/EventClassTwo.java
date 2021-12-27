package com.nx.nettychat.event;

import java.util.EventObject;

public class EventClassTwo extends EventObject {
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public EventClassTwo(Object source) {
        super(source);
    }
}
