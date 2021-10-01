package pro.inmost.amazon.chime.service;

import pro.inmost.amazon.chime.exception.UserWithEmailAlreadyExistsException;
import pro.inmost.amazon.chime.model.dto.UserDTO;

public interface RegistrationService {

    <T> T registerNewUserAccount(UserDTO userDTO) throws UserWithEmailAlreadyExistsException;


}
