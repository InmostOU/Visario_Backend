package pro.inmost.amazon.chime.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
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
            joinColumns = @JoinColumn(name = "app_instance_user_id"),
            inverseJoinColumns = @JoinColumn(name = "channel_id")
    )
    private List<Channel> channels;

    @ManyToMany
    @JoinTable(
            name = "conversations_to_app_inst_users",
            joinColumns = @JoinColumn(name = "app_instance_user_id"),
            inverseJoinColumns = @JoinColumn(name = "conversation_id")
    )
    private List<Conversation> conversations;

    @OneToMany(mappedBy = "appInstanceUser")
    private List<Message> messages;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppInstanceUser that = (AppInstanceUser) o;
        return appInstanceUserArn.equals(that.appInstanceUserArn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(appInstanceUserArn);
    }
}
