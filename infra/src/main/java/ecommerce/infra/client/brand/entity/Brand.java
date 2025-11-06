package ecommerce.infra.client.brand.entity;


import ecommerce.infra.client.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "brands")
@NoArgsConstructor
@AllArgsConstructor
public class Brand extends BaseEntity {

    private String name;
    private String slug;
    private String logo_url;

}
