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
public class MenuSubgroup {

    private String id;
    private String title;
    private List<MenuItem> items = new ArrayList<>();


}