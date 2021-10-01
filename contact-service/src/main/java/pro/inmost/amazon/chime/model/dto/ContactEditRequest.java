package pro.inmost.amazon.chime.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContactEditRequest {
    private Long id;
    private String firstName;
    private String lastName;
}
