package pt.ua.deti.uasmartsignage.controllers;

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
            @ApiResponse(responseCode = "200", description = "Logs list retrieved", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/logs/backend")
    public ResponseEntity<List<BackendLog>> getBackendLogs(@RequestParam Integer hours) {
        List<BackendLog> logs = logsService.getBackendLogs(hours);
        return new ResponseEntity<>(logs, HttpStatus.OK);
    }
}


