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

package net.sakuramilk.TweakGS4.SoundAndVib;

import net.sakuramilk.TweakGS4.R;
import net.sakuramilk.util.Misc;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;

public class BootSoundPreferenceActivity extends PreferenceActivity
    implements OnPreferenceChangeListener {

    private BootSoundSetting mSetting;
    private CheckBoxPreference mBootSoundEnabled;
    private ListPreference mBootSoundVolume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.boot_sound_pref);

        mSetting = new BootSoundSetting();

        mBootSoundEnabled = (CheckBoxPreference)findPreference(BootSoundSetting.KEY_BOOT_SOUND_ENABLED);
        mBootSoundEnabled.setChecked(mSetting.getBootSoundEnabled());
        mBootSoundEnabled.setOnPreferenceChangeListener(this);

        mBootSoundVolume = (ListPreference)findPreference(BootSoundSetting.KEY_BOOT_SOUND_VOLUME);
        String value = mSetting.getBootSoundVolume();
        mBootSoundVolume.setValue(value);
        mBootSoundVolume.setSummary(Misc.getCurrentValueText(this,
                Misc.getEntryFromEntryValue(mBootSoundVolume.getEntries(), mBootSoundVolume.getEntryValues(), value)));
        mBootSoundVolume.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mBootSoundEnabled) {
            boolean value = (Boolean)newValue;
            mSetting.setBootSoundEnabled(value);
            mBootSoundEnabled.setChecked(value);

        } else if (preference == mBootSoundVolume) {
            String value = (String)newValue;
            mSetting.setBootSoundVolume(value);
            mBootSoundVolume.setValue(value);
            mBootSoundVolume.setSummary(Misc.getCurrentValueText(this,
                    Misc.getEntryFromEntryValue(mBootSoundVolume.getEntries(), mBootSoundVolume.getEntryValues(), value)));

        }
        return false;
    }
}
