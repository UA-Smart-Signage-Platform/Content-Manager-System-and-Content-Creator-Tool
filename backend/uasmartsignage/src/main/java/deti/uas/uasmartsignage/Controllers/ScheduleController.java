package deti.uas.uasmartsignage.Controllers;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import deti.uas.uasmartsignage.Models.Schedule;
import deti.uas.uasmartsignage.Services.ScheduleService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/schedules")

public class ScheduleController {

    private ScheduleService scheduleService;

    @Operation(summary = "Get all schedules")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all schedules", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "No schedules found", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    })
    @GetMapping
    public ResponseEntity<?> getAllSchedules() {
        List<Schedule> schedules = (List<Schedule>) scheduleService.getAllSchedules();
        return new ResponseEntity<>(schedules, HttpStatus.OK);
    }

    @Operation(summary = "Get schedule by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Schedule found", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Schedule not found", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getScheduleById(Long id) {
        Schedule schedule = scheduleService.getScheduleById(id);
        if (schedule == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(schedule, HttpStatus.OK);
    }

    @Operation
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Schedule updated", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Schedule not found", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSchedule(Long id, Schedule schedule) {
        Schedule updatedSchedule = scheduleService.updateSchedule(id, schedule);
        if (updatedSchedule == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedSchedule, HttpStatus.OK);
    }

    @Operation
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Schedule deleted", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Schedule not found", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSchedule(Long id) {
        Schedule schedule = scheduleService.getScheduleById(id);
        if (schedule == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        scheduleService.deleteSchedule(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Create a new schedule")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Schedule created", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    })
    @PostMapping
    public ResponseEntity<?> createSchedule(Schedule schedule) {
        Schedule newSchedule = scheduleService.createSchedule(schedule);
        return new ResponseEntity<>(newSchedule, HttpStatus.CREATED);
    }

}
