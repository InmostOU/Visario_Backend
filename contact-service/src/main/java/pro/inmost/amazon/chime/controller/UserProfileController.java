package pro.inmost.amazon.chime.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pro.inmost.amazon.chime.exeption.UsernameAlreadyTakenException;
import pro.inmost.amazon.chime.model.dto.ContactDetails;
import pro.inmost.amazon.chime.model.dto.PhotoAddResponse;
import pro.inmost.amazon.chime.model.dto.UpdateProfileRequest;
import pro.inmost.amazon.chime.model.dto.UpdateProfileResponse;
import pro.inmost.amazon.chime.model.dto.UserProfile;
import pro.inmost.amazon.chime.service.UserService;

import java.util.Date;

@AllArgsConstructor
@RequestMapping("/user")
@RestController
public class UserProfileController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserProfile> getUserProfile() {
        return ResponseEntity.ok(userService.getUserProfile());
    }

    @PostMapping("/profile/update")
    public ResponseEntity<UpdateProfileResponse> updateProfile(@RequestBody UpdateProfileRequest updateProfileRequest) throws UsernameAlreadyTakenException {
        return ResponseEntity.ok(userService.updateUserProfile(updateProfileRequest));
    }

    @PostMapping("/uploadUserPhoto")
    public ResponseEntity<PhotoAddResponse> uploadUserPhoto(@RequestPart(value = "file") MultipartFile file) throws Exception {
        userService.uploadUserPhoto(file);

        return ResponseEntity.ok(
                PhotoAddResponse.builder()
                        .timestamp(new Date().getTime())
                        .status(200)
                        .message("Photo is added successfully")
                        .path("/user/uploadUserPhoto")
                        .build()
        );
    }

    @GetMapping("/deleteAccount/{email}")
    public ResponseEntity<String> deleteAccount(@PathVariable String email) {
        userService.deleteAccount(email);

        return ResponseEntity.ok("User with email " + email + " was deleted");
    }
}
