package deti.uas.uasmartsignage.Repositories;

import deti.uas.uasmartsignage.Models.AppUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long>{

    AppUser findByEmail(String email);
    List<AppUser> findByRole(String role);
}
