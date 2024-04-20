package deti.uas.uasmartsignage.Repositories;

import java.util.List;
import java.util.Optional;
import deti.uas.uasmartsignage.Models.MonitorsGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonitorGroupRepository extends JpaRepository<MonitorsGroup, Long>{

    MonitorsGroup findByName(String name);

    Optional<MonitorsGroup> findById(Long id);

    List<MonitorsGroup> findAllByMonitorsPendingFalse();

    List<MonitorsGroup> findAllByMadeForMonitorFalse();
}
