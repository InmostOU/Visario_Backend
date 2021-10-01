package pro.inmost.amazon.chime.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChannelSummary {
    private String channelArn;
    private String metadata;
    private String privacy;
    private String name;
    private String mode;
    private Boolean isMember;
    private Boolean isModerator;
    private Boolean isAdmin;
    private String description;
    private int membersCount;
}
