package com.example.Controller;

import com.example.entity.Incident;
import com.example.Service.IncidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class IncidentController {

    @Autowired
    private IncidentService incidentService;

    //create
    @PostMapping("/{userId}/create")
    public ResponseEntity<?> createIncident(@PathVariable Long userId, @RequestBody Incident incident) {
        Incident newIncident = incidentService.createIncident(userId, incident);
        return ResponseEntity.ok(newIncident);
    }
    // Get all incidents
    @GetMapping("/{userId}")
    public ResponseEntity<List<Incident>> getUserIncidents(@PathVariable Long userId) {
        return ResponseEntity.ok(incidentService.getUserIncidents(userId));
    }
    //  Get 1 incident by ID for a user
    @GetMapping("/{userId}/{incidentId}")
    public ResponseEntity<?> getIncidentById(@PathVariable Long userId, @PathVariable String incidentId) {
        return ResponseEntity.ok(incidentService.getIncidentById(userId, incidentId));
    }
     //update
    @PutMapping("/{userId}/{incidentId}")
    public ResponseEntity<?> updateIncident(@PathVariable Long userId, @PathVariable String incidentId, @RequestBody Incident updatedIncident) {
        return ResponseEntity.ok(incidentService.updateIncident(userId, incidentId, updatedIncident));
    }

    //search by id
    @GetMapping("/search/{incidentId}")
    public ResponseEntity<?> searchIncident(@PathVariable String incidentId) {
        return ResponseEntity.ok(incidentService.searchIncidentById(incidentId));
    }
}
