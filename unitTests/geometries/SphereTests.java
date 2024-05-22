package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

class SphereTests {

    @Test
    void testGetNormal() {
        Sphere s = new Sphere(Point.ZERO, 1);
        Point p = new Point(0, 0, 10);
        assertEquals(new Vector(0, 0, 1), s.getNormal(p));
    }
}