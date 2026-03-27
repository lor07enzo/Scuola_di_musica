package com.scuoladimusica.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scuoladimusica.exception.BusinessRuleException;
import com.scuoladimusica.exception.DuplicateResourceException;
import com.scuoladimusica.exception.ResourceNotFoundException;
import com.scuoladimusica.model.dto.request.CourseRequest;
import com.scuoladimusica.model.dto.request.LessonRequest;
import com.scuoladimusica.model.dto.response.CourseResponse;
import com.scuoladimusica.model.dto.response.LessonResponse;
import com.scuoladimusica.model.entity.Course;
import com.scuoladimusica.model.entity.Instrument;
import com.scuoladimusica.model.entity.Lesson;
import com.scuoladimusica.model.entity.Livello;
import com.scuoladimusica.repository.CourseRepository;
import com.scuoladimusica.repository.InstrumentRepository;
import com.scuoladimusica.repository.LessonRepository;

@Service
@Transactional
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private InstrumentRepository instrumentRepository;


    public CourseResponse createCourse(CourseRequest request) {
        if (courseRepository.existsByCodiceCorso(request.codiceCorso())) {
            throw new DuplicateResourceException("Codice corso già esistente: " + request.codiceCorso());
        }

        validateDates(request);

        Course course = Course.builder()
                .codiceCorso(request.codiceCorso())
                .nome(request.nome())
                .dataInizio(request.dataInizio())
                .dataFine(request.dataFine())
                .costoOrario(request.costoOrario())
                .totaleOre(request.totaleOre())
                .online(request.online())
                .livello(request.livello() != null ? request.livello() : Livello.PRINCIPIANTE)
                .build();

        return mapToCourseResponse(courseRepository.save(course));
    }

    @Transactional(readOnly = true)
    public CourseResponse getCourseByCode(String codiceCorso) {
        Course course = courseRepository.findByCodiceCorso(codiceCorso)
                .orElseThrow(() -> new ResourceNotFoundException("Corso non trovato: " + codiceCorso));
        return mapToCourseResponse(course);
    }

    @Transactional(readOnly = true)
    public List<CourseResponse> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(this::mapToCourseResponse)
                .toList(); 
    }

    @Transactional(readOnly = true)
    public List<CourseResponse> getOnlineCourses() {
        return courseRepository.findByOnlineTrue().stream()
                .map(this::mapToCourseResponse)
                .toList();
    }

   public CourseResponse updateCourse(String codiceCorso, CourseRequest request) {
        Course course = courseRepository.findByCodiceCorso(codiceCorso)
                .orElseThrow(() -> new ResourceNotFoundException("Corso non trovato: " + codiceCorso));

        validateDates(request);

        course.setNome(request.nome());
        course.setDataInizio(request.dataInizio());
        course.setDataFine(request.dataFine());
        course.setCostoOrario(request.costoOrario());
        course.setTotaleOre(request.totaleOre());
        course.setOnline(request.online());
        course.setLivello(request.livello());

        return mapToCourseResponse(courseRepository.save(course));
    }

    public void deleteCourse(String codiceCorso) {
        Course course = courseRepository.findByCodiceCorso(codiceCorso)
                .orElseThrow(() -> new ResourceNotFoundException("Corso non trovato: " + codiceCorso));
        courseRepository.delete(Objects.requireNonNull(course));
    }

    public LessonResponse addLesson(String codiceCorso, LessonRequest request) {
        Course course = courseRepository.findByCodiceCorso(codiceCorso)
                .orElseThrow(() -> new ResourceNotFoundException("Corso non trovato: " + codiceCorso));

        // Controllo unicità numero lezione all'interno dello stesso corso
        if (lessonRepository.existsByCourseIdAndNumero(course.getId(), request.numero())) {
            throw new DuplicateResourceException("La lezione n. " + request.numero() + " è già presente in questo corso.");
        }

        Lesson lesson = Lesson.builder()
                .numero(request.numero())
                .data(request.data())
                .oraInizio(request.oraInizio())
                .durata(request.durata())
                .aula(request.aula())
                .argomento(request.argomento())
                .course(course)
                .build();

        return mapToLessonResponse(lessonRepository.save(lesson));
    }

    public void addInstrumentToCourse(String codiceCorso, String codiceStrumento) {
        Course course = courseRepository.findByCodiceCorso(codiceCorso)
                .orElseThrow(() -> new ResourceNotFoundException("Corso non trovato: " + codiceCorso));

        Instrument instrument = instrumentRepository.findByCodiceStrumento(codiceStrumento)
                .orElseThrow(() -> new ResourceNotFoundException("Strumento non trovato: " + codiceStrumento));

        if (course.getInstruments().contains(instrument)) {
            throw new DuplicateResourceException("Strumento già associato");
        }

        course.getInstruments().add(instrument);
        courseRepository.save(course);
    }


    private void validateDates(CourseRequest request) {
        if (request.dataFine().isBefore(request.dataInizio())) {
            throw new BusinessRuleException("La data di fine deve essere successiva alla data di inizio");
        }
    }

    private CourseResponse mapToCourseResponse(Course course) {
        String teacherName = (course.getTeacher() != null) 
            ? course.getTeacher().getNome() + " " + course.getTeacher().getCognome() 
            : "Docente non assegnato";

        List<LessonResponse> lezioniDto = course.getLessons().stream()
                .map(this::mapToLessonResponse)
                .toList();

        return new CourseResponse(
                course.getId(),
                course.getCodiceCorso(),
                course.getNome(),
                course.getDataInizio(),
                course.getDataFine(),
                course.getCostoOrario(),
                course.getTotaleOre(),
                course.getCostoTotale(),
                course.getDurataGiorni(),
                course.isOnline(),
                course.getLivello(),
                teacherName,
                course.getEnrollments().size(),
                lezioniDto
        );
    }

    private LessonResponse mapToLessonResponse(Lesson lesson) {
        return new LessonResponse(
                lesson.getId(),
                lesson.getNumero(),
                lesson.getData(),
                lesson.getOraInizio(),
                lesson.getDurata(),
                lesson.getAula(),
                lesson.getArgomento()
        );
    }
}
