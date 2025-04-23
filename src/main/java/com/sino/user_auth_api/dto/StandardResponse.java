package com.sino.user_auth_api.dto;

import lombok.*;


@Setter
@Getter
public class StandardResponse<T> {
    private int status;

    public StandardResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    private String message;
    private T data;
}
