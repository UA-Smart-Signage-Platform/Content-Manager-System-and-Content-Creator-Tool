package deti.uas.uasmartsignage.Repositories;

import deti.uas.uasmartsignage.Models.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

}
