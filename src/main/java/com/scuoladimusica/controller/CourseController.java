package com.scuoladimusica.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scuoladimusica.model.dto.request.CourseRequest;
import com.scuoladimusica.model.dto.request.LessonRequest;
import com.scuoladimusica.model.dto.response.CourseResponse;
import com.scuoladimusica.model.dto.response.LessonResponse;
import com.scuoladimusica.model.dto.response.MessageResponse;
import com.scuoladimusica.service.CourseService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CourseResponse> createCourse(@Valid @RequestBody CourseRequest request) {
        return new ResponseEntity<>(courseService.createCourse(request), HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()") 
    public ResponseEntity<List<CourseResponse>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/online")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<CourseResponse>> getOnlineCourses() {
        return ResponseEntity.ok(courseService.getOnlineCourses());
    }

    @GetMapping("/{codiceCorso}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CourseResponse> getCourseByCode(@PathVariable String codiceCorso) {
        return ResponseEntity.ok(courseService.getCourseByCode(codiceCorso));
    }

    @PutMapping("/{codiceCorso}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CourseResponse> updateCourse(
            @PathVariable String codiceCorso,
            @Valid @RequestBody CourseRequest request) {
        return ResponseEntity.ok(courseService.updateCourse(codiceCorso, request));
    }

    @DeleteMapping("/{codiceCorso}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCourse(@PathVariable String codiceCorso) {
        courseService.deleteCourse(codiceCorso);
        return ResponseEntity.noContent().build(); 
    }

    @PostMapping("/{codiceCorso}/lessons")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<LessonResponse> addLesson(
            @PathVariable String codiceCorso,
            @Valid @RequestBody LessonRequest request) {
        return new ResponseEntity<>(courseService.addLesson(codiceCorso, request), HttpStatus.CREATED);
    }

    @PostMapping("/{codiceCorso}/instruments/{codiceStrumento}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> addInstrumentToCourse(
            @PathVariable String codiceCorso,
            @PathVariable String codiceStrumento) {
        
        courseService.addInstrumentToCourse(codiceCorso, codiceStrumento);
        return ResponseEntity.ok(new MessageResponse("Strumento associato con successo al corso"));
    }
}
