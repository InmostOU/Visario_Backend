package pro.inmost.amazon.chime.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "conversations")
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "channel_arn")
    private java.lang.String channelArn;

    @Column
    private java.lang.String metadata;

    @Column
    private java.lang.String privacy;

    @Column
    private java.lang.String name;

    @Column
    private java.lang.String mode;

    @Column
    private java.lang.String description;

    @ManyToMany
    @JoinTable(
            name = "conversations_to_app_inst_users",
            joinColumns = @JoinColumn(name = "conversation_id"),
            inverseJoinColumns = @JoinColumn(name = "app_instance_user_id")
    )
    private List<AppInstanceUser> participants;

}
