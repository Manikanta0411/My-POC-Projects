package com.record.exception;

public class BadRequestException extends Exception {

	private static final long serialVersionUID = 3334261301168302623L;

	/**
	 * BadRequestException Method
	 *
	 * @param message String value
	 */
	public BadRequestException(String message) {
		super(message);
	}
}
