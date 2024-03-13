package deti.uas.uasmartsignage.Controllers;

import deti.uas.uasmartsignage.Models.Screen;
import deti.uas.uasmartsignage.Services.ScreenService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class ScreenController {

    private ScreenService screenService;

    @GetMapping("/screens")
    public ResponseEntity<?> getAllScreens() {
        List<Screen> screens = (List<Screen>) screenService.getAllScreens();
        return new ResponseEntity<>(screens, HttpStatus.OK);
    }

    @GetMapping("/screens/{id}")
    public String getScreenById(Long id) {
        return "screen";
    }

    @GetMapping("/screens/{id}/templates")
    public String getScreenTemplates(Long id) {
        return "templates";
    }

    @GetMapping("/screens/{id}/templates/{t_id}")
    public String getScreenTemplateById(Long id) {
        return "template";
    }

    @PostMapping("/screens")
    public ResponseEntity<?> saveScreen(@RequestBody Screen screen) {
        Screen savedScreen = screenService.saveScreen(screen);
        return new ResponseEntity<>(savedScreen, HttpStatus.CREATED);
    }

    @PutMapping("/screens/{id}")
    public ResponseEntity<?> updateScreen(@PathVariable("id") Long id, @RequestBody Screen screen) {
        Screen updatedScreen = screenService.updateScreen(id,screen);
        if (updatedScreen == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedScreen, HttpStatus.OK);
    }
    
}
