package org.kammerer_neuhold;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class UserStatus {
    @Id
    @Column
    private String username;
    private String status;
    @Column(nullable = false)
    private LocalDateTime updated;
    private boolean active;
}
