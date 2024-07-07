package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static primitives.Util.isZero;

/**
 * Unit tests for primitives.Vector class
 * @author maor  and amiel
 */
class VectorTests {

    @Test
    void testAdd() {
        //================= Equivalence Partitions Tests =================
        // TC0: the function works properly: vector+ vector= vector.
        Vector v1=new Vector(1,2,3);
        Vector v2=new Vector(4,5,6);
        assertEquals(new Vector(5,7,9),v1.add(v2),"ERROR: (point + vector) = other point does not work correctly");
        //================= Boundary Values Tests ======================
        // TC1: if u vector+-the same vector. will it throw a vec zero exception.
        Vector v3=new Vector(-1,-2,-3);
        assertThrows(IllegalArgumentException.class,()-> v1.add(v3),"Adding a vector to the opposite vector should throw an exception");
    }

    /**
     * Test method for {@link primitives.Vector#scale(double)}.
     */
    @Test
    void testScale() {
        // ============ Equivalence Partitions Tests ==============
        Vector vector = new Vector(1, 2, 3);
        //TC01: test that scaling a vector by a positive factor is right
        assertEquals(new Vector (4,8,12),vector.scale(4),"scale() wrong result for scaling by a positive factor");
        //TC02: test that scaling a vector by a negative factor is right
        assertEquals(new Vector(-4,-8,-12),vector.scale(-4),"scale() wrong result for scaling by a negative factor");
    }

    /**
     *  Test Method for {@link Vector#dotProduct(Vector)}
     */
    @Test
    void testDotProduct() {
        Vector v1 = new Vector(3, 6, 7);
        // ============ Equivalence Partitions Tests ==============
        //TC01: Test that dot product of two vector with an acute angle is proper
        Vector v2 = new Vector(2, 7, 1);
        assertEquals(2 * 3 + 6 * 7 + 7, v1.dotProduct(v2), 0.00001, "dotProduct() wrong result for acute angle");

        //TC02: test that dot product of two vectors with an obtuse angle is proper
        Vector v3 = new Vector(-5, 2, 4);
        assertEquals((-5) * 3 + 2 * 6 + 4 * 7, v1.dotProduct(v3), 0.00001, "dotProduct() wrong result for obtuse angle");

        // =============== Boundary Values Tests ==================
        //TC11: test dot product two orthogonal vectors
        Vector v4 = new Vector(-6, 3, 0);
        assertTrue(isZero(v1.dotProduct(v4)), "dotProduct() for two orthogonal vectors does not return 0");

        //TC12: test that dot product of two vectors when one of the vectors is a unit vector
        assertEquals((0.8 * 3 + 0.6 * 6 + 0), v1.dotProduct((new Vector(4, 3, 0)).normalize()), 0.00001, "dotProduct() wrong result when one of the vectors is a unit vector");
    }

    /**
     * Test Method for {@link Vector#crossProduct(Vector)}
     */
    @Test
    void testCrossProduct() {
        Vector v1 = new Vector(1, 2, 3);

        // ============ Equivalence Partitions Tests ==============
        Vector v2 = new Vector(0, 3, -2);
        Vector vr = v1.crossProduct(v2);

        // TC01: Test that length of cross-product is proper
        assertEquals(v1.length() * v2.length(), vr.length(), 0.00001, "crossProduct() wrong result length");
        // TC02: Test cross-product result orthogonality to its operands
        assertTrue(primitives.Util.isZero(vr.dotProduct(v1)), "crossProduct() result is not orthogonal to 1st operand");
        assertTrue(primitives.Util.isZero(vr.dotProduct(v2)), "crossProduct() result is not orthogonal to 2nd operand");
        // =============== Boundary Values Tests ==================
        // TC11: test zero vector from cross product of co-lined vectors
        Vector v3 = new Vector(-2, -4, -6);
        assertThrows(IllegalArgumentException.class, () -> v1.crossProduct(v3), "crossProduct() for parallel vectors does not throw an exception");
    }

    /**
     * Test Method for {@link Vector#lengthSquared()}
     */
    @Test
    void testLengthSquared() {
        Vector v1 = new Vector(1, 2, 3);
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test that length squared result of vector is proper
        assertEquals(14, v1.lengthSquared(), 0.00001, "distanceSquared() wrong result");
    }

    /**
     * Test Method for {@link Vector#length()}
     */
    @Test
    void testLength() {
        // ============ Equivalence Partitions Tests ==============
        // TC0: Test that length result of vector is proper
        Vector v1 = new Vector(1, 2, 3);
        assertEquals(3.7416, v1.length(), 0.0001, "length() wrong result");
    }

    /**
     * Test Method for {@link Vector#normalize()}
     */
    @Test
    void testNormalize(){
        // ============ Equivalence Partitions Tests ==============
        Vector v = new Vector(2, 4, 0);
        Vector u = v.normalize();

        // TC01: Test normalized vector is a unit vector
        assertEquals(1, u.length(), 0.00001, "normalize() result vector isn't a unit vector");
        // TC02: Test normalized vector is on the same line as the vector that was normalized
        assertThrows(IllegalArgumentException.class, () -> v.crossProduct(u), "normalize() normalized vector and the vector that was normalized are not on the same line");

        // TC02: Test normalized vector is on the same line as the vector that was normalized
        assertTrue(v.dotProduct(u) > 0, "normalize() normalized vector and the vector that was normalized are with opposite signs");

    }
}