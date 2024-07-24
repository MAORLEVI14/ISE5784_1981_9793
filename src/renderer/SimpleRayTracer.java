package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;

import java.util.List;

public class SimpleRayTracer extends RayTracerBase {
    /**
     * Constructor that get a scene
     *
     * @param scene
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    /**
     * Get the color of an intersection point
     * @param point point of intersection
     * @return Color of the intersection point
     */
    private Color calcColor(Point point) {
        return this.scene.ambientLight.getIntensity();
    }

    @Override
    Color traceRay(Ray ray) {
        List<Point> intersections  = this.scene.geometries.findIntsersections(ray);

        if (intersections == null)
            return this.scene.background;

        Point closestPoint = ray.findClosestPoint(intersections);

        return calcColor(closestPoint);
    }
}
