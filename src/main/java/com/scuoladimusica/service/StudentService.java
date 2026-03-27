package com.scuoladimusica.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scuoladimusica.exception.DuplicateResourceException;
import com.scuoladimusica.exception.ResourceNotFoundException;
import com.scuoladimusica.model.dto.request.StudentRequest;
import com.scuoladimusica.model.dto.response.StudentReportResponse;
import com.scuoladimusica.model.dto.response.StudentResponse;
import com.scuoladimusica.model.entity.Livello;
import com.scuoladimusica.model.entity.Student;
import com.scuoladimusica.repository.StudentRepository;

@Service
@Transactional
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public StudentResponse createStudent(StudentRequest request) {
        if (studentRepository.existsByMatricola(request.matricola())) {
            throw new DuplicateResourceException("Matricola già presente: " + request.matricola());
        }
        if (studentRepository.existsByCf(request.cf())) {
            throw new DuplicateResourceException("Codice Fiscale già presente: " + request.cf());
        }

        Student student = Student.builder()
                .matricola(request.matricola())
                .cf(request.cf())
                .nome(request.nome())
                .cognome(request.cognome())
                .telefono(request.telefono())
                .dataNascita(request.dataNascita())
                .livello(request.livello() != null ? request.livello() : Livello.PRINCIPIANTE)
                .build();
        
        Student savedStudent = Objects.requireNonNull(studentRepository.save(Objects.requireNonNull(student)));

        return mapToStudentResponse(studentRepository.save(savedStudent));
    }

    @Transactional(readOnly = true)
    public StudentResponse getStudentByMatricola(String matricola) {
        Student student = studentRepository.findByMatricola(matricola)
                .orElseThrow(() -> new ResourceNotFoundException("Studente non trovato: " + matricola));
        return mapToStudentResponse(student);
    }

    @Transactional(readOnly = true)
    public List<StudentResponse> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(this::mapToStudentResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<StudentResponse> getStudentsByLivello(Livello livello) {
        return studentRepository.findByLivello(livello).stream()
                .map(this::mapToStudentResponse)
                .toList();
    }

    public StudentResponse updateStudent(String matricola, StudentRequest request) {
        Student student = studentRepository.findByMatricola(matricola)
                .orElseThrow(() -> new ResourceNotFoundException("Studente non trovato: " + matricola));

        student.setNome(request.nome());
        student.setCognome(request.cognome());
        student.setTelefono(request.telefono());
        student.setLivello(request.livello());

        return mapToStudentResponse(studentRepository.save(student));
    }

    public void deleteStudent(String matricola) {
        Student student = studentRepository.findByMatricola(matricola)
                .orElseThrow(() -> new ResourceNotFoundException("Studente non trovato: " + matricola));
        studentRepository.delete(Objects.requireNonNull(student));
    }

    @Transactional(readOnly = true)
    public StudentReportResponse getStudentReport(String matricola) {
        Student student = studentRepository.findByMatricola(matricola)
                .orElseThrow(() -> new ResourceNotFoundException("Studente non trovato: " + matricola));

        return new StudentReportResponse(
                student.getNomeCompleto(),
                student.getLivello(),
                student.getNumeroCorsiFrequentati(),
                student.getMediaVoti(),
                student.getEnrollments().stream().map(e -> e.getCourse().getNome()).toList()
        );
    }

    // --- Helper Methods per il Mapping ---

    private StudentResponse mapToStudentResponse(Student student) {
        return new StudentResponse(
                student.getId(),
                student.getMatricola(),
                student.getCf(),
                student.getNome(),
                student.getCognome(),
                student.getNomeCompleto(),  
                student.getDataNascita(),
                student.getTelefono(),
                student.getLivello(),
                student.getNumeroCorsiFrequentati(), 
                student.getMediaVoti() 
        );
    }
}
