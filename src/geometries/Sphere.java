package geometries;

import primitives.Point;
import primitives.Vector;

public class Sphere extends RadialGeometry{
    private final Point center;
    public Sphere(Point center, double radius) {
        super(radius);
        this.center = center;
    }
    public Vector getNormal(Point p) {
        return p.subtract(center).normalize();
    }
    public Point getCenter() {
        return center;
    }
    //check dot product
    public boolean checkDotProduct(Point p, Vector v) {
        return getNormal(p).dotProduct(v) == 0;
    }
}
