package com.scuoladimusica.service;

import com.scuoladimusica.model.dto.request.InstrumentRequest;
import com.scuoladimusica.model.dto.response.InstrumentResponse;
import com.scuoladimusica.model.dto.response.LoanResponse;
import com.scuoladimusica.repository.InstrumentRepository;
import com.scuoladimusica.repository.LoanRepository;
import com.scuoladimusica.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class InstrumentService {

    @Autowired
    private InstrumentRepository instrumentRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private StudentRepository studentRepository;

    /**
     * TODO: Creare un nuovo strumento.
     *
     * Requisiti:
     * - Verificare unicità codice strumento (DuplicateResourceException)
     * - Salvare e restituire la response
     */
    public InstrumentResponse createInstrument(InstrumentRequest request) {
        throw new UnsupportedOperationException("TODO: Implementare createInstrument");
    }

    /**
     * TODO: Recuperare uno strumento per codice.
     *
     * Requisiti:
     * - ResourceNotFoundException se non trovato
     * - Includere nella response se è disponibile o meno
     */
    @Transactional(readOnly = true)
    public InstrumentResponse getInstrumentByCode(String codiceStrumento) {
        throw new UnsupportedOperationException("TODO: Implementare getInstrumentByCode");
    }

    /**
     * TODO: Recuperare tutti gli strumenti.
     */
    @Transactional(readOnly = true)
    public List<InstrumentResponse> getAllInstruments() {
        throw new UnsupportedOperationException("TODO: Implementare getAllInstruments");
    }

    /**
     * TODO: Recuperare solo gli strumenti disponibili (senza prestiti attivi).
     *
     * Requisiti:
     * - Usare il metodo findAvailable() del repository
     */
    @Transactional(readOnly = true)
    public List<InstrumentResponse> getAvailableInstruments() {
        throw new UnsupportedOperationException("TODO: Implementare getAvailableInstruments");
    }

    /**
     * TODO: Prestare uno strumento a uno studente.
     *
     * Requisiti:
     * - Trovare lo strumento per codice (ResourceNotFoundException se non trovato)
     * - Trovare lo studente per matricola (ResourceNotFoundException se non trovato)
     * - Verificare che lo strumento sia disponibile (BusinessRuleException se già in prestito)
     * - Creare un nuovo Loan con dataInizio e dataFine = null (prestito attivo)
     * - Salvare e restituire la LoanResponse
     *
     * Eccezioni da usare: ResourceNotFoundException, BusinessRuleException
     */
    public LoanResponse loanToStudent(String codiceStrumento, String matricolaStudente, LocalDate dataInizio) {
        throw new UnsupportedOperationException("TODO: Implementare loanToStudent");
    }

    /**
     * TODO: Restituire uno strumento (chiudere il prestito attivo).
     *
     * Requisiti:
     * - Trovare lo strumento per codice (ResourceNotFoundException se non trovato)
     * - Trovare il prestito attivo (dataFine IS NULL) per questo strumento
     *   (BusinessRuleException se non c'è un prestito attivo)
     * - Verificare che dataFine >= dataInizio del prestito (BusinessRuleException)
     * - Impostare la dataFine sul prestito e salvare
     * - Restituire la LoanResponse
     *
     * Eccezioni da usare: ResourceNotFoundException, BusinessRuleException
     */
    public LoanResponse returnInstrument(String codiceStrumento, LocalDate dataFine) {
        throw new UnsupportedOperationException("TODO: Implementare returnInstrument");
    }
}
