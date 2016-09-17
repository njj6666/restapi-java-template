package com.hp.es.cto.sp.rest;

import javax.ws.rs.core.Response;

import org.jboss.resteasy.spi.BadRequestException;
import org.jboss.resteasy.spi.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * REST resource base class for defining common fields and methods.
 * 
 * @author Pinghua
 */
@SuppressWarnings("deprecation")
public class SPResource {

	private static final Logger logger = LoggerFactory.getLogger(SPResource.class);

	protected static final String TYPE = "type";
	protected static final String CONTENT_TYPE = "Content-Type";

	protected static final String SIZE = "size";
	protected static final String LIMIT = "limit";
	protected static final String OFFSET = "offset";

	// define the default limit and offset
	protected static final int DEFAULT_LIMIT = 100;
	protected static final int ZERO_LIMIT = 0;
	protected static final int DEFAULT_OFFSET = 0;

	/**
	 * Returns a success rest response.
	 * 
	 * @return
	 *         a success rest response
	 */
	protected Response ok() {
		return Response.ok().build();
	}

	/**
	 * Logs and throws a bad request exception.
	 * 
	 * @param msg
	 *            exception message
	 */
	protected void throwBadRequest(String msg) {
		logger.error(msg);
		throw new BadRequestException(msg);
	}

	/**
	 * Logs and throws a not found exception.
	 * 
	 * @param msg
	 *            exception message
	 */
	protected void throwNotFound(String msg) {
		logger.error(msg);
		throw new NotFoundException(msg);
	}

}
