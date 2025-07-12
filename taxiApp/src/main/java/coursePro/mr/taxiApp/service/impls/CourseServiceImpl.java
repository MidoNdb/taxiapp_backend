package coursePro.mr.taxiApp.service.impls;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import coursePro.mr.taxiApp.dao.ConducteurRepository;
import coursePro.mr.taxiApp.dao.CourseRepository;
import coursePro.mr.taxiApp.dao.PassagerRepository;
import coursePro.mr.taxiApp.dao.UtilisateurRepository;
import coursePro.mr.taxiApp.dto.CourseDto;
import coursePro.mr.taxiApp.entity.Conducteur;
import coursePro.mr.taxiApp.entity.Course;
import coursePro.mr.taxiApp.entity.Passager;
import coursePro.mr.taxiApp.entity.Utilisateur;
import coursePro.mr.taxiApp.enums.Role;
import coursePro.mr.taxiApp.enums.StatutCourse;
import coursePro.mr.taxiApp.mapper.CourseMapper;
import coursePro.mr.taxiApp.mapper.PassagerMapper;
import coursePro.mr.taxiApp.model.NotificationMessage;
import coursePro.mr.taxiApp.service.CourseService;
import coursePro.mr.taxiApp.web.NotificationSocketController;
import jakarta.transaction.Transactional;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository repo;
    @Autowired
    ConducteurRepository conducteurRepo;
    @Autowired
    UtilisateurRepository userRepo;
    @Autowired
    PassagerRepository passagerRepo;
    private final NotificationSocketController notificationSocketController;

    @Autowired
    public CourseServiceImpl(NotificationSocketController notificationSocketController) {
        this.notificationSocketController = notificationSocketController;
    }

    @Override
    public Course findById(Long id) {
        return repo.findById(id).orElseThrow();
    }

    @Override
    public List<CourseDto> findByPassagerId(Long passagerId) {
        return repo.findByPassager_Id(passagerId).stream()
                .map(CourseMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<CourseDto> findByConducteurId(Long conducteurId) {
        return repo.findByConducteur_Id(conducteurId).stream()
                .map(CourseMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<CourseDto> findAll() {
        return repo.findAll().stream().map(CourseMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<CourseDto> FindAllCoursEnAttant() {
        return repo.findByStatut(StatutCourse.EN_ATTENTE).stream()
                .map(CourseMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<CourseDto> FindAllCoursEnCours() {
        return repo.findByStatut(StatutCourse.EN_COURS).stream()
                .map(CourseMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<CourseDto> FindAllCoursTerminee() {
        return repo.findByStatut(StatutCourse.TERMINEE).stream()
                .map(CourseMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<CourseDto> FindAllCoursAcceptee() {
        return repo.findByStatut(StatutCourse.ACCEPTEE).stream()
                .map(CourseMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public CourseDto save(CourseDto dto) {
        Course entity = CourseMapper.toEntity(dto);

        // IMPORTANT: Sauvegarder D'ABORD, puis notifier
        Course savedEntity = repo.save(entity);
        CourseDto savedDto = CourseMapper.toDto(savedEntity);

        // Maintenant envoyer la notification avec les données complètes (avec ID)
        notificationSocketController.notifyNewCourse(savedDto);

        return savedDto;
    }
    // @Override
    // public CourseDto save(CourseDto dto) {
    //     Course entity = CourseMapper.toEntity(dto);
    //     notificationSocketController.notifyNewCourse(CourseMapper.toDto(entity));

    //     return CourseMapper.toDto(repo.save(entity));
    // }
    @Override
    public void accepterCourse(Long courseId, Long conducteurId) {
        Conducteur c = conducteurRepo.findById(conducteurId).orElseThrow();
        Course course = repo.findById(courseId).orElseThrow();
        course.setStatut(StatutCourse.ACCEPTEE);
        course.setConducteur(c);
        // logiques d'acceptation ici (mise à jour de la course...)
        repo.save(course);
        // 📢 e une notification pour le passager concerné
        Long passagerId = course.getPassager().getId(); // à implémenter

        NotificationMessage notif = new NotificationMessage();
        notif.setMessage("Votre course a été acceptée par un conducteur !");
        notif.setUtilisateurId(passagerId);

        // 👉 Envoie ciblé au passager
        notificationSocketController.sendToUtilisateur(passagerId, notif);
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

@Override
@Transactional
public CourseDto createCourseByAdmin(CourseDto course) {
    // Validation des données d'entrée
    if (course == null || course.getPassager() == null || 
        course.getPassager().getTelephone() == null) {
        throw new IllegalArgumentException("Données de course invalides");
    }
    
    Optional<Utilisateur> user = userRepo.findByTelephone(course.getPassager().getTelephone());
    Passager passager;
    
    if (!user.isPresent()) {
        // Cas 1: Nouvel utilisateur + nouveau passager
        Passager newPassager = PassagerMapper.toEntity(course.getPassager());
        
        // Sauvegarder l'utilisateur d'abord
        Utilisateur savedUser = userRepo.save(newPassager.getUtilisateur());
        
        // Mettre à jour les références
        newPassager.setUtilisateur(savedUser);
        newPassager.setId(savedUser.getId());
        
        // Sauvegarder le passager
        passager = passagerRepo.save(newPassager);
        
    } else {
        // Cas 2: Utilisateur existant
        Optional<Passager> optPsgr = passagerRepo.findByUtilisateur_Id(user.get().getId());
        
        if (!optPsgr.isPresent()) {
            // Cas 2a: Utilisateur existant mais pas de profil passager
            Passager p = new Passager();
            p.setId(user.get().getId());
            p.setUtilisateur(user.get());
            p.setAdresse(course.getPassager().getAdresse());
            
            passager = passagerRepo.save(p);
        } else {
            // Cas 2b: Passager existant
            passager = optPsgr.get();
        }
    }
    
    // Créer la course avec le passager persisté
    Course entity = CourseMapper.toEntity(course);
    entity.setPassager(passager);
    entity.setConducteur(null); // Pas de conducteur assigné initialement
    
    // Sauvegarder la course
    Course savedEntity = repo.save(entity);
    CourseDto savedDto = CourseMapper.toDto(savedEntity);
    
    // Envoyer la notification
    notificationSocketController.notifyNewCourse(savedDto);
    
    return savedDto;
}
}
