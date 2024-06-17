package pt.ua.deti.uasmartsignage.repositories;

import java.util.List;
import java.util.Optional;
import pt.ua.deti.uasmartsignage.models.MonitorsGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonitorGroupRepository extends JpaRepository<MonitorsGroup, Long>{

    MonitorsGroup findByName(String name);

    Optional<MonitorsGroup> findById(Long id);

    List<MonitorsGroup> findAllByMonitorsPendingFalseOrMonitorsIsEmpty();

    List<MonitorsGroup> findAllByMadeForMonitorFalse();
}
