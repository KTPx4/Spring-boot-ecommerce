package ecommerce.infra.client.user.entity;

import ecommerce.infra.client.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "user_roles")
@AllArgsConstructor
@NoArgsConstructor
public class UserRole extends BaseEntity{
    @ManyToOne
    private User user;
    @ManyToOne
    private Role role;

}
