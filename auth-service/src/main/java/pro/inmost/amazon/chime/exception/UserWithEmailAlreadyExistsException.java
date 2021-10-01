package pro.inmost.amazon.chime.exception;

public class UserWithEmailAlreadyExistsException extends Exception {
    public UserWithEmailAlreadyExistsException(String message) {
        super(message);
    }
}
