package br.edu.ufjf.core;

import android.graphics.Point;

/**
 * Created by Paulo VItor on 07/12/2015.
 */
public abstract class Entity {

    public Point point;
    //TODO inserir no inicio

    public Entity(Point point){
        setPoint(point);
    }


    void setPoint(Point point) {
        this.point = point;
    }


}
