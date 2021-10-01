package pro.inmost.amazon.chime.service;

import org.springframework.web.multipart.MultipartFile;
import pro.inmost.amazon.chime.exeption.UsernameAlreadyTakenException;
import pro.inmost.amazon.chime.model.dto.UpdateProfileRequest;
import pro.inmost.amazon.chime.model.dto.UpdateProfileResponse;
import pro.inmost.amazon.chime.model.dto.UserProfile;
import pro.inmost.amazon.chime.model.entity.User;

public interface UserService {
    User findUserByUsername(String username);

    User findUserByEmail(String email);

    String getCurrentUserEmail();

    User getCurrentUser();

    UserProfile getUserProfile();

    void uploadUserPhoto(MultipartFile file) throws Exception;

    UpdateProfileResponse updateUserProfile(UpdateProfileRequest updateProfileRequest) throws UsernameAlreadyTakenException;

    String getUserAvatarUrl(Long userId);

    void deleteAccount(String email);
}
