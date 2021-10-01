package pro.inmost.amazon.chime.controlers;

import pro.inmost.amazon.chime.errors.Error;
import pro.inmost.amazon.chime.exception.BadCredentialsException;
import pro.inmost.amazon.chime.exception.UserNotActiveException;
import pro.inmost.amazon.chime.model.dto.LoginRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.inmost.amazon.chime.service.LoginService;

@RestController
@RequestMapping(value = "/auth")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        Object response = loginService.login(loginRequestDTO);

        if (response instanceof Error && response == Error.USER_IS_NOT_ACTIVATED) {
            throw new UserNotActiveException("Account isn't activated yet. Please confirm your account.");
        }
        if (response instanceof Error) {
            throw new BadCredentialsException("Bad credentials.");
        }

        return ResponseEntity.ok(response);

    }

}
