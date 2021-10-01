package pro.inmost.amazon.chime.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.inmost.amazon.chime.model.entity.User;
import pro.inmost.amazon.chime.model.dto.UserDTO;
import pro.inmost.amazon.chime.model.mapper.UserToUserDTOMapper;
import pro.inmost.amazon.chime.repositories.RoleRepository;
import pro.inmost.amazon.chime.repositories.UserRepository;
import pro.inmost.amazon.chime.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository repository, RoleRepository roleRepository) {

        this.userRepository = repository;
        this.roleRepository = roleRepository;
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public UserDTO findUserByUsername(String username) {
       return UserToUserDTOMapper.map(userRepository.findByUsername(username));
    }

    public UserDTO findUserByEmail(String email) {
        return UserToUserDTOMapper.map(userRepository.findByEmail(email));
    }

    public User findById(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public User findUser(String email) {
        return userRepository.findByEmail(email);
    }
}
