package pro.inmost.amazon.chime.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pro.inmost.amazon.chime.model.entity.AppInstanceUser;
import pro.inmost.amazon.chime.model.entity.ChimeAccount;
import pro.inmost.amazon.chime.model.entity.User;
import pro.inmost.amazon.chime.model.entity.VerificationToken;
import pro.inmost.amazon.chime.repositories.JPAUserRepository;
import pro.inmost.amazon.chime.repositories.VerificationTokenRepo;
import pro.inmost.amazon.chime.service.*;

import java.time.LocalDateTime;

@Service
public class VerificationTokenServiceImpl implements VerificationTokenService {

    @Value("${app.instance.arn}")
    private String appInstanceArn;

    @Value("${chime.account.id}")
    private String accountId;

    @Autowired
    private VerificationTokenRepo verificationTokenRepository;

    @Autowired
    private SendingMailService sendingMailService;

    @Autowired
    private UserService userService;

    @Autowired
    private JPAUserRepository userRepository;

    @Autowired
    private ChimeAccountsService chimeAccountsService;

    @Autowired
    private ChimeAppInstanceUserService chimeAppInstanceUserService;

    public Long createVerification(String email, Long userId) {

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setUser_id(userId);
        Long tokenId = verificationTokenRepository.save(verificationToken).getId();
        sendingMailService.sendVerificationMail(email, verificationToken.getToken());

        return tokenId;
    }

    public ResponseEntity<String> verifyUser(String token) {

        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);

        if (verificationToken == null)
            return ResponseEntity.badRequest().body("Invalid verification token.");

        if (verificationToken.getExpiredDateTime().isBefore(LocalDateTime.now()))
            return ResponseEntity.unprocessableEntity().body("Expired verification token.");

        verificationToken.setConfirmedDateTime(LocalDateTime.now());
        verificationToken.setStatus(VerificationToken.STATUS_VERIFIED);
        setUserIsActive(verificationToken.getUser_id());
        verificationTokenRepository.save(verificationToken);

        // get verificated user
        User user = userRepository.findById(verificationToken.getUser_id()).get();
        // create amazon chime account
//        ChimeAccount account = chimeAccountsService
//                .createAccount(user.getUsername());
        // create app instance user
        AppInstanceUser appInstanceUser =
                chimeAppInstanceUserService.createUser(chimeAccountsService.findById(73l),
                        user.getFirstName() + " " + user.getLastName(), appInstanceArn, user);
        // bind chime account to app instance user
        //account.setAppInstanceUser(appInstanceUser);


        return ResponseEntity.ok("You have successfully verified your email address.");
    }

    private void setUserIsActive(Long userId) {
        User user = userService.findById(userId);
        user.setIsActive(true);
        userRepository.save(user);
    }
}
