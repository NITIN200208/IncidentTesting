package com.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Random;

@Entity
@Data
public class Incident {

    @Id
    private String incidentId; // Auto-generated ID

    @ManyToOne
    @JoinColumn(name = "reporter_id", nullable = false)
    private User reporter; // Reporter details

    private String reporterName;
    private String enterpriseGovtId;
    private String incidentDetails;
    private LocalDateTime reportedDateTime;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    private Status status;

    // Generate unique Incident ID
    public static String generateIncidentId() {
        Random random = new Random();
        int randomNum = random.nextInt(90000) + 10000; // Generate a 5-digit number
        int year = LocalDateTime.now().getYear(); // Get current year
        return "RMG" + randomNum + "-" + year; // Format: RMG12345-2025
    }

    public enum Priority {
        HIGH, MEDIUM, LOW
    }

    public enum Status {
        OPEN, IN_PROGRESS, CLOSED
    }
}
