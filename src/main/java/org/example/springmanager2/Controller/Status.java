package org.example.springmanager2.Controller;

import lombok.Data;

@Data
public class Status {

    int status;
    String message;

    public Status(int code, String message) {
        status = code;
        this.message = message;
    }
}
