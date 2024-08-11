package primitives;

public class Point {

    protected final Double3 xyz;
    public static Point ZERO=new Point(0,0,0);
    /**
     * parameters constructor
     * @param MyXyz=xyz
     */
    public Point(Double3 MyXyz) {
        this.xyz = MyXyz;
    }

    /**
     * A parameter constructor that accepts three numbers and creates a dot
     * @param i=xyx.d1
     * @param i1=xyz.d2
     * @param i2=xyz.d3
     */
    public Point(double i, double i1, double i2) {
        this.xyz = new Double3(i,i1,i2);
    }

    /**
     * @param obj=point
     * @return If two points are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Point other) && this.xyz.equals(other.xyz);
    }

    /**
     * @return A string representing the point class
     */
    @Override
    public String toString() {
        return "Point"+"xyz="+xyz;
    }

    /**
     *Vector subtraction
     * @param point1=Starting point of the vector
     * @return vector between the two points
     */
    public Vector subtract(Point point1) {
        double x=this.xyz.d1-point1.xyz.d1;
        double y=this.xyz.d2-point1.xyz.d2;
        double z=this.xyz.d3-point1.xyz.d3;
        if(x==0 && y==0 && z==0) {
            throw new IllegalArgumentException("vector is zero");
        }
        return new Vector(x,y,z);
    }

    /**
     * @param point1=the point that we want to add
     * @return new point after adding
     */
    public Point add(Point point1) {
        return new Point(xyz.add(point1.xyz));
    }

    /**
     * @param point1==the point that we want
     * @return distance Squared between two points
     */
    public double distanceSquared(Point point1) {
        double x=(this.xyz.d1-point1.xyz.d1);
        double y=(this.xyz.d2-point1.xyz.d2);
        double z=(this.xyz.d3-point1.xyz.d3);
        return x*x+y*y+z*z;
    }

    /**
     * @param point1==the point that we want
     * @return distance between two points
     */
    public double distance(Point point1) {
        return Math.sqrt(this.distanceSquared(point1));
    }
    public double getX(){
        return this.xyz.d1;
    }
    public double getY(){
        return this.xyz.d2;
    }
    public double getZ(){
        return this.xyz.d3;
    }
}
