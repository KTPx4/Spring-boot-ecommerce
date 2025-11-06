package ecommerce.infra.client.entity;

import ecommerce.infra.client.category.entity.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "attributes")
public class Attribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private Category category;

    private String name;
    private String description;
    private boolean is_classification;
    private boolean is_display;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<AttrValue> attrValues =  new ArrayList<>();
}
