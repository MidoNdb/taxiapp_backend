package coursePro.mr.taxiApp.service.impls;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import coursePro.mr.taxiApp.dao.NotificationRepository;
import coursePro.mr.taxiApp.dao.UtilisateurRepository;
import coursePro.mr.taxiApp.dto.NotificationDto;
import coursePro.mr.taxiApp.entity.Notification;
import coursePro.mr.taxiApp.mapper.NotificationMapper;
import coursePro.mr.taxiApp.service.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository repo;

    @Autowired
    private UtilisateurRepository utilisateurRepo;

    @Override
    public List<NotificationDto> findAll() {
        return repo.findAll().stream()
                .map(NotificationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationDto> findByUserId(Long userId) {
        return repo.findByUtilisateur_Id(userId).stream()
                .map(NotificationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public NotificationDto save(Notification n) {
        // Notification n = NotificationMapper.toEntity(dto, user);
        return NotificationMapper.toDto(repo.save(n));
    }
}

