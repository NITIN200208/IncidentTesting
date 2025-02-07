package com.example.Service;

import com.example.Repository.IncidentRepository;
import com.example.Repository.UserRepository;
import com.example.entity.Incident;
import com.example.entity.User;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class IncidentService {

    @Autowired
    private IncidentRepository incidentRepository;

    @Autowired
    private UserRepository userRepository;

    // Create a new incident
    public Incident createIncident(Long userId, Incident incident) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found!");
        }

        User reporter = userOpt.get();
        incident.setIncidentId(Incident.generateIncidentId());
        incident.setReporter(reporter);
        incident.setReporterName(reporter.getUserName()); // Auto-fill reporter name
        incident.setReportedDateTime(LocalDateTime.now());
        incident.setStatus(Incident.Status.OPEN); // Default status: Open

        return incidentRepository.save(incident);
    }

    // Get all incidents for the logged-in user
    public List<Incident> getUserIncidents(Long userId) {
        return incidentRepository.findByReporterId(userId);
    }

    // Get incident by ID (with access control)
    public Incident getIncidentById(Long userId, String incidentId) {
        Incident incident = incidentRepository.findByIncidentId(incidentId);
        if (incident == null) {
            throw new RuntimeException("Incident not found!");
        }
        if (!incident.getReporter().getId().equals(userId)) {
            throw new RuntimeException("Access Denied!");
        }
        return incident;
    }

    // Update an incident (Only if it's not closed)
    public Incident updateIncident(Long userId, String incidentId, Incident updatedIncident) {
        Incident existingIncident = getIncidentById(userId, incidentId);
        if (existingIncident.getStatus() == Incident.Status.CLOSED) {
            throw new RuntimeException("Closed incidents cannot be edited!");
        }

        existingIncident.setIncidentDetails(updatedIncident.getIncidentDetails());
        existingIncident.setPriority(updatedIncident.getPriority());
        existingIncident.setStatus(updatedIncident.getStatus());

        return incidentRepository.save(existingIncident);
    }

    // Search incidents by Incident ID
    public Incident searchIncidentById(String incidentId) {
        return incidentRepository.findByIncidentId(incidentId);
    }
}
