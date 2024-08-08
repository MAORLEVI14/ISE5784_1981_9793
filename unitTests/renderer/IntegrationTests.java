package renderer;

import geometries.Intersectable;
import geometries.Plane;
import geometries.Sphere;
import geometries.Triangle;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import scene.Scene;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IntegrationTests {
    /**
     *Integration tests.
     * Testing camera combined with constructRay for sphere,plane and triangle
     */
    private final Camera.Builder cameraBuilder = Camera.getBuilder() .setRayTracer(new SimpleRayTracer(new Scene("Test")))
            .setImageWriter(new ImageWriter("Test", 1, 1)).setDirection(new Vector(0, 0, -1),
                    new Vector(0, 1, 0)).setVpSize(3, 3);

    /**
     * function for constructing ray and calculating the number of intersections
     * preventing needles repetitions
     * @return sum of intersections
     */
    private int sumIntersection(Camera camera, Intersectable body, int nX, int nY) {
        int sum = 0;
        // sum the intersections for each ray from each pixel
        for (int j = 0; j < nX; j++) {
            for (int i = 0; i < nY; i++) {
                Ray ray = camera.constructRay(nX, nY, i, j);
                List<Point> res = body.findIntsersections(ray);
                if (res != null) {
                    sum += res.size();
                }
            }
        }
        return sum;
    }

    /**
     * Integration tests for sphere
     */
    @Test
    void sphereTest() {
        //TC01: test sphere with 2 intersections
        Camera camera1=cameraBuilder.setLocation( new Point(0, 0, 0)).setVpDistance(1).build();
        Sphere s1 = new Sphere( new Point(0, 0, -3),1);
        int sum1 = sumIntersection(camera1, s1, 3, 3);

        assertEquals(2, sum1,
                "constructRay() and findIntersections() wrong result");


        //TC02: test sphere with 18 intersections
        Camera camera2 =cameraBuilder.setLocation( new Point(0, 0, 0.5)).setVpDistance(1).build();
        Sphere s2 = new Sphere(new Point(0, 0, -2.5),2.5);
        int sum2 = sumIntersection(camera2, s2, 3, 3);

        assertEquals(18, sum2,
                "constructRay() and findIntersections() wrong result");


        //TC03: test sphere with 10 intersections
        Sphere s3 = new Sphere(new Point(0, 0, -2),2);
        int sum3 = sumIntersection(camera2, s3, 3, 3);

        assertEquals(10, sum3,
                "constructRay() and findIntersections() wrong result");


        //TC04: test sphere with 9 intersections
        Sphere s4 = new Sphere(new Point(0, 0, -2),4);
        int sum4 = sumIntersection(camera2, s4, 3, 3);

        assertEquals(9, sum4,
                "constructRay() and findIntersections() wrong result");

        //TC05: test sphere with 0 intersections
        Sphere s5 = new Sphere(new Point(0, 0, 1),0.5);
        int sum5 = sumIntersection(camera1, s5, 3, 3);

        assertEquals(0, sum5,
                "constructRay() and findIntersections() wrong result");
    }

    /**
     * Integration test for plane
     */
    @Test
    void planeTest() {
        Camera camera =cameraBuilder.setLocation( new Point(0, 0, 0)).setVpDistance(1).build();

        //TC01: plane parallel to view plane
        Plane p1 = new Plane(new Point(1, 1, -3), new Point(2,1,-3),  new Point(1,2,-3));
        int sum1 = sumIntersection(camera, p1, 3, 3);

        assertEquals(9, sum1,
                "constructRay() and findIntersections() wrong result");


        //TC02: plane not parallel to view plane
        Plane p2 = new Plane(new Point(0, 0, -3), new Point(1, 0, -3),new Point(0, 1, -2.5));
        int sum2 = sumIntersection(camera, p2, 3, 3);

        assertEquals(9, sum2,
                "constructRay() and findIntersections() wrong result");


        //TC03: plane is not parallel to view plane
        Plane p3 = new Plane(new Point(0, 0, -3), new Point(1, 0, -3), new Point(0, 1, -2));
        int sum3 = sumIntersection(camera, p3, 3, 3);

        assertEquals(6, sum3,
                "constructRay() and findIntersections() wrong result");


    }

    @Test
    void TriangleTest() {
        Camera camera = cameraBuilder.setLocation( new Point(0, 0, 0)).setVpDistance(1).build();

        //TC01: test plane is not parallel to view plane- 1 intersection
        Triangle t1 = new Triangle(new Point(0, 1, -2), new Point(1, -1, -2), new Point(-1, -1, -2));
        int sum1 = sumIntersection(camera, t1, 3, 3);

        assertEquals(1, sum1,
                "constructRay() and findIntersections() wrong result");


        //TC02: test plane is not parallel to view plane- 2 intersection
        Triangle t2 = new Triangle(new Point(0, 20, -2), new Point(1, -1, -2), new Point(-1, -1, -2));
        int sum2 = sumIntersection(camera, t2, 3, 3);

        assertEquals(2, sum2,
                "constructRay() and findIntersections() wrong result");
    }

}