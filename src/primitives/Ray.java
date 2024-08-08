package primitives;
import java.util.List;
import geometries.Intersectable.GeoPoint;

import static primitives.Util.*;

public class Ray {
    private final Point head;
    private final Vector direction;


    /**
     *parameters constructor
     * @param head=point
     * @param direction=vector
     */
    public Ray(Point head, Vector direction) {
        this.head = head;
        this.direction = direction.normalize(); // Assuming normalize() method normalizes the vector
    }

    /**
     *
     * @return head
     */
    public Point getHead() {
        return head;
    }

    /**
     *
     * @return direction
     */
    public Vector getDirection() {
        return direction;
    }

    /**
     * @param obj=point
     * @return If two points are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Ray) {
            if(head.xyz==((Ray) obj).head.xyz){
                return true;
            }
        }
        return false;
    }

    /**
     * @return A string representing the vector class
     */
    public String tostring(){
        return "Ray{"+
                "head='"+head+
                "direction'"+direction+
                "}";

    }


    /**
     *get Point at specific distance in the ray's direction
     *
     * @param t is a distance for reaching new Point
     * @return new {@link Point}
     */

    public Point getPoint(double t) {
        if (isZero(t)) {
            // אם המרחק הוא 0, יש להחזיר את נקודת ההתחלה עצמה
            return head;
        }

        return head.add(direction.scale(t));
    }
    public Point findClosestPoint(List<Point> points) {
        return points == null || points.isEmpty() ? null
                : findClosestGeoPoint(points.stream().map(p -> new GeoPoint(null, p)).toList()).point;
    }

    /**
     * Return the closest GeoPoint from all intersection GeoPoints
     *
     * @param geoPointList list of intersections
     * @return {@link GeoPoint}
     */
    public GeoPoint findClosestGeoPoint(List<GeoPoint> geoPointList) {

        GeoPoint closestPoint = null;
        double minDistance = Double.MAX_VALUE;
        double geoPointDistance; // the distance between the "this.p0" to each point in the list

        if (!geoPointList.isEmpty()) {
            for (var geoPoint : geoPointList) {
                geoPointDistance = this.head.distance(geoPoint.point);
                if (geoPointDistance < minDistance) {
                    minDistance = geoPointDistance;
                    closestPoint = geoPoint;
                }
            }
        }
        return closestPoint;
    }

}

