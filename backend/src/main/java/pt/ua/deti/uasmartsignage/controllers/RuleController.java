package pt.ua.deti.uasmartsignage.controllers;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pt.ua.deti.uasmartsignage.dto.RuleDTO;
import pt.ua.deti.uasmartsignage.models.Rule;
import pt.ua.deti.uasmartsignage.services.RuleService;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*") // NOSONAR
@RequiredArgsConstructor
@RequestMapping("/api/rules")
public class RuleController {

    private final RuleService ruleService;

    @Operation(summary = "Get all rules")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all rules")
    })
    @GetMapping
    public ResponseEntity<List<Rule>> getAllRules() {
        List<Rule> rules = ruleService.getAllRules();
        return new ResponseEntity<>(rules, HttpStatus.OK);
    }

    @Operation(summary = "Get rule by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved rule"),
            @ApiResponse(responseCode = "404", description = "Rule not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Rule> getRuleById(@PathVariable("id") String id) {
        Rule rule = ruleService.getRuleById(id);
        if (rule == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(rule, HttpStatus.OK);
    }

    @Operation(summary = "Save rule")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully saved rule"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping
    public ResponseEntity<Rule> saveRule(@RequestBody @Valid RuleDTO ruleDTO) {
        Rule savedRule = ruleService.saveRule(ruleDTO);
        if (savedRule == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(savedRule, HttpStatus.CREATED);
    }

    @Operation(summary = "Delete rule")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted rule"),
            @ApiResponse(responseCode = "404", description = "Rule not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRule(@PathVariable("id") String id) {
        if (ruleService.getRuleById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ruleService.deleteRuleById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Update rule")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully updated rule"),
            @ApiResponse(responseCode = "404", description = "Rule not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Rule> updateRule(@PathVariable("id") String id, @RequestBody @Valid RuleDTO ruleDTO) {
        Rule updatedRule = ruleService.updateRule(id, ruleDTO);
        if (updatedRule == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedRule, HttpStatus.OK);
    }
}
