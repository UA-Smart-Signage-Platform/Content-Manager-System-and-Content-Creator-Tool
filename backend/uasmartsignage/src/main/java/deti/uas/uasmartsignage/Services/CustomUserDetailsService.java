package deti.uas.uasmartsignage.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import deti.uas.uasmartsignage.Models.AppUser;
import deti.uas.uasmartsignage.Repositories.UserRepository;

import java.security.SecureRandom;

import org.mindrot.jbcrypt.BCrypt;

@Service
@Primary
public class CustomUserDetailsService implements UserDetailsService {

    private final List<UserDetails> users = new ArrayList<>();

    private final UserDetails adminUser = User.builder()
        .username("admin")
        .password(BCrypt.hashpw("admin", BCrypt.gensalt()))
        .roles("ADMIN")
        .build();


    @Autowired
    public CustomUserDetailsService( UserRepository userRepository) {
        users.add(adminUser);

        List<AppUser> appUsers = userRepository.findAll();
        for (AppUser appUser : appUsers) {
            UserDetails userDetails = User.builder()
                .username(appUser.getEmail())
                .password(BCrypt.hashpw(generateRandomPassword(), BCrypt.gensalt()))
                .roles(appUser.getRole())
                .build();

            users.add(userDetails);
        }
    }

    /**
     * Loads a user by username
     * @param username The username of the user
     * @return The user
     * @throws UsernameNotFoundException If the user is not found
    */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return users.stream()
            .filter(user -> user.getUsername().equals(username))
            .findFirst()
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    /**
     * Loads a user by username and password
     * @param username The username of the user
     * @param password The password of the user
     * @return The user
     * @throws UsernameNotFoundException If the user is not found
    */
    public UserDetails loadUserByUsernameAndPassword(String username, String password) throws UsernameNotFoundException {
        return users.stream()
            .filter(user -> user.getUsername().equals(username) && BCrypt.checkpw(password, user.getPassword()))
            .findFirst()
            .orElseThrow(() -> new UsernameNotFoundException("Invalid username or password"));
    }


    public void addUser(UserDetails newUser) {
        users.add(newUser);
    }

    public void deleteUser(String username) {
        users.removeIf(user -> user.getUsername().equals(username));
    }

    /**
     * Updates the password of a user
     * @param username The username of the user
     * @param newPassword The new password
     * @throws UsernameNotFoundException If the user is not found
     * @throws BadCredentialsException If the current password is incorrect
    */
    public void updateUserPassword(String username, String newPassword) throws UsernameNotFoundException {
        Optional<UserDetails> userOptional = users.stream()
            .filter(user -> user.getUsername().equals(username))
            .findFirst();
    
        if (userOptional.isPresent()) {
            UserDetails user = userOptional.get();
            if (user instanceof User existingUser) {
    
                List<String> roles = existingUser.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .map(role -> role.startsWith("ROLE_") ? role.substring(5) : role)
                    .toList();
    
                UserDetails updatedUser = User.builder()
                    .username(existingUser.getUsername())
                    .password(BCrypt.hashpw(newPassword, BCrypt.gensalt()))
                    .roles(roles.toArray(new String[0]))
                    .build();
                
                users.remove(user);  // Remove old user
                users.add(updatedUser);  // Add updated user
            } else {
                throw new IllegalArgumentException("User type not supported");
            }
        } else {
            throw new BadCredentialsException("Bad credentials");
        }
    }

    /**
     * Generates a random password
     * @return The random password
    */
    private String generateRandomPassword() {
        // Define the characters that can be used in the random password
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+[{]}|;:,<.>/?";
        StringBuilder password = new StringBuilder();
    
        // Generate a random password of length 20
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < 20; i++) {
            int index = random.nextInt(chars.length());
            password.append(chars.charAt(index));
        }
    
        return password.toString();
    }
    
    
    
}
