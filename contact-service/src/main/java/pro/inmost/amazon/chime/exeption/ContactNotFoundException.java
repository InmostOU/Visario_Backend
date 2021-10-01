package pro.inmost.amazon.chime.exeption;

import lombok.Data;

@Data
public class ContactNotFoundException extends Exception {

    private String path;

    public ContactNotFoundException(String message) {
        super(message);
    }
}
