package com.record.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ServiceUnavailableException Class
 */
public class ExternalServiceException extends Exception {

	private static final long serialVersionUID = 3334261301168302623L;

	private final transient Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * ServiceUnavailableException Method
	 *
	 * @param message String value
	 */
	public ExternalServiceException(String message) {

		super(message);

		log.debug(" ExternalServiceException{}", message);
	}
}
