package deti.uas.uasmartsignage.Configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import deti.uas.uasmartsignage.Configuration.CustomUserDetailsService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import deti.uas.uasmartsignage.Services.jwtUtil;


import java.io.IOException;

// This class helps us to validate the generated jwt token
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private jwtUtil jwtUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Get the token from the header
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // Check if the token is not null and starts with Bearer
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
            
            try {
                // Try to extract username using jwtUtil
                username = jwtUtil.extractUsername(token);
            } catch (Exception e) {
                logger.error("Failed to extract username from jwtUtil, trying to extract from IDP");
                
                try {
                    // Try to extract username using Ua IDP
                    username = getUsernameFromToken(token);
                    // Split the username by "@" and reconstruct it with "ua.pt" because ua IDP returns the email with @live.ua.pt 
                    String[] parts = username.split("@");
                    if (parts.length == 2) {
                        username = parts[0] + "@ua.pt";
                    } else {
                        logger.error("Invalid username format: " + username);
                    }

                } catch (Exception ex) {
                    logger.error("Failed to extract username from token");
                }
            }
        }
        
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Get the user details from the token
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
            );
            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }

    // Calls the UA IDP to get the username from the token
    private String getUsernameFromToken(String token) throws JsonProcessingException {
        String idpUserinfoEndpoint = "https://wso2-gw.ua.pt/userinfo";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
            idpUserinfoEndpoint,
            HttpMethod.GET,
            entity,
            String.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
           
            JsonElement jsonElement = JsonParser.parseString(response.getBody());
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            
            return jsonObject.get("email").getAsString();
        } else {
            throw new RuntimeException("Failed to fetch username from IDP");
        }
    }
}
