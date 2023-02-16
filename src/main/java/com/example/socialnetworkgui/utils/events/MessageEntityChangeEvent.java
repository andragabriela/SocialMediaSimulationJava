package com.example.socialnetworkgui.utils.events;

import com.example.socialnetworkgui.domain.Message;

public class MessageEntityChangeEvent implements Event{
    private ChangeEventType type;
    private Message oldData, data;

    public MessageEntityChangeEvent(ChangeEventType type, Message data) {
        this.type = type;
        this.data = data;
    }

    public MessageEntityChangeEvent(ChangeEventType type, Message oldData, Message data) {
        this.type = type;
        this.oldData = oldData;
        this.data = data;
    }

    public ChangeEventType getType() {
        return type;
    }

    public Message getOldData() {
        return oldData;
    }

    public Message getData() {
        return data;
    }
}
