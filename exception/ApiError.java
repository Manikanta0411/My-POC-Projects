package com.record.exception;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Class define the API Error thrown from our exception layer.
 * ApiError Class
 */
@Data
@JsonTypeIdResolver(LowerCaseClassNameResolver.class)
public class ApiError {

	private HttpStatus status;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	private LocalDateTime timestamp;

	private String message;

	private String debugMessage;

	private List<ApiSubError> subErrors;

	/**
	 * Set the local date time
	 */
	private ApiError() {
		timestamp = LocalDateTime.now();
	}

	/**
	 * Set status
	 *
	 * @param status HttpStatus Object
	 */
	public ApiError(HttpStatus status) {
		this();
		this.status = status;
	}

	/**
	 * Set status,errorMessage and debugMessage
	 *
	 * @param status HttpStatus Object
	 * @param ex     Throwable Object
	 */
	public ApiError(HttpStatus status, Throwable ex) {
		this();
		this.status = status;
		this.message = "Unexpected error";
		this.debugMessage = ex.getLocalizedMessage();
	}

	/**
	 * Set status,errorMessage and debugMessage
	 *
	 * @param status  HttpStatus Object
	 * @param message String value
	 * @param ex      Throwable Object
	 */
	public ApiError(HttpStatus status, String message, Throwable ex) {
		this();
		this.status = status;
		this.message = message;
		this.debugMessage = ex.getLocalizedMessage();
	}

	/**
	 * Add Sub Error
	 *
	 * @param subError ApiSubError
	 */
	private void addSubError(ApiSubError subError) {
		if (subErrors == null) {
			subErrors = new ArrayList<>();
		}
		subErrors.add(subError);
	}

	/**
	 * Add Validation Error
	 *
	 * @param object        String value
	 * @param field         String value
	 * @param rejectedValue Object
	 * @param message       String value
	 */
	private void addValidationError(String object, String field, Object rejectedValue, String message) {
		addSubError(new ApiValidationError(object, field, rejectedValue, message));
	}

	/**
	 * Add Validation Error
	 *
	 * @param object  String value
	 * @param message String value
	 */
	private void addValidationError(String object, String message) {
		addSubError(new ApiValidationError(object, message));
	}

	/**
	 * Add Validation Error
	 *
	 * @param fieldError FieldError
	 */
	private void addValidationError(FieldError fieldError) {
		this.addValidationError(fieldError.getObjectName(), fieldError.getField(), fieldError.getRejectedValue(),
				fieldError.getDefaultMessage());
	}

	/**
	 * Add Validation Errors
	 *
	 * @param fieldErrors list of FieldError
	 */
	public void addValidationErrors(List<FieldError> fieldErrors) {
		fieldErrors.forEach(this::addValidationError);
	}

	/**
	 * Add Validation Error
	 *
	 * @param objectError ObjectError
	 */
	private void addValidationError(ObjectError objectError) {
		this.addValidationError(objectError.getObjectName(), objectError.getDefaultMessage());
	}

	/**
	 * Add Validation Error
	 *
	 * @param globalErrors list of ObjectError
	 */
	public void addValidationError(List<ObjectError> globalErrors) {
		globalErrors.forEach(this::addValidationError);
	}

	/**
	 * Utility method for adding error of ConstraintViolation. Usually when
	 * a @Validated validation fails.
	 *
	 * @param cv the ConstraintViolation
	 */
	private void addValidationError(ConstraintViolation<?> cv) {
		this.addValidationError(cv.getRootBeanClass().getSimpleName(),
				((PathImpl) cv.getPropertyPath()).getLeafNode().asString(), cv.getInvalidValue(), cv.getMessage());
	}

	/**
	 * Add Validation Error
	 *
	 * @param constraintViolations set of ConstraintViolation
	 */
	public void addValidationErrors(Set<ConstraintViolation<?>> constraintViolations) {
		constraintViolations.forEach(this::addValidationError);
	}

	/**
	 * Api Sub Error Interface
	 */
	interface ApiSubError {

	}

	@Data
	@EqualsAndHashCode(callSuper = false)
	@AllArgsConstructor
	/**
	 * ApiValidatorError Class Implements AbiSubError
	 */
	class ApiValidationError implements ApiSubError {

		private String object;

		private String field;

		private Object rejectedValue;

		private String message;

		/**
		 * Api Validation Error parameterized Construcctor
		 *
		 * @param object  String value
		 * @param message String value
		 */
		ApiValidationError(String object, String message) {
			this.object = object;
			this.message = message;
		}
	}
}

/**
 * LowerCaseNameResolver Class Extends Type Id Resolver Base
 */
class LowerCaseClassNameResolver extends TypeIdResolverBase {

	/**
	 * Implementation of idFromValue()
	 *
	 * @return String value
	 */
	@Override
	public String idFromValue(Object value) {
		return value.getClass().getSimpleName().toLowerCase();
	}

	/**
	 * Implementation of idFromValueAndType()
	 *
	 * @return String value
	 */
	@Override
	public String idFromValueAndType(Object value, Class<?> suggestedType) {
		return idFromValue(value);
	}

	/**
	 * Implementation of getMechanism()
	 *
	 * @return JsonTypeInfo.Id
	 */
	@Override
	public JsonTypeInfo.Id getMechanism() {
		return JsonTypeInfo.Id.CUSTOM;
	}

}
