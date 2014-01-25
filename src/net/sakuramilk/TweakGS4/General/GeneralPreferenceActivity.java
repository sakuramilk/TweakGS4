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

package net.sakuramilk.TweakGS4.General;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import net.sakuramilk.TweakGS4.Config;
import net.sakuramilk.TweakGS4.R;
import net.sakuramilk.util.Misc;
import net.sakuramilk.util.SystemCommand;
import android.app.AlertDialog;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.view.Menu;
import android.view.MenuItem;

public class GeneralPreferenceActivity extends PreferenceActivity implements OnPreferenceChangeListener, OnPreferenceClickListener {

    private GeneralSetting mSetting;
    private ListPreference mIoSched;
    private CheckBoxPreference mReplaceKey;
    private PreferenceScreen mFelicaKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.general_pref);
        mSetting = new GeneralSetting(this);

        mIoSched = (ListPreference)findPreference(GeneralSetting.KEY_IO_SCHED);
        String curValue = mSetting.getCurrentIoScheduler();
        String entries[] = mSetting.getIoSchedulerList();
        mIoSched.setEntries(entries);
        mIoSched.setEntryValues(entries);
        mIoSched.setValue(curValue);
        mIoSched.setOnPreferenceChangeListener(this);
        mIoSched.setSummary(Misc.getCurrentValueText(this, curValue));

        mReplaceKey = (CheckBoxPreference)findPreference(GeneralSetting.KEY_REPLACE_KEY);
        if (mSetting.isEnableReplaceKey()) {
        	mReplaceKey.setEnabled(true);
        	mReplaceKey.setChecked(mSetting.getReplaceKey());
        	mReplaceKey.setOnPreferenceChangeListener(this);
        }

        File felicaFile = new File(Misc.getSdcardPath(true) + Config.KBC_DATA_DIR + "/felica_key");
        String curFelicaKey = "";
        if (felicaFile.exists()) {
        	try {
        		BufferedReader br = new BufferedReader(new FileReader(felicaFile));
				curFelicaKey = br.readLine();
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        mFelicaKey = (PreferenceScreen)findPreference(GeneralSetting.KEY_FELICA_KEY);
        mFelicaKey.setOnPreferenceClickListener(this);
        if ("".equals(curFelicaKey)) {
        	mFelicaKey.setSummary(getText(R.string.felica_key_current_value) + " : ");
        } else {
        	mFelicaKey.setSummary(getText(R.string.felica_key_current_value) + " : " + curFelicaKey.substring(10) + "...");
        }
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (mIoSched == preference) {
            mSetting.setIoScheduler(newValue.toString());
            mIoSched.setSummary(Misc.getCurrentValueText(this, newValue.toString()));
            return true;
        } else if (mReplaceKey == preference) {
        	mSetting.setReplaceKey((Boolean)newValue);
        	mReplaceKey.setChecked((Boolean)newValue);
        	return true;
        }
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
        case R.id.menu_reset:
            mSetting.reset();
            Misc.confirmReboot(this, R.string.reboot_reflect_comfirm);
            return true;
        case R.id.menu_recommend:
            mSetting.setRecommend();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

	@Override
	public boolean onPreferenceClick(Preference preference) {
		if (preference == mFelicaKey) {
			String felicaKey = SystemCommand.get_felica_key();
			if (felicaKey == null || "".equals(felicaKey)){
	            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
	            alertDialogBuilder.setTitle(android.R.string.dialog_alert_title);
	            alertDialogBuilder.setMessage(R.string.error_get_felica_key);
	            alertDialogBuilder.setPositiveButton(android.R.string.ok, null);
	            alertDialogBuilder.show();
	            return false;
			}

			File felicaFile = new File(Misc.getSdcardPath(true) + Config.KBC_DATA_DIR + "/felica_key");
			PrintWriter pw;
			try {
				pw = new PrintWriter(new BufferedWriter(new FileWriter(felicaFile)));
				pw.write(felicaKey);
				pw.close();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}

            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(R.string.app_name);
            alertDialogBuilder.setMessage(R.string.success_get_felica_key);
            alertDialogBuilder.setPositiveButton(android.R.string.ok, null);
            alertDialogBuilder.show();
			mFelicaKey.setSummary(getText(R.string.felica_key_current_value) + " : " + felicaKey.substring(10) + "...");
		}
		return false;
	}
}
