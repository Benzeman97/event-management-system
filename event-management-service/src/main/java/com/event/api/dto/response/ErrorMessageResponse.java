package com.event.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMessageResponse implements Serializable {

    private int errorCode;
    private String errorMessage;
}
