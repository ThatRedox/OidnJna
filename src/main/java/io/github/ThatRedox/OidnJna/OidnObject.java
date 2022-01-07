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

import java.util.function.Consumer;

/**
 * An abstract OIDN object. This handles the cleanup of objects automatically.
 */
public abstract class OidnObject implements AutoCloseable {
    private final Cleaner.Cleanable cleanable;
    protected final OidnJna.Oidn lib;
    protected Pointer ptr;

    /**
     * @param library OidnJna library instance.
     * @param object  Oidn object pointer.
     * @param cleanup Oidn function to release the pointer.
     */
    protected OidnObject(OidnJna.Oidn library, Pointer object, Consumer<Pointer> cleanup) {
        this.lib = library;
        this.ptr = object;

        this.cleanable = Cleaner.CLEANER.create(this,
                () -> cleanup.accept(object));
    }

    @Override
    public void close() {
        cleanable.clean();
    }
}
