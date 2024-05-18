package deti.uas.uasmartsignage.Services;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import deti.uas.uasmartsignage.Models.MonitorsGroup;
import deti.uas.uasmartsignage.Models.Schedule;
import deti.uas.uasmartsignage.Models.Severity;
import deti.uas.uasmartsignage.Models.TemplateGroup;
import deti.uas.uasmartsignage.Repositories.ScheduleRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    private final TemplateGroupService templateGroupService;

    private final Logger logger = org.slf4j.LoggerFactory.getLogger(ScheduleService.class);

    private final String source = this.getClass().getSimpleName();

    private final LogsService logsService;

    private static final String ADDLOGERROR = "Failed to add log to InfluxDB";
    private static final String ADDLOGSUCCESS = "Added log to InfluxDB: {}";


    public ScheduleService(ScheduleRepository scheduleRepository, LogsService logsService,@Lazy TemplateGroupService templateGroupService) {
        this.scheduleRepository = scheduleRepository;
        this.logsService = logsService;
        this.templateGroupService = templateGroupService;
    }


    /**
     * Retrieves and returns a Schedule by its id.
     * @param id The id of the Schedule to retrieve.
     * @return Schedule with the given id.
     */
    public Schedule getScheduleById(Long id) {
        String operation = "getScheduleById";
        String description = "Getting schedule by id " + id;
        if (!logsService.addBackendLog(Severity.INFO, source, operation, description)) {
            logger.error(ADDLOGERROR);
        } else {
            logger.info(ADDLOGSUCCESS, description);
        }
        return scheduleRepository.findById(id).orElse(null);
    }

    /**
     * Saves a Schedule to the database.
     * @param schedule The Schedule to save.
     * @return The saved Schedule.
     */
    public Schedule saveSchedule(Schedule schedule) {
        String operation = "saveSchedule";
        String description = "Saving schedule " + schedule.getId();
        if (!logsService.addBackendLog(Severity.INFO, source, operation, description)) {
            logger.error(ADDLOGERROR);
        } else {
            logger.info(ADDLOGSUCCESS, description);
        }
        return scheduleRepository.save(schedule);
    }

    /**
     * Deletes a Schedule from the database.
     * @param id The id of the Schedule to delete.
     */
    public void deleteSchedule(Long id) {
        String operation = "deleteSchedule";
        String description = "Deleting schedule by id " + id;
        if (!logsService.addBackendLog(Severity.INFO, source, operation, description)) {
            logger.error(ADDLOGERROR);
        } else {
            logger.info(ADDLOGSUCCESS, description);
        }
        scheduleRepository.deleteById(id);
    }

    /**
     * Updates a Schedule in the database.
     * @param schedule The Schedule to update.
     * @return The updated Schedule.
     */
    public Schedule updateSchedule(Schedule schedule) {
        String operation = "updateSchedule";
        String description = "Updating schedule " + schedule.getId();
        if (!logsService.addBackendLog(Severity.INFO, source, operation, description)) {
            logger.error(ADDLOGERROR);
        } else {
            logger.info(ADDLOGSUCCESS, description);
        }
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

        List<MonitorsGroup> monitorGroups = new ArrayList<>();

        for (Schedule schedule : schedules) {
            List <MonitorsGroup> groups = new ArrayList<>();
            for (TemplateGroup templateGroup : schedule.getTemplateGroups()) {
                MonitorsGroup group = templateGroup.getGroup();
                if (!groups.contains(group)) {
                    groups.add(group);
                }
            }
            for (MonitorsGroup group : groups) {
                if (!monitorGroups.contains(group)) {
                    monitorGroups.add(group);
                }
            }
            Schedule newSchedule = scheduleRepository.findById(schedule.getId()).get();
            newSchedule.setPriority(schedule.getPriority());

            scheduleRepository.save(newSchedule);
            savedSchedules.add(newSchedule);
        }

        for (MonitorsGroup monitorGroup : monitorGroups) {
            templateGroupService.sendAllSchedulesToMonitorGroup(monitorGroup, false);
        }

        return savedSchedules;
    }

    /**
     * Retrieves and returns a list of all Schedules.
     * @return A list of all Schedules.
     */
    public List<Schedule> getAllSchedules() {
        String operation = "getAllSchedules";
        String description = "Getting all schedules";
        if (!logsService.addBackendLog(Severity.INFO, source, operation, description)) {
            logger.error(ADDLOGERROR);
        } else {
            logger.info(ADDLOGSUCCESS, description);
        }
        return scheduleRepository.findAll();
    }


    /**
     * Retrieves and returns a list of Schedules by the id of a Group.
     * @param id The id of the Group to retrieve Schedules from.
     * @return A list of Schedules by the id of a Group.
     */
    public List<Schedule> getSchedulesByGroupId(Long id) {
        String operation = "getSchedulesByGroupId";
        String description = "Getting schedules by group id " + id;
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
        if (!logsService.addBackendLog(Severity.INFO, source, operation, description)) {
            logger.error(ADDLOGERROR);
        } else {
            logger.info(ADDLOGSUCCESS, description);
        }
        return groupSchedules;
    }
}
