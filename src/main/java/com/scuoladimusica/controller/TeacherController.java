package com.scuoladimusica.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scuoladimusica.model.dto.request.TeacherRequest;
import com.scuoladimusica.model.dto.response.MessageResponse;
import com.scuoladimusica.model.dto.response.TeacherResponse;
import com.scuoladimusica.service.TeacherService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TeacherResponse> createTeacher(@Valid @RequestBody TeacherRequest request) {
        return new ResponseEntity<>(teacherService.createTeacher(request), HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TeacherResponse>> getAllTeachers() {
        return ResponseEntity.ok(teacherService.getAllTeachers());
    }

    @GetMapping("/{matricola}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<TeacherResponse> getTeacherByMatricola(@PathVariable String matricola) {
        return ResponseEntity.ok(teacherService.getTeacherByMatricola(matricola));
    }

    @PutMapping("/{matricola}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TeacherResponse> updateTeacher(
            @PathVariable String matricola,
            @Valid @RequestBody TeacherRequest request) {
        return ResponseEntity.ok(teacherService.updateTeacher(matricola, request));
    }

    @DeleteMapping("/{matricola}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTeacher(@PathVariable String matricola) {
        teacherService.deleteTeacher(matricola);
        return ResponseEntity.noContent().build(); // Restituisce 204 No Content
    }

    @PostMapping("/{matricola}/courses/{codiceCorso}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> assignCourse(
            @PathVariable String matricola,
            @PathVariable String codiceCorso) {
        
        teacherService.assignCourse(matricola, codiceCorso);
        return ResponseEntity.ok(new MessageResponse("Corso assegnato con successo"));
    }
}
