package geometries;

public abstract class RadialGeometry implements Geometry{
    protected double radius;

    /**
     * parameters construcrtor
     * @param myRadius=radius
     */
    public RadialGeometry(double myRadius) {
        this.radius = myRadius;
    }
}
