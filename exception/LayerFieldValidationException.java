package com.record.exception;

import com.record.dto.FieldErrorDto;

import java.util.List;

public class LayerFieldValidationException extends RuntimeException {
    private List<FieldErrorDto> fieldErrorDtos;

    public LayerFieldValidationException(List<FieldErrorDto> errors) {
        this.fieldErrorDtos = errors;
    }

    public List<FieldErrorDto> getFieldErrorDtos() {
        return fieldErrorDtos;
    }
}
