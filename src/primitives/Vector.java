package primitives;
import static primitives.Util.alignZero;
public class Vector extends Point {

    public Vector(double x, double y, double z) {
        super(x, y, z);
    }

    public Vector(Double3 xyz) {
        super(xyz);
        if (xyz.equals(Double3.ZERO)) {
            throw new IllegalArgumentException("Vector cannot be zero");
        }
    }

    @Override
    public String toString() {
        return "Vector{"  + xyz + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if(!(obj instanceof Vector vector)) return false;
        return xyz.equals(vector.xyz);
    }
    public Vector add(Vector vector){
        return new Vector(xyz.add(vector.xyz));
    }
    public Vector scale(double scale){
        return new Vector(xyz.scale(scale));
    }
    public double dotProduct(Vector vector){
        return xyz.d1*vector.xyz.d1+xyz.d2*vector.xyz.d2+xyz.d3*vector.xyz.d3;
    }
    public Vector crossProduct(Vector vector){
        return new Vector(
                xyz.d2*vector.xyz.d3-xyz.d3*vector.xyz.d2,
                xyz.d3*vector.xyz.d1-xyz.d1*vector.xyz.d3,
                xyz.d1*vector.xyz.d2-xyz.d2*vector.xyz.d1
        );
    }
    public double lengthSquared(){
        return xyz.d1*xyz.d1+xyz.d2*xyz.d2+xyz.d3*xyz.d3;
    }
    //normalization
    public Vector normalize(){
        double length=alignZero(length());
        if(length==0){
            throw new IllegalArgumentException("Cannot normalize the zero vector");
        }
        return new Vector(xyz.scale(1/length));
    }
    public double length(){
        return Math.sqrt(lengthSquared());
    }

    public Vector subtract(Vector vector){
        return new Vector(xyz.subtract(vector.xyz));
    }
}
