package hueimmersive;
import java.awt.Color;


public class HColor
{
	// created with help from: https://github.com/PhilipsHue/PhilipsHueSDK-iOS-OSX/commit/f41091cf671e13fe8c32fcced12604cd31cceaf3
	
	public static double[] translate(Color color, Boolean useGammaCorrection) // translate in CIE 1931 colorspace for hue
	{	    
	    double[] xyDefault = {0.31, 0.32}; 
	    
	    float[] colorHSB = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
	    
	    float red = ((float)color.getRed() / 255f);
	    float green = ((float)color.getGreen() / 255f);
	    float blue = ((float)color.getBlue() / 255f);
	    
	    if (useGammaCorrection)
	    {
		    if (red > 0.04045) 
		    {
		        red = (float) Math.pow((red + 0.055) / (1.0 + 0.055), 2.4);
		    } 
		    else 
		    {
		        red = (float) (red / 12.92);
		    }

		    if (green > 0.04045) 
		    {
		        green = (float) Math.pow((green + 0.055) / (1.0 + 0.055), 2.4);
		    } 
		    else 
		    {
		        green = (float) (green / 12.92);
		    }

		    if (blue > 0.04045) 
		    {
		        blue = (float) Math.pow((blue + 0.055) / (1.0 + 0.055), 2.4);
		    }
		    else
		    {
		        blue = (float) (blue / 12.92);
		    }
	    }
	    else
	    {
	    	red = (float) (red / 12.92);
	    	green = (float) (green / 12.92);
	    	blue = (float) (blue / 12.92);
	    }
	    
	    float X = (float) (red * 0.649926 + green * 0.103455 + blue * 0.197109);
	    float Y = (float) (red * 0.234327 + green * 0.743075 + blue * 0.022598);
	    float Z = (float) (red * 0.0000000 + green * 0.053077 + blue * 1.035763);

	    double[] xy = new double[2];
	    xy[0] = X / (X + Y + Z);
	    xy[1] = Y / (X + Y + Z);
	    
	    // set default color if float/color is NaN
	    if (Double.isNaN(xy[0]) || Double.isNaN(xy[1]))
	    {
	    	xy = xyDefault;
	    }
	    // prevent oversaturated purple
	    else if(colorHSB[2] < 1.55f / 255f && (colorHSB[0] > 0.78 && colorHSB[0] < 0.87))
	    {
	    	xy = xyDefault;
	    }
	    
	    return xy;
	}
}
