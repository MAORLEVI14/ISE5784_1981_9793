package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static primitives.Util.isZero;

/**
 * Unit tests for primitives.Point class
  * @author maor and amiel
 */
class PointTests {

    /**
     * Test method for {@link primitives.Point#subtract(primitives.Point)}.
     */
    @Test
    void testSubtract() {
        // ============ Equivalence Partitions Tests ==============
        Point p1 = new Point(1, 2, 3);
        Point p2 = new Point(2, 4, 6);
        //TC01: test that subtraction result is right
        assertEquals(new Vector(1, 2, 3), p2.subtract(p1), "ERROR: (point2 - point1) does not work correctly");
        // =============== Boundary Values Tests ==================
        // TC11: test zero vector from subtraction of the same point from itself
        assertThrows(IllegalArgumentException.class, () -> p1.subtract(p1), "subtract() for same point does not throw an exception");
    }

    /**
     * Test method for {@link primitives.Point#add(primitives.Point)}.
     */
    @Test
    void testAdd() {
        // ============ Equivalence Partitions Tests ==============
        Point p1=new Point(1,2,3);
        Point p2=new Point(4,5,6);
        // TC01: test that addition result is right
        assertEquals(new Point(5,7,9),p1.add(p2),"ERROR: (point + vector) = other point does not work correctly");
    }
    /**
     * Test method for {@link primitives.Point#distance(primitives.Point)}.
     */
    @Test
    void testDistance() {
        // ============ Equivalence Partitions Tests ==============
        Point p1 = new Point(2, 4, 6);
        Point p2 = new Point(1, 3, 5);
        //TC01: test that distance result is right
        assertEquals(1.73205,p1.distance(p2), 0.00001, "distance() wrong result");

        // =============== Boundary Values Tests ==================
        // TC11: test distance zero from distance of the same point from itself
        assertTrue(isZero(p1.distance(p1)), "distance() when equals 0 wrong result");
    }


    /**
     * Test method for {@link primitives.Point#distanceSquared(primitives.Point)}.
     */
    @Test
    void testDistanceSquared() {
        Point p1 = new Point(1, 5, 2);
        Point p2 = new Point(2, -3, 7);
        Point p3 = new Point(0, 0, 0);
        // ============ Equivalence Partitions Tests ==============
        // TC0: that the function actually works
        assertEquals(90, p1.distanceSquared(p2), 0.00001, "ERROR: Point.distanceSquared() does not work properly");
        // =============== Boundary Values Tests ==================
        // TC1: squared distance from p0 (0,0,0)
        assertEquals(30, p1.distanceSquared(p3), 0.00001, "ERROR: Point.distanceSquared() does not work when distance is from p0");
        // TC2: squared distance from the same point = 0
        assertTrue(isZero(p1.distanceSquared(p1)), "ERROR: Point.distanceSquared does not work from point to itself");
    }
}