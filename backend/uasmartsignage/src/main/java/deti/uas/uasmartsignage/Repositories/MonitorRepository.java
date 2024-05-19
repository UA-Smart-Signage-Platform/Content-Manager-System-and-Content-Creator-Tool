package deti.uas.uasmartsignage.Repositories;

import deti.uas.uasmartsignage.Models.Monitor;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonitorRepository extends JpaRepository<Monitor, Long> {
    Monitor findByName(String name);
    List<Monitor> findByPendingAndGroup_Id(boolean pending,long groupId);
    List<Monitor> findByPending(boolean pending);
    Monitor findByUuid(String uuid);
}
