package coursePro.mr.taxiApp.service;

import java.util.List;

import coursePro.mr.taxiApp.dto.CourseDto;
import coursePro.mr.taxiApp.entity.Course;

public interface CourseService {

    Course findById(Long id);
    List<CourseDto> findByPassagerId(Long passagerId);
    List<CourseDto> findByConducteurId(Long conducteurId);
    List<CourseDto> findAll();

    List<CourseDto> FindAllCoursEnAttant();
    List<CourseDto> FindAllCoursEnCours();
    List<CourseDto> FindAllCoursTerminee();
    List<CourseDto> FindAllCoursAcceptee();

    CourseDto save(CourseDto course);
    CourseDto createCourseByAdmin(CourseDto course);
    CourseDto updateCourseByAdmin(CourseDto course,Long idCourse);
    public void accepterCourse(Long courseId, Long conducteurId);
    boolean delete(Long id);
    
}

