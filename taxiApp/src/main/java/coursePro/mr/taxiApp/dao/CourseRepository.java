package coursePro.mr.taxiApp.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import coursePro.mr.taxiApp.entity.Course;
import coursePro.mr.taxiApp.enums.StatutCourse;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByPassager_IdOrderByCreatedAtDesc(Long passagerId);
    List<Course> findByConducteur_IdOrderByCreatedAtDesc(Long conducteurId);
    List<Course> findByStatutOrderByCreatedAtDesc(StatutCourse statut);
}

