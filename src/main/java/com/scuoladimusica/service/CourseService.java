package com.scuoladimusica.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
import com.scuoladimusica.repository.EnrollmentRepository;
import com.scuoladimusica.repository.InstrumentRepository;
import com.scuoladimusica.repository.LessonRepository;
import com.scuoladimusica.repository.TeacherRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final LessonRepository lessonRepository;
    private final InstrumentRepository instrumentRepository;
    private final EnrollmentRepository enrollmentRepository;


    @Transactional
    public CourseResponse createCourse(CourseRequest request) {
        validateDates(request.dataInizio(), request.dataFine());

        if (courseRepository.existsByCodiceCorso(request.codiceCorso())) {
            throw new DuplicateResourceException("Codice corso già esistente");
        }

        Livello livello = (request.livello() != null) ? request.livello() : Livello.PRINCIPIANTE;

        Course course = Course.builder()
                .codiceCorso(request.codiceCorso())
                .nome(request.nome())
                .dataInizio(request.dataInizio())
                .dataFine(request.dataFine())
                .costoOrario(request.costoOrario())
                .totaleOre(request.totaleOre())
                .online(request.online())
                .livello(livello)
                .build();

        return mapToResponse(courseRepository.save(course));
    }

    @Transactional
    public CourseResponse updateCourse(String codice, CourseRequest request) {
        Course course = courseRepository.findByCodiceCorso(codice)
                .orElseThrow(() -> new ResourceNotFoundException("Corso non trovato"));

        validateDates(request.dataInizio(), request.dataFine());

        course.setNome(request.nome());
        course.setDataInizio(request.dataInizio());
        course.setDataFine(request.dataFine());
        course.setCostoOrario(request.costoOrario());
        course.setTotaleOre(request.totaleOre());
        course.setOnline(request.online());
        course.setLivello(request.livello());

        return mapToResponse(courseRepository.save(course));
    }

    @Transactional
    public void deleteCourse(String codice) {
        Course course = courseRepository.findByCodiceCorso(codice)
                .orElseThrow(() -> new ResourceNotFoundException("Corso non trovato"));
        courseRepository.delete(course);
    }

    @Transactional
    public LessonResponse addLesson(String codiceCorso, LessonRequest request) {
        Course course = courseRepository.findByCodiceCorso(codiceCorso)
                .orElseThrow(() -> new ResourceNotFoundException("Corso non trovato"));

        if (lessonRepository.existsByCourseCodiceCorsoAndNumero(codiceCorso, request.numero())) {
            throw new DuplicateResourceException("Lezione numero " + request.numero() + " già presente in questo corso");
        }

        Lesson lesson = Lesson.builder()
                .course(course)
                .numero(request.numero())
                .data(request.data())
                .oraInizio(request.oraInizio())
                .durata(request.durata())
                .aula(request.aula())
                .argomento(request.argomento())
                .build();

        Lesson salvata = lessonRepository.save(lesson);
        if (course.getLessons() == null) {
            course.setLessons(new ArrayList<>());
        }
        course.getLessons().add(salvata); 

        return mapToLessonResponse(salvata);
    }

    @Transactional
    public void addInstrumentToCourse(String codiceCorso, String codiceStrumento) {
        Course course = courseRepository.findByCodiceCorso(codiceCorso)
                .orElseThrow(() -> new ResourceNotFoundException("Corso non trovato"));

        Instrument instrument = instrumentRepository.findByCodiceStrumento(codiceStrumento)
                .orElseThrow(() -> new ResourceNotFoundException("Strumento non trovato"));

        if (course.getInstruments().contains(instrument)) {
            throw new DuplicateResourceException("Strumento già associato al corso");
        }

        course.getInstruments().add(instrument);
        courseRepository.save(course);
    }

    public CourseResponse getCourseByCode(String codice) {
        return courseRepository.findByCodiceCorso(codice)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Corso non trovato"));
    }

    public List<CourseResponse> getAllCourses() {
        return courseRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    public List<CourseResponse> getOnlineCourses() {
        return courseRepository.findByOnlineTrue().stream().map(this::mapToResponse).toList();
    }

    // HELPER E MAPPER
    private void validateDates(LocalDate inizio, LocalDate fine) {
        if (fine.isBefore(inizio) || fine.isEqual(inizio)) {
            throw new BusinessRuleException("La data di fine deve essere successiva alla data di inizio");
        }
    }

    private LessonResponse mapToLessonResponse(Lesson l) {
        return new LessonResponse(
                l.getId(),
                l.getNumero(),
                l.getData(),
                l.getOraInizio(),
                l.getDurata(),
                l.getAula(),
                l.getArgomento()
        );
    }

    private CourseResponse mapToResponse(Course c) {
        String nomeInsegnante = (c.getTeacher() != null) 
                ? c.getTeacher().getNome() + " " + c.getTeacher().getCognome() 
                : null;

        double costoTotale = (c.getCostoOrario() != null && c.getTotaleOre() != null)
                ? c.getCostoOrario() * c.getTotaleOre() : 0.0;

        long durataGiorni = java.time.temporal.ChronoUnit.DAYS.between(c.getDataInizio(), c.getDataFine());

        int numeroIscritti = enrollmentRepository.countByCourseCodiceCorso(c.getCodiceCorso());

        List<LessonResponse> lezioniDto = lessonRepository.findAllByCourseCodiceCorso(c.getCodiceCorso())
                .stream()
                .map(l -> new LessonResponse(
                        l.getId(), 
                        l.getNumero(), 
                        l.getData(), 
                        l.getOraInizio(), 
                        l.getDurata(), 
                        l.getAula(), 
                        l.getArgomento()))
                .toList();

        return new CourseResponse(
                c.getId(),
                c.getCodiceCorso(),
                c.getNome(),
                c.getDataInizio(),
                c.getDataFine(),
                c.getCostoOrario(),
                c.getTotaleOre(),
                costoTotale,
                durataGiorni,
                c.isOnline(),
                c.getLivello(),
                nomeInsegnante,
                numeroIscritti,
                lezioniDto
        );
    }
}
