package pro.inmost.amazon.chime.service.impl;

import com.amazonaws.services.connect.model.UserNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pro.inmost.amazon.chime.exeption.UsernameAlreadyTakenException;
import pro.inmost.amazon.chime.model.dto.FileUrlDTO;
import pro.inmost.amazon.chime.model.dto.UpdateProfileRequest;
import pro.inmost.amazon.chime.model.dto.UpdateProfileResponse;
import pro.inmost.amazon.chime.model.dto.UserProfile;
import pro.inmost.amazon.chime.model.entity.AppInstanceUser;
import pro.inmost.amazon.chime.model.entity.Contacts;
import pro.inmost.amazon.chime.model.entity.Message;
import pro.inmost.amazon.chime.model.entity.User;
import pro.inmost.amazon.chime.model.entity.UserAvatar;
import pro.inmost.amazon.chime.repository.AppInstanceUserRepository;
import pro.inmost.amazon.chime.repository.ContactsRepository;
import pro.inmost.amazon.chime.repository.MessageRepository;
import pro.inmost.amazon.chime.repository.UserAvatarRepository;
import pro.inmost.amazon.chime.repository.UserRepository;
import pro.inmost.amazon.chime.service.UserService;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.apache.http.entity.ContentType.*;


@Service
public class UserServiceImpl implements UserService {
    private final FileStore fileStore;
    private final UserRepository userRepository;
    private final UserAvatarRepository userAvatarRepository;
    private final AppInstanceUserRepository appInstanceUserRepository;
    private final ContactsRepository contactsRepository;
    private final MessageRepository messageRepository;

    @Value("${amazonProperties.bucketName}")
    private String bucketName;

    @Value("${amazonProperties.maxSizeForFile}")
    private Long maxSizeForFile;

    public UserServiceImpl(UserRepository userRepository,
                           AppInstanceUserRepository appInstanceUserRepository,
                           FileStore fileStore, UserAvatarRepository userAvatarRepository,
                           ContactsRepository contactsRepository,
                           MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.appInstanceUserRepository = appInstanceUserRepository;
        this.fileStore = fileStore;
        this.userAvatarRepository = userAvatarRepository;
        this.contactsRepository = contactsRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @Override
    public User getCurrentUser() {
        return userRepository.findByEmail(getCurrentUserEmail());
    }

    @Override
    public UserProfile getUserProfile() {
        User user = getCurrentUser();
        String url = getUserAvatarUrl(null);


        return UserProfile.builder()
                .id(user.getId())
                .userArn(appInstanceUserRepository.findByUser(user).getAppInstanceUserArn())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .birthday(user.getBirthdayInMilliseconds())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber() != null ? user.getPhoneNumber() : "")
                .image(user.getAvatar() != null ? user.getAvatar() : url)
                .about(user.getAbout() != null ? user.getAbout() : "")
                .showEmailTo(user.getShowEmailTo() != null ? user.getShowEmailTo() : "")
                .showPhoneNumberTo(user.getShowPhoneNumberTo() != null ? user.getShowPhoneNumberTo() : "")
                .build();
    }


