package deti.uas.uasmartsignage.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import deti.uas.uasmartsignage.Models.Schedule;
import deti.uas.uasmartsignage.Repositories.ScheduleRepository;

import java.util.List;

@Service
public class ScheduleService {

        @Autowired
        private ScheduleRepository scheduleRepository;

        public ScheduleService(ScheduleRepository scheduleRepository) {
            this.scheduleRepository = scheduleRepository;
        }

        public Schedule getScheduleById(Long id) {
            return scheduleRepository.findById(id).orElse(null);
        }

        public Schedule saveSchedule(Schedule schedule) {
            return scheduleRepository.save(schedule);
        }

        public void deleteSchedule(Long id) {
            scheduleRepository.deleteById(id);
        }

        public Schedule updateSchedule(Long id,Schedule schedule) {
            return scheduleRepository.save(schedule);
        }

        public Iterable<Schedule> getAllSchedules() {
            return scheduleRepository.findAll();
        }
}
