package pl.adamklimko.kkpandroid.exception;

import java.io.IOException;

public class NoNetworkConnectedException extends IOException {
    public NoNetworkConnectedException() {
        super();
    }

    public NoNetworkConnectedException(String message) {
        super(message);
    }
}
