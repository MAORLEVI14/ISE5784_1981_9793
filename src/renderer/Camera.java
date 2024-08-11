package renderer;

import primitives.*;
import primitives.Vector;

import java.util.*;

import static java.lang.Math.sqrt;
import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * Class Camera represents a camera in a 3D space.
 * The camera has a location, a direction, a view plane size and a view plane distance.
 */
public class Camera implements Cloneable {

    /**
     * Pixel manager for supporting:
     * <ul>
     * <li>multi-threading</li>
     * <li>debug print of progress percentage in Console window/tab</li>
     * <ul>
     */
    private PixelManager pixelManager;
    private Point location;
    private Vector vright;
    private Vector vup;
    private Vector vto;
    private double height = 0.0;
    private double width = 0.0;
    private double distance = 0.0;
    private ImageWriter imageWriter;
    private RayTracerBase rayTracer;
    private int printInterval = 0;
    private int threadsCount = 0;
    private static final double MAXDIF = 3d;
    private boolean adaptive;
    private int numberOfRays=1;



    /**
     * private constructor to prevent instantiation
     */
    private Camera() {
    }

    // Static method to get a new Builder instance

    /**
     * get the height of the view plane
     */
    public double getHeight() {
        return height;
    }

    /**
     * get the width of the view plane
     *
     * @return the width of the view plane
     */
    public double getWidth() {
        return width;
    }

    /**
     * get the distance of the view plane
     *
     * @return the distance of the view plane
     */
    public double getDistance() {
        return distance;
    }



    private Point findPIJ(int nX, int nY, double j, double i) {
        Point pIJ = location.add(vto.scale(distance));

        double rY = height / nY;
        double rX = width / nX;

        double yI = alignZero(-(i - (nY - 1) / 2d) * (rY));
        double xJ = alignZero((j - (nX - 1) / 2d) * (rX));

        if (!isZero(xJ)) pIJ = pIJ.add(vright.scale(xJ));
        if (!isZero(yI)) pIJ = pIJ.add(vup.scale(yI));
        return pIJ;

    }

    private Ray constractJitteredRay(int nX, int nY, double j, double i, double pw, double ph, int numOfRays) {
        Point pIJ = findPIJ(nX, nY, j, i); // Find the center of the sub-pixel

        Random r = new Random(); // We want a random point for each pixel for more precision

        double subPixelHeight = height / (nY * numOfRays);
        double subPixelWidth = width / (nX * numOfRays);

        // Add a random point to the pixel
        double jitterX = (r.nextDouble() - 0.5) * subPixelWidth;
        double jitterY = (r.nextDouble() - 0.5) * subPixelHeight;

        double xJ = ((pw + jitterX) - ((width / nX - 1) / 2.0)) * subPixelWidth;
        double yI = (-((ph + jitterY) - ((height / nY - 1) / 2.0)) * subPixelHeight);

        // Check if xJ or yI are not zero, so we will not add zero vector
        if (!Util.isZero(xJ)) pIJ = pIJ.add(vright.scale(xJ));
        if (!Util.isZero(yI)) pIJ = pIJ.add(vup.scale(yI));
        Vector vIJ = pIJ.subtract(location);

        return new Ray(location, vIJ); // Return a new ray from a pixel
    }

    /**
     * Construct a ray through a pixel in the view plane
     *
     * @param nX the number of pixels in the x direction
     * @param nY the number of pixels in the y direction
     * @param j  the x index of the pixel
     * @param i  the y index of the pixel
     * @return the ray through the pixel
     */
    public Ray constructRay(int nX, int nY, double j, double i) {
        Point imgCenter = findPIJ(nX, nY, j, i);
        Vector vIJ = imgCenter.subtract(location);
        return new Ray(location, vIJ);

    }
    public static Builder getBuilder() {
        return new Builder();
    }






