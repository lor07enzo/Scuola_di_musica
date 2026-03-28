package com.scuoladimusica.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scuoladimusica.model.dto.request.EnrollmentRequest;
import com.scuoladimusica.model.dto.request.VoteRequest;
import com.scuoladimusica.model.dto.response.EnrollmentResponse;
import com.scuoladimusica.service.EnrollmentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseEntity<EnrollmentResponse> enrollStudent(
            @Valid @RequestBody EnrollmentRequest request) {

        EnrollmentResponse response = enrollmentService.enrollStudent(
            request.matricolaStudente(), 
            request.codiceCorso(), 
            request.annoIscrizione()
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/{matricola}/{codiceCorso}/vote")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<EnrollmentResponse> registerVote(
            @PathVariable String matricola,
            @PathVariable String codiceCorso,
            @Valid @RequestBody VoteRequest request) {
                
        EnrollmentResponse response = enrollmentService.registerVote(
            matricola, 
            codiceCorso, 
            request.voto()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/student/{matricola}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<List<EnrollmentResponse>> getEnrollmentsByStudent(
            @PathVariable String matricola) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByStudent(matricola));
    }

    @GetMapping("/course/{codiceCorso}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<List<EnrollmentResponse>> getEnrollmentsByCourse(
            @PathVariable String codiceCorso) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByCourse(codiceCorso));
    }
}
