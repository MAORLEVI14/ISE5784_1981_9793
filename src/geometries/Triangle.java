package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

public class Triangle extends Polygon {
    /**
     * parameters constructor
     * @param A=point
     * @param B=point
     * @param C=point
     */
    public Triangle(Point A, Point B, Point C) {
        super(A,B,C);
    }


    /**
     * @param ray=Ray object type
     * @return a list of intersection points between the ray and the geometry
     */
    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {

        //Check if the ray intersect the plane.
        List<GeoPoint> intersections = plane.findGeoIntersections(ray);
        if (intersections == null) return null;

        Point p0 = ray.getHead();
        Vector v = ray.getDirection();

        Vector v1 = vertices.get(0).subtract(p0);
        Vector v2 = vertices.get(1).subtract(p0);
        Vector v3 = vertices.get(2).subtract(p0);

        //Check every side of the triangle
        double s1 = v.dotProduct(v1.crossProduct(v2));

        if (isZero(s1)) return null;

        double s2 = v.dotProduct(v2.crossProduct(v3));

        if (isZero(s2)) return null;

        double s3 = v.dotProduct(v3.crossProduct(v1));

        if (isZero(s3)) return null;

        if (!((s1 > 0 && s2 > 0 && s3 > 0) || (s1 < 0 && s2 < 0 && s3 < 0))) return null;

        return List.of(new GeoPoint(this,intersections.get(0).point));
    }
}


