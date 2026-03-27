package com.scuoladimusica.service;

import com.scuoladimusica.model.dto.request.TeacherRequest;
import com.scuoladimusica.model.dto.response.TeacherResponse;
import com.scuoladimusica.repository.CourseRepository;
import com.scuoladimusica.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private CourseRepository courseRepository;

    /**
     * TODO: Creare un nuovo insegnante.
     *
     * Requisiti:
     * - Verificare unicità matricola insegnante (DuplicateResourceException)
     * - Verificare unicità CF (DuplicateResourceException)
     * - Validare che lo stipendio sia > 0 (BusinessRuleException)
     * - Salvare e restituire la response
     *
     * Eccezioni da usare: DuplicateResourceException, BusinessRuleException
     */
    public TeacherResponse createTeacher(TeacherRequest request) {
        throw new UnsupportedOperationException("TODO: Implementare createTeacher");
    }

    /**
     * TODO: Recuperare un insegnante per matricola.
     *
     * Requisiti:
     * - Cercare per matricolaInsegnante
     * - ResourceNotFoundException se non trovato
     * - Restituire la response con il numero di corsi tenuti
     */
    @Transactional(readOnly = true)
    public TeacherResponse getTeacherByMatricola(String matricola) {
        throw new UnsupportedOperationException("TODO: Implementare getTeacherByMatricola");
    }

    /**
     * TODO: Recuperare tutti gli insegnanti.
     */
    @Transactional(readOnly = true)
    public List<TeacherResponse> getAllTeachers() {
        throw new UnsupportedOperationException("TODO: Implementare getAllTeachers");
    }

    /**
     * TODO: Aggiornare un insegnante.
     *
     * Requisiti:
     * - Trovare per matricola (ResourceNotFoundException se non trovato)
     * - Aggiornare: nome, cognome, telefono, stipendio, specializzazione, anniEsperienza
     * - NON aggiornare: matricolaInsegnante, cf, dataNascita
     * - Salvare e restituire la response
     */
    public TeacherResponse updateTeacher(String matricola, TeacherRequest request) {
        throw new UnsupportedOperationException("TODO: Implementare updateTeacher");
    }

    /**
     * TODO: Eliminare un insegnante per matricola.
     *
     * Requisiti:
     * - Trovare per matricola (ResourceNotFoundException se non trovato)
     * - Eliminare dal database
     */
    public void deleteTeacher(String matricola) {
        throw new UnsupportedOperationException("TODO: Implementare deleteTeacher");
    }

    /**
     * TODO: Assegnare un corso a un insegnante.
     *
     * Requisiti:
     * - Trovare l'insegnante per matricola (ResourceNotFoundException se non trovato)
     * - Trovare il corso per codice (ResourceNotFoundException se non trovato)
     * - Verificare che il corso non sia già assegnato a un insegnante (BusinessRuleException)
     * - Impostare il teacher sul corso e salvare
     */
    public void assignCourse(String matricolaInsegnante, String codiceCorso) {
        throw new UnsupportedOperationException("TODO: Implementare assignCourse");
    }
}
