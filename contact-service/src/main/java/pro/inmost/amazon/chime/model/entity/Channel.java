package pro.inmost.amazon.chime.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "channels")
public class Channel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "channel_arn")
    private String channelArn;

    @Column
    private String metadata;

    @Column
    private String privacy;

    @Column
    private String name;

    @Column
    private String mode;

    @ManyToMany
    @JoinTable(
            name = "channels_to_users",
            joinColumns = @JoinColumn(name = "app_instance_user_id"),
            inverseJoinColumns = @JoinColumn(name = "channel_id")
    )
    private Set<AppInstanceUser> users;
}