    /**
     * Method to render an image
     * @return the Camera object
     */
    public Camera renderImage() {
        // בדיקת תנאים מקדימים
        if (this.rayTracer == null || this.imageWriter == null || this.width == 0 || this.height == 0 || this.distance == 0)
            throw new UnsupportedOperationException("MissingResourcesException");
        if (numberOfRays == 0) {
            throw new IllegalArgumentException("Number of rays cannot be 0");
        }

        int nX = imageWriter.getNx();
        int nY = imageWriter.getNy();
        PixelManager pixelManager = new PixelManager(nY, nX, 100L);

        if (threadsCount == 0) { // רינדור חד-חוטי
            for (int i = 0; i < nY; i++) {
                for (int j = 0; j < nX; j++) {
                    Color color = castRay(nX, nY, j, i);
                    imageWriter.writePixel(j, i, color);
                }
            }
        } else { // רינדור מרובה חוטים
            var threads = new LinkedList<Thread>(); // רשימת חוטים
            while (threadsCount-- > 0) { // הוסף מספר חוטים מתאים
                threads.add(new Thread(() -> { // הוסף חוט עם הקוד שלו
                    PixelManager.Pixel pixel; // פיקסל נוכחי(row, col)
                    // הקצה פיקסל(row, col) בלולאה עד שאין עוד פיקסלים
                    while ((pixel = pixelManager.nextPixel()) != null) {
                        // חשב את צבע הפיקסל
                        Color color = castRay(nX, nY, pixel.col(), pixel.row());
                        imageWriter.writePixel(pixel.col(), pixel.row(), color);
                        pixelManager.pixelDone();
                    }
                }));
            }
            // התחל את כל החוטים
            for (var thread : threads) thread.start();
            // חכה עד שכל החוטים יסיימו
            try { for (var thread : threads) thread.join(); } catch (InterruptedException ignore) {}
        }
        return this;
    }

    private List<Ray> constructRayBeam(int i, int j, int nX, int nY, int gridWidth, int gridHighet, double pixelHighet, double pixelWidth)
    {
        List<Ray> beam = new ArrayList<>();
        Point center = getCenterOfPixel(i,j,nX,nY, pixelHighet, pixelWidth);
        for (int i1=0;i1<gridHighet;i1++)
        {
            for (int j1=0;j1<gridWidth;j1++)
            {
                beam.add(constructRayInPixel(nX, nY,j1,i1,center,gridWidth,gridHighet));
            }
        }
        return beam;
    }

    private Ray constructRayInPixel(int nX, int nY, int j, int i, Point center, int gridWidth, int gridHeight) {
        Point pij = center;
        double yi = -(i - ((double) gridHeight - 1) / 2) * (height / nY) / gridHeight;
        if (yi != 0) pij = pij.add(vup.scale(yi));
        double xj = (j - ((double) gridWidth - 1) / 2) * (width / nX) / gridWidth;
        if (xj != 0) pij = pij.add(vright.scale(xj));
        return new Ray(location, pij.subtract(location ));
    }

    /**
     *
     * Method to print a grid on the view plane
     *
     * @param interval the interval between the lines of the grid
     * @param color    the color of the grid
     * @return the Camera object
     */
    public Camera printGrid(int interval, Color color) {
        if (imageWriter == null)
            throw new MissingResourceException("Image writer was null", getClass().getName(), "");
        int nY = imageWriter.getNy();
        int nX = imageWriter.getNx();
        for (int i = 0; i < nY; i += interval)
            for (int j = 0; j < nX; j += 1)
                imageWriter.writePixel(i, j, color);
        for (int i = 0; i < nY; i += 1)
            for (int j = 0; j < nX; j += interval)
                imageWriter.writePixel(i, j, color);
        imageWriter.writeToImage();
        return this;
    }

    /**
     * Method to write the image to a file
     */
    public void writeToImage() {
        imageWriter.writeToImage();
    }

//    private void castRay(int nX, int nY, int j, int i) {
//        castRay(nX, nY, j, i, 1);
//    }

