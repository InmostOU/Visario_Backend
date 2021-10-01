package pro.inmost.amazon.chime.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "messages")
public class Message implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "message_id")
    private String messageId;

    @Column(name = "content")
    private String content;

    @Column
    private String metadata;

    @ManyToOne
    @JoinColumn(name = "channel_id")
    private Channel channel;

    @Column(name = "created_timestamp")
    private Date createdTimestamp;

    @Column(name = "last_edited_timestamp")
    private Date lastEditedTimestamp;

    @Column
    private Boolean redacted;

    @Column
    private Boolean delivered;

    @Column(name = "user_to_user")
    private Boolean userToUser;

    @Column(name = "with_attachment")
    private Boolean withAttachment;

    @OneToOne
    @JoinColumn(name = "attachment_id")
    private Attachment attachment;

    @ManyToOne
    @JoinColumn(name = "app_instance_user_id")
    private AppInstanceUser appInstanceUser;
}
