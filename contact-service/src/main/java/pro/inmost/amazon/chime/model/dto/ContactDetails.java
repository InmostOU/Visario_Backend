package pro.inmost.amazon.chime.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ContactDetails {
    private int status;
    private String message;
    private List<ContactDetail> data;
}
