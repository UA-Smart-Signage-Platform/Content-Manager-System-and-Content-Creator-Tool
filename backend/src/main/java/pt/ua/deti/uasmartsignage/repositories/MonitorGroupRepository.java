package pt.ua.deti.uasmartsignage.repositories;

import java.util.List;
import java.util.Optional;

import pt.ua.deti.uasmartsignage.models.MonitorGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonitorGroupRepository extends JpaRepository<MonitorGroup, Long>{
    Optional<MonitorGroup> findByName(String name);
    List<MonitorGroup> findAllByMonitorsIsEmptyOrMonitorsPendingFalse();
    List<MonitorGroup> findAllByMonitorsIsEmptyOrIsDefaultGroupFalse();
}
