package geometries;

import primitives.Color;
import primitives.Material;
import primitives.Point;
import primitives.Vector;

public  abstract class   Geometry extends Intersectable {
    private Color emission = Color.BLACK; // the geometry's default color
    private Material material = new Material(); // the material the geometry has made of
    public abstract Vector getNormal(Point p);
    /**
     * getEmission function
     *
     * @return the geometry's color
     */
    public  Color getEmission() {
        return this.emission;
    }

    /**
     * setEmission function
     *
     * @param emission
     * @return
     */
    public Geometry setEmission(Color emission) {
        this.emission = emission;
        return this;
    }
    /**
     * Get material of the geometry
     *
     * @return Material of the geometry
     */
    public Material getMaterial() {
        return this.material;
    }

    /**
     * Set material of the geometry
     *
     * @param material the Material of the geometry
     * @return the geometry itself
     */
    public Geometry setMaterial(Material material) {
        this.material = material;
        return this;
    }
}
