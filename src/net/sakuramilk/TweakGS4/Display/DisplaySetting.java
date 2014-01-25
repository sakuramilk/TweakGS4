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

package net.sakuramilk.TweakGS4.Display;

import android.content.Context;
import net.sakuramilk.util.Convert;
import net.sakuramilk.util.Misc;
import net.sakuramilk.util.RootProcess;
import net.sakuramilk.util.SettingManager;
import net.sakuramilk.util.SysFs;

public class DisplaySetting extends SettingManager {

    public static final String KEY_MDNIE_FORCE_DISABLE = "disp_mdnie_force_disable";
    public static final String KEY_MDNIE_CTRL_ENABLE = "disp_mdnie_ctrl_enable";
    public static final String KEY_MDNIE_CTRL_RED = "disp_mdnie_ctrl_red";
    public static final String KEY_MDNIE_CTRL_GREEN = "disp_mdnie_ctrl_green";
    public static final String KEY_MDNIE_CTRL_BLUE = "disp_mdnie_ctrl_blue";
    public static final String KEY_MDNIE_CTRL_SHARPNESS = "disp_mdnie_ctrl_sharpness";

    private final SysFs mSysFsMdnieForceDisable = new SysFs("/sys/devices/virtual/mdnie/mdnie/mdnie_force_disable");
    private final SysFs mSysFsMdnieCtrlEnable = new SysFs("/sys/devices/virtual/mdnie/mdnie/mdnie_ctrl_enable");
    private final SysFs mSysFsMdnieCtrlRed = new SysFs("/sys/devices/virtual/mdnie/mdnie/mdnie_ctrl_red");
    private final SysFs mSysFsMdnieCtrlGreen = new SysFs("/sys/devices/virtual/mdnie/mdnie/mdnie_ctrl_green");
    private final SysFs mSysFsMdnieCtrlBlue = new SysFs("/sys/devices/virtual/mdnie/mdnie/mdnie_ctrl_blue");
    private final SysFs mSysFsMdnieCtrlSharpness = new SysFs("/sys/devices/virtual/mdnie/mdnie/mdnie_ctrl_sharpness");

    public DisplaySetting(Context context, RootProcess rootProcess) {
        super(context, rootProcess);
    }

    public DisplaySetting(Context context) {
        this(context, null);
    }

    public boolean isEnableMdnieForceDisable() {
        return mSysFsMdnieForceDisable.exists();
    }

    public boolean getMdnieForceDisable() {
        return Convert.toBoolean(mSysFsMdnieForceDisable.read(mRootProcess));
    }

    public void setMdnieForceDisable(boolean value) {
        mSysFsMdnieForceDisable.write(Convert.toString(value), mRootProcess);
    }

    public boolean loadMdnieForceDisable() {
        return getBooleanValue(KEY_MDNIE_FORCE_DISABLE, false);
    }

    public void saveMdnieForceDisable(boolean value) {
        setValue(KEY_MDNIE_FORCE_DISABLE, value);
    }

    public boolean isEnableMdnieCtrlEnable() {
        return mSysFsMdnieCtrlEnable.exists();
    }

    public boolean getMdnieCtrlEnable() {
        return Convert.toBoolean(mSysFsMdnieCtrlEnable.read(mRootProcess));
    }

    public void setMdnieCtrlEnable(boolean value) {
        mSysFsMdnieCtrlEnable.write(Convert.toString(value), mRootProcess);
    }

    public boolean loadMdnieCtrlEnable() {
        return getBooleanValue(KEY_MDNIE_CTRL_ENABLE);
    }

    public void saveMdnieCtrlEnable(boolean value) {
        setValue(KEY_MDNIE_CTRL_ENABLE, value);
    }

    public String getMdnieCtrlRed() {
        String value = mSysFsMdnieCtrlRed.read(mRootProcess);
        if (Misc.isNullOfEmpty(value)) {
            return "0";
        }
        return value;
    }

    public void setMdnieCtrlRed(String value) {
        mSysFsMdnieCtrlRed.write(value, mRootProcess);
    }

    public String loadMdnieCtrlRed() {
        return getStringValue(KEY_MDNIE_CTRL_RED);
    }

    public void saveMdnieCtrlRed(String value) {
        setValue(KEY_MDNIE_CTRL_RED, value);
    }

    public String getMdnieCtrlGreen() {
        String value = mSysFsMdnieCtrlGreen.read(mRootProcess);
        if (Misc.isNullOfEmpty(value)) {
            return "0";
        }
        return value;
    }

    public void setMdnieCtrlGreen(String value) {
        mSysFsMdnieCtrlGreen.write(value, mRootProcess);
    }

    public String loadMdnieCtrlGreen() {
        return getStringValue(KEY_MDNIE_CTRL_GREEN);
    }

    public void saveMdnieCtrlGreen(String value) {
        setValue(KEY_MDNIE_CTRL_GREEN, value);
    }

    public String getMdnieCtrlBlue() {
        String value = mSysFsMdnieCtrlBlue.read(mRootProcess);
        if (Misc.isNullOfEmpty(value)) {
            return "0";
        }
        return value;
    }

    public void setMdnieCtrlBlue(String value) {
        mSysFsMdnieCtrlBlue.write(value, mRootProcess);
    }

    public String loadMdnieCtrlBlue() {
        return getStringValue(KEY_MDNIE_CTRL_BLUE);
    }

    public void saveMdnieCtrlBlue(String value) {
        setValue(KEY_MDNIE_CTRL_BLUE, value);
    }

    public String getMdnieCtrlSharpness() {
        String value = mSysFsMdnieCtrlSharpness.read(mRootProcess);
        if (Misc.isNullOfEmpty(value)) {
            return "0";
        }
        return value;
    }

    public void setMdnieCtrlSharpness(String value) {
        mSysFsMdnieCtrlSharpness.write(value, mRootProcess);
    }

    public String loadMdnieCtrlSharpness() {
        return getStringValue(KEY_MDNIE_CTRL_SHARPNESS);
    }

    public void saveMdnieCtrlSharpness(String value) {
        setValue(KEY_MDNIE_CTRL_SHARPNESS, value);
    }

    @Override
    public void setOnBoot() {
        if (isEnableMdnieForceDisable()) {
            setMdnieForceDisable(!loadMdnieForceDisable());
        }
        if (isEnableMdnieCtrlEnable()) {
            setMdnieCtrlEnable(loadMdnieCtrlEnable());
            String value = loadMdnieCtrlRed();
            if (!Misc.isNullOfEmpty(value)) {
                setMdnieCtrlRed(value);
            }
            value = loadMdnieCtrlGreen();
            if (!Misc.isNullOfEmpty(value)) {
                setMdnieCtrlGreen(value);
            }
            value = loadMdnieCtrlBlue();
            if (!Misc.isNullOfEmpty(value)) {
                setMdnieCtrlBlue(value);
            }
            value = loadMdnieCtrlSharpness();
            if (!Misc.isNullOfEmpty(value)) {
                setMdnieCtrlSharpness(value);
            }
        }
    }

    @Override
    public void setRecommend() {
        // noop
    }

    @Override
    public void reset() {
    	clearValue(KEY_MDNIE_FORCE_DISABLE);
        clearValue(KEY_MDNIE_CTRL_ENABLE);
        clearValue(KEY_MDNIE_CTRL_RED);
        clearValue(KEY_MDNIE_CTRL_GREEN);
        clearValue(KEY_MDNIE_CTRL_BLUE);
        clearValue(KEY_MDNIE_CTRL_SHARPNESS);
    }
}
