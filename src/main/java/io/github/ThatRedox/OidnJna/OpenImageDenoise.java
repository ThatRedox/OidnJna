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

/**
 * A wrapper for OpenImageDenoise (OIDN). This holds the actual library and can be used
 * to create devices. For documentation on OIDN, see https://www.openimagedenoise.org/documentation.html
 */
public class OpenImageDenoise {
    protected final OidnJna.Oidn lib;

    /**
     * Load the OIDN library from a string path.
     * @param path Path to the OIDN library (ie {@code OpenImageDenoise.dll} for Windows).
     */
    public OpenImageDenoise(String path) {
        this.lib = Native.load(path, OidnJna.Oidn.class);
    }

    /**
     * Create a device with the default type.
     * @return An OIDN device.
     */
    public Device createDevice() {
        return createDevice(DeviceType.DEFAULT);
    }

    /**
     * Create a device with a specific device type.
     * @param deviceType The type of device to create.
     * @return An OIDN device.
     */
    public Device createDevice(DeviceType deviceType) {
        return new Device(this, deviceType);
    }
}
