package com.magnum.coffe.category.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuItem {

    private Integer id;
    private String name;
    private String description;
    private String price;
    private String badge;
    private List<String> images = new ArrayList<>();


}