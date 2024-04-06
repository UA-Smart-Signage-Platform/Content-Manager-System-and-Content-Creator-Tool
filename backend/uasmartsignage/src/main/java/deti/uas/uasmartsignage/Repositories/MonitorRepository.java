package deti.uas.uasmartsignage.Repositories;

import deti.uas.uasmartsignage.Models.MonitorsGroup;
import deti.uas.uasmartsignage.Models.Monitor;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonitorRepository extends JpaRepository<Monitor, Long> {
    Monitor findByLocation(String location);
    List<Monitor> findByMonitorsGroupForScreens(MonitorsGroup monitorsGroupForScreens);
}
