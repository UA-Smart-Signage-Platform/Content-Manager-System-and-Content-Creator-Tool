package pt.ua.deti.uasmartsignage.controllers;

import pt.ua.deti.uasmartsignage.enums.Severity;
import pt.ua.deti.uasmartsignage.models.embedded.BackendLog;
import pt.ua.deti.uasmartsignage.services.LogsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("api")
@CrossOrigin(origins = "*")
public class LogController {

    private final LogsService logsService;

    public LogController(LogsService logsService) {
        this.logsService = logsService;
    }

    @Operation(summary = "Get all backend logs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logs list retrieved based on hours", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/logs/backend")
    public ResponseEntity<List<BackendLog>> getBackendLogs(@RequestParam Integer hours) {
        List<BackendLog> logs = logsService.getBackendLogs(hours);
        return new ResponseEntity<>(logs, HttpStatus.OK);
    }

    @Operation(summary = "Get all backend logs in the last X days, based on their severity and group them together to count their total per day.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logs list retrieved based on days and severity", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/logs/backend/count")
    public ResponseEntity<List<Integer>> getBackendLogsByNumberDaysAndSeverity(@RequestParam Integer days, @RequestParam Severity severity) {
        List<Integer> logs = logsService.getBackendLogsByNumberDaysAndSeverity(days, severity);
        return new ResponseEntity<>(logs, HttpStatus.OK);
    }

    @Operation(summary = "WIP")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "WIP", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/logs/backend/operation")
    public ResponseEntity<Map<String, Integer>> getBackendLogsNumberPerOperationByNumberDaysAndSeverity(@RequestParam Integer days, @RequestParam Severity severity) {
        Map<String, Integer> logs = logsService.getBackendLogsNumberPerOperationByNumberDaysAndSeverity(days, severity);
        return new ResponseEntity<>(logs, HttpStatus.OK);
    }
}


