package geometries;

import primitives.Point;
import primitives.Vector;

public class Plane {
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
        this.q=point1;
        Vector v1=point2.subtract(point1);
        Vector v2 =point3.subtract(point1);
        this.normal=v1.crossProduct(v2).normalize();
    }

    /**
     * @return normal
     */
    Vector getNormal(){
        return normal;
    }

    Vector getNormal(Point point){
        return null;
    }

}
