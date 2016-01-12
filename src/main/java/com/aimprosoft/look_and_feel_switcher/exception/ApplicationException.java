package com.aimprosoft.look_and_feel_switcher.exception;

/**
 * crated by m.tkachenko on 16.10.15 12:18
 */
public class ApplicationException extends Exception {

    public ApplicationException() {
        super("lfs-internal-server-error");
    }

    public ApplicationException(String message, Object... args) {
        super(String.format(message, args));
    }

}