package hueimmersive;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.prefs.Preferences;


public class Settings
{
	private static Preferences prefs = Preferences.userRoot().node("/hueimmersive");
	
	public static SettingsLight Light = new SettingsLight();
	public static SettingsBridge Bridge = new SettingsBridge();
	
	public static void check() throws Exception
	{
		if (prefs.node("/hueimmersive").keys().length != 0)
		{
			ArrayList<String> keys = new ArrayList<String>(Arrays.asList(prefs.keys()));
			String[] settingList = {
				"ui_x", 
				"ui_y", 
				"cpi_x", 
				"cpi_y", 
				"oi_x", 
				"oi_y", 
				"chunks", 
				"brightness", 
				"saturation", 
				"format", 
				"colorgrid", 
				"restorelight", 
				"autoswitch", 
				"gammacorrection", 
				"screen"};
			
			ArrayList<String> settings = new ArrayList<String>(Arrays.asList(settingList));
			
			if(keys.containsAll(settings) == false)
			{
				Debug.info(null, "some settings are missing");
				setDefaultSettings();
			}
		}
		else
		{
			setDefaultSettings();
		}
	}
	
	public static void debug() throws Exception
	{
		String[] keys;
		ArrayList<String> settings;
		keys = prefs.keys();
		Arrays.sort(keys);
		settings = new ArrayList<String>();
		for (String s : keys)
		{
			settings.add(s + " = " + prefs.get(s, null));
		}
		Debug.info("settings general", settings);
	}
	
	public static void setDefaultSettings()
	{
		Debug.info(null, "set default settings");
		
		prefs.putInt("ui_x", 250);
		prefs.putInt("ui_y", 200);
		prefs.putInt("cpi_x", 600);
		prefs.putInt("cpi_y", 200);
		prefs.putInt("oi_x", 250);
		prefs.putInt("oi_y", 450);
		prefs.putInt("chunks", 12);
		prefs.putInt("brightness", 100);
		prefs.putInt("saturation", 110);
		prefs.putInt("format", 0);
		prefs.putBoolean("colorgrid", false);
		prefs.putBoolean("restorelight", true);
		prefs.putBoolean("autoswitch", false);
		prefs.putBoolean("gammacorrection", true);
		prefs.putInt("screen", 0);
	}

	public static void reset(boolean exit) throws Exception // delete all settings and exit the program
	{
		Debug.info(null, "reset all settings");
		prefs.node("/hueimmersive").removeNode();
		if(exit == true)
		{
			Debug.closeLog();
			System.exit(0);
		}
	}
	
	public static ArrayList<String> getArguments()
	{
		String args = prefs.get("arguments", null);
		
		ArrayList<String> arrArgs = new ArrayList<String>();
		if (args != null)
		{
			arrArgs.addAll(Arrays.asList(args.split(",")));
		}
		
		return arrArgs;
	}
	
	public static void setArguments(ArrayList<String> args)
	{
		if (args.size() != 0)
		{
			String arguments = "";
			for (String arg : args)
			{
				arguments += "," + arg;
			}
			arguments = arguments.replaceFirst(",", "");
			prefs.put("arguments", arguments);
		}
		else
		{
			prefs.remove("arguments");
		}
	}
	
	public static int getInteger(String key)
	{
		return prefs.getInt(key, 0);
	}
	public static boolean getBoolean(String key)
	{
		return prefs.getBoolean(key, false);
	}
	
	public static void set(String key, int value)
	{
		 prefs.putInt(key, value);
	}
	public static void set(String key, boolean value)
	{
		 prefs.putBoolean(key, value);
	}
}

class SettingsBridge // bridge settings
{
	private Preferences prefs = Preferences.userRoot().node("/hueimmersive/bridge");
	
	public void debug() throws Exception
	{
		String[] keys = prefs.keys();
		Arrays.sort(keys);
		ArrayList<String> settings = new ArrayList<String>();
		for (String k : keys)
		{
			settings.add(k + " = " + prefs.get(k, null));
		}
		Debug.info("settings bridge", settings);
	}
	
	public void setInternalipaddress(String internalipaddress)
	{
		prefs.put("internalipaddress", internalipaddress);
	}
	
	public String getInternalipaddress()
	{
		return prefs.get("internalipaddress", null);
	}

	public void setUsername(String username)
	{
		prefs.put("username", username);
	}

	public String getUsername()
	{
		return prefs.get("username", null);
	}

}

class SettingsLight // light settings
{
	private Preferences prefs = Preferences.userRoot().node("/hueimmersive/lights");
	
	private int nexAlg = 0;
	private int maxAlg = ImmersiveProcess.algorithms;
	
	public void check(HLight light) throws Exception // setup default light settings if it doesn't have
	{
		Preferences lprefs = Preferences.userRoot().node(prefs.absolutePath() + "/" + light.uniqueid);
		if (lprefs.get("active", null) == null)
		{
			lprefs.putBoolean("active", true);
		}
		if (lprefs.get("bri", null) == null)
		{
			lprefs.putInt("bri", 100);
		}
		if (lprefs.get("alg", null) == null)
		{
			lprefs.putInt("alg", nexAlg);
			nexAlg++;
			if (nexAlg > maxAlg)
			{
				nexAlg = 0;
			}
		}
	}
	
	public void debug() throws Exception
	{
		ArrayList<String> settings = new ArrayList<String>();
		for (String node : prefs.childrenNames())
		{	
			settings.add(node + "");
			String[] keys = prefs.node(node).keys();
			Arrays.sort(keys);
			for (String s : keys)
			{
				settings.add("  " + s + " = " + prefs.node(node).get(s, null));
			}
		}
		Debug.info("settings lights", settings);
	}
	
	public void setBrightness(HLight light, int bri)
	{
		Preferences lprefs = Preferences.userRoot().node(prefs.absolutePath() + "/" + light.uniqueid);
		lprefs.putInt("bri", bri);
	}
	public void setActive(HLight light, boolean active)
	{
		Preferences lprefs = Preferences.userRoot().node(prefs.absolutePath() + "/" + light.uniqueid);
		lprefs.putBoolean("active", active);
	}
	public void setAlgorithm(HLight light, int alg)
	{
		Preferences lprefs = Preferences.userRoot().node(prefs.absolutePath() + "/" + light.uniqueid);
		lprefs.putInt("alg", alg);
	}
	
	public boolean getActive(HLight light)
	{
		Preferences lprefs = Preferences.userRoot().node(prefs.absolutePath() + "/" + light.uniqueid);
		return lprefs.getBoolean("active", true);
	}
	public int getAlgorithm(HLight light)
	{
		Preferences lprefs = Preferences.userRoot().node(prefs.absolutePath() + "/" + light.uniqueid);
		return lprefs.getInt("alg", -1);
	}
	public int getBrightness(HLight light)
	{
		Preferences lprefs = Preferences.userRoot().node(prefs.absolutePath() + "/" + light.uniqueid);
		return lprefs.getInt("bri", -1);
	}
}
