package ru.netology.vetyugov.mipt.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@Data
@Getter
@Builder
@ToString
public class ForbesDto {
    private int rank;
    private String name;
    private float netWorth;
    private int age;
    private String country;
    private String source;
    private String industry;
}
