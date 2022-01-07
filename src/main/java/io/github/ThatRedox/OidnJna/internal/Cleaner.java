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

package io.github.ThatRedox.OidnJna.internal;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cleaner extends Thread {
    public static Cleaner CLEANER = new Cleaner();

    private final ReferenceQueue<Object> refQueue = new ReferenceQueue<>();
    private final List<Object> cleanables = Collections.synchronizedList(new ArrayList<>());

    public class Cleanable extends PhantomReference<Object> {
        private final Runnable cleaner;
        private boolean cleaned = false;

        private Cleanable(Object obj, Runnable cleaner) {
            super(obj, refQueue);
            this.cleaner = cleaner;
        }

        public synchronized void clean() {
            if (!cleaned) {
                this.cleaner.run();
                cleaned = true;
                cleanables.remove(this);
            }
        }
    }

    public Cleaner() {
        super();
        this.setDaemon(true);
    }

    public Cleanable create(Object obj, Runnable cleaner) {
        Cleanable cleanable = new Cleanable(obj, cleaner);
        cleanables.add(cleanable);
        return cleanable;
    }

    @Override
    public void run() {
        try {
            while (!isInterrupted()) {
                Reference<?> ref = refQueue.remove();
                if (ref instanceof Cleanable) {
                    ((Cleanable) ref).clean();
                }
            }
        } catch (InterruptedException e) {
            // Ignore
        }
    }
}
