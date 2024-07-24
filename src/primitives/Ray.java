package primitives;
import java.util.List;

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
    public Point findClosestPoint(List<Point> pointList) {
        Point closestPoint = null;
        double minDistance = Double.MAX_VALUE;
        double pointDistance; // the distance between the "this.p0" to each point in the list

        if (!pointList.isEmpty()) {
            for (var pointInList : pointList) {
                pointDistance = this.head.distance(pointInList);
                if (pointDistance < minDistance) {
                    minDistance = pointDistance;
                    closestPoint = pointInList;
                }
            }
        }
        return closestPoint;
    }

}

