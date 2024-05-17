package deti.uas.uasmartsignage.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import deti.uas.uasmartsignage.Models.MonitorsGroup;
import deti.uas.uasmartsignage.Models.Schedule;
import deti.uas.uasmartsignage.Models.TemplateGroup;
import deti.uas.uasmartsignage.Repositories.ScheduleRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScheduleService {


    private final ScheduleRepository scheduleRepository;

    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
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
     * Updates all Schedules in the database.
     * 
     * @param schedules The list of Schedules to update.
     * @return The updated list with the updated Schedules.
     */
    public List<Schedule> updateSchedules(List<Schedule> schedules) {
        List<Schedule> savedSchedules = new ArrayList<>();

        for (Schedule schedule : schedules) {
            Schedule newSchedule = scheduleRepository.findById(schedule.getId()).get();
            newSchedule.setPriority(schedule.getPriority());

            scheduleRepository.save(newSchedule);
            savedSchedules.add(newSchedule);
        }

        return savedSchedules;
    }

    /**
     * Retrieves and returns a list of all Schedules.
     * @return A list of all Schedules.
     */
    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }


    /**
     * Retrieves and returns a list of Schedules by the id of a Group.
     * @param id The id of the Group to retrieve Schedules from.
     * @return A list of Schedules by the id of a Group.
     */
    public List<Schedule> getSchedulesByGroupId(Long id) {
        List<Schedule> schedules = scheduleRepository.findAll();
        List<Schedule> groupSchedules = new ArrayList<>();
        for (Schedule schedule : schedules) {
            List<TemplateGroup> templateGroups = schedule.getTemplateGroups();
            for (TemplateGroup templateGroup : templateGroups) {
                MonitorsGroup group = templateGroup.getGroup();
                if (group.getId().equals(id)) {
                    groupSchedules.add(schedule);
                    break;
                }
            }
        }
        return groupSchedules;
    }
}
