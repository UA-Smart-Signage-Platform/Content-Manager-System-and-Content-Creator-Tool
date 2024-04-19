package deti.uas.uasmartsignage.Configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Primary
public class CustomUserDetailsService implements UserDetailsService {

    private final List<UserDetails> users = new ArrayList<>();

    private final UserDetails defaultUser = User.builder()
        .username("miguelcruzeiro@ua.pt")
        .password("{bcrypt}$2a$10$EBdA5hBJs3DBtWMme9A7fO1RD5k2B3wQsP1zsAqjOh4K/WA7bqA8W") // bcrypt hash of "password"
        .roles("ADMIN", "USER")
        .build();

    public CustomUserDetailsService() {
        users.add(defaultUser);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return users.stream()
            .filter(user -> user.getUsername().equals(username))
            .findFirst()
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public void addUser(UserDetails newUser) {
        users.add(newUser);
    }

    public void deleteUser(String username) {
        users.removeIf(user -> user.getUsername().equals(username));
    }
}
