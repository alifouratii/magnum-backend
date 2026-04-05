package com.magnum.coffe.category.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "categories")
public class MenuCategory {

    @Id
    private String id;

    private String title;
    private String name;
    private String slug;
    private String icon;
    private Boolean is_active = true;
    private int sort_order = 0;
    private List<MenuSubgroup> subgroups = new ArrayList<>();
    private List<MenuItem> items = new ArrayList<>();

}