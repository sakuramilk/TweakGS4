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

import net.sakuramilk.TweakGS4.R;
import net.sakuramilk.util.Misc;
import net.sakuramilk.widget.SeekBarPreference;
import net.sakuramilk.widget.SeekBarPreference.OnSeekBarPreferenceDoneListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.view.Menu;
import android.view.MenuItem;

public class DisplayPreferenceActivity extends PreferenceActivity implements
    Preference.OnPreferenceChangeListener, OnSeekBarPreferenceDoneListener {

    private DisplaySetting mSetting;

    private CheckBoxPreference mMdnieForceDisable;
    private CheckBoxPreference mMdnieCtrlEnable;
    private SeekBarPreference mMdnieCtrlRed;
    private SeekBarPreference mMdnieCtrlGreen;
    private SeekBarPreference mMdnieCtrlBlue;
    private SeekBarPreference mMdnieCtrlSharpness;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.display_pref);
        mSetting = new DisplaySetting(this);

        mMdnieForceDisable = (CheckBoxPreference)findPreference(DisplaySetting.KEY_MDNIE_FORCE_DISABLE);
        mMdnieCtrlEnable = (CheckBoxPreference)findPreference(DisplaySetting.KEY_MDNIE_CTRL_ENABLE);
        mMdnieCtrlRed = (SeekBarPreference)findPreference(DisplaySetting.KEY_MDNIE_CTRL_RED);
        mMdnieCtrlGreen = (SeekBarPreference)findPreference(DisplaySetting.KEY_MDNIE_CTRL_GREEN);
        mMdnieCtrlBlue = (SeekBarPreference)findPreference(DisplaySetting.KEY_MDNIE_CTRL_BLUE);
        mMdnieCtrlSharpness = (SeekBarPreference)findPreference(DisplaySetting.KEY_MDNIE_CTRL_SHARPNESS);

        if (mSetting.isEnableMdnieForceDisable()) {
            mMdnieForceDisable.setEnabled(true);
            mMdnieForceDisable.setOnPreferenceChangeListener(this);
            mMdnieForceDisable.setChecked(!mSetting.getMdnieForceDisable());
        }

        if (mSetting.isEnableMdnieCtrlEnable()) {
            mMdnieCtrlEnable.setEnabled(true);
            mMdnieCtrlEnable.setOnPreferenceChangeListener(this);
            mMdnieCtrlEnable.setChecked(mSetting.getMdnieCtrlEnable());

            mMdnieCtrlRed.setEnabled(true);
            mMdnieCtrlRed.setOnPreferenceDoneListener(this);
            String value = mSetting.getMdnieCtrlRed();
            mMdnieCtrlRed.setSummary(Misc.getCurrentValueText(this, value));
            mMdnieCtrlRed.setValue(127, -127, Integer.parseInt(value));

            mMdnieCtrlGreen.setEnabled(true);
            mMdnieCtrlGreen.setOnPreferenceDoneListener(this);
            value = mSetting.getMdnieCtrlGreen();
            mMdnieCtrlGreen.setSummary(Misc.getCurrentValueText(this, value));
            mMdnieCtrlGreen.setValue(127, -127, Integer.parseInt(value));

            mMdnieCtrlBlue.setEnabled(true);
            mMdnieCtrlBlue.setOnPreferenceDoneListener(this);
            value = mSetting.getMdnieCtrlBlue();
            mMdnieCtrlBlue.setSummary(Misc.getCurrentValueText(this, value));
            mMdnieCtrlBlue.setValue(127, -127, Integer.parseInt(value));

            mMdnieCtrlSharpness.setEnabled(true);
            mMdnieCtrlSharpness.setOnPreferenceDoneListener(this);
            value = mSetting.getMdnieCtrlSharpness();
            mMdnieCtrlSharpness.setSummary(Misc.getCurrentValueText(this, value));
            mMdnieCtrlSharpness.setValue(127, -127, Integer.parseInt(value));
        }
    }

    public boolean onPreferenceChange(Preference preference, Object objValue) {
        if (mMdnieForceDisable == preference) {
            mSetting.setMdnieForceDisable(!(Boolean)objValue);
            return true;

        } else if (mMdnieCtrlEnable == preference) {
            mSetting.setMdnieCtrlEnable(((Boolean)objValue));
            return true;
        }
        return false;
    }

    @Override
    public boolean onPreferenceDone(Preference preference, String newValue) {
        if (mMdnieCtrlRed == preference) {
            mSetting.setMdnieCtrlRed(newValue);
            mMdnieCtrlRed.setSummary(Misc.getCurrentValueText(this, newValue));
            return true;

        } else if (mMdnieCtrlGreen == preference) {
            mSetting.setMdnieCtrlGreen(newValue);
            mMdnieCtrlGreen.setSummary(Misc.getCurrentValueText(this, newValue));
            return true;

        } else if (mMdnieCtrlBlue == preference) {
            mSetting.setMdnieCtrlBlue(newValue);
            mMdnieCtrlBlue.setSummary(Misc.getCurrentValueText(this, newValue));
            return true;

        } else if (mMdnieCtrlSharpness == preference) {
            mSetting.setMdnieCtrlSharpness(newValue);
            mMdnieCtrlSharpness.setSummary(Misc.getCurrentValueText(this, newValue));
            return true;

        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean ret = super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.reset_menu, menu);
        return ret;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_reset:
            mSetting.reset();
            Misc.confirmReboot(this, R.string.reboot_reflect_comfirm);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
}
