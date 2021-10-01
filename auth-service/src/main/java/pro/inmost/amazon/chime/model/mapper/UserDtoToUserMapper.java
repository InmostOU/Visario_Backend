package pro.inmost.amazon.chime.model.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import pro.inmost.amazon.chime.model.entity.User;
import pro.inmost.amazon.chime.model.dto.UserDTO;
import pro.inmost.amazon.chime.repositories.RoleRepository;
import javax.annotation.PostConstruct;
import java.util.Date;

@Component
public class UserDtoToUserMapper {

    private static RoleRepository roleRepository;

    @Autowired
    private RoleRepository injectedRepository;

    @PostConstruct
    public void init() {
        roleRepository = injectedRepository;
    }

    public static User map(UserDTO userDTO) {
        return userDTO == null ? null :
                User.builder()
                .username(userDTO.getUsername())
                .password(BCrypt.hashpw(userDTO.getPassword(), BCrypt.gensalt()))
                .email(userDTO.getEmail())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName() == null ? "" : userDTO.getLastName())
                .birthdayInMilliseconds(userDTO.getBirthdayInMilliseconds() == null ? 0 : userDTO.getBirthdayInMilliseconds())
                .isActive(false)
                .createdAt(new Date())
                .role(roleRepository.findById(1l).get())
                .showEmailTo("EVERYONE")
                .showPhoneNumberTo("EVERYONE")
                .build();
    }
}
