package geometries;
import primitives.Point;
import primitives.Ray;
import java.util.List;

public interface Intersectable {
    /**
     * @param ray=Ray object type
     * @return a list of intersection points between the ray and the geometry
     */
    public List<Point> findIntsersections(Ray ray);
}
