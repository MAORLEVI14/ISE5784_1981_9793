package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Comparator;
import java.util.List;
class SphereTests {




    /**
     * Test method for {@link geometries.Sphere#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: test getting a normal vector from any point on sphere
        Sphere sph = new Sphere(new Point(1, 4, 5),3);
        // ensure there are no exceptions
        assertDoesNotThrow(() -> sph.getNormal(new Point(4, 4, 5)), "getNormal() throws an unexpected exception");
        // generate the test result
        Vector result = sph.getNormal(new Point(4, 4, 5));
        // ensure |result| = 1
        assertEquals(1, result.length(), 0.00000001, "Sphere's normal is not a unit vector");
        // ensure the result is orthogonal to sphere at the given point
        assertEquals(new Vector(1, 0, 0), result, "getNormal() wrong result");
    }

    private final Point p001 = new Point(0, 0, 1);
    private final Point p100 = new Point(1, 0, 0);
    private final Vector v001 = new Vector(0, 0, 1);
    /**
     * Test method for {@link geometries.Sphere#findIntsersections(Ray)} (primitives.Ray)
     */
    @Test
    public void testFindIntersections() {
        Sphere sphere = new Sphere(p100, 1d);
        final Point gp1 = new Point(0.0651530771650466, 0.355051025721682, 0);
        final Point gp2 = new Point(1.5348469228349537, 0.8449489742783177, 0);
        final var exp = List.of(gp1, gp2);
        final Vector v310 = new Vector(3, 1, 0);
        final Vector v110 = new Vector(1, 1, 0);
        final Point p01 = new Point(-1, 0, 0);

        // ============ Equivalence Partitions Tests ==============

        // TC01: Ray's line is outside the sphere (0 points)
        assertNull(sphere.findIntsersections(new Ray(p01, v110)), "Ray's line out of sphere");

        // TC02: Ray starts before and crosses the sphere (2 points)
        //List<Point> result1 = sphere.findIntsersections(new Ray(new Point(-1, 0, 0),
        //      new Vector(3, 1, 0)));

        //assertEquals(2, result1.size(), "Wrong number of points");
        //assertEquals(exp.getFirst(), result1, "Ray crosses sphere");

        // TC03: Ray starts inside the sphere (1 point)
        assertEquals(List.of(gp2), sphere.findIntsersections(new Ray(new Point(0.8, 0.6, 0),v310)),"Ray crosses sphere on one point");

        // TC04: Ray starts after the sphere (0 points)
        assertNull(sphere.findIntsersections(new Ray(new Point(3, 0, 0), v110)), "Ray's line after the sphere");

        // =============== Boundary Values Tests ==================
        // **** Group: Ray's line crosses the sphere (but not the center)
        // TC11: Ray starts at sphere and goes inside (1points)
        assertEquals(List.of(gp2),sphere.findIntsersections(new Ray(gp1,v310)), "Ray starts at sphere and goes inside");
        // TC12: Ray starts at sphere and goes outside (0 points)
        assertNull(sphere.findIntsersections(new Ray(gp1,new Vector(-3,-1,0))), "Ray starts at sphere and goes inside");


        // **** Group: Ray's line goes through the center
        // TC13: Ray starts before the sphere (2 points)
        assertEquals(List.of(new Point(0, 0, 0), new Point(2, 0, 0)), sphere.findIntsersections(new Ray(new Point(-1, 0, 0), new Vector(1, 0, 0))), "Ray starts before the sphere and need to be 2 intersections");
        // TC14: Ray starts at sphere and goes inside (1points)
        assertEquals(List.of(new Point(2, 0, 0)), sphere.findIntsersections(new Ray(new Point(0, 0, 0), new Vector(1, 0, 0))), "Ray starts at sphere and goes inside");
        // TC15: Ray starts inside (1 points)
        // TC15: Ray starts inside (1points)
        assertEquals(List.of(new Point(2, 0, 0)), sphere.findIntsersections(new Ray(new Point(0.5, 0, 0), new Vector(1, 0, 0))), "Ray starts inside ");
        // TC16: Ray starts at the center (1points)
        assertEquals(List.of(new Point(2, 0, 0)), sphere.findIntsersections(new Ray(new Point(1, 0, 0), new Vector(0.5, 0, 0))), "Ray starts at the center ");
        // TC17: Ray starts at sphere and goes outside (0 points)
        assertNull(sphere.findIntsersections(new Ray(new Point(2, 0, 0), new Vector(1, 0, 0))), "Ray starts at sphere and goes outside");
        // TC18: Ray starts after sphere (0 points)
        assertNull(sphere.findIntsersections(new Ray(new Point(3, 0, 0), new Vector(1, 0, 0))), "Ray starts after sphere");


        // **** Group: Ray's line is tangent to the sphere (all tests 0 points)
        // TC19: Ray starts before the tangent point
        assertNull(sphere.findIntsersections(new Ray(new Point(2, -1, -1), new Vector(0, 1, 1))), "Ray starts before the tangent point");
        // TC20: Ray starts at the tangent point
        assertNull(sphere.findIntsersections(new Ray(new Point(2, 0, 0), new Vector(0, 1, 1))), "Ray starts at the tangent point");
        // TC21: Ray starts after the tangent point
        assertNull(sphere.findIntsersections(new Ray(new Point(2, 2, 2), new Vector(0, 1, 1))), "Ray starts after the tangent point");


        // **** Group: Special cases
        // TC22: Ray's line is outside, ray is orthogonal to ray start to sphere's center line
        assertNull(sphere.findIntsersections(new Ray(new Point(3, 0, 0), new Vector(0, 0, 1))), "Ray starts after the tangent point");
    }
}