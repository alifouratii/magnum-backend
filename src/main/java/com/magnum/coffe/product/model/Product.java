package com.magnum.coffe.product.model;


import com.magnum.coffe.document.dao.model.Doc;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "products")
public class Product {

    @Id
    private String id;

    private String categoryId;
    private String subgroupId;

    private String name;
    private String slug;

    private String shortDescription;
    private String longDescription;

    private Double price;
    private String badge;

    private Boolean featured = false;
    private Boolean available = true;

    private Integer sortOrder = 0;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<Doc> images = new ArrayList<>();


}