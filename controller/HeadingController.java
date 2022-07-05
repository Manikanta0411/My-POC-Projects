package com.record.controller;

import static com.record.util.Constants.HEADING;
import static com.record.util.Constants.RECORD_ADMIN_USER_TOKEN;

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
import com.record.model.HeadingModel;
import com.record.repository.HeadingRepository;
import com.record.service.HeadingService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

@RestController
@RequestMapping(HEADING)
@Api(value = "Heading Controller", tags = { "heading" })
public class HeadingController {

	@Autowired
	public HeadingService headingService;
	
	@Autowired
	public HeadingRepository headingRepository;
	
	@ApiOperation(value = "Create heading in system", nickname = "CreateHeading", notes = "Create heading for Record Admin", response = HeadingModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "heading" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = HeadingModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@PostMapping
	public ResponseEntity<HeadingModel> createHeading(
			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken,
			@ApiParam(value = "Create role", required = true) @Valid @RequestBody HeadingModel headingModel)
			throws BadRequestException {
		return new ResponseEntity<>(headingService.createHeading(headingModel), HttpStatus.CREATED);
	}
	
	@ApiOperation(value = "Retrieve heading in system", nickname = "RetrieveHeading", notes = "Retrieve heading for Record Admin", response = HeadingModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "heading" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = HeadingModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@GetMapping("/{headingId}")
	public ResponseEntity<HeadingModel> getHeading(
			@ApiParam(value = "Identifier of Lead to be updated")@PathVariable("headingId") Long headingId,
			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken)
			throws EntityNotFoundException {
		HeadingModel headingModel = headingService.findByHeadingId(headingId);
		return new ResponseEntity<>(headingModel, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Retrieve heading in system", nickname = "RetrieveHeading", notes = "Retrieve heading for Record Admin", response = HeadingModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "heading" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = HeadingModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@GetMapping("byHeadingCode/{headingCode}")
	public ResponseEntity<HeadingModel> getHeadingByCode(
			@ApiParam(value = "Identifier of Lead to be updated")@PathVariable("headingCode") String headingCode,
			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken)
			throws EntityNotFoundException {
		HeadingModel headingModel = headingService.findByHeadingCode(headingCode);
		return new ResponseEntity<>(headingModel, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Update heading in system", nickname = "UpdateHeading", notes = "Update heading for Record Admin", response = HeadingModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "heading" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = HeadingModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@PutMapping("/{headingId}")
	public ResponseEntity<HeadingModel> updateHeading(
			@ApiParam(value = "Identifier of Lead to be updated")@PathVariable("headingId") Long headingId,
			@ApiParam(value = "User Token issued during login call")@Valid @RequestBody HeadingModel headingModel, @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken)
			throws EntityNotFoundException, BadRequestException {
		return new ResponseEntity<>(headingService.updateHeading(headingId, headingModel), HttpStatus.OK);
	}
	
	@ApiOperation(value = "Update heading in system by heading code", nickname = "UpdateHeadingByCode", notes = "Update heading for Record Admin", response = HeadingModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "heading" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = HeadingModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@PutMapping("byHeadingCode/{headingCode}")
	public ResponseEntity<HeadingModel> updateHeadingByCode(
			@ApiParam(value = "Identifier of Lead to be updated")@PathVariable("headingCode") String headingCode,
			@ApiParam(value = "User Token issued during login call")@Valid @RequestBody HeadingModel headingModel, @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken)
			throws EntityNotFoundException, BadRequestException {
		return new ResponseEntity<>(headingService.updateHeadingByCode(headingCode, headingModel), HttpStatus.OK);
	}
	
	@ApiOperation(value = "Delete heading in system", nickname = "DeleteHeading", notes = "Delete heading for Record Admin", response = HeadingModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "heading" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = HeadingModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@DeleteMapping("/{headingId}")
	public ResponseEntity<Void> deleteRepository(
			@ApiParam(value = "Identifier of User to be delete") @PathVariable("headingId") Long headingId,
			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken) {
		headingService.deleteHeading(headingId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@ApiOperation(value = "Delete heading in system", nickname = "DeleteHeading", notes = "Delete heading for Record Admin", response = HeadingModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "heading" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = HeadingModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@DeleteMapping("byHeadingCode/{headingCode}")
	public ResponseEntity<Void> deleteRepositoryByCode(
			@ApiParam(value = "Identifier of User to be delete") @PathVariable("headingCode") String headingCode,
			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken) {
		headingService.deleteHeadingByCode(headingCode);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
