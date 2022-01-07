/* Copyright 2022 ThatRedox
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.ThatRedox.OidnJna;

import com.sun.jna.Native;
import io.github.ThatRedox.OidnJna.internal.OidnJna;

public class OpenImageDenoise {
    protected final OidnJna.Oidn lib;

    public OpenImageDenoise(String path) {
        this.lib = Native.load(path, OidnJna.Oidn.class);
    }

    public Device createDevice() {
        return createDevice(DeviceType.DEFAULT);
    }

    public Device createDevice(DeviceType deviceType) {
        return new Device(this, deviceType);
    }
}
