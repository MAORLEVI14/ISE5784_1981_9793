package primitives;

import geometries.Sphere;
import org.junit.jupiter.api.Test;
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



}