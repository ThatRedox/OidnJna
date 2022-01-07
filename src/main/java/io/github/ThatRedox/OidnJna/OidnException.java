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

/**
 * An exception in OIDN.
 */
public class OidnException extends RuntimeException {
    public OidnException(String exception) {
        super(exception);
    }

    public OidnException(String format, Object... args) {
        this(String.format(format, args));
    }

    /**
     * Create an exception from a OIDN code and message.
     * @param code    OIDN code
     * @param message OIDN message
     * @return Corresponding OIDN exception.
     */
    public static OidnException fromCode(int code, String message) {
        String codeMessage;
        switch (code) {
            default:
            case 1:
                codeMessage = "Unknown";
                break;
            case 2:
                codeMessage = "Invalid Argument";
                break;
            case 3:
                codeMessage = "Invalid Operation";
                break;
            case 4:
                codeMessage = "Out of Memory";
                break;
            case 5:
                codeMessage = "Unsupported Hardware";
                break;
            case 6:
                codeMessage = "Cancelled";
                break;
        }

        return new OidnException("%s: %s", codeMessage, message);
    }
}
