package ecommerce.infra.client.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "brands", schema = "main")
@NoArgsConstructor
@AllArgsConstructor
public class Brand extends BaseEntity {

    private String name;
    private String slug;
    private String logo_url;

}
