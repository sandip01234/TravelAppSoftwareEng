package com.example.Travel.Application.Dto;

import lombok.Data;

import java.util.Set;

@Data
public class DestinationDto {

    private String name;
    private Boolean visited;
    private String priority;
    private Double estinamtedCost;
    private String notes;
    private Set<Long> categoryIds;

}