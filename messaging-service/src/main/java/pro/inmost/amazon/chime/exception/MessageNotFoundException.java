package pro.inmost.amazon.chime.exception;

import lombok.Builder;

public class MessageNotFoundException extends RuntimeException {
    public MessageNotFoundException() {
        super("Message not found");
    }

}
