package com.record.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * UnauthorizedException Class
 */
public class UnauthorizedException extends Exception {

	private static final long serialVersionUID = 6353900531222752213L;

	private final transient Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * UnauthorizedException Method
	 *
	 * @param message String value
	 */
	public UnauthorizedException(String message) {
		super(message);

		log.debug(" UnauthorizedException{}", message);
	}

    
}
