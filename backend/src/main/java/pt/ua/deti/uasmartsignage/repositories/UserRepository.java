package pt.ua.deti.uasmartsignage.Repositories;

import pt.ua.deti.uasmartsignage.models.AppUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long>{

    AppUser findByEmail(String email);
    List<AppUser> findByRole(String role);
}
