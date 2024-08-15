package pt.ua.deti.uasmartsignage.repositories;

import pt.ua.deti.uasmartsignage.models.Monitor;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonitorRepository extends JpaRepository<Monitor, Long> {
    Monitor findByName(String name);
    Monitor findByUuid(String uuid);
    List<Monitor> findByPending(boolean pending);
    List<Monitor> findByPendingAndOnline(boolean pending, boolean online);
    List<Monitor> findByPendingAndGroup_Id(boolean pending,long groupId);
    List<Monitor> findByPendingAndGroup_IdAndOnline(boolean pending,long groupId, boolean online);
}
