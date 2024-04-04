package deti.uas.uasmartsignage.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import deti.uas.uasmartsignage.Repositories.UserRepository;
import deti.uas.uasmartsignage.Models.User;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getUsersByRole(Integer role) {
        return userRepository.findByRole(role);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    
}
