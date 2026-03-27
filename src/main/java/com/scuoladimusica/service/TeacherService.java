package com.scuoladimusica.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scuoladimusica.exception.BusinessRuleException;
import com.scuoladimusica.exception.DuplicateResourceException;
import com.scuoladimusica.exception.ResourceNotFoundException;
import com.scuoladimusica.model.dto.request.TeacherRequest;
import com.scuoladimusica.model.dto.response.TeacherResponse;
import com.scuoladimusica.model.entity.Course;
import com.scuoladimusica.model.entity.Teacher;
import com.scuoladimusica.repository.CourseRepository;
import com.scuoladimusica.repository.TeacherRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private CourseRepository courseRepository;

    public TeacherResponse createTeacher(TeacherRequest request) {
        log.info("Creazione nuovo insegnante con matricola: {}", request.matricolaInsegnante());

        if (teacherRepository.existsByMatricolaInsegnante(request.matricolaInsegnante())) {
            throw new DuplicateResourceException("Matricola già esistente: " + request.matricolaInsegnante());
        }
        if (teacherRepository.existsByCf(request.cf())) {
            throw new DuplicateResourceException("Codice Fiscale già presente: " + request.cf());
        }

        // Validazione Business Rule
        if (request.stipendio() <= 0) {
            throw new BusinessRuleException("Lo stipendio deve essere maggiore di zero");
        }

        Teacher teacher = mapToEntity(request);
        Teacher savedTeacher = teacherRepository.save(teacher);
        
        return mapToResponse(savedTeacher);
    }

    @Transactional(readOnly = true)
    public TeacherResponse getTeacherByMatricola(String matricola) {
        log.debug("Recupero insegnante con matricola: {}", matricola);
        
        Teacher teacher = teacherRepository.findByMatricolaInsegnante(matricola)
                .orElseThrow(() -> new ResourceNotFoundException("Insegnante non trovato con matricola: " + matricola));
        
        return mapToResponse(teacher);
    }

    @Transactional(readOnly = true)
    public List<TeacherResponse> getAllTeachers() {
        log.debug("Recupero lista completa insegnanti");
        return teacherRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public TeacherResponse updateTeacher(String matricola, TeacherRequest request) {
        log.info("Aggiornamento insegnante matricola: {}", matricola);

        Teacher teacher = teacherRepository.findByMatricolaInsegnante(matricola)
                .orElseThrow(() -> new ResourceNotFoundException("Insegnante non trovato con matricola: " + matricola));

        // Aggiornamento campi permessi
        teacher.setNome(request.nome());
        teacher.setCognome(request.cognome());
        teacher.setTelefono(request.telefono());
        teacher.setStipendio(request.stipendio());
        teacher.setSpecializzazione(request.specializzazione());
        teacher.setAnniEsperienza(request.anniEsperienza());

        return mapToResponse(teacherRepository.save(teacher));
    }

    public void deleteTeacher(String matricola) {
        log.warn("Eliminazione insegnante matricola: {}", matricola);
        
        Teacher teacher = teacherRepository.findByMatricolaInsegnante(matricola)
                .orElseThrow(() -> new ResourceNotFoundException("Insegnante non trovato con matricola: " + matricola));
        
        teacherRepository.delete(teacher);
    }

    public void assignCourse(String matricolaInsegnante, String codiceCorso) {
        log.info("Assegnazione corso {} all'insegnante {}", codiceCorso, matricolaInsegnante);

        Teacher teacher = teacherRepository.findByMatricolaInsegnante(matricolaInsegnante)
                .orElseThrow(() -> new ResourceNotFoundException("Insegnante non trovato"));

        Course course = courseRepository.findByCodiceCorso(codiceCorso)
                .orElseThrow(() -> new ResourceNotFoundException("Corso non trovato"));

        if (course.getTeacher() != null) {
            throw new BusinessRuleException("Il corso è già assegnato all'insegnante: " + course.getTeacher().getMatricolaInsegnante());
        }

        course.setTeacher(teacher);
        courseRepository.save(course);
    }

    private Teacher mapToEntity(TeacherRequest request) {
        Teacher teacher = new Teacher();
        teacher.setMatricolaInsegnante(request.matricolaInsegnante());
        teacher.setNome(request.nome());
        teacher.setCognome(request.cognome());
        teacher.setCf(request.cf());
        teacher.setDataNascita(request.dataNascita());
        teacher.setTelefono(request.telefono());
        teacher.setStipendio(request.stipendio());
        teacher.setSpecializzazione(request.specializzazione());
        teacher.setAnniEsperienza(request.anniEsperienza());
        return teacher;
    }

    private TeacherResponse mapToResponse(Teacher teacher) {
        
        int numeroCorsi = (teacher.getCourses() != null) ? teacher.getCourses().size() : 0;
        String nomeCompleto = teacher.getNome() + " " + teacher.getCognome();

        return new TeacherResponse(
            teacher.getId(),
            teacher.getMatricolaInsegnante(),
            teacher.getCf(),
            teacher.getNome(),
            teacher.getCognome(),
            nomeCompleto,
            teacher.getDataNascita(),
            teacher.getTelefono(),
            teacher.getStipendio(),
            teacher.getSpecializzazione(),
            teacher.getAnniEsperienza(),
            numeroCorsi
        );
    }
}
