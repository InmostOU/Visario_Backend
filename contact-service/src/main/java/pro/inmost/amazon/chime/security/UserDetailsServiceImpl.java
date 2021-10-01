package pro.inmost.amazon.chime.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pro.inmost.amazon.chime.repository.UserRepository;

import static java.util.Collections.emptyList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository corePersonRepository;

    public UserDetailsServiceImpl(UserRepository corePersonRepository) {
        this.corePersonRepository = corePersonRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // TO-do rename User
        pro.inmost.amazon.chime.model.entity.User applicationUser = corePersonRepository.findByEmail(username);
        pro.inmost.amazon.chime.model.entity.User corePerson = applicationUser;
        return new User(corePerson.getEmail(), corePerson.getPassword(), emptyList());
    }
}