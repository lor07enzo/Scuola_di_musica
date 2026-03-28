package com.scuoladimusica.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scuoladimusica.exception.DuplicateResourceException;
import com.scuoladimusica.exception.ResourceNotFoundException;
import com.scuoladimusica.model.dto.request.StudentRequest;
import com.scuoladimusica.model.dto.response.StudentReportResponse;
import com.scuoladimusica.model.dto.response.StudentResponse;
import com.scuoladimusica.model.entity.Enrollment;
import com.scuoladimusica.model.entity.Livello;
import com.scuoladimusica.model.entity.Student;
import com.scuoladimusica.repository.EnrollmentRepository;
import com.scuoladimusica.repository.StudentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Transactional
    public StudentResponse createStudent(StudentRequest request) {
        
        if (studentRepository.existsByMatricola(request.matricola())) {
            throw new DuplicateResourceException("Matricola già esistente");
        }
        if (studentRepository.existsByCf(request.cf())) {
            throw new DuplicateResourceException("Codice Fiscale già esistente");
        }

        Livello livello = (request.livello() != null) ? request.livello() : Livello.PRINCIPIANTE;

        Student student = Student.builder()
                .matricola(request.matricola())
                .cf(request.cf())
                .nome(request.nome())
                .cognome(request.cognome())
                .dataNascita(request.dataNascita())
                .telefono(request.telefono())
                .livello(livello)
                .build();

        return mapToResponse(studentRepository.save(student));
    }

    @Transactional
    public StudentResponse updateStudent(String matricola, StudentRequest request) {
        Student student = studentRepository.findByMatricola(matricola)
                .orElseThrow(() -> new ResourceNotFoundException("Studente non trovato"));

        student.setNome(request.nome());
        student.setCognome(request.cognome());
        student.setDataNascita(request.dataNascita());
        student.setTelefono(request.telefono());
        
        if (request.livello() != null) {
            student.setLivello(request.livello());
        }

        return mapToResponse(studentRepository.save(student));
    }

    @Transactional
    public void deleteStudent(String matricola) {
        Student student = studentRepository.findByMatricola(matricola)
                .orElseThrow(() -> new ResourceNotFoundException("Studente non trovato"));
        
        studentRepository.delete(student);
    }

    public StudentResponse getStudentByMatricola(String matricola) {
        return studentRepository.findByMatricola(matricola)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Studente non trovato"));
    }

    public List<StudentResponse> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<StudentResponse> getStudentsByLivello(Livello livello) {
        return studentRepository.findAllByLivello(livello).stream()
                .map(this::mapToResponse)
                .toList();
    }


    public StudentReportResponse getStudentReport(String matricola) {
        Student student = studentRepository.findByMatricola(matricola)
                .orElseThrow(() -> new ResourceNotFoundException("Studente non trovato"));

        List<Enrollment> enrollments = enrollmentRepository.findAllByStudentMatricola(matricola);

        double media = enrollments.stream()
                .filter(e -> e.getVotoFinale() != null)
                .mapToInt(Enrollment::getVotoFinale)
                .average()
                .orElse(0.0);

        List<String> nomiCorsi = enrollments.stream()
                .map(e -> e.getCourse().getNome())
                .toList();

        return new StudentReportResponse(
                student.getNome() + " " + student.getCognome(),
                student.getLivello(),
                enrollments.size(),
                media,
                nomiCorsi
        );
    }


    private StudentResponse mapToResponse(Student s) {
        // Recuperiamo le iscrizioni per calcolare statistiche rapide se richiesto dal DTO
        List<Enrollment> enrollments = enrollmentRepository.findAllByStudentMatricola(s.getMatricola());
        
        double media = enrollments.stream()
                .filter(e -> e.getVotoFinale() != null)
                .mapToInt(Enrollment::getVotoFinale)
                .average()
                .orElse(0.0);

        return new StudentResponse(
                s.getId(),
                s.getMatricola(),
                s.getCf(),
                s.getNome(),
                s.getCognome(),
                s.getNome() + " " + s.getCognome(),
                s.getDataNascita(),
                s.getTelefono(),
                s.getLivello(),
                enrollments.size(), 
                media               
        );
    }
}
