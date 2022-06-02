package com.advantal.onebrush.utilies_pkg;

/**
 * Created by Sonam on 06-05-2022 22:52.
 */
public class ErrorModel {
    private String error;
    private String errorTitle;

    public ErrorModel(String error, String errorTitle) {
        this.error = error;
        this.errorTitle = errorTitle;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrorTitle() {
        return errorTitle;
    }

    public void setErrorTitle(String errorTitle) {
        this.errorTitle = errorTitle;
    }
}
