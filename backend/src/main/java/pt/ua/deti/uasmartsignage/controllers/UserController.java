package pt.ua.deti.uasmartsignage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import pt.ua.deti.uasmartsignage.models.AppUser;
import pt.ua.deti.uasmartsignage.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@CrossOrigin(origins = "*") //NOSONAR
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Create user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User created", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
    })
    @PostMapping
    public ResponseEntity<AppUser> createUser(@RequestBody AppUser user) {
        
        return ResponseEntity.ok(userService.saveUser(user));
    }

    @Operation(summary = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all users", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
    })
    @GetMapping
    public ResponseEntity<Iterable<AppUser>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Delete user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }


    
}
