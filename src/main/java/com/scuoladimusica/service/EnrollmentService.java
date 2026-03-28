package com.scuoladimusica.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scuoladimusica.exception.BusinessRuleException;
import com.scuoladimusica.exception.DuplicateResourceException;
import com.scuoladimusica.exception.ResourceNotFoundException;
import com.scuoladimusica.model.dto.response.EnrollmentResponse;
import com.scuoladimusica.model.entity.Course;
import com.scuoladimusica.model.entity.Enrollment;
import com.scuoladimusica.model.entity.Student;
import com.scuoladimusica.repository.CourseRepository;
import com.scuoladimusica.repository.EnrollmentRepository;
import com.scuoladimusica.repository.StudentRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class EnrollmentService {

   
    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;


    @Transactional
    public EnrollmentResponse enrollStudent(String matricola, String codiceCorso, int annoIscrizione) {
        Student student = studentRepository.findByMatricola(matricola)
                .orElseThrow(() -> new ResourceNotFoundException("Studente con matricola " + matricola + " non trovato"));

        Course course = courseRepository.findByCodiceCorso(codiceCorso)
                .orElseThrow(() -> new ResourceNotFoundException("Corso con codice " + codiceCorso + " non trovato"));

        if (enrollmentRepository.existsByStudentMatricolaAndCourseCodiceCorso(matricola, codiceCorso)) {
            throw new DuplicateResourceException("Lo studente è già iscritto a questo corso");
        }

        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .course(course)
                .annoIscrizione(annoIscrizione)
                .build();

        return mapToResponse(enrollmentRepository.save(enrollment));
    }

    @Transactional
    public EnrollmentResponse registerVote(String matricola, String codiceCorso, int voto) {
        if (voto < 18 || voto > 30) {
            throw new BusinessRuleException("Il voto deve essere compreso tra 18 e 30");
        }

        Enrollment enrollment = enrollmentRepository.findByStudentMatricolaAndCourseCodiceCorso(matricola, codiceCorso)
                .orElseThrow(() -> new ResourceNotFoundException("Iscrizione non trovata per i parametri forniti"));

        enrollment.setVotoFinale(voto);
        return mapToResponse(enrollmentRepository.save(enrollment));
    }

    public List<EnrollmentResponse> getEnrollmentsByStudent(String matricola) {
        if (!studentRepository.existsByMatricola(matricola)) {
            throw new ResourceNotFoundException("Studente non trovato");
        }
        return enrollmentRepository.findAllByStudentMatricola(matricola).stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<EnrollmentResponse> getEnrollmentsByCourse(String codiceCorso) {
        if (!courseRepository.existsByCodiceCorso(codiceCorso)) {
            throw new ResourceNotFoundException("Corso non trovato");
        }
        return enrollmentRepository.findAllByCourseCodiceCorso(codiceCorso).stream()
                .map(this::mapToResponse)
                .toList();
    }

    private EnrollmentResponse mapToResponse(Enrollment e) {
        return new EnrollmentResponse(
                e.getId(),
                e.getStudent().getMatricola(),
                e.getStudent().getNome() + " " + e.getStudent().getCognome(),
                e.getCourse().getCodiceCorso(),
                e.getCourse().getNome(),
                e.getAnnoIscrizione(),
                e.getVotoFinale()
        );
    }
}
