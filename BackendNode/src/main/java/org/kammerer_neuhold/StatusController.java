package org.kammerer_neuhold;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/status/{username}")
public class StatusController {

    @Autowired
    private UserStatusService userStatusService;

    @PostMapping
    public ResponseEntity<?> createStatus(@PathVariable String username, @RequestBody String status) {
        try {
            userStatusService.createUserStatus(username, status);
            return ResponseEntity.ok("Status created for user " + username);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getStatus(@PathVariable String username) {
        return ResponseEntity.ok(userStatusService.getUserStatus(username));
    }

    @PutMapping
    public ResponseEntity<?> updateStatus(@PathVariable String username, @RequestBody String status) {
        try {
            userStatusService.updateUserStatus(username, status);
            return ResponseEntity.ok("Updated status for user " + username);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteStatus(@PathVariable String username) {
        try {
            userStatusService.deleteUserStatus(username);
            return ResponseEntity.ok("Deleted status for user " + username);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
