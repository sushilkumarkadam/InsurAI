package com.insurai.backend.service;

import com.insurai.backend.dto.NotificationDto;
import java.util.List;

public interface NotificationService {
    List<NotificationDto> getUserNotifications(Long userId);
    void createNotification(Long userId, String message);
    void markAsRead(Long notificationId);
}
