package pro.inmost.amazon.chime.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity
@Table(name = "moderator_rights")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Moderator {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "app_instance_user_id")
    private Long appInstanceUserId;

    @Column(name = "channel_id")
    private Long channelId;
}
