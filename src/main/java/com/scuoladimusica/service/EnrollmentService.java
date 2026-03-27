package com.scuoladimusica.service;

import com.scuoladimusica.model.dto.response.EnrollmentResponse;
import com.scuoladimusica.repository.CourseRepository;
import com.scuoladimusica.repository.EnrollmentRepository;
import com.scuoladimusica.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    /**
     * TODO: Iscrivere uno studente a un corso.
     *
     * Requisiti:
     * - Trovare lo studente per matricola (ResourceNotFoundException se non trovato)
     * - Trovare il corso per codice (ResourceNotFoundException se non trovato)
     * - Verificare che lo studente non sia già iscritto allo stesso corso (DuplicateResourceException)
     * - Creare l'Enrollment con studente, corso e anno di iscrizione
     * - Salvare e restituire la EnrollmentResponse
     *
     * Eccezioni da usare: ResourceNotFoundException, DuplicateResourceException
     */
    public EnrollmentResponse enrollStudent(String matricola, String codiceCorso, int annoIscrizione) {
        throw new UnsupportedOperationException("TODO: Implementare enrollStudent");
    }

    /**
     * TODO: Registrare un voto per un'iscrizione.
     *
     * Requisiti:
     * - Trovare l'iscrizione tramite matricola e codice corso
     *   (ResourceNotFoundException se non trovata - "Iscrizione non trovata")
     * - Verificare che il voto sia tra 18 e 30 (BusinessRuleException)
     * - Impostare il votoFinale sull'iscrizione e salvare
     * - Restituire la EnrollmentResponse aggiornata
     *
     * Eccezioni da usare: ResourceNotFoundException, BusinessRuleException
     */
    public EnrollmentResponse registerVote(String matricola, String codiceCorso, int voto) {
        throw new UnsupportedOperationException("TODO: Implementare registerVote");
    }

    /**
     * TODO: Recuperare tutte le iscrizioni di uno studente.
     *
     * Requisiti:
     * - Trovare lo studente per matricola (ResourceNotFoundException se non trovato)
     * - Restituire la lista di EnrollmentResponse
     */
    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getEnrollmentsByStudent(String matricola) {
        throw new UnsupportedOperationException("TODO: Implementare getEnrollmentsByStudent");
    }

    /**
     * TODO: Recuperare tutte le iscrizioni per un corso.
     *
     * Requisiti:
     * - Trovare il corso per codice (ResourceNotFoundException se non trovato)
     * - Restituire la lista di EnrollmentResponse
     */
    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getEnrollmentsByCourse(String codiceCorso) {
        throw new UnsupportedOperationException("TODO: Implementare getEnrollmentsByCourse");
    }
}
