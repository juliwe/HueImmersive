package hueimmersive;

import java.awt.Color;
import java.util.Timer;
import java.util.TimerTask;


public class Control
{
	public static boolean immersiveProcessIsActive = false;
	private Timer captureLoop;
	private int transitionTime = 5;
	
	private float lastAutoOffBri = 0f;
	
	public Control() throws Exception
	{
		HBridge.setup();
		
		if (Main.arguments.contains("force-on") && !Main.arguments.contains("force-off"))
		{
			turnAllLightsOn();
		}
		if (Main.arguments.contains("force-off") && !Main.arguments.contains("force-on"))
		{
			turnAllLightsOff();
		}
		if (Main.arguments.contains("force-start"))
		{
			startImmersiveProcess();
		}
	}
	
	public void setLight(HLight light, Color color) throws Exception // calculate color and send it to light
	{		
		float[] colorHSB = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null); // unmodified HSB color
		
		color = Color.getHSBColor(colorHSB[0], Math.max(0f, Math.min(1f, colorHSB[1] * (Main.ui.slider_Saturation.getValue() / 100f))), (float)(colorHSB[2] * (Main.ui.slider_Brightness.getValue() / 100f) * (Settings.Light.getBrightness(light) / 100f))); // modified color
		
		double[] xy = HColor.translate(color, Settings.getBoolean("gammacorrection")); // xy color
		int bri = Math.round(Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null)[2] * 255); // brightness
		
		String APIurl = "http://" + HBridge.internalipaddress + "/api/" + HBridge.username + "/lights/" + light.id + "/state";	
		String data = "{\"xy\":[" + xy[0] + ", " + xy[1] + "], \"bri\":" + bri + ", \"transitiontime\":" + transitionTime + "}";
		
		// turn light off automatically if the brightness is very low
		if (Settings.getBoolean("autoswitch"))
		{
			if (colorHSB[2] > lastAutoOffBri + 0.1f && light.isOn() == false)
			{
				data = "{\"on\":true, \"xy\":[" + xy[0] + ", " + xy[1] + "], \"bri\":" + bri + ", \"transitiontime\":" + transitionTime + "}";
			}
			else if (colorHSB[2] <= 0.0627451f && light.isOn() == true)
			{
				data = "{\"on\":false, \"transitiontime\":3}";
				lastAutoOffBri = colorHSB[2];
			}
		}
		else if (Settings.getBoolean("autoswitch") == false && light.isOn() == false)
		{
			data = "{\"on\":true, \"xy\":[" + xy[0] + ", " + xy[1] + "], \"bri\":" + bri + ", \"transitiontime\":" + transitionTime + "}";
		}
			
		HRequest.PUT(APIurl, data);
	}
	
	public void startImmersiveProcess() throws Exception
	{
		Main.ui.button_Off.setEnabled(false);
		Main.ui.button_On.setEnabled(false);
		
		Main.ui.button_Stop.setEnabled(true);
		Main.ui.button_Start.setEnabled(false);
		Main.ui.button_Once.setEnabled(false);
		
		immersiveProcessIsActive = true;
		
		for(HLight light : HBridge.lights)
		{
			light.storeLightColor();
		}
		
		// create a loop to execute the immersive process
		captureLoop = new Timer();
		TimerTask task = new TimerTask()
		{
			public void run()
			{
				try
				{
					ImmersiveProcess.execute();
				}
				catch (Exception e)
				{
					Debug.exception(e);
				}
			}
		};
		captureLoop.scheduleAtFixedRate(task, 0, Math.round(transitionTime * 100 * 0.68));
	}
	
	public void stopImmersiveProcess() throws Exception
	{
		captureLoop.cancel();
		captureLoop.purge();
		
		immersiveProcessIsActive = false;
		
		Main.ui.setupOnOffButton();
		
		Main.ui.button_Stop.setEnabled(false);
		Main.ui.button_Start.setEnabled(true);
		Main.ui.button_Once.setEnabled(true);
		
		Thread.sleep(250);
		ImmersiveProcess.setStandbyOutput();
		
		if (Settings.getBoolean("restorelight"))
		{
			Thread.sleep(750);
			for(HLight light : HBridge.lights)
			{
				light.restoreLightColor();
			}
		}
	}
	
	public void onceImmersiveProcess() throws Exception
	{
		Main.ui.button_Stop.setEnabled(false);
		Main.ui.button_Start.setEnabled(true);
		Main.ui.button_Once.setEnabled(true);
		
		ImmersiveProcess.execute();
	}
	
	public void turnAllLightsOn() throws Exception
	{
		for(HLight light : HBridge.lights)
		{
			if (Settings.Light.getActive(light))
			{
				light.turnOn();
			}
		}
		Main.ui.setupOnOffButton();
	}

	public void turnAllLightsOff() throws Exception
	{
		for(HLight light : HBridge.lights)
		{
			if (Settings.Light.getActive(light))
			{
				light.turnOff();
			}
		}
		Main.ui.setupOnOffButton();
	}

}
