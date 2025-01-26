package com.e_commerce.entity;

import com.e_commerce.entity.Role;
import com.e_commerce.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_role")
@Data
@NoArgsConstructor
public class UserRole {

    @EmbeddedId
    private UserRoleId id = new UserRoleId();

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @MapsId("roleId")
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;





    @Embeddable
    @Data
    public static class UserRoleId implements java.io.Serializable {

        private Long userId;
        private Long roleId;

    }
}