    /**
     * Method to cast a ray through a pixel
     * @param nX the number of pixels in the x direction
     * @param nY the number of pixels in the y direction
     * @param i the y index of the pixel
     * @param j the x index of the pixel
     */
    private Color castRay(int nX, int nY, int i, int j) {
        return rayTracer.traceRay(constructRay(nX, nY, i, j));
    }


    private Color AdaptiveSuperSampling(int nX, int nY, int j, int i,  int numOfRays)  {
        Vector Vright = vright;
        Vector Vup = vup;
        Point cameraLoc = this.location;
        int numOfRaysInRowCol = (int)Math.floor(Math.sqrt(numOfRays));
        if(numOfRaysInRowCol == 1)  return castRay(nX,nY,j,i);
        double rY = alignZero(height / nY);
        // the ratio Rx = w/Nx, the width of the pixel
        double rX = alignZero(width / nX);
        Point pIJ = getCenterOfPixel(i,j,nX,nY,rY,rX);

        double PRy = rY/numOfRaysInRowCol;
        double PRx = rX/numOfRaysInRowCol;
        return AdaptiveSuperSamplingRec(pIJ, rX, rY, PRx, PRy,null);
    }

    /**
     * Recursive function to calculate the color of a pixel using adaptive super sampling
     * @param centerP the center of the pixel
     * @param Width the width of the pixel
     * @param Height the height of the pixel
     * @param minWidth the minimum width of the pixel
     * @param minHeight the minimum height of the pixel
     * @param prePoints the list of points that were already calculated
     * @return the color of the pixel
     */
    // AdaptiveSuperSamplingRec is a recursive method for adaptive super sampling in ray tracing.
// It computes the color of a pixel by subdividing the area around the pixel and tracing rays to determine color variations.
    private Color AdaptiveSuperSamplingRec(Point centerP, double Width, double Height, double minWidth, double minHeight, List<Point> prePoints) {

        // If the current pixel area is smaller than the minimum width or height, return the color of the center point.
        if (Width < minWidth || Height < minHeight) {
            return rayTracer.traceRay(new Ray(location, centerP.subtract(location)));
        }

        // Lists to store next center points, corner points of the current area, and the corresponding colors.
        List<Point> nextCenterPList = new LinkedList<>();
        List<Point> cornersList = new LinkedList<>();
        List<primitives.Color> colorList = new LinkedList<>();

        Point tempCorner;
        Ray tempRay;

        // Iterate over the four corners of the current area.
        for (int i = -1; i <= 1; i += 2) {
            for (int j = -1; j <= 1; j += 2) {
                // Calculate the corner point.
                tempCorner = centerP.add(vright.scale(i * Width / 2)).add(vup.scale(j * Height / 2));
                cornersList.add(tempCorner);

                // If the current corner point is not in the list of previously processed points, trace a ray through it.
                if (prePoints == null || !isInList(prePoints, tempCorner)) {
                    tempRay = new Ray(location, tempCorner.subtract(location));

                    // Calculate the next center point for the recursive call.
                    nextCenterPList.add(centerP.add(vright.scale(i * Width / 4)).add(vup.scale(j * Height / 4)));

                    // Trace the ray and store the color.
                    colorList.add(rayTracer.traceRay(tempRay));
                }
            }
        }

        // If no new center points were added, return black color.
        if (nextCenterPList == null || nextCenterPList.size() == 0) {
            return primitives.Color.BLACK;
        }

        // Check if all the colors in the color list are almost equal.
        boolean isAllEquals = true;
        primitives.Color tempColor = colorList.get(0);
        for (primitives.Color color : colorList) {
            if (!tempColor.isAlmostEquals(color)) {
                isAllEquals = false;
            }
        }

        // If all colors are equal and there is more than one color, return the first color.
        if (isAllEquals && colorList.size() > 1) {
            return tempColor;
        }

        // Initialize the temporary color to black.
        tempColor = primitives.Color.BLACK;

        // Recursively call AdaptiveSuperSamplingRec for each next center point and add the resulting color.
        for (Point center : nextCenterPList) {
            tempColor = tempColor.add(AdaptiveSuperSamplingRec(center, Width / 2, Height / 2, minWidth, minHeight, cornersList));
        }

        // Reduce the accumulated color by the number of next center points and return the result.
        return tempColor.reduce(nextCenterPList.size());
    }



