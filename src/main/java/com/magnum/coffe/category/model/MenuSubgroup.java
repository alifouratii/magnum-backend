package com.magnum.coffe.category.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuSubgroup {
    @Field("_id")

    private String id;
    private String title;
    private Integer sort_order = 0;
    private List<MenuItem> items = new ArrayList<>();


}