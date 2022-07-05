package com.record.controller;

import static com.record.util.Constants.ACTION;
import static com.record.util.Constants.ACTIONS_ID;
import static com.record.util.Constants.ACTION_ID;
import static com.record.util.Constants.ACTIONS_CODE;
import static com.record.util.Constants.ACTION_CODE;


import static com.record.util.Constants.ROLES_CODE;
import static com.record.util.Constants.ROLE_CODE;

import static com.record.util.Constants.RECORD_ADMIN_USER_TOKEN;

import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import com.record.model.ActionModel;
import com.record.model.ActionResponseModel;
import com.record.model.RoleModel;
import com.record.service.ActionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

@RestController
@RequestMapping(ACTION)
@Api(value = "Action Controller", tags = { "action" })
public class ActionController {

	@Autowired
	public ActionService actionService;

	@ApiOperation(value = "Create action in system", nickname = "CreateAction", notes = "Create action for Record Admin", response = ActionModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "action" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = ActionModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@PostMapping
	public ResponseEntity<ActionModel> create(
			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken,
			@ApiParam(value = "Create role", required = true) @Valid @RequestBody ActionModel action)
			throws BadRequestException {
		return new ResponseEntity<>(actionService.createAction(action), HttpStatus.CREATED);
	}
	
	@ApiOperation(value = "Update Action", nickname = "updateAction", notes = "Update Action for  Record Admin", response = ActionModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "action" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = ActionModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@PutMapping(ACTION_ID)
	public ResponseEntity<ActionModel> update(
			@ApiParam(value = "Identifier of Role to be updated") @PathVariable(ACTIONS_ID) Long actionId,
			@ApiParam(value = "Request User information", required = true) @Valid @RequestBody ActionModel action,
			@ApiParam(value = "Role Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken)
			throws EntityNotFoundException, BadRequestException {
		return new ResponseEntity<>(actionService.updateAction(actionId, action), HttpStatus.OK);
	}
	
	@ApiOperation(value = "Delete Action", nickname = "DeleteAction ", notes = "Delete Action from Record Admin", response = ActionModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "action" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = ActionModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@DeleteMapping(ACTION_ID)
	public ResponseEntity<Void> deleteAction(
			@ApiParam(value = "Identifier of User to be delete") @PathVariable(ACTIONS_ID) Long actionId,
			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken) {
		actionService.deleteAction(actionId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@ApiOperation(value = "Retrieve All Actions ", nickname = "Retrieve All Actions ", notes = "Retrieve Actions from Record Admin", response = ActionModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "action" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = ActionModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@GetMapping
	public ResponseEntity<List<ActionModel>> getActions(
			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken) {
		return new ResponseEntity<>(actionService.getActions(), HttpStatus.OK);
	}
	
	@ApiOperation(value = "Retrieve Actions by action code", nickname = "Retrieve Actions by action code", notes = "Retrieve Actions from Record Admin", response = ActionModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "action" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = ActionModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@GetMapping(ACTION_CODE)
	public ResponseEntity<ActionModel> getActionByCode(
			@ApiParam(value = "Identifier of Lead to be updated") @PathVariable(ACTIONS_CODE) String roleCode,
			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken)
			throws EntityNotFoundException {
		return new ResponseEntity<>(actionService.findByActionCode(roleCode), HttpStatus.OK);
	}
	
	@ApiOperation(value = "Retrieve Actions by role code", nickname = "Retrieve Actions by role code", notes = "Retrieve Actions from Record Admin", response = ActionModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "action" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = ActionModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@GetMapping(ROLE_CODE)
	public ResponseEntity<List<ActionModel>> getActionByRoleCode(
			@ApiParam(value = "roleCode Example R1") @PathVariable(ROLES_CODE) String roleCode,
			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken)
			throws EntityNotFoundException {
		return new ResponseEntity<>(actionService.findActionByRole(roleCode), HttpStatus.OK);
	}
	
	@ApiOperation(value = "Retrieve Actions by status", nickname = "Retrieve Actions by status", notes = "Retrieve Actions from Record Admin", response = ActionModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "action" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = ActionModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@GetMapping("/byStatus/{status}")
	public ResponseEntity<List<ActionModel>> getActionByStatus(
			@ApiParam(value = "Identifier of Lead to be updated") @PathVariable("status") String status,
			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken)
			throws EntityNotFoundException {
		return new ResponseEntity<>(actionService.getAllActions(status), HttpStatus.OK);
	}
	


}