    private boolean isInList(List<Point> pointsList, Point point) {
        for (Point tempPoint : pointsList) {
            if(point.equals(tempPoint))
                return true;
        }
        return false;
    }
    public Point getCenterOfPixel(int i, int j, int nX,int nY,double pixelHeight,double pixelWidth)
    {
        Point center = this.location.add(this.vto.scale(distance));
        double yi = -(i - ((double)nY - 1) / 2) * pixelHeight;
        if (yi !=0 ) center = center.add(this.vup.scale(yi));
        double xj = (j - ((double)nX - 1) / 2) * pixelWidth;
        if (xj !=0 ) center = center.add(this.vright.scale(xj));
        return center;
    }

    //    private Color adaptiveSupersampling(int nX, int nY, int j, int i, int maxDepth) {
//        double pixelWidth = width / nX;
//        double pixelHeight = height / nY;
//        double halfWidth = pixelWidth / 2;
//        double halfHeight = pixelHeight / 2;
//        Point pIJ = findPIJ(nX, nY, j, i);
//        Point topLeft = pIJ.add(new Vector(-halfWidth, halfHeight, 0));
//        Point topRight = pIJ.add(new Vector(halfWidth, halfHeight, 0));
//        Point bottomLeft = pIJ.add(new Vector(-halfWidth, -halfHeight, 0));
//        Point bottomRight = pIJ.add(new Vector(halfWidth, -halfHeight, 0));
//        Color c1 = rayTracer.traceRay(constructRay(nX, nY, j, i- pixelHeight));
//        Color c2 = rayTracer.traceRay(constructRay(nX, nY, j + pixelWidth, i));
//        Color c3 = rayTracer.traceRay(constructRay(nX, nY, j, i + pixelHeight));
//        Color c4 = rayTracer.traceRay(constructRay(nX, nY, j - pixelWidth, i ));
//
//        return adaptiveSupersamplingRecursive(topLeft,topRight,bottomLeft,bottomRight,c1,c2,c3,c4  ,1, maxDepth);
//    }
//
//    private Color adaptiveSupersamplingRecursive(Point topLeft, Point topRight, Point bottomLeft, Point bottomRight, Color c1, Color c2, Color c3, Color c4  , int depth, int maxDepth) {
//        if (!needSubdivision(c1, c2, c3, c4)|| depth >= maxDepth)// if the difference between the colors is not crucial or we reached the max depth return the average color
//            return c1.add(c2, c3, c4).reduce(4);
//
//        Point center = findCenter(topLeft, topRight, bottomLeft, bottomRight);
//         List<Color> colors = new ArrayList<>();
//         //calc all 4 mid points
//         Point topMid = topLeft.add(topLeft.subtract(topRight).scale(0.5));
//         Point rightMid = topRight.add(topRight.subtract(bottomRight).scale(0.5));
//         Point bottomMid = bottomLeft.add(bottomLeft.subtract(bottomRight).scale(0.5));
//         Point leftMid = bottomLeft.add(bottomLeft.subtract(topLeft).scale(0.5));
//
//         //calc all 5 mid points
//         Color nc1 = rayTracer.traceRay(new Ray(location, topMid.subtract(location)));
//         Color nc2 = rayTracer.traceRay(new Ray(location, rightMid.subtract(location)));
//         Color nc3 = rayTracer.traceRay(new Ray(location, bottomMid.subtract(location)));
//         Color nc4 = rayTracer.traceRay(new Ray(location, leftMid.subtract(location)));
//         Color centerColor = rayTracer.traceRay(new Ray(location, center.subtract(location)));
//
//         //recursive call for each sub square
//         colors.add(adaptiveSupersamplingRecursive( topLeft,topMid , leftMid , center , c1, nc1, nc4,centerColor   , depth + 1, maxDepth));
//         colors.add(adaptiveSupersamplingRecursive( topMid,topRight , center , rightMid , nc1, c2, nc2,  centerColor , depth + 1, maxDepth));
//         colors.add(adaptiveSupersamplingRecursive( leftMid,center ,  bottomLeft, bottomMid , nc4,centerColor , c3, nc3  , depth + 1, maxDepth));
//         colors.add(adaptiveSupersamplingRecursive( center,rightMid ,bottomMid, bottomRight  ,centerColor , nc2, nc3, c4 , depth + 1, maxDepth));
//
//        return colors.stream().reduce(Color::add).get().reduce(4);
//
//    }
//
//    private Point findCenter(Point p1, Point p2, Point p3, Point p4) {
//        Point a = p2.add(p1.subtract(p2).scale(0.5));
//        Point b = p3.add(p3.subtract(p4).scale(0.5));
//        return a.add(b.subtract(a).scale(0.5));
//
//    }
//    private Color adaptiveSupersamplingRecursive(int nX, int nY, double j, double i, int depth, int maxDepth, Color c1, Color c2, Color c3, Color c4) {
//        double pixelWidth = width / nX;
//        double pixelHeight = height / nY;
//
//        if (depth >= maxDepth || !needSubdivision(c1, c2, c3, c4)) {
//            return c1.add(c2, c3, c4).reduce(4);
//        }
//
//        double centerJ = j + (pixelWidth / 4);
//        double centerI = i + (pixelHeight / 4);
//
//        Color centerColor = rayTracer.traceRay(constructRay(nX, nY, centerJ, centerI));
//
//        return adaptiveSupersamplingRecursive(nX/2, nY/2, j, i, depth + 1, maxDepth, c1, c2, c3, centerColor)
//                .add(adaptiveSupersamplingRecursive(nX/2, nY/2, centerJ, i, depth + 1, maxDepth, c2, c2, centerColor, c4))
//                .add(adaptiveSupersamplingRecursive(nX/2, nY/2, j, centerI, depth + 1, maxDepth, c1, centerColor, c3, c3))
//                .add(adaptiveSupersamplingRecursive(nX/2, nY/2, centerJ, centerI, depth + 1, maxDepth, centerColor, c4, c4, c4))
//                .reduce(4);
//    }
//
    private boolean needSubdivision(Color c1, Color c2, Color c3, Color c4) {
        return c1.isDifferenceCrucial(c2) ||
                c1.isDifferenceCrucial(c3) ||
                c1.isDifferenceCrucial(c4) ||
                c2.isDifferenceCrucial(c3) ||
                c2.isDifferenceCrucial(c4) ||
                c3.isDifferenceCrucial(c4);
    }


