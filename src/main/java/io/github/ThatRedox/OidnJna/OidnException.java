package io.github.ThatRedox.OidnJna;

/**
 * An exception in OIDN.
 */
public class OidnException extends RuntimeException {
    public OidnException(String exception) {
        super(exception);
    }

    public OidnException(String format, Object... args) {
        super(String.format(format, args));
    }
}
