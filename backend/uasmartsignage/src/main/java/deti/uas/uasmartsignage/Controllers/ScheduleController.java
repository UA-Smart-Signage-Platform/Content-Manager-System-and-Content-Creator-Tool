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
@CrossOrigin(origins = "*")
@RequestMapping("/api/schedules")

public class ScheduleController {

    private ScheduleService scheduleService;

    @Operation(summary = "Get all schedules")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all schedules", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "No schedules found", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    })
    @GetMapping
    public ResponseEntity<List<Schedule>> getAllSchedules() {
        List<Schedule> schedules = scheduleService.getAllSchedules();
        if (schedules.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(schedules, HttpStatus.OK);
    }

    @Operation(summary = "Get schedule by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Schedule found", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Schedule not found", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Schedule> getScheduleById(@PathVariable Long id){
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
    public ResponseEntity<Schedule> updateSchedule(@PathVariable Long id) {
        Schedule schedule = scheduleService.getScheduleById(id);
        Schedule updatedSchedule = scheduleService.updateSchedule(schedule);
        if (updatedSchedule == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedSchedule, HttpStatus.OK);
    }

    @Operation
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Schedules updated", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Schedules not found", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    })
    @PutMapping
    public ResponseEntity<List<Schedule>> updateSchedules(@RequestBody List<Schedule> schedules){
        List<Schedule> scheduleSavedList = scheduleService.updateSchedules(schedules);
        return new ResponseEntity<>(scheduleSavedList, HttpStatus.OK);
    }

    @Operation
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Schedule deleted", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Schedule not found", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id){
        Schedule schedule = scheduleService.getScheduleById(id);
        if (schedule == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        scheduleService.deleteSchedule(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Create a new schedule")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Schedule created", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    })
    @PostMapping
    public ResponseEntity<Schedule> saveSchedule(@RequestBody Schedule schedule) {
        Schedule savedSchedule = scheduleService.saveSchedule(schedule);
        if (savedSchedule == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(savedSchedule, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all schedules by group id")
    @ApiResponse(responseCode = "200", description = "List of all schedules", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    @GetMapping("/group/{id}")
    public ResponseEntity<List<Schedule>> getSchedulesByGroupId(@PathVariable Long id) {
        List<Schedule> schedules = scheduleService.getSchedulesByGroupId(id);
        return new ResponseEntity<>(schedules, HttpStatus.OK);
    }

}
