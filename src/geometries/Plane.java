package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import static primitives.Util.*;
import java.util.List;

public class Plane implements Geometry{
    Point q;
    Vector normal;

    /**
     * parameters constructors
     * @param myq=q
     * @param myNormal=normal
     */
    public Plane(Point myq, Vector myNormal) {
        this.q = myq;
        this.normal = myNormal.normalize();
    }



    /**
     * parameters constructors
     * @param point1 point number 1
     * @param point2 point number 2
     * @param point3 point number 3
     */
    public Plane (Point point1,Point point2,Point point3){
        Vector v1=point2.subtract(point1);
        Vector v2=point3.subtract(point1);
        normal =v1.crossProduct(v2).normalize();
        q=point1;
    }

    /**
     * @return normal
     */
    Vector getNormal(){
        return normal;
    }

    public Vector getNormal(Point point) {return normal;}

    /**
     * @param ray=Ray object type
     * @return a list of intersection points between the ray and the geometry
     */
    public List<Point> findIntsersections(Ray ray){
        Point p0=ray.getHead();
        Vector v=ray.getDirection();
        //הקרן על המישור
        if (q.equals(p0))//if ray start on the plane but doesn't cut
            return null;

        Vector n=normal;//vector normal to plane
        double nv=n.dotProduct(v);// the formula's denominator of "t" (t =(n*(Q-P0))/nv)

        //if the ray is lying on the plane
        if (isZero(nv))
            return null;

        Vector q_p0= q.subtract(p0);
        double nP0Q0= alignZero(n.dotProduct(q_p0));

        // t should be bigger than 0
        if(isZero(nP0Q0)){
            return null;
        }

        double t =alignZero(nP0Q0 / nv);

        // t should be bigger than 0
        if(t<=0){
            return null;
        }

        return List.of(ray.getPoint(t));
    }
}
