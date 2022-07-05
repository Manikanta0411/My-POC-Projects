package com.record.exception.handler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

import java.util.Objects;

import javax.naming.ServiceUnavailableException;
import javax.persistence.EntityNotFoundException;

import org.apache.logging.log4j.util.Strings;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.record.exception.ApiError;
import com.record.exception.ExternalServiceException;
import com.record.exception.RecordException;
import com.record.exception.SqlException;
import com.record.exception.UnauthorizedException;
import com.record.exception.BadRequestException;

/**
 * RestExceptionHandler Class For Handling Exceptions
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	/**
	 * Handle MissingServletRequestParameterException. Triggered when a 'required'
	 * request parameter is missing.
	 *
	 * @param ex      MissingServletRequestParameterException
	 * @param headers HttpHeaders
	 * @param status  HttpStatus
	 * @param request WebRequest
	 * @return the ApiError object
	 */
	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		String error = ex.getParameterName() + " parameter is missing";

		return buildResponseEntity(new ApiError(BAD_REQUEST, error, ex));
	}

	/**
	 * Handle HttpMediaTypeNotSupportedException. This one triggers when JSON is
	 * invalid as well.
	 *
	 * @param ex      HttpMediaTypeNotSupportedException
	 * @param headers HttpHeaders
	 * @param status  HttpStatus
	 * @param request WebRequest
	 * @return the ApiError object
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		StringBuilder builder = new StringBuilder();
		builder.append(ex.getContentType());
		builder.append(" media type is not supported. Supported media types are ");

		ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));

		return buildResponseEntity(
				new ApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, builder.substring(0, builder.length() - 2), ex));
	}

	/**
	 * Handle MethodArgumentNotValidException. Triggered when an object fails @Valid
	 * validation.
	 *
	 * @param ex      the MethodArgumentNotValidException that is thrown when @Valid
	 *                validation fails
	 * @param headers HttpHeaders
	 * @param status  HttpStatus
	 * @param request WebRequest
	 * @return the ApiError object
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		ApiError apiError = new ApiError(BAD_REQUEST);
		apiError.setMessage("Validation error");
		apiError.addValidationErrors(ex.getBindingResult().getFieldErrors());
		apiError.addValidationError(ex.getBindingResult().getGlobalErrors());

		return buildResponseEntity(apiError);
	}

	/**
	 * Handle HttpMessageNotWritableException.
	 *
	 * @param ex      HttpMessageNotWritableException
	 * @param headers HttpHeaders
	 * @param status  HttpStatus
	 * @param request WebRequest
	 * @return the ApiError object
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		String error = "Error writing JSON output";

		return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, error, ex));
	}

	/**
	 * Handle HttpMessageNotReadableException. Happens when request JSON is
	 * malformed.
	 *
	 * @param ex      HttpMessageNotReadableException
	 * @param headers HttpHeaders
	 * @param status  HttpStatus
	 * @param request WebRequest
	 * @return the ApiError object
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		String error = "Malformed JSON request";

		return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, ex));
	}

	/**
	 * Handles javax.validation.ConstraintViolationException. Thrown when @Validated
	 * fails.
	 *
	 * @param ex the ConstraintViolationException
	 * @return the ApiError object
	 */
	@ExceptionHandler(javax.validation.ConstraintViolationException.class)
	protected ResponseEntity<Object> handleConstraintViolation(javax.validation.ConstraintViolationException ex) {

		ApiError apiError = new ApiError(BAD_REQUEST);
		apiError.setMessage("Validation error");
		apiError.addValidationErrors(ex.getConstraintViolations());

		return buildResponseEntity(apiError);
	}

	/**
	 * Handle DataIntegrityViolationException, inspects the cause for different DB
	 * causes.
	 *
	 * @param ex      the DataIntegrityViolationException
	 * @param request WebRequest
	 * @return the ApiError object
	 */
	@ExceptionHandler(DataIntegrityViolationException.class)
	protected ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex,
			WebRequest request) {

		if (ex.getCause() instanceof ConstraintViolationException) {
			return buildResponseEntity(new ApiError(HttpStatus.CONFLICT, "Database error", ex.getCause()));
		}

		return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex));
	}

	/**
	 * Handle Exception, handle generic Exception.class
	 *
	 * @param ex      the Exception
	 * @param request WebRequest
	 * @return the ApiError object
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
			WebRequest request) {

		ApiError apiError = new ApiError(BAD_REQUEST);
		apiError.setMessage(String.format("The parameter '%s' of value '%s' could not be converted to type '%s'",
				ex.getName(), ex.getValue(), getSimpleName(ex)));
		apiError.setDebugMessage(ex.getMessage());

		return buildResponseEntity(apiError);
	}

	/**
	 * Handles EntityNotFoundException. Created to encapsulate errors with more
	 * detail than javax.persistence.EntityNotFoundException.
	 *
	 * @param ex the EntityNotFoundException Object
	 * @return the ApiError Object
	 */
	@ExceptionHandler(EntityNotFoundException.class)
	protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {

		ApiError apiError = new ApiError(NOT_FOUND);
		apiError.setMessage(ex.getMessage());

		return buildResponseEntity(apiError);
	}

	/**
	 * Handles BadRequestException. Created to encapsulate errors with more detail
	 * than javax.persistence.BadRequestException.
	 *
	 * @param ex the BadRequestException Object
	 * @return the ApiError Object
	 */
	@ExceptionHandler(BadRequestException.class)
	protected ResponseEntity<Object> handleBadRequestException(BadRequestException ex) {
		ApiError apiError = new ApiError(BAD_REQUEST);
		apiError.setMessage(ex.getMessage());
		return buildResponseEntity(apiError);
	}
	
	/**
	 * Handles ServiceUnavailableException. Created to encapsulate errors with more detail
	 * than javax.persistence.ServiceUnavailableException.
	 *
	 * @param ex the ServiceUnavailableException Object
	 * @return the ApiError Object
	 */
	@ExceptionHandler(ServiceUnavailableException.class)
	protected ResponseEntity<Object> handleServiceUnavailableExceptionRequestException(ServiceUnavailableException ex) {

		ApiError apiError = new ApiError(SERVICE_UNAVAILABLE);
		apiError.setMessage(ex.getMessage());

		return buildResponseEntity(apiError);
	}

	/**
	 * Handles UnauthorizedException. Created to encapsulate errors with more detail
	 * than javax.persistence.UnauthorizedException.
	 *
	 * @param ex the UnauthorizedException Object
	 * @return the ApiError Object
	 */
	@ExceptionHandler(UnauthorizedException.class)
	protected ResponseEntity<Object> handleUnauthorizedException(UnauthorizedException ex) {

		ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED);
		apiError.setMessage(ex.getMessage());

		return buildResponseEntity(apiError);
	}
	
	/**
	 * Handles IllegalArgumentException. Created to encapsulate errors with more detail
	 * than javax.persistence.UnauthorizedException.
	 *
	 * @param ex the IllegalArgumentException Object
	 * @return the ApiError Object
	 */
	@ExceptionHandler(IllegalArgumentException.class)
	protected ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {

		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
		apiError.setMessage(ex.getMessage());

		return buildResponseEntity(apiError);
	}
	
	
	/**
	 * Handles IllegalArgumentException. Created to encapsulate errors with more detail
	 * than javax.persistence.UnauthorizedException.
	 *
	 * @param ex the IllegalArgumentException Object
	 * @return the ApiError Object
	 */
	@ExceptionHandler(ExternalServiceException.class)
	protected ResponseEntity<Object> handleExternalServiceError(ExternalServiceException ex) {

		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
		apiError.setMessage(ex.getMessage());

		return buildResponseEntity(apiError);
	}
	
	/**
	 * Handler AuthenticationException. Created to encapsulate errors with more detail
	 * than org.springframework.security.core.AuthenticationException.
	 * 
	 * @param ex AuthenticationException Object
	 * @return the ApiError Object
	 */
	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex) {

		ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED);
		apiError.setMessage(ex.getMessage());

		return buildResponseEntity(apiError);
	}
	
	/**
	 * BuildResponseEntity
	 *
	 * @param apiError ApiError Object
	 * @return ResponseEntity ResponseEntity Object
	 */
	private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
		return new ResponseEntity<>(apiError, apiError.getStatus());
	}
	
	
	/**
	 * Handler SqlException.
	 * 
	 * @param ex AuthenticationException Object
	 * @return the ApiError Object
	 */
	@ExceptionHandler(SqlException.class)
	public ResponseEntity<Object> handleSqlException(SqlException ex) {

		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
		apiError.setMessage(ex.getMessage());

		return buildResponseEntity(apiError);
	}
	
	public static String getSimpleName(MethodArgumentTypeMismatchException ex) {
		Class<?> genericClass = ex.getRequiredType();
		if (Objects.nonNull(genericClass)) {
			return genericClass.getSimpleName();
		}
		return Strings.EMPTY;
	}

	/**
	 * Handles IllegalArgumentException. Created to encapsulate errors with more detail
	 * than javax.persistence.UnauthorizedException.
	 *
	 * @param ex the IllegalArgumentException Object
	 * @return the ApiError Object
	 */
	@ExceptionHandler(RecordException.class)
	protected ResponseEntity<Object> handleRecordException(RecordException ex) {

		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR);
		apiError.setMessage(ex.getMessage());

		return buildResponseEntity(apiError);
	}
}
