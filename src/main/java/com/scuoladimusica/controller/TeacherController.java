package com.scuoladimusica.controller;

import com.scuoladimusica.model.dto.request.TeacherRequest;
import com.scuoladimusica.model.dto.response.MessageResponse;
import com.scuoladimusica.model.dto.response.TeacherResponse;
import com.scuoladimusica.service.TeacherService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TeacherResponse> createTeacher(@Valid @RequestBody TeacherRequest request) {
        log.info("Ricevuta richiesta creazione insegnante: {}", request.matricolaInsegnante());
        TeacherResponse response = teacherService.createTeacher(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TeacherResponse>> getAllTeachers() {
        log.info("Ricevuta richiesta elenco completo insegnanti");
        return ResponseEntity.ok(teacherService.getAllTeachers());
    }

    @GetMapping("/{matricola}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<TeacherResponse> getTeacher(@PathVariable String matricola) {
        log.info("Ricevuta richiesta recupero insegnante matricola: {}", matricola);
        return ResponseEntity.ok(teacherService.getTeacherByMatricola(matricola));
    }

    @PutMapping("/{matricola}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TeacherResponse> updateTeacher(
            @PathVariable String matricola,
            @Valid @RequestBody TeacherRequest request) {
        log.info("Ricevuta richiesta aggiornamento insegnante matricola: {}", matricola);
        return ResponseEntity.ok(teacherService.updateTeacher(matricola, request));
    }

    @DeleteMapping("/{matricola}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTeacher(@PathVariable String matricola) {
        log.info("Ricevuta richiesta eliminazione insegnante matricola: {}", matricola);
        teacherService.deleteTeacher(matricola);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{matricola}/courses/{codiceCorso}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> assignCourse(
            @PathVariable String matricola,
            @PathVariable String codiceCorso) {
        log.info("Ricevuta richiesta assegnazione corso {} a insegnante {}", codiceCorso, matricola);
        teacherService.assignCourse(matricola, codiceCorso);
        
        // Assumo che MessageResponse sia un record con un singolo campo String
        return ResponseEntity.ok(new MessageResponse("Corso assegnato con successo"));
    }
}
