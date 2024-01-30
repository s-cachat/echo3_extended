package com.cachat.prj.echo3.base;

import com.cachat.prj.echo3.blockeditor.InvalidException;

/**
 * une exception signalant un contenu en doublon
 *
 * @author scachat
 */
public class DuplicateException extends InvalidException {

    public DuplicateException() {
    }

    public DuplicateException(String message) {
        super(message);
    }

    public DuplicateException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateException(Throwable cause) {
        super(cause);
    }

}
