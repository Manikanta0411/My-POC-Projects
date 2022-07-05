package com.record.controller;

import static com.record.util.Constants.ENTITYS_ID;
import static com.record.util.Constants.ENTITY_ID;
import static com.record.util.Constants.RECORD_ADMIN_USER_TOKEN;
import static com.record.util.Constants.TAX;
import static com.record.util.Constants.USEREMIL_ID;
import static com.record.util.Constants.USERSEMIL_ID;
import static com.record.util.Constants.USERS_ID;

import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.record.exception.BadRequestException;
import com.record.model.TaxModel;
import com.record.model.UserModel;
import com.record.service.TaxService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

@RestController
@RequestMapping(TAX)
@Api(value = "Tax Controller", tags = { "tax" })
public class TaxController {
	@Autowired
	private TaxService taxService;

	@ApiOperation(value = "Create Tax entity in system", nickname = "CreateTax", notes = "Create Tax entity for Record Admin", response = TaxModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "tax" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = TaxModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@PostMapping
	public ResponseEntity<TaxModel> create(
			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken,
			@ApiParam(value = "Create lob", required = true) @Valid @RequestBody TaxModel calReq)
			throws BadRequestException { 
		return new ResponseEntity<>(taxService.createTax(calReq), HttpStatus.CREATED);
	}
	
	/*
	
	@ApiOperation(value = "Update Business Entity", nickname = "updateBusinessEntity", notes = "Update Business Entity for  Record Admin", response = BusinessModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "business" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = BusinessModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@PutMapping(BUSINESS_ID)
	public ResponseEntity<BusinessModel> update(
			@ApiParam(value = "Identifier of Role to be updated") @PathVariable(BUSINESSS_ID) Long id,
			@ApiParam(value = "Request User information", required = true) @Valid @RequestBody BusinessModel businessReq,
			@ApiParam(value = "Role Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken)
			throws EntityNotFoundException {
		return new ResponseEntity<>(businessService.updateBusniessEntity(id, businessReq), HttpStatus.OK);
	}
	
	*/
	@ApiOperation(value = "retrieve Tax entity in system", nickname = "RetrieveTax", notes = "Retrieve Tax entity for Record Admin", response = TaxModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "tax" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = TaxModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@GetMapping(ENTITY_ID)
	public ResponseEntity<List<TaxModel>> getUserByEmail(
			@ApiParam(value = "Identifier of Lead to be updated") @PathVariable(ENTITYS_ID) String entityCode,
			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken)
			throws EntityNotFoundException {
		return new ResponseEntity<>(taxService.getAllTax(entityCode), HttpStatus.OK);
	}
	
	@ApiOperation(value = "	Update Tax entity in system", nickname = "UpdateTax", notes = "Update Tax entity for Record Admin", response = TaxModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "tax" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = TaxModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@PutMapping("/{taxCode}")
	public ResponseEntity<TaxModel> update(
			@PathVariable("taxCode")String taxCode,
			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken,
			@ApiParam(value = "Create lob", required = true) @Valid @RequestBody TaxModel model)
			throws BadRequestException {
		
		return new ResponseEntity<>(taxService.updateTaxEntity(taxCode, model), HttpStatus.OK);
	}

	@ApiOperation(value = "To change Tax entity status in system", nickname = "UpdateTaxStatus", notes = "change Tax entity status for Record Admin", response = TaxModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "tax" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = TaxModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@DeleteMapping("/{taxCode}")
	public ResponseEntity<Void> delete(
			@PathVariable("taxCode")String taxCode,
			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken)
			throws BadRequestException {
		taxService.deleteTax(taxCode);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	
}
