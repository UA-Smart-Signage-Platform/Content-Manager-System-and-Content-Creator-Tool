package deti.uas.uasmartsignage.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import deti.uas.uasmartsignage.Repositories.UserRepository;
import deti.uas.uasmartsignage.Models.AppUser;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import deti.uas.uasmartsignage.Configuration.CustomUserDetailsService;

import org.mindrot.jbcrypt.BCrypt;

@Service
public class UserService implements UserDetailsService {

    private final CustomUserDetailsService customUserDetailsService;

    private final UserRepository userRepository;

    @Autowired
    public UserService(CustomUserDetailsService customUserDetailsService, UserRepository userRepository) {
        this.customUserDetailsService = customUserDetailsService;
        this.userRepository = userRepository;
    }

    public AppUser getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public AppUser saveUser(AppUser user) {
        UserDetails newUser = User.builder()
            .username(user.getEmail())
            // bcrypt hash of a random password
            .password(BCrypt.hashpw(generateRandomPassword(), BCrypt.gensalt()))
            .roles(user.getRole())
            .build();
        
        customUserDetailsService.addUser(newUser);
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        AppUser user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return;
        }
        customUserDetailsService.deleteUser(user.getEmail());
        userRepository.deleteById(id);
    }

    public AppUser updateUser(AppUser user) {
        return userRepository.save(user);
    }

    public List<AppUser> getAllUsers() {
        return userRepository.findAll();
    }

    public List<AppUser> getUsersByRole(String role) {
        return userRepository.findByRole(role);
    }

    public AppUser getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return customUserDetailsService.loadUserByUsername(username);
    }

    private String generateRandomPassword() {
        // Define the characters that can be used in the random password
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+[{]}|;:,<.>/?";
        StringBuilder password = new StringBuilder();
    
        // Generate a random password of length 20
        for (int i = 0; i < 20; i++) {
            int index = (int) (Math.random() * chars.length());
            password.append(chars.charAt(index));
        }
    
        return password.toString();
    }


    
}
