package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
public class Geometries implements Intersectable{
    List<Intersectable> MyIntersectables= new LinkedList<Intersectable>();

    public Geometries() {

    }

    public Geometries(Intersectable...intersectables) {
        MyIntersectables = new LinkedList<Intersectable>();
        Collections.addAll(MyIntersectables,intersectables);
    }

    public void add(Intersectable... intersectables){
        Collections.addAll(MyIntersectables,intersectables);
    }

    @Override
    public List<Point> findIntsersections(Ray ray) {
        LinkedList<Point> points=null; // החזקת נקודות חיתוך עם כל אחתד מהקאומטרים
        for(var geometry: MyIntersectables){
            var geometryList=geometry.findIntsersections(ray);
            if(geometryList!=null){
                if(points==null){
                    points=new LinkedList<>();
                }
                points.addAll(geometryList);
            }
        }
        return points;
    }
}
