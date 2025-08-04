package coursePro.mr.taxiApp.service.impls;

import java.time.LocalDateTime;
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
import coursePro.mr.taxiApp.enums.StatutCourse;
import coursePro.mr.taxiApp.mapper.CourseMapper;
import coursePro.mr.taxiApp.mapper.PassagerMapper;
import coursePro.mr.taxiApp.model.NotificationMessage;
import coursePro.mr.taxiApp.service.CourseService;
import coursePro.mr.taxiApp.web.NotificationSocketController;
import jakarta.persistence.EntityNotFoundException;
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
        return repo.findByPassager_IdOrderByCreatedAtDesc(passagerId).stream()
                .map(CourseMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<CourseDto> findByConducteurId(Long conducteurId) {  
        return repo.findByConducteur_IdOrderByCreatedAtDesc(conducteurId).stream()
                .map(CourseMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<CourseDto> findAll() {
        return repo.findAll().stream().map(CourseMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<CourseDto> FindAllCoursEnAttant() {
        return repo.findByStatutOrderByCreatedAtDesc(StatutCourse.EN_ATTENTE).stream()
                .map(CourseMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<CourseDto> FindAllCoursEnCours() {
        return repo.findByStatutOrderByCreatedAtDesc(StatutCourse.EN_COURS).stream()
                .map(CourseMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<CourseDto> FindAllCoursTerminee() {
        return repo.findByStatutOrderByCreatedAtDesc(StatutCourse.TERMINEE).stream()
                .map(CourseMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<CourseDto> FindAllCoursAcceptee() {
        return repo.findByStatutOrderByCreatedAtDesc(StatutCourse.ACCEPTEE).stream()
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
   
   @Override
public void accepterCourse(Long courseId, Long conducteurId) {
    // Validation des paramètres
    if (courseId == null || conducteurId == null) {
        throw new IllegalArgumentException("L'ID de la course et l'ID du conducteur ne peuvent pas être null");
    }
    
    // Récupération du conducteur avec gestion d'erreur
    Conducteur conducteur = conducteurRepo.findById(conducteurId)
        .orElseThrow(() -> new EntityNotFoundException("Conducteur non trouvé avec l'ID: " + conducteurId));
    
    // Récupération de la course avec gestion d'erreur
    Course course = repo.findById(courseId)
        .orElseThrow(() -> new EntityNotFoundException("Course non trouvée avec l'ID: " + courseId));
    
    // Vérification que la course peut être acceptée
    if (course.getStatut() != StatutCourse.EN_ATTENTE) {
        throw new IllegalStateException("Cette course ne peut pas être acceptée. Statut actuel: " + course.getStatut());
    }
    
    course.setStatut(StatutCourse.ACCEPTEE);
    course.setConducteur(conducteur);
    course.setPickupTime(LocalDateTime.now()); // Optionnel: définir l'heure d'acceptation
    
    // Sauvegarde de la course
    Course savedCourse = repo.save(course);
    
    try {
        Long passagerId = savedCourse.getPassager().getId();
        
        NotificationMessage notif = new NotificationMessage();
        notif.setMessage("Votre course a été acceptée par un conducteur !");
        notif.setUtilisateurId(passagerId);
        
        // Envoi ciblé au passager
        notificationSocketController.sendToUtilisateur(passagerId, notif);
        
        // Optionnel: Notification au conducteur aussi
        NotificationMessage notifConducteur = new NotificationMessage();
        notifConducteur.setMessage("Vous avez accepté une nouvelle course");
        notifConducteur.setUtilisateurId(conducteurId);
        notificationSocketController.sendToUtilisateur(conducteurId, notifConducteur);
        
    } catch (Exception e) {
        // Log l'erreur mais ne fait pas échouer la transaction principale
       // logger.error("Erreur lors de l'envoi de la notification pour la course " + courseId, e);
    }
}
    @Override
public CourseDto updateStatusCourse(Long courseId, String status) {
    // Validation des paramètres
    if (courseId == null || status == null || status.trim().isEmpty()) {
        throw new IllegalArgumentException("L'ID de la course et le statut ne peuvent pas être null ou vides");
    }
    
    // Récupération de la course avec gestion d'erreur
    Course course = repo.findById(courseId)
        .orElseThrow(() -> new EntityNotFoundException("Course non trouvée avec l'ID: " + courseId));
    
    // Conversion du string en enum StatutCourse
    StatutCourse nouveauStatut;
    try {
        nouveauStatut = StatutCourse.valueOf(status.toUpperCase());
    } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException("Statut invalide: " + status);
    }
    
    // Mise à jour du statut uniquement
    course.setStatut(nouveauStatut);
    
    // Sauvegarde de la course
    Course savedCourse = repo.save(course);
    
    // Retourner le DTO
    return CourseMapper.toDto(savedCourse);
}
   @Override
@Transactional
public boolean delete(Long idCourse) {
    // Validation de l'ID
    if (idCourse == null || idCourse <= 0) {
        throw new IllegalArgumentException("ID de course invalide");
    }
    
    // Vérifier si la course existe
    Optional<Course> optCourse = repo.findById(idCourse);
    if (!optCourse.isPresent()) {
        throw new EntityNotFoundException("Course non trouvée avec l'ID: " + idCourse);
    }
    
    Course course = optCourse.get();
    
    // Vérifier si la course peut être supprimée
    // (par exemple, ne pas supprimer une course en cours)
    if (!course.getStatut().equals(StatutCourse.EN_ATTENTE)) {
        throw new IllegalStateException("Impossible de supprimer une course en cours");
    }
    
    try {
        // Supprimer la course
        repo.deleteById(idCourse);
        
        // Envoyer une notification de suppression
        //notificationSocketController.notifyDeletedCourse(idCourse);
        
        return true;
        
    } catch (Exception e) {
        throw new RuntimeException("Erreur lors de la suppression de la course", e);
    }
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


@Override
@Transactional
public CourseDto updateCourseByAdmin(CourseDto course, Long idCourse) {
    // Validation des données d'entrée
    if (course == null || course.getPassager() == null || 
        course.getPassager().getTelephone() == null) {
        throw new IllegalArgumentException("Données de course invalides");
    }
    
    // Vérifier si la course existe
    Optional<Course> optCourse = repo.findById(idCourse);
    if (!optCourse.isPresent()) {
        throw new IllegalArgumentException("Course non trouvée avec l'ID: " + idCourse);
    }
    
    Course existingCourse = optCourse.get();
    
    // Récupérer le passager existant de la course
    Passager passager = existingCourse.getPassager();
    
    // Mettre à jour seulement le nom et le téléphone
    Utilisateur utilisateur = passager.getUtilisateur();
    utilisateur.setNom(course.getPassager().getNom());
    utilisateur.setTelephone(course.getPassager().getTelephone());
    
    // Sauvegarder l'utilisateur mis à jour
    Utilisateur savedUser = userRepo.save(utilisateur);
    
    // Mettre à jour les références du passager
    passager.setUtilisateur(savedUser);
    
    // Mettre à jour la course existante avec les nouvelles données
    Course entity = CourseMapper.toEntity(course);
    entity.setId(idCourse); // Conserver l'ID de la course existante
    entity.setPassager(passager);
    
    // Conserver le conducteur existant (ne pas le modifier)
    entity.setConducteur(existingCourse.getConducteur());
    
    // Conserver les autres champs qui ne doivent pas être modifiés
    //entity.setCreatedAt(existingCourse.getCreatedAt());
    //entity.setUpdatedAt(new Date()); // Mettre à jour la date de modification
    
    // Sauvegarder la course mise à jour
    Course savedEntity = repo.save(entity);
    CourseDto savedDto = CourseMapper.toDto(savedEntity);
    
    // Envoyer la notification pour course mise à jour
    notificationSocketController.notifyNewCourse(savedDto);
    
    return savedDto;
}
}
