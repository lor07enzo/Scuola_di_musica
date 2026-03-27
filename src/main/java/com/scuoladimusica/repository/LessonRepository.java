package com.scuoladimusica.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scuoladimusica.model.entity.Lesson;


@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    List<Lesson> findByCourseId(Long courseId);

    boolean existsByCourseIdAndNumero(Long courseId, int numero);
    
}
