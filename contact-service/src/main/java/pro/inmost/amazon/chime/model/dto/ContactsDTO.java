package pro.inmost.amazon.chime.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class ContactsDTO {
    private long id;
    private String userName;
    private Boolean isFavorite;
    private Boolean isMuted;
    private String userArn;
}
