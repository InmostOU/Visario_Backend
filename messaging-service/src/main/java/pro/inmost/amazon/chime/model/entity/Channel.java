package pro.inmost.amazon.chime.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.FetchMode;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
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

    @Column
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_app_user_id")
    private AppInstanceUser createdBy;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "channels_to_users",
            joinColumns = @JoinColumn(name = "channel_id"),
            inverseJoinColumns = @JoinColumn(name = "app_instance_user_id")
    )
    private Set<AppInstanceUser> users;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "moderator_rights",
            joinColumns = @JoinColumn(name = "channel_id"),
            inverseJoinColumns = @JoinColumn(name = "app_instance_user_id")
    )
    private List<AppInstanceUser> moderators;
}

