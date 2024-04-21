package deti.uas.uasmartsignage.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import deti.uas.uasmartsignage.Models.Schedule;
import deti.uas.uasmartsignage.Repositories.ScheduleRepository;
import deti.uas.uasmartsignage.Repositories.MonitorGroupRepository;
import deti.uas.uasmartsignage.Models.MonitorsGroup;

import java.util.List;

@Service
public class ScheduleService {


        private final ScheduleRepository scheduleRepository;
        private final MonitorGroupRepository groupRepository;

        @Autowired
        public ScheduleService(ScheduleRepository scheduleRepository, MonitorGroupRepository groupRepository) {
            this.scheduleRepository = scheduleRepository;
            this.groupRepository = groupRepository;
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

        public Schedule updateSchedule(Schedule schedule) {
            return scheduleRepository.save(schedule);
        }

        public Iterable<Schedule> getAllSchedules() {
            return scheduleRepository.findAll();
        }
}
