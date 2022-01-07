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

import com.sun.jna.Pointer;
import io.github.ThatRedox.OidnJna.internal.Cleaner;
import io.github.ThatRedox.OidnJna.internal.OidnJna;

public class Filter implements AutoCloseable {
    private final Cleaner.Cleanable cleanable;
    private final OidnJna.Oidn lib;
    protected final Pointer ptr;

    protected Filter(OidnJna.Oidn lib, Device device, String type) {
        this.lib = lib;
        this.ptr = this.lib.oidnNewFilter(device.ptr, type);

        OidnJna.Oidn cleanerLib = this.lib;
        Pointer cleanerPtr = this.ptr;
        cleanable = Cleaner.CLEANER.create(this, () ->
                cleanerLib.oidnReleaseFilter(cleanerPtr));
    }

    public void setFilterImage(String name, Buffer buffer, long width, long height) {
        lib.oidnSetFilterImage(ptr, name, buffer.ptr, 3,
                new OidnJna.size_t(width), new OidnJna.size_t(height),
                OidnJna.ZERO, OidnJna.ZERO, OidnJna.ZERO);
    }

    public void removeFilterImage(String name) {
        lib.oidnRemoveFilterimage(ptr, name);
    }

    public void setFilterParam(String name, boolean value) {
        lib.oidnSetFilter1b(ptr, name, value);
    }

    public void setFilterParam(String name, int value) {
        lib.oidnSetFilter1i(ptr, name, value);
    }

    public void setFilterParam(String name, float value) {
        lib.oidnSetFilter1f(ptr, name, value);
    }

    public boolean getFilterValueB(String name) {
        return lib.oidnGetFilter1b(ptr, name);
    }

    public int getFilterValueI(String name) {
        return lib.oidnGetFilter1i(ptr, name);
    }

    public float getFilterValueF(String name) {
        return lib.oidnGetFilter1f(ptr, name);
    }

    public void commit() {
        lib.oidnCommitFilter(ptr);
    }

    public void execute() {
        lib.oidnExecuteFilter(ptr);
    }

    public void close() {
        cleanable.clean();
    }
}
