package deti.uas.uasmartsignage.Repositories;

import deti.uas.uasmartsignage.Models.Screen;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScreenRepository extends JpaRepository<Screen, Long> {
    Screen findByLocation(String location);

    List<Screen> findByGroupID(Long group);
    
}
