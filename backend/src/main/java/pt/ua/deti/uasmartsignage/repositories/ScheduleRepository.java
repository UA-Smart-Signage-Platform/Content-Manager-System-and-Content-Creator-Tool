package pt.ua.deti.uasmartsignage.Repositories;

import pt.ua.deti.uasmartsignage.models.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
