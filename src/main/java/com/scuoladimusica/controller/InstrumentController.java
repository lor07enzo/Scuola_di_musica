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

    /**
     * TODO: POST /api/instruments - Creare un nuovo strumento.
     *
     * Requisiti:
     * - Solo ADMIN può creare strumenti
     * - Validare la request con @Valid
     * - Restituire 201 CREATED con la InstrumentResponse
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InstrumentResponse> createInstrument(
            @Valid @RequestBody InstrumentRequest request) {
        throw new UnsupportedOperationException("TODO: Implementare createInstrument nel controller");
    }

    /**
     * TODO: GET /api/instruments - Recuperare tutti gli strumenti.
     *
     * Requisiti:
     * - Tutti gli utenti autenticati
     * - Restituire 200 OK con la lista
     */
    @GetMapping
    public ResponseEntity<List<InstrumentResponse>> getAllInstruments() {
        throw new UnsupportedOperationException("TODO: Implementare getAllInstruments nel controller");
    }

    /**
     * TODO: GET /api/instruments/available - Recuperare strumenti disponibili.
     *
     * Requisiti:
     * - Tutti gli utenti autenticati
     * - Restituire 200 OK con la lista filtrata
     */
    @GetMapping("/available")
    public ResponseEntity<List<InstrumentResponse>> getAvailableInstruments() {
        throw new UnsupportedOperationException("TODO: Implementare getAvailableInstruments nel controller");
    }

    /**
     * TODO: GET /api/instruments/{codiceStrumento} - Recuperare uno strumento.
     *
     * Requisiti:
     * - Tutti gli utenti autenticati
     * - Restituire 200 OK con la InstrumentResponse
     */
    @GetMapping("/{codiceStrumento}")
    public ResponseEntity<InstrumentResponse> getInstrument(@PathVariable String codiceStrumento) {
        throw new UnsupportedOperationException("TODO: Implementare getInstrument nel controller");
    }

    /**
     * TODO: POST /api/instruments/{codiceStrumento}/loan - Prestare strumento.
     *
     * Requisiti:
     * - Solo ADMIN e TEACHER possono prestare strumenti
     * - Validare la request con @Valid
     * - Restituire 201 CREATED con la LoanResponse
     */
    @PostMapping("/{codiceStrumento}/loan")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<LoanResponse> loanInstrument(
            @PathVariable String codiceStrumento,
            @Valid @RequestBody LoanRequest request) {
        throw new UnsupportedOperationException("TODO: Implementare loanInstrument nel controller");
    }

    /**
     * TODO: POST /api/instruments/{codiceStrumento}/return - Restituire strumento.
     *
     * Requisiti:
     * - Solo ADMIN e TEACHER possono gestire restituzioni
     * - Validare la request con @Valid
     * - Restituire 200 OK con la LoanResponse
     */
    @PostMapping("/{codiceStrumento}/return")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<LoanResponse> returnInstrument(
            @PathVariable String codiceStrumento,
            @Valid @RequestBody ReturnRequest request) {
        throw new UnsupportedOperationException("TODO: Implementare returnInstrument nel controller");
    }
}
