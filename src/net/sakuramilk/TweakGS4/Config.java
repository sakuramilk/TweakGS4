package net.sakuramilk.TweakGS4;

import net.sakuramilk.util.SystemCommand;

public class Config {
	public static final String KBC_DATA_DIR = "/kbc";
    public static final String BACKUP_DIR = KBC_DATA_DIR + "/backup";
    
    
    public static boolean checkDevice() {
        String model = SystemCommand.get_prop("ro.product.model");
        String device = SystemCommand.get_prop("ro.product.device");
        if ("SC-04E".equals(model) || "jfltedcm".equals(model)) {
        	return true;
        }
        if ("SC-04E".equals(device) || "jfltedcm".equals(device)) {
        	return true;
        }
        return false;
    }
}
