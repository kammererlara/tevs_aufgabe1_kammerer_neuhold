package org.kammerer_neuhold;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStatusRepository extends JpaRepository<UserStatus, Long> {
    UserStatus findByUsername(String username);
    void deleteByUsername(String username);
}