    @Override
    public void uploadUserPhoto(MultipartFile file) throws Exception {

        if (file.getSize() > maxSizeForFile) throw new IllegalStateException("File size to big ");

        if (file.isEmpty()) throw new IllegalStateException("Cannot upload empty file");
        System.out.println(file.getContentType());
        if (!Arrays.asList(IMAGE_PNG.getMimeType(),
                IMAGE_BMP.getMimeType(),
                IMAGE_GIF.getMimeType(),
                IMAGE_JPEG.getMimeType(),
                "image/*").contains(file.getContentType().toLowerCase())) {
            throw new IllegalStateException("FIle uploaded is not an image");
        }

        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));

        StringBuilder fileName;
        UserAvatar userAvatar = null;
        String oldUserAvatar = checkIfUserHaveAvatar();

        if (!oldUserAvatar.equals("")) {
            fileName = new StringBuilder(oldUserAvatar);
            userAvatar = userAvatarRepository.findByUser(getCurrentUser()).get();
            userAvatar.setExpTime(null);
        } else {
            fileName = new StringBuilder(UUID.randomUUID() + "." + getExtensionByStringHandling(file.getOriginalFilename()));
            userAvatar = new UserAvatar();
            userAvatar.setUser(getCurrentUser());
            userAvatar.setFileName(fileName.toString());
        }

        userAvatarRepository.save(userAvatar);
        try {
            fileStore.upload(bucketName, fileName.toString(), Optional.of(metadata), file.getInputStream());
        } catch (IOException e) {
            throw new IllegalStateException("Failed to upload file", e);
        }
    }

    public String getExtensionByStringHandling(String filename) throws Exception {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1)).orElseThrow(() -> new Exception("cant recognize file extension"));
    }


    public String getCurrentUserAvatarUrl() {
        if (userAvatarRepository.findByUser(getCurrentUser()).isPresent()) {
            UserAvatar userAvatar = userAvatarRepository.findByUser(getCurrentUser()).get();
            return fileStore.getFileUrl(bucketName, userAvatar.getFileName()).getFileUrl();
        } else return "";
    }

  
    @Override
    public String getUserAvatarUrl(Long userId) {
        UserAvatar userAvatar = null;
        try {
            if (userId != null) {
                userAvatar = userAvatarRepository.findByUser(userRepository.findById(userId).get()).get();
            } else {
                userAvatar = userAvatarRepository.findByUser(getCurrentUser()).get();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        if (userAvatar != null) {
            Date expiration = new Date();
            long timeNow = expiration.getTime();
            if (userAvatar.getExpTime() != null && userAvatar.getExpTime() > timeNow) {
                return userAvatar.getFileUrl();
            } else {
                FileUrlDTO newPhoto = fileStore.getFileUrl(bucketName, userAvatar.getFileName());
                userAvatar.setFileUrl(newPhoto.getFileUrl());
                userAvatar.setExpTime(newPhoto.getExpTimeMillis());
                userAvatarRepository.save(userAvatar);
                return newPhoto.getFileUrl();
            }
        } else return "";
    }

    private String checkIfUserHaveAvatar() {
        Optional<UserAvatar> userAvatar = userAvatarRepository.findByUser(getCurrentUser());
        String fileName = "";
        if (userAvatar.isPresent())
            fileName = userAvatar.get().getFileName();
        return fileName;
    }

    @Override
    public UpdateProfileResponse updateUserProfile(UpdateProfileRequest updateProfileRequest) throws UsernameAlreadyTakenException {
        User user = getCurrentUser();

        if (userRepository.findByUsername(updateProfileRequest.getUsername()) != null && !user.getUsername().equals(updateProfileRequest.getUsername()))
            throw new UsernameAlreadyTakenException("Username " + updateProfileRequest.getUsername() + " has been already taken.");


        user.setFirstName(updateProfileRequest.getFirstName());
        user.setLastName(updateProfileRequest.getLastName());
        user.setUsername(updateProfileRequest.getUsername());
        user.setBirthdayInMilliseconds(updateProfileRequest.getBirthday());
        user.setAbout(updateProfileRequest.getAbout());
        user.setShowEmailTo(updateProfileRequest.getShowEmailTo());
        user.setShowPhoneNumberTo(updateProfileRequest.getShowPhoneNumberTo());

        userRepository.save(user);

        return UpdateProfileResponse.builder()
                .status(200)
                .timestamp(new Date().getTime())
                .message("Profile has been updated successfully.")
                .path("/user/profile/update")
                .build();
    }

    @Override
    public void deleteAccount(String email) {
        User user = findUserByEmail(email);
        if (user == null) throw new UserNotFoundException("User with email " + email + " is not found");
        AppInstanceUser appInstanceUser = appInstanceUserRepository.findByUser(user);
        Set<Contacts> contacts = contactsRepository.findAllByOwnerId(user.getId());
        userAvatarRepository.findByUser(user).ifPresent(avatar -> userAvatarRepository.delete(avatar));
        List<Message> userMessages = messageRepository.findByAppInstanceUser(appInstanceUser);

        userMessages.forEach(message -> messageRepository.delete(message));
        contacts.forEach(contact -> contactsRepository.deleteById(contact.getId()));

        appInstanceUserRepository.delete(appInstanceUser);
        userRepository.delete(user);
    }
}
