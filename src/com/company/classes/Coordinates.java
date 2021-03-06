package com.company.classes;

public class Coordinates {
    private long x; //Максимальное значение поля: 768
    private Integer y; //Поле не может быть null

    public long getX() {
        return x;
    }

    public void setX(long x) {
        //todo change on exception
        if (x > 768){
            System.out.println("X cannot be bigger than 768");
        } else{
            this.x = x;
        }
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        //todo change on exception
        if (y == null){
            System.out.println("Y cannot be null");
        } else {
            this.y = y;
        }
    }
}
