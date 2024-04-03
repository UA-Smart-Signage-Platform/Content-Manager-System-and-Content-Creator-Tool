package deti.uas.uasmartsignage.Repositories;

import deti.uas.uasmartsignage.Models.Monitor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonitorRepository extends JpaRepository<Monitor, Long> {
    Monitor findByName(String name);
}
