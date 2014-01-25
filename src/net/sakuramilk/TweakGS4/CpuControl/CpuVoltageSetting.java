/*
 * Copyright (C) 2011-2012 sakuramilk <c.sakuramilk@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.sakuramilk.TweakGS4.CpuControl;

import java.util.ArrayList;

import net.sakuramilk.util.Misc;
import net.sakuramilk.util.SettingManager;
import net.sakuramilk.util.SysFs;
import android.content.Context;

public class CpuVoltageSetting extends SettingManager {

    public static final String KEY_CPU_VOLT_ROOT_PREF = "root_pref";
    public static final String KEY_CPU_VOLT_CTRL_BASE = "cpu_vc_";

    private static final String CRTL_PATH = "/sys/devices/system/cpu/cpu0/cpufreq";
    private final SysFs mSysFsUV_mV_table = new SysFs(CRTL_PATH + "/UV_mV_table");
    private final SysFs mSysFsDefaultUV_mV_table = new SysFs(CRTL_PATH + "/UV_mV_default_table");

    public CpuVoltageSetting(Context context) {
        super(context);
    }

    public boolean isEnableVoltageControl() {
        return mSysFsUV_mV_table.exists();
    }

    private String[] getVoltageTable(SysFs sysFsVoltageTable) {
        ArrayList<String> ret = new ArrayList<String>();
        String[] values = sysFsVoltageTable.readMuitiLine(mRootProcess);
        if (values == null) {
        	return null;
        }
        for (String value : values) {
            String voltage = value.substring(value.indexOf(" ") + 1).split(" ")[0];
            ret.add(voltage);
        }
        return ret.toArray(new String[0]);
    }
    
    public String[] getCurrentVoltageTable() {
    	return getVoltageTable(mSysFsUV_mV_table);
    }
    
    public String[] getDefaulVoltageTable() {
    	return getVoltageTable(mSysFsDefaultUV_mV_table);
    }

    public void setVoltageTable(String[] voltageTable) {
        String value = "";
        for (String volt : voltageTable) {
            value += volt + " ";
        }
        mSysFsUV_mV_table.write(value, mRootProcess);
    }

    public String loadVoltage(String key) {
        return getStringValue(key);
    }
    
    public void saveVoltage(String key, String value) {
        setValue(key, value);
    }

    @Override
    public void setOnBoot() {
        String[] voltTable = getCurrentVoltageTable();
        if (voltTable != null) {
        	CpuControlSetting cpuSetting = new CpuControlSetting(mContext);
        	String[] availableFrequencies = cpuSetting.getAvailableFrequencies();
        	for (int i = 0; i < voltTable.length; i++) {
        		String freq = String.valueOf(Integer.parseInt(availableFrequencies[i]) / 1000);
        		String volt = loadVoltage(KEY_CPU_VOLT_CTRL_BASE + freq);
        		if (!Misc.isNullOfEmpty(volt)) {
        			voltTable[i] = volt;
        		}
        	}
        	setVoltageTable(voltTable);
        }
    }

    @Override
    public void setRecommend() {
        // noop
    }

    @Override
    public void reset() {
        String[] voltTable = getCurrentVoltageTable();
        if (voltTable != null) {
        	for (int i = 0; i < voltTable.length; i++) {
        		clearValue(KEY_CPU_VOLT_CTRL_BASE + i);
        	}
        }
    }
    
    public void preset() {
    	// low
    	
    }
}
