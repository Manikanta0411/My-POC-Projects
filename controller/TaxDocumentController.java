package com.record.controller;

import static com.record.util.Constants.RECORD_ADMIN_USER_TOKEN;
import static com.record.util.Constants.TAXDOCS_ID;
import static com.record.util.Constants.TAXDOCUMENT;
import static com.record.util.Constants.TAXDOC_ID;

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
import com.record.model.TaxDocumentModel;
import com.record.service.TaxDocumentService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

@RestController
@RequestMapping(TAXDOCUMENT)
@Api(value = "Tax Document Controller", tags = { "tax document" })
public class TaxDocumentController {

	@Autowired
	public TaxDocumentService taxDocumentService;

	@ApiOperation(value = "Create Tax Document in system", nickname = "CreateTaxDocument", notes = "Create TaxDocument for Record Admin", response = TaxDocumentModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "tax document" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = TaxDocumentModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@PostMapping
	public ResponseEntity<TaxDocumentModel> create(
			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken,
			@ApiParam(value = "Create role", required = true) @Valid @RequestBody TaxDocumentModel req)
			throws BadRequestException {
		return new ResponseEntity<>(taxDocumentService.createTaxDocument(req), HttpStatus.CREATED);
	}
	
	
	@ApiOperation(value = "Update Tax Document", nickname = "updateAction", notes = "Update Tax Document for  Record Admin", response = TaxDocumentModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "tax document" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = TaxDocumentModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@PutMapping(TAXDOC_ID)
	public ResponseEntity<TaxDocumentModel> update(
			@ApiParam(value = "Identifier of Role to be updated") @PathVariable(TAXDOCS_ID) Long id,
			@ApiParam(value = "Request User information", required = true) @Valid @RequestBody TaxDocumentModel req,
			@ApiParam(value = "Role Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken)
			throws EntityNotFoundException {
		return new ResponseEntity<>(taxDocumentService.updateTaxDoc(id, req), HttpStatus.OK);
	}
	
	
	@ApiOperation(value = "Delete tax document", nickname = "DeleteAction ", notes = "Delete Action from Record Admin", response = TaxDocumentModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "tax document" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = TaxDocumentModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@DeleteMapping(TAXDOC_ID)
	public ResponseEntity<String> deleteTaxDoc(
			@ApiParam(value = "Identifier of User to be delete") @PathVariable(TAXDOCS_ID) Long id,
			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken) {
		taxDocumentService.deleteTaxDoc(id);
		return ResponseEntity.ok("Success");
	}
	
	@ApiOperation(value = "Retrieve All tax documents", nickname = "Retrieve Tax Documents ", notes = "Retrieve Tax Documents from Record Admin", response = TaxDocumentModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "tax document" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = TaxDocumentModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@GetMapping
	public ResponseEntity<List<TaxDocumentModel>> getAllTaxDocs(
			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken) {
		return new ResponseEntity<>(taxDocumentService.getAllTaxDocs(), HttpStatus.OK);
	}
	
	@ApiOperation(value = "Retrieve tax document by id", nickname = "Retrieve Actions ", notes = "Retrieve Actions from Record Admin", response = TaxDocumentModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "tax document" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = TaxDocumentModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@GetMapping(TAXDOC_ID)
	public ResponseEntity<TaxDocumentModel> getTaxDocById(
			@ApiParam(value = "Identifier of Lead to be updated") @PathVariable(TAXDOCS_ID) Long id,
			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken)
			throws EntityNotFoundException {
		return new ResponseEntity<>(taxDocumentService.findByTaxDocId(id), HttpStatus.OK);
	}
	
	
	@ApiOperation(value = "Retrieve tax document by Entity id", nickname = "Retrieve tax document by entity id ", notes = "Retrieve tax document from Record Admin", response = TaxDocumentModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "tax document" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = TaxDocumentModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@GetMapping("/entityId/{entityId}")
	public ResponseEntity<List<TaxDocumentModel>> getTaxDocByEntityId(
			@ApiParam(value = "Identifier of Lead to be updated") @PathVariable("entityId") String entityId,
			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken)
			throws EntityNotFoundException {
		return new ResponseEntity<>(taxDocumentService.findByTaxDocByEntityId(entityId), HttpStatus.OK);
	}
	
	
	
}
