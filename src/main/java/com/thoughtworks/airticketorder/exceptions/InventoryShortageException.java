package com.thoughtworks.airticketorder.exceptions;

public class InventoryShortageException extends BusinessException {
    public InventoryShortageException(Integer code, String msg) {
        super(code, msg);
    }

    public InventoryShortageException(String message, Integer code, String msg) {
        super(message, code, msg);
    }

    public InventoryShortageException(String message, Throwable cause, Integer code, String msg) {
        super(message, cause, code, msg);
    }

    public InventoryShortageException(Throwable cause, Integer code, String msg) {
        super(cause, code, msg);
    }
}
