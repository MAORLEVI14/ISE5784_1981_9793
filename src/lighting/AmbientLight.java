package lighting;

import primitives.Color;
import primitives.Double3;

public class AmbientLight {
    private final Color intensity; // intensity (עצמה) of ambient Light
    public static final AmbientLight NONE=new AmbientLight(Color.BLACK,new Double3(0,0,0));

    /**
     * constructor for knowing the intensity after the light factor
     *
     * @param Ia - Light illumination (RGB עצמת האור לפי קומפוננטות)
     * @param Ka - Light factor - מקדם הנחתה של האור
     */
    public AmbientLight(Color Ia, Double3 Ka) {
        //calculation of the intensity after the light factor//
        this.intensity = Ia.scale(Ka);
    }

    /**
     * default constructor for initialize the background to black
     */
    public AmbientLight() {
        this.intensity = Color.BLACK;
    }

    /**
     * getter for intensity
     * @return the intensity
     */
    public Color getIntensity() {
        return this.intensity;
    }

}
