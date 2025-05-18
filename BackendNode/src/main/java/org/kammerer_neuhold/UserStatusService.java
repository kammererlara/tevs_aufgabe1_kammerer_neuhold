package org.kammerer_neuhold;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserStatusService {

    @Autowired
    private UserStatusRepository userStatusRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private final String EXCHANGE_NAME = "user.status.exchange";
    private final String ROUTING_KEY_UPDATE = "user.status.update";

    public void createUserStatus(String username, String status) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }

        UserStatus existingStatus = userStatusRepository.findByUsername(username);
        if (existingStatus != null && existingStatus.isActive()) {
            throw new IllegalArgumentException("User status already exists for username: " + username);
        }

        UserStatus userStatus = new UserStatus();
        userStatus.setUsername(username);
        userStatus.setStatus(status);
        userStatus.setUpdated(LocalDateTime.now());
        userStatus.setActive(true);

        userStatusRepository.save(userStatus);

        sendUserStatusUpdate(userStatus);
    }

    public UserStatus getUserStatus(String username) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        UserStatus userStatus = userStatusRepository.findByUsername(username);
        return (userStatus == null || !userStatus.isActive()) ? null : userStatus;
    }

    @Transactional
    public void updateUserStatus(String username, String status) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }

        UserStatus userStatus = userStatusRepository.findByUsername(username);
        if (userStatus == null || !userStatus.isActive()) {
            throw new IllegalArgumentException("User status does not exist for username: " + username);
        }

        userStatus.setStatus(status);
        userStatus.setUpdated(LocalDateTime.now());
        userStatus.setActive(true);

        userStatusRepository.save(userStatus);

        sendUserStatusUpdate(userStatus);
    }

    public void deleteUserStatus(String username) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }

        UserStatus userStatus = userStatusRepository.findByUsername(username);

        if (userStatus == null || !userStatus.isActive()) {
            throw new IllegalArgumentException("User status does not exist for username: " + username);
        }

        userStatus.setUpdated(LocalDateTime.now());
        userStatus.setActive(false);

        userStatusRepository.save(userStatus);

        System.out.println("Sende Update-Nachricht für Delete: " + userStatus);
        sendUserStatusUpdate(userStatus);
    }

    private void sendUserStatusUpdate(UserStatus userStatus) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY_UPDATE, userStatus);
    }

    @RabbitListener(queues = "user.status.queue")
    @Transactional
    public void receiveUserStatusUpdate(UserStatus receivedStatus) {
        UserStatus currentStatus = userStatusRepository.findByUsername(receivedStatus.getUsername());
        if (currentStatus != null) {
            if (receivedStatus.getUpdated().isAfter(currentStatus.getUpdated())) {
                currentStatus.setStatus(receivedStatus.getStatus());
                currentStatus.setUpdated(receivedStatus.getUpdated());
                currentStatus.setActive(receivedStatus.isActive());
                userStatusRepository.save(currentStatus);
                System.out.println("Status für Benutzer: " + currentStatus.getUsername() + " aktualisiert von RabbitMQ");
            }
        } else {
            userStatusRepository.save(receivedStatus);
            System.out.println("Status für Benutzer: " + receivedStatus.getUsername() + " neu von RabbitMQ empfangen und gespeichert");
        }
    }

    @Scheduled(fixedRate = 500) // Alle 0,5 Sekunden
    public void publishAllUserStatuses() {
        List<UserStatus> allStatuses = userStatusRepository.findAll();
        for (UserStatus status : allStatuses) {
            sendUserStatusUpdate(status);
        }
        System.out.println("Alle User-Status über RabbitMQ veröffentlicht");
    }
}