    /**
     * Class Builder is a static inner class that is used to build a Camera object.
     * The builder has a fluent interface, which means that it returns the object itself after each method call.
     * This allows the user to chain method calls together.
     */
    public static class Builder {
        private final Camera camera = new Camera();


        /**
         * Default constructor
         */
        public Builder() {
        }

        /**
         * Constructor that receives a Camera object
         *
         * @param cam the Camera object
         */
        public Builder(Camera cam) {
            this.camera.location = cam.location;
            this.camera.vright = cam.vright;
            this.camera.vup = cam.vup;
            this.camera.vto = cam.vto;
            this.camera.height = cam.height;
            this.camera.width = cam.width;
            this.camera.distance = cam.distance;
            this.camera.imageWriter = cam.imageWriter;
            this.camera.rayTracer = cam.rayTracer;
            this.camera.printInterval = cam.printInterval;
            this.camera.threadsCount = cam.threadsCount;

        }

        public Builder setMultithreading(int threadsCount) {
            this.camera.threadsCount = threadsCount;
            return this;
        }
        public Builder setAdaptive(boolean adaptive) {
            this.camera.adaptive = adaptive;
            return this;
        }

        public Builder setNumberOfRays(int numberOfRays) {
            this.camera.numberOfRays = numberOfRays;
            return this;
        }

