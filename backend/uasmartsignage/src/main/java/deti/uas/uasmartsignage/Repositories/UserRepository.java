package deti.uas.uasmartsignage.Repositories;

import deti.uas.uasmartsignage.Models.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    User findByUsername(String username);

    List<User> findByRole(Integer role);


    
}
