package com.record.controller;

import static com.record.util.Constants.CALCULATION;
import static com.record.util.Constants.RECORD_ADMIN_USER_TOKEN;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.record.entity.CalculationEntity;
import com.record.exception.BadRequestException;
import com.record.mapper.CalculationMapper;
import com.record.model.CalculationModel;
import com.record.service.CalculationService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

@RestController
@RequestMapping(CALCULATION)
@Api(value = "Calculation Controller", tags = { "calculation" })
public class CalculationController {
	@Autowired
	private CalculationService calculationService;

	@Autowired
	private CalculationMapper calculationMapper;

	@ApiOperation(value = "Create calculation entity in system", nickname = "CreateCalculation", notes = "Create calculation entity for Record Admin", response = CalculationModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "calculation" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = CalculationModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@PostMapping
	public ResponseEntity<CalculationModel> create(
			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken,
			@ApiParam(value = "Create lob", required = true) @Valid @RequestBody CalculationModel calReq)
			throws BadRequestException {
		return new ResponseEntity<>(calculationService.createCalculation(calReq), HttpStatus.CREATED);
	}

	/*
	 * 
	 * @ApiOperation(value = "Update Business Entity", nickname =
	 * "updateBusinessEntity", notes = "Update Business Entity for  Record Admin",
	 * response = BusinessModel.class, authorizations = {
	 * 
	 * @Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = {
	 * "business" })
	 * 
	 * @ApiResponses({
	 * 
	 * @ApiResponse(code = 200, message =
	 * "OK in case there is no technical or validation errors", response =
	 * BusinessModel.class),
	 * 
	 * @ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
	 * 
	 * @ApiResponse(code = 401, message =
	 * "Un Authorised user see error object for details", response = Error.class),
	 * 
	 * @ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	 * 
	 * @PutMapping(BUSINESS_ID) public ResponseEntity<BusinessModel> update(
	 * 
	 * @ApiParam(value =
	 * "Identifier of Role to be updated") @PathVariable(BUSINESSS_ID) Long id,
	 * 
	 * @ApiParam(value = "Request User information", required =
	 * true) @Valid @RequestBody BusinessModel businessReq,
	 * 
	 * @ApiParam(value = "Role Token issued during login call") @RequestHeader(value
	 * = RECORD_ADMIN_USER_TOKEN) String UserToken) throws EntityNotFoundException {
	 * return new ResponseEntity<>(businessService.updateBusniessEntity(id,
	 * businessReq), HttpStatus.OK); }
	 * 
	 * 
	 * @ApiOperation(value = "Retrieve Business Entity", nickname =
	 * "Retrieve Business Entity ", notes =
	 * "Retrieve Business Entity from Record Admin", response = BusinessModel.class,
	 * authorizations = {
	 * 
	 * @Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = {
	 * "business" })
	 * 
	 * @ApiResponses({
	 * 
	 * @ApiResponse(code = 200, message =
	 * "OK in case there is no technical or validation errors", response =
	 * BusinessModel.class),
	 * 
	 * @ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
	 * 
	 * @ApiResponse(code = 401, message =
	 * "Un Authorised user see error object for details", response = Error.class),
	 * 
	 * @ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	 * 
	 * @GetMapping public ResponseEntity<List<BusinessEntity>>
	 * getbusinesss( @RequestParam("key") BusinessEntityEnum
	 * key,@RequestParam("value") Object valueReq,
	 * 
	 * @ApiParam(value = "User Token issued during login call") @RequestHeader(value
	 * = RECORD_ADMIN_USER_TOKEN) String UserToken) { return new
	 * ResponseEntity<>(businessService.getAllBusinessEntity(key,valueReq),
	 * HttpStatus.OK); }
	 * 
	 */

	@ApiOperation(value = "Retrieve calculation entity in system", nickname = "RetrieveCalculation", notes = "Retrieve calculation entity for Record Admin", response = CalculationModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "calculation" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = CalculationModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@GetMapping("/calculation/{calculationCode}")
	public ResponseEntity<CalculationModel> getCalculation(@PathVariable("calculationCode") String calculationCode,
			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken)
			throws EntityNotFoundException {
		CalculationEntity calculationEntity = calculationService.findByCode(calculationCode);
		CalculationModel calculationModel = calculationMapper.mapToModel(calculationEntity);
		return ResponseEntity.ok(calculationModel);

	}

	@ApiOperation(value = "Update calculation entity in system", nickname = "UpdateCalculation", notes = "Update calculation entity for Record Admin", response = CalculationModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "calculation" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = CalculationModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@PutMapping("/calculation/{calculationCode}")
	public ResponseEntity<CalculationModel> update(@PathVariable("calculationCode") String calculationCode,
			@Valid @RequestBody CalculationModel calculationModel,
			@ApiParam(value = "Role Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken) {

		return new ResponseEntity<>(calculationService.updateCalculation(calculationCode, calculationModel),
				HttpStatus.OK);

	}

}
