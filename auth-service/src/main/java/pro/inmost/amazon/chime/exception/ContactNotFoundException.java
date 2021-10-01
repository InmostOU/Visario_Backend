package pro.inmost.amazon.chime.exception;

public class ContactNotFoundException extends RuntimeException {
    ContactNotFoundException(String message) {
        super(message);
    }
}
