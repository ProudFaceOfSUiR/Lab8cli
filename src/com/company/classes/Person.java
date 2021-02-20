package com.company.classes;

public class Person {
    private Long height; //Поле может быть null, Значение поля должно быть больше 0
    private Integer weight; //Поле может быть null, Значение поля должно быть больше 0

    public Long getHeight() {
        return height;
    }

    public void setHeight(Long height) {
        if (height <= 0){
            System.out.println("Height must be positive number");
        } else{
            this.height = height;
        }
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        if (weight <= 0){
            System.out.println("weight must be positive number");
        } else{
            this.weight = weight;
        }
    }
}
