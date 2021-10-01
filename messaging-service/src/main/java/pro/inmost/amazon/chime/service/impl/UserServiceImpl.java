package pro.inmost.amazon.chime.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import pro.inmost.amazon.chime.model.dto.UserDTO;
import pro.inmost.amazon.chime.model.entity.User;
import pro.inmost.amazon.chime.model.mapper.UserToUserDTOMapper;
import pro.inmost.amazon.chime.repository.RoleRepository;
import pro.inmost.amazon.chime.repository.UserRepository;
import pro.inmost.amazon.chime.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

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
}
