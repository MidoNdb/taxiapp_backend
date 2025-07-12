package coursePro.mr.taxiApp.service;

import java.util.List;

import coursePro.mr.taxiApp.dto.NotificationDto;
import coursePro.mr.taxiApp.entity.Notification;

public interface NotificationService {

    List<NotificationDto> findAll();
    List<NotificationDto> findByUserId(Long userId);
    NotificationDto save(Notification notification);
}

