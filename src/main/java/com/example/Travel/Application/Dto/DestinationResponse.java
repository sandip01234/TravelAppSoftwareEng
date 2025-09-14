package com.example.Travel.Application.Dto;

import lombok.Data;

import java.util.Set;

@Data
public class DestinationResponse {

    private Long id;
    private String name;
    private Boolean visited;
    private String priority;
    private Double estinamtedCost;
    private String notes;
    private Set<String> categories;



}
