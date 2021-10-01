package pro.inmost.amazon.chime.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateProfileRequest {
    private String firstName;
    private String lastName;
    private String username;
    private Long birthday;
    private String about;
    private String showEmailTo;
    private String showPhoneNumberTo;
}
