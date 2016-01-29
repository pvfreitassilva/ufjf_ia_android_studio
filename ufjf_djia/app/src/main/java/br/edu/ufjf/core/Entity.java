package br.edu.ufjf.core;

import android.graphics.Point;

import java.io.Serializable;

/**
 * Created by Paulo VItor on 07/12/2015.
 */
public abstract class Entity {

    public Point point;

    public Entity(Point point){
        setPoint(point);
    }

    void setPoint(Point point) {
        this.point = point;
    }


}
