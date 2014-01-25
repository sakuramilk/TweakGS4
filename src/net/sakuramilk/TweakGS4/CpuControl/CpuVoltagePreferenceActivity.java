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

import net.sakuramilk.TweakGS4.CpuControl.CpuControlSetting;
import net.sakuramilk.TweakGS4.CpuControl.CpuVoltageSetting;
import net.sakuramilk.TweakGS4.R;
import net.sakuramilk.util.Misc;
import net.sakuramilk.widget.ApplyButtonPreferenceActivity;
import net.sakuramilk.widget.SeekBarPreference;
import net.sakuramilk.widget.SeekBarPreference.OnSeekBarPreferenceDoneListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class CpuVoltagePreferenceActivity extends ApplyButtonPreferenceActivity
    implements OnSeekBarPreferenceDoneListener, OnClickListener {

    private CpuVoltageSetting mSetting;
    private ArrayList<SeekBarPreference> mFreqPrefList;
    private String[] mCurVoltTable;
    private String[] mSavedVoltTable;
    
    private void setValues() {
        for (int i = 0; i < mCurVoltTable.length; i++) {
            SeekBarPreference pref = mFreqPrefList.get(i);
            pref.setValue(1500, 700, mSavedVoltTable[i] == null ?
                    Integer.valueOf(mCurVoltTable[i]) : Integer.valueOf(mSavedVoltTable[i]));
            pref.setSummary(Misc.getCurrentAndSavedValueText(this,
                    mCurVoltTable[i] + "mV",
                    (mSavedVoltTable[i] == null ? getText(R.string.none).toString() : mSavedVoltTable[i] + "mV")));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.cpu_control_voltage_pref);
        mSetting = new CpuVoltageSetting(this);

        mApplyButton.setOnClickListener(this);

        CpuControlSetting cpuSetting = new CpuControlSetting(this);
        String[] availableFrequencies = cpuSetting.getAvailableFrequencies();
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < availableFrequencies.length; i++) {
        	String freq = availableFrequencies[availableFrequencies.length - i - 1];
        	list.add(String.valueOf(Integer.parseInt(freq) / 1000) + "MHz");
        }
        String[] availableFreqEntries = list.toArray(new String[0]);

        PreferenceManager prefManager = getPreferenceManager();
        PreferenceScreen rootPref = (PreferenceScreen)prefManager.findPreference(CpuVoltageSetting.KEY_CPU_VOLT_ROOT_PREF);
        if (mSetting.isEnableVoltageControl()) {
            mFreqPrefList = new ArrayList<SeekBarPreference>();
            mCurVoltTable = mSetting.getCurrentVoltageTable();
            mSavedVoltTable = new String[mCurVoltTable.length];
            for (int i = 0; i < availableFrequencies.length; i++) {
                SeekBarPreference pref = new SeekBarPreference(this, null);
                String freq = String.valueOf(Integer.parseInt(availableFrequencies[i]) / 1000);
                String key = CpuVoltageSetting.KEY_CPU_VOLT_CTRL_BASE + freq;
                mSavedVoltTable[i] = mSetting.loadVoltage(key);
                pref.setKey(key);
                pref.setTitle(availableFreqEntries[i]);
                pref.setOnPreferenceDoneListener(this);
                rootPref.addPreference(pref);
                mFreqPrefList.add(pref);
            }
            setValues();
        } else {
            PreferenceScreen pref = prefManager.createPreferenceScreen(this);
            pref.setTitle(R.string.not_support);
            rootPref.addPreference(pref);
        }
    }

    @Override
    public void onClick(View v) {
        mApplyButton.setEnabled(false);
        mCurVoltTable = mSetting.getCurrentVoltageTable();
        for (int i = 0; i < mSavedVoltTable.length; i++) {
            if (mSavedVoltTable[i] != null) {
                mCurVoltTable[i] = mSavedVoltTable[i];
            }
        }
        mSetting.setVoltageTable(mCurVoltTable);
        setValues();
        
        int length = mFreqPrefList.size();
        for (int i = 0; i < length; i++) {
        	SeekBarPreference pref = mFreqPrefList.get(i);
        	mSetting.saveVoltage(pref.getKey(), mCurVoltTable[i]);
        }
    }

    @Override
    public boolean onPreferenceDone(Preference preference, String newValue) {
        mApplyButton.setEnabled(true);
        int index = mFreqPrefList.indexOf(preference);
        mSavedVoltTable[index] = newValue;
        preference.setSummary(Misc.getCurrentAndSavedValueText(this,
                mCurVoltTable[index] + "mV", newValue + "mV"));
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean ret = super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.default_menu, menu);
        return ret;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_recommend:
        	if (mSetting.isEnableVoltageControl()) {
        		mSavedVoltTable = mSetting.getDefaulVoltageTable();
        		setValues();
        		mApplyButton.setEnabled(true);
        	}
            return true;
        case R.id.menu_reset:
            mSetting.reset();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
}
