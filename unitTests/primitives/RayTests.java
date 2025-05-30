package primitives;
import geometries.Sphere;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class RayTests {
    @Test
    public void getPoint(){
        // ============ Equivalence Partitions Tests ==============
        //TC01: test getPoint Standard

        Point p1=new Point(1,1,1);
        Vector v1=new Vector(1,0,0);
        Ray r=new Ray(p1,v1);
        assertEquals(r.getPoint(2),new Point(3,1,1),"ERROR:getPoint does not work correctly");

        //TC02: test negative getPoint
        assertEquals(r.getPoint(-2),new Point(-1,1,1),"ERROR:getPoint for negative distance does not work correctly");

        // =============== Boundary Values Tests ==================

        //TC10: test getPoint for distance=0
        assertEquals(r.getPoint(0),p1,"ERROR:getPoint for distance=0 does not work correctly");

    }

    /**
     * Test method for {@link primitives.Ray#findClosestPoint(List)}.
     */
    @Test
    void findClosestPoint() {

        List<Point> pointList = new LinkedList<>();

        Point p1 = new Point(1, 1, 1);
        Point p2 = new Point(2, 2, 2);
        Point p3 = new Point(3, 3, 3);

        pointList.add(p1);
        pointList.add(p2);
        pointList.add(p3);

        Vector vector = new Vector(0, -0.5, 0);

        // ============ Equivalence Partitions Tests ==============
        //TC01: The closest point is in the middle of the list
        Ray ray1 = new Ray(new Point(2, 2.5, 2), vector);
        assertEquals(p2, ray1.findClosestPoint(pointList), "The point in the middle");

        // =============== Boundary Values Tests ==================
        //TC10: The closest point is the first point in the list
        Ray ray2 = new Ray(new Point(1, 1.25, 1), vector);
        assertEquals(p1, ray2.findClosestPoint(pointList), "The point is the first one");

        //TC11: The closest point is the last point in the list
        Ray ray3 = new Ray(new Point(3, 3.5, 3), vector);
        assertEquals(p3, ray3.findClosestPoint(pointList), "The point is the last one");

        //TC12: The list is null
        pointList.clear();
        assertNull(ray3.findClosestPoint(pointList), "The list is empty");
    }


}