package com.record.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * EntityNotFoundException Class
 */
public class SqlException extends Exception {

	private static final long serialVersionUID = -7374235275003296287L;

	private final transient Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * EntityNotFoundException Method
	 *
	 * @param message String value
	 */
	public SqlException(String message) {
		super(message);

		log.debug(" EntityNotFoundException{}", message);
	}
}
