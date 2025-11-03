package com.main.ecommerce.model.products;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "attribute_values")
public class AttrValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Attribute attribute;

    private String value;
    private String image_url;
}
