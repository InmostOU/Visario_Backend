package pro.inmost.amazon.chime.service.impl;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.inmost.amazon.chime.model.dto.*;
import pro.inmost.amazon.chime.errors.Error;
import pro.inmost.amazon.chime.model.entity.User;
import pro.inmost.amazon.chime.repositories.AppInstanceUserRepository;
import pro.inmost.amazon.chime.service.JwtService;
import pro.inmost.amazon.chime.service.LoginService;
import pro.inmost.amazon.chime.service.UserService;

import java.util.Date;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwt;

    @Autowired
    private AppInstanceUserRepository appInstanceUserRepository;

    public <T extends Object> T login(LoginRequestDTO loginRequestDTO) {

        UserDTO user = userService.findUserByEmail(loginRequestDTO.getEmail());

        if (user != null) {
            if (!user.isActive()) {
                return (T) Error.USER_IS_NOT_ACTIVATED;
            }
            else if (BCrypt.checkpw(loginRequestDTO.getPassword(), user.getPassword())) {
                User currentUser = userService.findUser(loginRequestDTO.getEmail());
                String accessToken = jwt.generateToken(user, "ACCESS");
                String refreshToken = jwt.generateToken(user, "REFRESH");
                return (T) LoginResponse.builder()
                        .timestamp(new Date().getTime())
                        .status(200)
                        .message("Login successful")
                        .path("/auth/login")
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .userProfile(
                                UserProfile.builder()
                                        .id(currentUser.getId())
                                        .userArn(appInstanceUserRepository.findByUser(currentUser).getAppInstanceUserArn())
                                        .firstName(currentUser.getFirstName())
                                        .lastName(currentUser.getLastName())
                                        .username(currentUser.getUsername())
                                        .birthday(currentUser.getBirthdayInMilliseconds())
                                        .email(currentUser.getEmail())
                                        .phoneNumber(currentUser.getPhoneNumber() != null ? currentUser.getPhoneNumber() : "")
                                        .image(currentUser.getAvatar() != null ? currentUser.getAvatar() : "")
                                        .about(currentUser.getAbout() != null ? currentUser.getAbout() : "")
                                        .showEmailTo(currentUser.getShowEmailTo() != null ? currentUser.getShowEmailTo() : "")
                                        .showPhoneNumberTo(currentUser.getShowPhoneNumberTo() != null ? currentUser.getShowPhoneNumberTo() : "")
                                        .build()
                        )
                        .build();

            } else {
                return (T) Error.CREDENTIALS_ARE_INCORRECT;
            }
        }

        return (T) Error.CREDENTIALS_ARE_INCORRECT;
    }
}
