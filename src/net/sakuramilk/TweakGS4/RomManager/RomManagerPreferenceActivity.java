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

package net.sakuramilk.TweakGS4.RomManager;

import net.sakuramilk.TweakGS4.Config;
import net.sakuramilk.TweakGS4.R;
import net.sakuramilk.util.Constant;
import net.sakuramilk.util.Misc;
import net.sakuramilk.util.SystemCommand;
import net.sakuramilk.widget.TextInputDialog;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.widget.Toast;

public class RomManagerPreferenceActivity extends PreferenceActivity
    implements OnPreferenceClickListener, OnPreferenceChangeListener {

    private PreferenceScreen mRebootNormal;
    private PreferenceScreen mRebootRecovery;
    private PreferenceScreen mRebootDownload;
    private ListPreference mNandroidBackup;
    private ListPreference mNandroidRestore;
    private ListPreference mNandroidManage;
    private PreferenceScreen mFlashInstallZip;
    private PreferenceScreen mPartitionBackup;
    private PreferenceScreen mTimeAdjust;
    private PreferenceScreen mTempUnroot;
    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.rom_manager_pref);

        mContext = this;

        mRebootNormal = (PreferenceScreen)findPreference("reboot_normal");
        mRebootNormal.setOnPreferenceClickListener(this);
        mRebootRecovery = (PreferenceScreen)findPreference("reboot_recovery");
        mRebootRecovery.setOnPreferenceClickListener(this);
        mRebootDownload = (PreferenceScreen)findPreference("reboot_download");
        mRebootDownload.setOnPreferenceClickListener(this);

        mNandroidBackup = (ListPreference)findPreference("nandroid_backup");
        mNandroidBackup.setOnPreferenceChangeListener(this);
        mNandroidRestore = (ListPreference)findPreference("nandroid_restore");
        mNandroidRestore.setOnPreferenceChangeListener(this);
        mNandroidManage = (ListPreference)findPreference("nandroid_manage");
        mNandroidManage.setOnPreferenceChangeListener(this);

        mFlashInstallZip = (PreferenceScreen)findPreference("flash_install_zip");
        mFlashInstallZip.setOnPreferenceClickListener(this);

        mPartitionBackup = (PreferenceScreen)findPreference("partition_backup");
        mPartitionBackup.setOnPreferenceClickListener(this);

        mTimeAdjust = (PreferenceScreen)findPreference("time_adjust");
        mTimeAdjust.setOnPreferenceClickListener(this);
        
        mTempUnroot = (PreferenceScreen)findPreference("temp_unroot");
        if (Misc.isSuperUserEnabled()) {
            mTempUnroot.setSummary(R.string.temp_unroot_summary_rooted);
        } else {
            mTempUnroot.setSummary(R.string.temp_unroot_summary_unrooted);
        }
        mTempUnroot.setOnPreferenceClickListener(this);
    }

    @SuppressLint("HandlerLeak")
    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference == mRebootNormal) {
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(R.string.reboot);
            alertDialogBuilder.setMessage(R.string.reboot_summary);
            alertDialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    SystemCommand.reboot("");
                }
            });
            alertDialogBuilder.setNegativeButton(android.R.string.no, null);
            alertDialogBuilder.create().show();

        } else if (preference == mRebootRecovery) {
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(R.string.reboot);
            alertDialogBuilder.setMessage(R.string.recovery_summary);
            alertDialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    SystemCommand.reboot("recovery");
                }
            });
            alertDialogBuilder.setNegativeButton(android.R.string.no, null);
            alertDialogBuilder.create().show();

        } else if (preference == mRebootDownload) {
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(R.string.reboot);
            alertDialogBuilder.setMessage(R.string.download_summary);
            alertDialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    SystemCommand.reboot("download");
                }
            });
            alertDialogBuilder.setNegativeButton(android.R.string.no, null);
            alertDialogBuilder.create().show();

        } else if (preference == mFlashInstallZip) {
            Intent intent = new Intent(getApplicationContext(), ZipFilePickerActivity.class);
            intent.putExtra("title", getText(R.string.select_zip_title));
            intent.putExtra("select", "file");
            intent.putExtra("filter", ".zip");
            this.startActivity(intent);

        } else if (preference == mPartitionBackup) {
            PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
            final WakeLock wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "Tgs2ImageCreate");
            wakeLock.acquire();
            final ProgressDialog dlg = new ProgressDialog(this);
            dlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dlg.setTitle(R.string.app_name);
            dlg.setMessage(getText(R.string.partitiion_backup_progress));
            dlg.show();

            String backupDir = Misc.getSdcardPath(true) + Config.BACKUP_DIR;
            final String backupPath = backupDir + "/" + Misc.getDateString();
            
            final Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    dlg.dismiss();
                    wakeLock.release();
                    Toast.makeText(mContext, getText(R.string.backup_completed) + "\n" + backupPath, Toast.LENGTH_LONG).show();
                }
            };

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    SystemCommand.partition_backup_for_gs4(backupPath);
                    handler.sendEmptyMessage(0);
                }
            });
            thread.start();

        } else if (preference == mTimeAdjust) {
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(R.string.reboot);
            alertDialogBuilder.setMessage(R.string.recovery_summary);
            alertDialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    SystemCommand.time_adjust_recovery();
                }
            });
            alertDialogBuilder.setNegativeButton(android.R.string.no, null);
            alertDialogBuilder.create().show();
        } else if (preference == mTempUnroot) {
            if (Misc.isSuperUserEnabled()) {
                Misc.doTempUnroot();
            } else if (Misc.isTempUnroot()) {
                Misc.revertTempUnroot();
            }
            
            if (Misc.isSuperUserEnabled()) {
                mTempUnroot.setSummary(R.string.temp_unroot_summary_rooted);
            } else {
                mTempUnroot.setSummary(R.string.temp_unroot_summary_unrooted);
            }
        }
        return false;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mNandroidBackup) {
            final String sdcardPath = Misc.getSdcardPath("internal".equals(newValue));
            TextInputDialog dlg = new TextInputDialog(this);
            dlg.setFinishTextInputListener(new TextInputDialog.FinishTextInputListener() {
                @Override
                public void onFinishTextInput(CharSequence input) {
                    String inputName = input.toString();
                    inputName = inputName.replace("\n", "").trim();
                    String sdPath = sdcardPath.replace(Misc.getSdcardPath(false), "/emmc");
                    SystemCommand.backup_rom(sdPath + Constant.CWM_BACKUP_DIR +  "/" + inputName);
                    SystemCommand.reboot("recovery");
                }
            });
            dlg.show(R.string.backup, R.string.backup_name, Misc.getDateString());

        } else if (preference == mNandroidRestore) {
            final String sdcardPath = Misc.getSdcardPath("internal".equals(newValue));
            Intent intent = new Intent(getApplicationContext(), RestoreDirPickerActivity.class);
            intent.putExtra("title", getText(R.string.select_backup_title));
            intent.putExtra("select", "dir");
            intent.putExtra("mode", "restore");
            intent.putExtra("path", sdcardPath + Constant.CWM_BACKUP_DIR);
            this.startActivity(intent);

        } else if (preference == mNandroidManage) {
            final String sdcardPath = Misc.getSdcardPath("internal".equals(newValue));
            Intent intent = new Intent(getApplicationContext(), RestoreDirPickerActivity.class);
            intent.putExtra("title", getText(R.string.select_backup_title));
            intent.putExtra("select", "dir");
            intent.putExtra("mode", "manage");
            intent.putExtra("path", sdcardPath + Constant.CWM_BACKUP_DIR);
            this.startActivity(intent);
        }
        return false;
    }
}
