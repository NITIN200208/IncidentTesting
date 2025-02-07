package com.example.Repository;

import com.example.entity.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, String> {
    List<Incident> findByReporterId(Long reporterId);
    Incident findByIncidentId(String incidentId);
}
