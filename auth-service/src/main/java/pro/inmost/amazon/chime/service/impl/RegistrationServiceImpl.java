package pro.inmost.amazon.chime.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import pro.inmost.amazon.chime.exception.UserWithEmailAlreadyExistsException;
import pro.inmost.amazon.chime.model.dto.UserDTO;
import pro.inmost.amazon.chime.model.entity.User;
import pro.inmost.amazon.chime.model.mapper.UserDtoToUserMapper;
import pro.inmost.amazon.chime.repositories.VerificationTokenRepo;
import pro.inmost.amazon.chime.service.RegistrationService;
import pro.inmost.amazon.chime.service.UserService;
import pro.inmost.amazon.chime.service.VerificationTokenService;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    @Autowired
    private VerificationTokenService verificationTokenService;

    @Autowired
    private UserService userService;

    @Autowired
    private VerificationTokenRepo verificationTokenRepo;

    @Value("${link.for.resetting.password}")
    private String LINK_FOR_RESETTING_PASSWORD;

    public <T> T registerNewUserAccount(UserDTO userDTO) throws UserWithEmailAlreadyExistsException {

        if (checkEmailExists(userDTO.getEmail()))
            throw new UserWithEmailAlreadyExistsException("Username with email " + userDTO.getEmail() + " is already exists.");

        User user = UserDtoToUserMapper.map(userDTO);

        Long tokenId = verificationTokenService.createVerification(userDTO.getEmail(), userService.saveUser(user).getId());

        user.setVerificationToken(verificationTokenRepo.findById(tokenId).get());


        return (T) userService.saveUser(user);
    }


//    public <T> T forgotPassword(String email)  {
//        User user = getUserByEmail(email.toLowerCase());
//
//        if (user == null)
//            return (T) Error.USER_WITH_PROVIDED_EMAIL_NOT_FOUND;
//
//        String linkForRestoring = LINK_FOR_RESETTING_PASSWORD + jwtProvider.generateAccessToken(coreUser.getEmail(), EXPIRATION_TIME_FOR_RESTORING_PASSWORD_TOKEN);
//        //   mailJetService.sendResetPasswordToUser(user.getEmail(), user.getFirstName(), linkForRestoring);
//    }
//
//    @Override
//    public UserInformationForm getCurrentUserInformation() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String email = authentication.getName();
//        CoreUser coreUser = userRepository.findByEmail(email).get();
//        UserInformationForm userInformationForm = UserInformationFormMapper.map(coreUser);
//        return userInformationForm;
//    }
//
//
//    @Override
//    public long getCurrentUserId() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String email = authentication.getName();
//        return userRepository.findByEmail(email).get().getId();
//    }
//
//    @Override
//    public CoreUser getCurrentUser() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String email = authentication.getName();
//        CoreUser coreUser = userRepository.findByEmail(email).get();
//        return coreUser;
//    }
//
//    @Override
//    public void setNewPassword(String password) {
//        CoreUser currentCoreUser = getCurrentUser();
//        currentCoreUser.setPassword(bCryptPasswordEncoder.encode(password));
//        userRepository.save(currentCoreUser);
//    }
//
//
    private boolean checkEmailExists(String email) {
        return getUserByEmail(email) == null ? false : true;
    }
//
//    @Override
//    public void deleteAccount(UserDeleteInfo userDeleteInfo) {
//        CoreUser coreUser = userRepository.findById(getCurrentUserId()).orElseThrow(() -> new UnsupportedOperationException("User does not exist"));
//      /*  user.setValidUntil(timeService.nowForUser(user.getId()));
//        ExitReason exitReason = new ExitReason();
//        exitReason.setReasonName(userDeleteInfo.getReason());
//        exitReason.setUserEmail(user.getEmail());
//        exitReasonRepository.save(exitReason);*/
//    }

    private UserDTO getUserByEmail(String email) {
        UserDTO user = userService.findUserByEmail(email);

        return user != null ? user : null;
    }
}
