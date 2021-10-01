package pro.inmost.amazon.chime.exception;

public class UserWithThisEmailNotFoundException extends Exception {
    public UserWithThisEmailNotFoundException(String message) {
        super(message);
    }
}
