package com.example.socialnetworkgui.domain;



import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Message extends Entity<Pair<Long, Long>>{
    private String message;
    private LocalDateTime data;
    private MessageStatus status;

    public Message(Long u1, Long u2,String message, LocalDateTime data, MessageStatus status) {
        super.setId(new Pair<Long, Long>(u1,u2));
        this.message = message;
        this.data = data;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message1 = (Message) o;
        return Objects.equals(getId().getFirst(), ((Message) o).getId().getFirst())&&
                Objects.equals(getId().getSecond(), ((Message) o).getId().getSecond());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId().getFirst(),getId().getSecond());
    }

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                ", data=" + data +
                ", status=" + status +
                '}';
    }
}
