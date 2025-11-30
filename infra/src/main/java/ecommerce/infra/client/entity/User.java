package ecommerce.infra.client.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users", schema = "main")
public class User extends BaseEntity {
    @Column(unique = true, nullable = true)
    private String userId;

    @Column(unique = true, nullable = false)
    private String userName;
    
    @Column(nullable = false)
    private String passHash;
    
    private String fullName;
    private String phone;
    
    @Column(unique = true)
    private String email;
    
    private String birthDay;
    private String imgUrl;
    private String gender;
    
    @Column(nullable = false)
    private Integer status = 1;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<UserRole> userRoles = new HashSet<>();
}
