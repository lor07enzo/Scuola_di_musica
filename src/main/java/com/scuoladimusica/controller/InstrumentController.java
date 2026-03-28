package com.scuoladimusica.controller;

import com.scuoladimusica.model.dto.request.InstrumentRequest;
import com.scuoladimusica.model.dto.request.LoanRequest;
import com.scuoladimusica.model.dto.request.ReturnRequest;
import com.scuoladimusica.model.dto.response.InstrumentResponse;
import com.scuoladimusica.model.dto.response.LoanResponse;
import com.scuoladimusica.service.InstrumentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/instruments")
public class InstrumentController {

    @Autowired
    private InstrumentService instrumentService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InstrumentResponse> createInstrument(@Valid @RequestBody InstrumentRequest request) {
        return new ResponseEntity<>(instrumentService.createInstrument(request), HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
    public ResponseEntity<List<InstrumentResponse>> getAllInstruments() {
        return ResponseEntity.ok(instrumentService.getAllInstruments());
    }

    @GetMapping("/available")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
    public ResponseEntity<List<InstrumentResponse>> getAvailableInstruments() {
        return ResponseEntity.ok(instrumentService.getAvailableInstruments());
    }

    @GetMapping("/{code}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<InstrumentResponse> getInstrumentByCode(@PathVariable String code) {
        return ResponseEntity.ok(instrumentService.getInstrumentByCode(code));
    }

    @PostMapping("/{code}/loan")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<LoanResponse> loanInstrument(
            @PathVariable String code,
            @Valid @RequestBody LoanRequest request) {
        
        LoanResponse response = instrumentService.loanToStudent(
                code, 
                request.matricolaStudente(), 
                request.dataInizio()
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/{code}/return")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<LoanResponse> returnInstrument(
            @PathVariable String code,
            @Valid @RequestBody ReturnRequest request) {
        
        return ResponseEntity.ok(instrumentService.returnInstrument(code, request.dataFine()));
    }
}
