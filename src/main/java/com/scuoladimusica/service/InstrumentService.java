package com.scuoladimusica.service;

import com.scuoladimusica.exception.BusinessRuleException;
import com.scuoladimusica.exception.DuplicateResourceException;
import com.scuoladimusica.exception.ResourceNotFoundException;
import com.scuoladimusica.model.dto.request.InstrumentRequest;
import com.scuoladimusica.model.dto.response.InstrumentResponse;
import com.scuoladimusica.model.dto.response.LoanResponse;
import com.scuoladimusica.model.entity.Instrument;
import com.scuoladimusica.model.entity.Loan;
import com.scuoladimusica.model.entity.Student;
import com.scuoladimusica.repository.InstrumentRepository;
import com.scuoladimusica.repository.LoanRepository;
import com.scuoladimusica.repository.StudentRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import lombok.RequiredArgsConstructor;

@Slf4j
@Service
@RequiredArgsConstructor
public class InstrumentService {

    private final InstrumentRepository instrumentRepository;
    private final StudentRepository studentRepository;
    private final LoanRepository loanRepository;


    @Transactional
    public InstrumentResponse createInstrument(InstrumentRequest request) {
        if (instrumentRepository.existsByCodiceStrumento(request.codiceStrumento())) {
            throw new DuplicateResourceException("Codice strumento già esistente: " + request.codiceStrumento());
        }

        Instrument instrument = Instrument.builder()
                .codiceStrumento(request.codiceStrumento())
                .nome(request.nome())
                .tipoStrumento(request.tipoStrumento())
                .marca(request.marca())
                .annoProduzione(request.annoProduzione())
                // Eventuali altri campi specifici (numCorde, ecc.) se presenti nel model
                .build();

        return mapToResponse(instrumentRepository.save(instrument));
    }


    @Transactional
    public LoanResponse loanToStudent(String codiceStrumento, String matricolaStudente, LocalDate dataInizio) {
        Instrument instrument = instrumentRepository.findByCodiceStrumento(codiceStrumento)
                .orElseThrow(() -> new ResourceNotFoundException("Strumento non trovato"));

        Student student = studentRepository.findByMatricola(matricolaStudente)
                .orElseThrow(() -> new ResourceNotFoundException("Studente non trovato"));

        if (loanRepository.existsByInstrumentCodiceStrumentoAndDataFineIsNull(codiceStrumento)) {
            throw new BusinessRuleException("Lo strumento è già in prestito");
        }

        Loan loan = Loan.builder()
                .instrument(instrument)
                .student(student)
                .dataInizio(dataInizio)
                .build();

        return mapToLoanResponse(loanRepository.save(loan));
    }

    @Transactional
    public LoanResponse returnInstrument(String codiceStrumento, LocalDate dataFine) {

        if (!instrumentRepository.existsByCodiceStrumento(codiceStrumento)) {
            throw new ResourceNotFoundException("Strumento con codice " + codiceStrumento + " non trovato");
        }

        Loan loan = loanRepository.findByInstrumentCodiceStrumentoAndDataFineIsNull(codiceStrumento)
                .orElseThrow(() -> new BusinessRuleException("Lo strumento non risulta attualmente in prestito"));

        if (dataFine.isBefore(loan.getDataInizio())) {
            throw new BusinessRuleException("La data di restituzione non può essere precedente alla data di inizio prestito");
        }

        loan.setDataFine(dataFine);
        return mapToLoanResponse(loanRepository.save(loan));
    }

    public InstrumentResponse getInstrumentByCode(String codice) {
        return instrumentRepository.findByCodiceStrumento(codice)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Strumento non trovato"));
    }

    public List<InstrumentResponse> getAllInstruments() {
        return instrumentRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<InstrumentResponse> getAvailableInstruments() {
        return instrumentRepository.findAll().stream()
                .filter(this::isAvailable)
                .map(this::mapToResponse)
                .toList();
    }

    // HELPER E MAPPER
    private boolean isAvailable(Instrument i) {
        return !loanRepository.existsByInstrumentCodiceStrumentoAndDataFineIsNull(i.getCodiceStrumento());
    }

    private InstrumentResponse mapToResponse(Instrument i) {
        return new InstrumentResponse(
                i.getId(),
                i.getCodiceStrumento(),
                i.getNome(),
                i.getTipoStrumento(),
                i.getMarca(),
                i.getAnnoProduzione(),
                isAvailable(i) 
        );
    }

    private LoanResponse mapToLoanResponse(Loan l) {
        return new LoanResponse(
                l.getId(),
                l.getInstrument().getCodiceStrumento(),
                l.getInstrument().getNome(),
                l.getStudent().getMatricola(),
                l.getStudent().getNome() + " " + l.getStudent().getCognome(),
                l.getDataInizio(),
                l.getDataFine()
        );
    }
}
