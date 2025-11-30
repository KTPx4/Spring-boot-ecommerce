package ecommerce.infra.client.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "user_roles", schema = "main", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "role_id"})
})
@AllArgsConstructor
@NoArgsConstructor
public class UserRole extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
}
