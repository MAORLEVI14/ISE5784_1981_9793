package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

class TubeTests {


    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        //TC01: test getting a normal vector
        Tube tube= new Tube(1,new Ray(new Point(0,0,0),new Vector(0,1,0)));
        Vector result= tube.getNormal(new Point (1,0,0));
        assertEquals(1,result.length(),0.0000001,"Tube's normal is not a unit vector");
        assertEquals(new Vector(1,0,0),result," wromg result of getNormal");

        // =============== Boundary Values Tests ==================
        //TC11:
        Vector myResult=tube.getNormal(new Point(1,0,0));
        //
        assertEquals(new Vector(1,0,0),myResult,"wromg result of getNormal");
    }
}
