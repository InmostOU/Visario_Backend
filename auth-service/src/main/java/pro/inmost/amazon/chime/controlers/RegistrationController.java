package pro.inmost.amazon.chime.controlers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.inmost.amazon.chime.exception.UserWithEmailAlreadyExistsException;
import pro.inmost.amazon.chime.model.dto.RegistrationResponse;
import pro.inmost.amazon.chime.model.dto.UserDTO;
import pro.inmost.amazon.chime.service.RegistrationService;
import pro.inmost.amazon.chime.service.VerificationTokenService;
import javax.validation.Valid;
import java.util.Date;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService userService;
    private final VerificationTokenService verificationTokenService;

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> signUp(@Valid @RequestBody UserDTO userDTO) throws UserWithEmailAlreadyExistsException {
        userService.registerNewUserAccount(userDTO);

        RegistrationResponse response = RegistrationResponse.builder()
                .timestamp(new Date().getTime())
                .status(200)
                .message("Registration successful")
                .path("/auth/register")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/verify-user")
    @ResponseBody
    public String verifyUser(String code) {
        return verificationTokenService.verifyUser(code).getBody();
    }

//    @PostMapping("refresh-token")
//    public ResponseEntity<AccessAndRefreshTokens> refreshToken(@RequestBody RefreshTokenForm form) throws InvalidRefreshTokenException {
//        AccessAndRefreshTokens tokens = authenticationService.refreshTokens(form.getRefreshToken());
//        return ResponseEntity.ok(tokens);
//    }
//
//    @PostMapping("forgot-password")
//    public ResponseEntity resetPassword(@RequestBody PasswordResettingForm form) throws UserWithThisEmailNotFoundException {
//        userService.forgotPassword(form.getEmail());
//        return ResponseEntity.status(HttpStatus.OK).build();
//    }
//
//
//    @PostMapping("new-password")
//    public ResponseEntity setNewPassword(@Valid @RequestBody NewPasswordForm form) {
//        userService.setNewPassword(form.getPassword());
//        return ResponseEntity.status(HttpStatus.OK).build();
//    }

   /* @PostMapping("delete-user")
    public ResponseEntity deleteUser(@RequestBody UserDeleteInfo userDeleteInfo) {
        userService.deleteAccount(userDeleteInfo);
        return ResponseEntity.ok().build();
    }*/
}
