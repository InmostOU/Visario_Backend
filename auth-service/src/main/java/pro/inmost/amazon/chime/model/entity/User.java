package pro.inmost.amazon.chime.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@ToString //delete before release
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "birthday")
    private long birthdayInMilliseconds;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "is_active")
    private Boolean isActive;

    @OneToOne
    @JoinColumn(name = "verification_token_id", referencedColumnName = "id")
    private VerificationToken verificationToken;

    private String avatar;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "about")
    private String about;

    @Column(name = "online")
    private Boolean online;

    @Column(name = "show_email_to")
    private String showEmailTo;

    @Column(name = "show_phone_number_to")
    private String showPhoneNumberTo;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
}

