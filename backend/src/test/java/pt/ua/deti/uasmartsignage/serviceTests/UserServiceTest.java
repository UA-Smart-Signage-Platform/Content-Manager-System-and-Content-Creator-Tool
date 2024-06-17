package pt.ua.deti.uasmartsignage.serviceTests;

import pt.ua.deti.uasmartsignage.services.CustomUserDetailsService;
import pt.ua.deti.uasmartsignage.services.UserService;
import pt.ua.deti.uasmartsignage.Repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pt.ua.deti.uasmartsignage.models.AppUser;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @InjectMocks
    private UserService service;

    @Test
    void testGetAllUsers(){
        AppUser user = new AppUser();
        user.setEmail("asdfgh");
        user.setPassword("123456");
        user.setRole("admin");

        AppUser user2 = new AppUser();
        user2.setEmail("qwerty");
        user2.setPassword("123456");
        user2.setRole("user");

        when(repository.findAll()).thenReturn(List.of(user, user2));

        List<AppUser> users = service.getAllUsers();

        assertThat(users).hasSize(2).contains(user, user2);
    }

    @Test
    void testGetUsersByRole() {
        AppUser user = new AppUser();
        user.setEmail("asdfgh");
        user.setPassword("123456");
        user.setRole("admin");

        AppUser user2 = new AppUser();
        user2.setEmail("qwerty");
        user2.setPassword("123456");
        user2.setRole("user");

        AppUser user3 = new AppUser();
        user3.setEmail("zxcvbn");
        user.setPassword("123456");
        user.setRole("admin");

        when(repository.findByRole("admin")).thenReturn(List.of(user, user3));

        List<AppUser> users = service.getUsersByRole("admin");

        assertThat(users).hasSize(2).contains(user, user3);
    }

    @Test
    void testGetUserByEmail(){
        AppUser user = new AppUser();
        user.setEmail("asdfgh");
        user.setPassword("123456");
        user.setRole("admin");

        when(repository.findByEmail("asdfgh")).thenReturn(user);

        AppUser user2 = service.getUserByEmail("asdfgh");

        assertThat(user2).isEqualTo(user);
    }

    @Test
    void testDeleteUser() {
        AppUser user = new AppUser();
        user.setEmail("asdfgh");
        user.setPassword("123456");
        user.setRole("admin");

        when(repository.findById(1L)).thenReturn(java.util.Optional.of(user));

        service.deleteUser(1L);

        assertThat(repository.existsById(1L)).isFalse();
    }

    @Test
    void testGetUserById(){
        AppUser user = new AppUser();
        user.setEmail("asdfgh");
        user.setPassword("123456");
        user.setRole("admin");

        when(repository.findById(1L)).thenReturn(java.util.Optional.of(user));

        AppUser user2 = service.getUserById(1L);

        assertThat(user2).isEqualTo(user);
    }

    @Test
    void testUpdateUser(){
        AppUser user = new AppUser();
        user.setEmail("asdfgh");
        user.setPassword("123456");
        user.setRole("user");

        AppUser user2 = new AppUser();
        user2.setEmail("ua@gmail.com");
        user2.setPassword("1234");
        user2.setRole("user");

        when(repository.findById(1L)).thenReturn(Optional.of(user));
        when(repository.save(Mockito.any())).thenReturn(user2);

        AppUser user3 = service.updateUser(1L, user2);

        assertThat(user3.getEmail()).isEqualTo("ua@gmail.com");
        assertThat(user3.getPassword()).isEqualTo("1234");
        assertThat(user3.getRole()).isEqualTo("user");
    }

    @Test
    void testSaveUser(){
        AppUser user = new AppUser(null, "asdfgh","user","123456");

        when(repository.save(user)).thenReturn(user);

        AppUser savedUser = service.saveUser(user);
        assertThat(savedUser.getEmail()).isEqualTo("asdfgh");
        assertThat(savedUser.getPassword()).isEqualTo("123456");
        assertThat(savedUser.getRole()).isEqualTo("user");
    }

    @Test
    void testGenerateRandomPassword() {
        String password = service.generateRandomPassword();

        assertThat(password).isNotNull();
        assertEquals(20,password.length());
        assertThat(password.chars().anyMatch(Character::isUpperCase)).isTrue();
        assertThat(password.chars().anyMatch(Character::isLowerCase)).isTrue();
        assertThat(password.chars().anyMatch(ch -> "!@#$%^&*()-_=+[{]}|;:,<.>/?".indexOf(ch) >= 0)).isTrue();
    }

    @Test
    void testSaveAdminUser(){
        AppUser user = new AppUser(null, "admin","admin","admin");

        when(repository.save(user)).thenReturn(user);

        AppUser savedUser = service.saveAdminUser(user);
        assertThat(savedUser.getEmail()).isEqualTo("admin");
        assertThat(savedUser.getRole()).isEqualTo("admin");
    }
}
