package geometries;
import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.List;

public class Tube extends RadialGeometry{
    Ray axis;


    /**
     *constractor
     * @param MyAxis=direction
     * @param MyRadius=radius
     */
    // Public constructor
    public Tube(double MyRadius, Ray MyAxis) {
        super(MyRadius);
        this.axis = MyAxis;
    }

    /**
     * Calculation of the normal vector for the Tube according to the formula
     * @param point=point on Tube
     * @return vector normal for tube
     */
    @Override
    public Vector getNormal(Point point) {
        Point o;
        double t=point.subtract(axis.getHead()).dotProduct(axis.getDirection());
        if(Util.isZero(t))
            o=axis.getHead();
        else
            o=(axis.getHead()).add(axis.getDirection().scale(t));
        return point.subtract(o).normalize();
    }

    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
        return null;
    }
}
