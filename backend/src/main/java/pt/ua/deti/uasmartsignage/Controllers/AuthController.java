package pt.ua.deti.uasmartsignage.Controllers;

import java.util.HashMap;
import java.util.Map;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pt.ua.deti.uasmartsignage.Services.CustomUserDetailsService;
import pt.ua.deti.uasmartsignage.Services.JwtUtilService;
import pt.ua.deti.uasmartsignage.authentication.AuthenticationRequest;
import pt.ua.deti.uasmartsignage.authentication.AuthenticationResponse;
import pt.ua.deti.uasmartsignage.authentication.ChangePasswordRequest;
import pt.ua.deti.uasmartsignage.authentication.IAuthenticationFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


@CrossOrigin(origins = "*") //NOSONAR
@RestController
@RequestMapping("/api/login")
public class AuthController {

    private final CustomUserDetailsService userDetailsService;
    private final JwtUtilService jwtUtil;
    private IAuthenticationFacade authenticationFacade;

    @Autowired
    public AuthController(CustomUserDetailsService userDetailsService, JwtUtilService jwtUtil, IAuthenticationFacade authenticationFacade) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.authenticationFacade = authenticationFacade;
    }

    

    @Operation(summary = "Create authentication token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token created", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    })
    @PostMapping
    public ResponseEntity<Object> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest){
        
        try {
            userDetailsService.loadUserByUsernameAndPassword(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.badRequest().body("Invalid username or password");
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsernameAndPassword(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        String username = userDetails.getUsername();
        String role = userDetails.getAuthorities().iterator().next().getAuthority();


        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt, username, role));
    }

    @Operation(summary = "Change password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    })
    @PutMapping("/change-password")
    public ResponseEntity<Object> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        try {
            // Load user details to get the current password
            UserDetails userDetails = userDetailsService.loadUserByUsername(changePasswordRequest.getUsername());
            if (!BCrypt.checkpw(changePasswordRequest.getCurrentPassword(), userDetails.getPassword())) {
                return ResponseEntity.badRequest().body("Incorrect current password");
            }

            userDetailsService.updateUserPassword(changePasswordRequest.getUsername(), changePasswordRequest.getNewPassword());
            return ResponseEntity.ok("Password changed successfully");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.badRequest().body("User not found");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Get user info")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User info", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
    })
    @GetMapping("/info")
    public ResponseEntity<Map<String, String>> getUserInfo() {
        Authentication authentication = authenticationFacade.getAuthentication();
        
        String username = authentication.getName();
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("username", username);
        userInfo.put("role", role);

        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    }
}

