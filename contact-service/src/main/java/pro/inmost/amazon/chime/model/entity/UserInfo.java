package pro.inmost.amazon.chime.model.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfo {
    private String username;
    private double money;
    private String avatarUrl;
}
