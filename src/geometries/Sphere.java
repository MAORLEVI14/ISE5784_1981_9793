package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

public class Sphere extends RadialGeometry {
    private final Point center;

    public Sphere(Point center, double radius) {
        super(radius);
        this.center = center;
    }


    @Override
    public Vector getNormal(Point MyPoint) {
        return MyPoint.subtract(center).normalize();
    }

    /**
     * @param ray=Ray object type
     * @return a list of intersection points between the ray and the geometry
     */


    @Override
    public List<Point> findIntsersections(Ray ray) {
        // if the ray starts at the center of the sphere
        if (ray.getHead().equals(center)) {
            return List.of(ray.getPoint(radius));
        }
        //check if there is intsersection between them
        Vector v = center.subtract(ray.getHead());

        double tm = alignZero(ray.getDirection().dotProduct(v));

        //check if the ray is tangent to the sphere
        double d = alignZero(Math.sqrt(v.lengthSquared() - tm * tm));
        if (d >= radius) return null;
        double th = alignZero(Math.sqrt(radius * radius - d * d));
        double t1 = alignZero(tm - th);
        double t2 = alignZero(tm + th);
        if (t1 > 0 && t2 > 0) {
            return List.of(ray.getPoint(t1), ray.getPoint(t2));
        }
        if (t1 > 0) {
            return List.of(ray.getPoint(t1));
        }
        if (t2 > 0) {
            return List.of(ray.getPoint(t2));
        }
        return null;


    }
}