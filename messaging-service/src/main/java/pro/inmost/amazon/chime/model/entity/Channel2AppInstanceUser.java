package pro.inmost.amazon.chime.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "channels_to_users")
public class Channel2AppInstanceUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "app_instance_user_id")
    private Long appInstanceUserId;

    @Column(name = "channel_id")
    private Long channelId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Channel2AppInstanceUser that = (Channel2AppInstanceUser) o;

        return appInstanceUserId.longValue() == that.getAppInstanceUserId().longValue()
                                        &&
               channelId.longValue() == that.channelId.longValue();
    }

    @Override
    public int hashCode() {
        return Objects.hash(appInstanceUserId, channelId);
    }
}
