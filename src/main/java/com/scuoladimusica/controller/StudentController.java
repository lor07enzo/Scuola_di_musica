package com.scuoladimusica.controller;

import java.util.List;

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

import com.scuoladimusica.model.dto.request.StudentRequest;
import com.scuoladimusica.model.dto.response.StudentReportResponse;
import com.scuoladimusica.model.dto.response.StudentResponse;
import com.scuoladimusica.model.entity.Livello;
import com.scuoladimusica.service.StudentService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/students")
@AllArgsConstructor
public class StudentController {

    private final StudentService studentService;

    /**
     *Permette l'accesso: ADMIN, TEACHER.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<StudentResponse> createStudent(@Valid @RequestBody StudentRequest request) {
        StudentResponse response = studentService.createStudent(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<StudentResponse>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/{matricola}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StudentResponse> getStudentByMatricola(@PathVariable String matricola) {
        return ResponseEntity.ok(studentService.getStudentByMatricola(matricola));
    }

    @GetMapping("/livello/{livello}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<StudentResponse>> getStudentsByLivello(@PathVariable Livello livello) {
        return ResponseEntity.ok(studentService.getStudentsByLivello(livello));
    }

    @PutMapping("/{matricola}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StudentResponse> updateStudent(
            @PathVariable String matricola,
            @Valid @RequestBody StudentRequest request) {
        return ResponseEntity.ok(studentService.updateStudent(matricola, request));
    }

    @DeleteMapping("/{matricola}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteStudent(@PathVariable String matricola) {
        studentService.deleteStudent(matricola);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{matricola}/report")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<StudentReportResponse> getStudentReport(@PathVariable String matricola) {
        return ResponseEntity.ok(studentService.getStudentReport(matricola));
    }
}