        /**
         * Method to set the location
         *
         * @param p1 the location
         * @return the Builder object
         */
        public Builder setLocation(Point p1) {
            this.camera.location = p1;
            return this;
        }

        /**
         * Method to set the threads count
         *
         * @param threadsCount the threads count
         * @return the Builder object
         */
        public Builder setThreadsCount(int threadsCount) {
            this.camera.threadsCount = threadsCount;
            return this;
        }


        /**
         * Method to set the threads count
         *
         * @param printInterval the threads count
         * @return the Builder object
         */
        public Builder setPrintInterval(int printInterval) {
            this.camera.printInterval = printInterval;
            return this;
        }

        /**
         * Method to set the direction
         *
         * @param vto the direction
         * @param vup the up vector
         * @return the Builder object
         */
        public Builder setDirection(Vector vto, Vector vup) {
            if (!isZero(vup.dotProduct(vto)))
                throw new IllegalArgumentException("ERROR: given vectors weren't perpendicular");
            this.camera.vto = vto.normalize();
            this.camera.vup = vup.normalize();
            this.camera.vright = vto.crossProduct(vup).normalize();
            return this;
        }

        /**
         * Method to set the view plane size
         *
         * @param height the height of the view plane
         * @param width  the width of the view plane
         * @return the Builder object
         */
        public Builder setVpSize(double height, double width) {
            if (height < 0 || width < 0)
                throw new IllegalArgumentException("ERROR: height and width are negative");
            this.camera.height = height;
            this.camera.width = width;
            return this;
        }

        /**
         * Method to set the view plane distance
         *
         * @param distance the distance of the view plane
         * @return the Builder object
         */
        public Builder setVpDistance(double distance) {
            if (distance < 0)
                throw new IllegalArgumentException("ERROR: distance is negative");
            this.camera.distance = distance;
            return this;
        }

        /**
         * Method to set the ImageWriter
         *
         * @param imageWriter the ImageWriter object
         * @return the Builder object
         */
        public Builder setImageWriter(ImageWriter imageWriter) {
            this.camera.imageWriter = imageWriter;
            return this;
        }

        public Builder setRayTracer(RayTracerBase rayTracer) {
            this.camera.rayTracer = rayTracer;
            return this;
        }

        /**
         * Method to build the Camera object
         *
         * @return the Camera object
         */
        public Camera build() {
            if (this.camera.vup == null)
                throw new MissingResourceException("ERROR: missing renderer argument",
                        "Camera", "vup");
            if (this.camera.vto == null)
                throw new MissingResourceException("ERROR: missing renderer argument",
                        "Camera", "vto");
            if (this.camera.height == 0)
                throw new MissingResourceException("ERROR: missing renderer argument",
                        "Camera", "height");
            if (this.camera.width == 0)
                throw new MissingResourceException("ERROR: missing renderer argument",
                        "Camera", "width");
            if (this.camera.distance == 0)
                throw new MissingResourceException("ERROR: missing renderer argument",
                        "Camera", "distance");
            if (this.camera.imageWriter == null) {
                this.camera.vright = this.camera.vto.crossProduct(this.camera.vup).normalize();
            }
            if (this.camera.rayTracer == null)
                throw new MissingResourceException("ERROR: missing renderer argument",
                        "Camera", "rayTracer");
            try {
                return (Camera) this.camera.clone();

            } catch (CloneNotSupportedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}


