package com.scuoladimusica.service;

import java.util.List;
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

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;


    @Transactional
    public TeacherResponse createTeacher(TeacherRequest request) {
        if (teacherRepository.existsByMatricolaInsegnante(request.matricolaInsegnante())) {
            throw new DuplicateResourceException("Matricola insegnante già esistente");
        }
        if (teacherRepository.existsByCf(request.cf())) {
            throw new DuplicateResourceException("Codice Fiscale già esistente");
        }

        Teacher teacher = Teacher.builder()
                .matricolaInsegnante(request.matricolaInsegnante())
                .cf(request.cf())
                .nome(request.nome())
                .cognome(request.cognome())
                .dataNascita(request.dataNascita())
                .telefono(request.telefono())
                .stipendio(request.stipendio())
                .specializzazione(request.specializzazione())
                .anniEsperienza(request.anniEsperienza())
                .build();

        return mapToResponse(teacherRepository.save(teacher));
    }

    @Transactional
    public TeacherResponse updateTeacher(String matricola, TeacherRequest request) {
        Teacher teacher = teacherRepository.findByMatricolaInsegnante(matricola)
                .orElseThrow(() -> new ResourceNotFoundException("Insegnante non trovato"));

        teacher.setNome(request.nome());
        teacher.setCognome(request.cognome());
        teacher.setDataNascita(request.dataNascita());
        teacher.setTelefono(request.telefono());
        teacher.setStipendio(request.stipendio());
        teacher.setSpecializzazione(request.specializzazione());
        teacher.setAnniEsperienza(request.anniEsperienza());

        return mapToResponse(teacherRepository.save(teacher));
    }

    @Transactional
    public void deleteTeacher(String matricola) {
        Teacher teacher = teacherRepository.findByMatricolaInsegnante(matricola)
                .orElseThrow(() -> new ResourceNotFoundException("Insegnante non trovato"));
        
        teacherRepository.delete(teacher);
    }


    @Transactional
    public void assignCourse(String matricolaInsegnante, String codiceCorso) {
        Teacher teacher = teacherRepository.findByMatricolaInsegnante(matricolaInsegnante)
                .orElseThrow(() -> new ResourceNotFoundException("Insegnante non trovato"));

        Course course = courseRepository.findByCodiceCorso(codiceCorso)
                .orElseThrow(() -> new ResourceNotFoundException("Corso non trovato"));

        if (course.getTeacher() != null) {
            throw new BusinessRuleException("Il corso è già assegnato a un altro insegnante");
        }

        course.setTeacher(teacher);
        courseRepository.save(course);
    }


    public TeacherResponse getTeacherByMatricola(String matricola) {
        return teacherRepository.findByMatricolaInsegnante(matricola)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Insegnante non trovato"));
    }

    public List<TeacherResponse> getAllTeachers() {
        return teacherRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    // MAPPER INTERNO
    private TeacherResponse mapToResponse(Teacher t) {
        int numCorsi = courseRepository.countByTeacherMatricolaInsegnante(t.getMatricolaInsegnante());

        return new TeacherResponse(
                t.getId(),
                t.getMatricolaInsegnante(),
                t.getCf(),
                t.getNome(),
                t.getCognome(),
                t.getNomeCompleto(),
                t.getDataNascita(),
                t.getTelefono(),
                t.getStipendio(),
                t.getSpecializzazione(),
                t.getAnniEsperienza(),
                numCorsi
        );
    }
}
