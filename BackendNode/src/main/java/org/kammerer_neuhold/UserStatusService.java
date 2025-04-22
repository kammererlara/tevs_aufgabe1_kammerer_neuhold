package org.kammerer_neuhold;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserStatusService {

    @Autowired
    private UserStatusRepository userStatusRepository;

    public void createUserStatus(String username, String status) {
        if (userStatusRepository.findByUsername(username) != null)
            throw new IllegalArgumentException("User status already exists for username: " + status);

        UserStatus userStatus = new UserStatus();
        userStatus.setUsername(username);
        userStatus.setStatus(status);
        userStatus.setUpdated(LocalDateTime.now());

        userStatusRepository.save(userStatus);
    }

    public UserStatus getUserStatus(String username) {
        if (username == null || username.isEmpty())
            throw new IllegalArgumentException("Username cannot be null or empty");

        return userStatusRepository.findByUsername(username);
    }

    public void updateUserStatus(String username, String status) {
        if (username == null || username.isEmpty())
            throw new IllegalArgumentException("Username cannot be null or empty");

        UserStatus userStatus = userStatusRepository.findByUsername(username);
        if (userStatus == null)
            throw new IllegalArgumentException("User status does not exist for username: " + username);

        userStatus.setStatus(status);
        userStatus.setUpdated(LocalDateTime.now());

        userStatusRepository.save(userStatus);
    }

    public void deleteUserStatus(String username) {
        if (username == null || username.isEmpty())
            throw new IllegalArgumentException("Username cannot be null or empty");

        if (userStatusRepository.findByUsername(username) == null)
            throw new IllegalArgumentException("User status does not exist for username: " + username);

        userStatusRepository.deleteByUsername(username);
    }
}
