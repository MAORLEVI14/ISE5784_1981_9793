package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static primitives.Util.isZero;

public class Cylinder extends Tube {
    public double height;
    /**
     *constractor
     * @param height=height of the cylinder
     * @param axis= axis of the cylinder
     * @param radius=radius the direction of the cylinder
     */
    public Cylinder(Ray axis, double radius, double height) {
        super(radius, axis);
        this.height = height;
    }



}
