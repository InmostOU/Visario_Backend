package pro.inmost.amazon.chime.model.mapper;

import pro.inmost.amazon.chime.model.dto.UserDTO;
import pro.inmost.amazon.chime.model.entity.User;

public class UserToUserDTOMapper {
    public static UserDTO map(User user) {
        return user == null ? null :
                UserDTO.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .birthdayInMilliseconds(user.getBirthdayInMilliseconds())
                .isActive(user.getIsActive())
                .build();
    }
}
