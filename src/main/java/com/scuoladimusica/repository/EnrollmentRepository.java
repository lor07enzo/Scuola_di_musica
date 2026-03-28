package com.scuoladimusica.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scuoladimusica.model.entity.Enrollment;


@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    
    boolean existsByStudentMatricolaAndCourseCodiceCorso(String matricola, String codiceCorso);
    Optional<Enrollment> findByStudentMatricolaAndCourseCodiceCorso(String matricola, String codiceCorso);
    List<Enrollment> findAllByStudentMatricola(String matricola);
    List<Enrollment> findAllByCourseCodiceCorso(String codiceCorso);
    int countByCourseCodiceCorso(String codice);
}
