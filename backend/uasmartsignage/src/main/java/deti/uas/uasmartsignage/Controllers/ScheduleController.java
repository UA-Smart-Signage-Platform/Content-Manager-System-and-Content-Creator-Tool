package deti.uas.uasmartsignage.Controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import deti.uas.uasmartsignage.Models.Schedule;
import deti.uas.uasmartsignage.Services.ScheduleService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/schedules")

public class ScheduleController {

    private ScheduleService scheduleService;

    @GetMapping
    public ResponseEntity<?> getAllSchedules() {
        List<Schedule> schedules = (List<Schedule>) scheduleService.getAllSchedules();
        return new ResponseEntity<>(schedules, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getScheduleById(Long id) {
        Schedule schedule = scheduleService.getScheduleById(id);
        if (schedule == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(schedule, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSchedule(Long id, Schedule schedule) {
        Schedule updatedSchedule = scheduleService.updateSchedule(id, schedule);
        if (updatedSchedule == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedSchedule, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSchedule(Long id) {
        Schedule schedule = scheduleService.getScheduleById(id);
        if (schedule == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        scheduleService.deleteSchedule(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
