package pro.inmost.amazon.chime.service;

import pro.inmost.amazon.chime.model.dto.UserDTO;
import pro.inmost.amazon.chime.model.entity.User;

public interface UserService {

    User findUserByUsername(String username);

    User findUserByEmail(String email);

    String getCurrentUserEmail();

    User getCurrentUser();
}
