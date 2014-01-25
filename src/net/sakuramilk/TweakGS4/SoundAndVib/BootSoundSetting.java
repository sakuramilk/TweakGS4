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

import net.sakuramilk.util.Convert;
import net.sakuramilk.util.SystemCommand;

public class BootSoundSetting {
    
    public static final String KEY_BOOT_SOUND_ENABLED = "boot_sound_enabled";
    public static final String KEY_BOOT_SOUND_VOLUME = "boot_sound_volume";

    void setBootSoundEnabled(boolean value) {
        SystemCommand.set_prop("persist.sys.nobootsound", Convert.toString(!value));
    }

    boolean getBootSoundEnabled() {
        return !Convert.toBoolean(SystemCommand.get_prop("persist.sys.nobootsound", "0"));
    }

    void setBootSoundVolume(String value) {
        SystemCommand.set_prop("persist.sys.boosound_volume", value);
    }

    String getBootSoundVolume() {
        return SystemCommand.get_prop("persist.sys.boosound_volume", "0.2");
    }
}
