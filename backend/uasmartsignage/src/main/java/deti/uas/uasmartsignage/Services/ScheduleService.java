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


    /**
     * Retrieves and returns a Schedule by its id.
     * @param id The id of the Schedule to retrieve.
     * @return Schedule with the given id.
     */
    public Schedule getScheduleById(Long id) {
            return scheduleRepository.findById(id).orElse(null);
    }

    /**
     * Saves a Schedule to the database.
     * @param schedule The Schedule to save.
     * @return The saved Schedule.
     */
    public Schedule saveSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    /**
     * Deletes a Schedule from the database.
     * @param id The id of the Schedule to delete.
     */
    public void deleteSchedule(Long id) {
        scheduleRepository.deleteById(id);
    }

    /**
     * Updates a Schedule in the database.
     * @param schedule The Schedule to update.
     * @return The updated Schedule.
     */
    public Schedule updateSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    /**
     * Retrieves and returns a list of all Schedules.
     * @return A list of all Schedules.
     */
    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }
}
