package com.scuoladimusica.controller;

import com.scuoladimusica.model.dto.request.StudentRequest;
import com.scuoladimusica.model.dto.response.StudentReportResponse;
import com.scuoladimusica.model.dto.response.StudentResponse;
import com.scuoladimusica.model.entity.Livello;
import com.scuoladimusica.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    /**
     * TODO: POST /api/students - Creare un nuovo studente.
     *
     * Requisiti:
     * - Solo ADMIN e TEACHER possono creare studenti
     * - Validare la request con @Valid
     * - Restituire 201 CREATED con la StudentResponse
     * - Chiamare studentService.createStudent(request)
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<StudentResponse> createStudent(@Valid @RequestBody StudentRequest request) {
        throw new UnsupportedOperationException("TODO: Implementare createStudent nel controller");
    }

    /**
     * TODO: GET /api/students - Recuperare tutti gli studenti.
     *
     * Requisiti:
     * - Solo ADMIN e TEACHER possono vedere tutti gli studenti
     * - Restituire 200 OK con la lista di StudentResponse
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<List<StudentResponse>> getAllStudents() {
        throw new UnsupportedOperationException("TODO: Implementare getAllStudents nel controller");
    }

    /**
     * TODO: GET /api/students/{matricola} - Recuperare uno studente per matricola.
     *
     * Requisiti:
     * - Tutti gli utenti autenticati possono cercare uno studente
     * - Restituire 200 OK con la StudentResponse
     */
    @GetMapping("/{matricola}")
    public ResponseEntity<StudentResponse> getStudent(@PathVariable String matricola) {
        throw new UnsupportedOperationException("TODO: Implementare getStudent nel controller");
    }

    /**
     * TODO: GET /api/students/livello/{livello} - Filtrare studenti per livello.
     *
     * Requisiti:
     * - Solo ADMIN e TEACHER possono filtrare
     * - Restituire 200 OK con la lista filtrata
     */
    @GetMapping("/livello/{livello}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<List<StudentResponse>> getStudentsByLivello(@PathVariable Livello livello) {
        throw new UnsupportedOperationException("TODO: Implementare getStudentsByLivello nel controller");
    }

    /**
     * TODO: PUT /api/students/{matricola} - Aggiornare uno studente.
     *
     * Requisiti:
     * - Solo ADMIN può aggiornare
     * - Validare la request
     * - Restituire 200 OK con la StudentResponse aggiornata
     */
    @PutMapping("/{matricola}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StudentResponse> updateStudent(
            @PathVariable String matricola,
            @Valid @RequestBody StudentRequest request) {
        throw new UnsupportedOperationException("TODO: Implementare updateStudent nel controller");
    }

    /**
     * TODO: DELETE /api/students/{matricola} - Eliminare uno studente.
     *
     * Requisiti:
     * - Solo ADMIN può eliminare
     * - Restituire 204 NO CONTENT
     */
    @DeleteMapping("/{matricola}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteStudent(@PathVariable String matricola) {
        throw new UnsupportedOperationException("TODO: Implementare deleteStudent nel controller");
    }

    /**
     * TODO: GET /api/students/{matricola}/report - Report dettagliato di uno studente.
     *
     * Requisiti:
     * - ADMIN e TEACHER possono vedere il report
     * - Restituire 200 OK con StudentReportResponse
     */
    @GetMapping("/{matricola}/report")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<StudentReportResponse> getStudentReport(@PathVariable String matricola) {
        throw new UnsupportedOperationException("TODO: Implementare getStudentReport nel controller");
    }
}
