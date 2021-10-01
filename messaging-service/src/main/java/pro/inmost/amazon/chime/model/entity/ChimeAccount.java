package pro.inmost.amazon.chime.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "chime_accounts")
public class ChimeAccount implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "account_id")
    private String accountId;

    @Column(name = "aws_account_id")
    private Long awsAccountId;

    @Column(name = "account_type")
    private String accountType;

    @Column(name = "created_time_stamp")
    private Date createdTimeStamp;

    @Column(name = "default_license")
    private String defaultLicense;

}
