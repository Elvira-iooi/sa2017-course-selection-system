package cn.eud.nju.cs.sa2017.courseSelectionSystem.models;

import org.springframework.http.HttpStatus;

import javax.validation.constraints.NotNull;

public class Error {

    @NotNull
    private int code;

    @NotNull
    private String message;

    private Error(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static Error Error400() {
        return new Error(400, "bad request");
    }

    public static Error Error404() {
        return new Error(404, "404 not found");
    }

    public static Error Error503() {
        return new Error(503, "Server is busy");
    }

    public static Error Error409() {
        return new Error(409, "This student has already added");
    }

    public static Error typedError(HttpStatus s) {
        if (s.equals(HttpStatus.SERVICE_UNAVAILABLE)){
            return Error503();
        } else if (s.equals(HttpStatus.CONFLICT)) {
            return Error409();
        } else if (s.equals(HttpStatus.NOT_FOUND)) {
            return Error404();
        } else {
            return Error400();
        }
    }

}
