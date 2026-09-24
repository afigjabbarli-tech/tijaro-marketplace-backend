package com.tijaro.marketplace.common.application.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private int statusCode;
    private String message;
    private T response;
}
