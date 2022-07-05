package com.record.controller;

import static com.record.util.Constants.BUSINESS;
import static com.record.util.Constants.BUSINESSS_ID;
import static com.record.util.Constants.BUSINESS_ID;
import static com.record.util.Constants.RECORD_ADMIN_USER_TOKEN;

import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.record.entity.BusinessEntityOp;
import com.record.exception.BadRequestException;
import com.record.model.BusinessEntityEnum;
import com.record.model.BusinessModel;
import com.record.service.BusinessService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

@RestController
@RequestMapping(BUSINESS)
@Api(value = "Business Entity Controller", tags = { "business" })
public class EntityController {
	@Autowired
	private BusinessService businessService;

	@ApiOperation(value = "Create business entity in system", nickname = "Create Business Entity", notes = "Create business entity for Record Admin", response = BusinessModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "business" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = BusinessModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@PostMapping
	public ResponseEntity<BusinessModel> create(
			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken,
			@ApiParam(value = "Create lob", required = true) @Valid @RequestBody BusinessModel business)
			throws BadRequestException { 
		return new ResponseEntity<>(businessService.createBusinessEntity(business), HttpStatus.CREATED);
	}
	
	
	@ApiOperation(value = "Update Business Entity", nickname = "updateBusinessEntity", notes = "Update Business Entity for  Record Admin", response = BusinessModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "business" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = BusinessModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@PutMapping(BUSINESS_ID)
	public ResponseEntity<BusinessModel> update(
			@ApiParam(value = "Identifier of Role to be updated") @PathVariable(BUSINESSS_ID) String entityId,
			@ApiParam(value = "Request User information", required = true) @Valid @RequestBody BusinessModel businessReq,
			@ApiParam(value = "Role Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken)
			throws EntityNotFoundException {
		return new ResponseEntity<>(businessService.updateBusniessEntity(entityId, businessReq), HttpStatus.OK);
	}
	
	
	@ApiOperation(value = "Retrieve Business Entity", nickname = "Retrieve Business Entity ", notes = "Retrieve Business Entity from Record Admin", response = BusinessModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "business" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = BusinessModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@GetMapping
	public ResponseEntity<List<BusinessEntityOp>> getbusinesss( @RequestParam("key") BusinessEntityEnum key,@RequestParam("value") Object valueReq,
			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken) 
	{
		return new ResponseEntity<>(businessService.getAllBusinessEntity(key,valueReq), HttpStatus.OK);
	}
	
	
	
	
	
}