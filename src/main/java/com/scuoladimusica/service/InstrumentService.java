package com.scuoladimusica.service;

import com.scuoladimusica.exception.BusinessRuleException;
import com.scuoladimusica.exception.DuplicateResourceException;
import com.scuoladimusica.exception.ResourceNotFoundException;
import com.scuoladimusica.model.dto.request.InstrumentRequest;
import com.scuoladimusica.model.dto.response.InstrumentResponse;
import com.scuoladimusica.model.entity.Instrument;
import com.scuoladimusica.model.entity.Loan;
import com.scuoladimusica.model.entity.Student;
import com.scuoladimusica.repository.InstrumentRepository;
import com.scuoladimusica.repository.LoanRepository;
import com.scuoladimusica.repository.StudentRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class InstrumentService {

    @Autowired
    private InstrumentRepository instrumentRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private StudentRepository studentRepository;

    public InstrumentResponse createInstrument(InstrumentRequest request) {
        log.info("Creazione nuovo strumento: {}", request.codiceStrumento());

        if (instrumentRepository.existsByCodiceStrumento(request.codiceStrumento())) {
            throw new DuplicateResourceException("Codice strumento esistente: " + request.codiceStrumento());
        }

        Instrument instrument = mapToEntity(request);
        Instrument savedInstrument = Objects.requireNonNull(instrumentRepository.save(Objects.requireNonNull(instrument)));
        return mapToResponse(instrumentRepository.save(savedInstrument));
    }

    @Transactional(readOnly = true)
    public List<InstrumentResponse> getAllInstruments() {
        return instrumentRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public InstrumentResponse getInstrumentByCodice(String codice) {
        return instrumentRepository.findByCodiceStrumento(codice)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Strumento non trovato: " + codice));
    }

    public void deleteInstrument(String codice) {
        Instrument instrument = instrumentRepository.findByCodiceStrumento(codice)
                .orElseThrow(() -> new ResourceNotFoundException("Strumento non trovato"));
        
        if (!instrument.isDisponibile()) {
            throw new BusinessRuleException("Impossibile eliminare uno strumento attualmente in prestito");
        }
        
        instrumentRepository.delete(instrument);
    }

    // --- LOGICA DI PRESTITO (LOAN) ---

    public void lendInstrument(String codiceStrumento, String matricolaStudente) {
        log.info("Tentativo di prestito strumento {} a studente {}", codiceStrumento, matricolaStudente);

        Instrument instrument = instrumentRepository.findByCodiceStrumento(codiceStrumento)
                .orElseThrow(() -> new ResourceNotFoundException("Strumento non trovato"));

        Student student = studentRepository.findByMatricola(matricolaStudente)
                .orElseThrow(() -> new ResourceNotFoundException("Studente non trovato"));

        if (!instrument.isDisponibile()) {
            throw new BusinessRuleException("Lo strumento è già in prestito");
        }

        Loan newLoan = new Loan();
        newLoan.setInstrument(instrument);
        newLoan.setStudent(student);
        newLoan.setDataInizio(LocalDate.now());
        newLoan.setDataFine(null); 

        loanRepository.save(newLoan);
    }

    public void returnInstrument(String codiceStrumento) {
        log.info("Restituzione strumento: {}", codiceStrumento);

        Instrument instrument = instrumentRepository.findByCodiceStrumento(codiceStrumento)
                .orElseThrow(() -> new ResourceNotFoundException("Strumento non trovato"));

        Loan activeLoan = loanRepository.findByInstrumentIdAndDataFineIsNull(instrument.getId())
                .orElseThrow(() -> new BusinessRuleException("Non risultano prestiti attivi per questo strumento"));

        activeLoan.setDataFine(LocalDate.now());
        loanRepository.save(activeLoan);
    }

    // --- MAPPING ---

    private Instrument mapToEntity(InstrumentRequest request) {
        return Instrument.builder()
                .codiceStrumento(request.codiceStrumento())
                .nome(request.nome())
                .tipoStrumento(request.tipoStrumento())
                .marca(request.marca())
                .annoProduzione(request.annoProduzione())
                .numeroCorde(request.numeroCorde())
                .tipoCorde(request.tipoCorde())
                .materiale(request.materiale())
                .tipoPelle(request.tipoPelle())
                .diametro(request.diametro())
                .build();
    }

    private InstrumentResponse mapToResponse(Instrument instrument) {
        return new InstrumentResponse(
                instrument.getId(),
                instrument.getCodiceStrumento(),
                instrument.getNome(),
                instrument.getTipoStrumento(),
                instrument.getMarca(),
                instrument.getAnnoProduzione(),
                instrument.isDisponibile() 
        );
    }
}
