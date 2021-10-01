package pro.inmost.amazon.chime.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "app_instance_users")
public class AppInstanceUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "app_instance_user_arn")
    private String appInstanceUserArn;

    @Column(name = "app_instance_arn")
    private String appInstanceArn;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "chime_account_id")
    private ChimeAccount chimeAccount;

    @ManyToMany
    @JoinTable(
            name = "channels_to_users",
            joinColumns = @JoinColumn(name = "channel_id"),
            inverseJoinColumns = @JoinColumn(name = "app_instance_user_id")
    )
    private Set<Channel> channels;

    @OneToMany(mappedBy = "appInstanceUser")
    private List<Message> messages;
}
