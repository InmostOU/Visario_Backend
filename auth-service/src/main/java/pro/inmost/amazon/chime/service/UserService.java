package pro.inmost.amazon.chime.service;

import pro.inmost.amazon.chime.model.dto.UserDTO;
import pro.inmost.amazon.chime.model.entity.User;

public interface UserService {

    User saveUser(User user);

    UserDTO findUserByUsername(String username);

    UserDTO findUserByEmail(String email);
    User findUser(String email);
    User findById(Long id);
}
