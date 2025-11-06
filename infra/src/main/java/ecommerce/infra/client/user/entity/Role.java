package ecommerce.infra.client.user.entity;

import ecommerce.infra.client.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "roles")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Role  extends BaseEntity {
    private String name;
    private String description;

}
