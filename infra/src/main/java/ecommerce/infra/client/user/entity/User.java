package ecommerce.infra.client.user.entity;

import ecommerce.infra.client.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {
    private String userName;
    private String passHash;
    private String fullName;
    private String phone;
    private String email;
    private String birthDay;
    private String imgUrl;
    private String gender;
    private Integer status;
}
