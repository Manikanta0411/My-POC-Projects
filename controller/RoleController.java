package com.record.controller;

import static com.record.util.Constants.RECORD_ADMIN_USER_TOKEN;
import static com.record.util.Constants.ROLES;
import static com.record.util.Constants.ROLES_ID;
import static com.record.util.Constants.ROLE_ID;
import static com.record.util.Constants.ROLE_CODE;
import static com.record.util.Constants.ROLES_CODE;
import static com.record.util.Constants.ROLE_BY_USER;
import static com.record.util.Constants.ROLE_USER;

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
import com.record.model.RoleModel;
import com.record.service.RoleService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

@RestController
@RequestMapping(ROLES)
@Api(value = "Role Controller", tags = { "role" })
public class RoleController {

	@Autowired
	public RoleService roleService;

	@ApiOperation(value = "Create role in system", nickname = "CreateRole", notes = "Create role for Record Admin", response = RoleModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "role" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = RoleModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@PostMapping
	public ResponseEntity<RoleModel> create(
			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken,
			@ApiParam(value = "Create role", required = true) @Valid @RequestBody RoleModel role)
			throws BadRequestException {
		return new ResponseEntity<>(roleService.createRole(role), HttpStatus.CREATED);
	}

	@ApiOperation(value = "Update Role", nickname = "updateRole", notes = "Update Role for  Record Admin", response = RoleModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "role" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = RoleModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@PutMapping(ROLE_ID)
	public ResponseEntity<RoleModel> update(
			@ApiParam(value = "Identifier of Role to be updated") @PathVariable(ROLES_ID) Long roleId,
			@ApiParam(value = "Request User information", required = true) @Valid @RequestBody RoleModel role,
			@ApiParam(value = "Role Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken)
			throws EntityNotFoundException {
		return new ResponseEntity<>(roleService.updateRole(roleId, role), HttpStatus.OK);
	}

	@ApiOperation(value = "Delete Role", nickname = "DeleteRole ", notes = "Delete role from Record Admin", response = RoleModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "role" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = RoleModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@DeleteMapping(ROLE_ID)
	public ResponseEntity<Void> deleteRole(
			@ApiParam(value = "Identifier of User to be delete") @PathVariable(ROLES_ID) Long roleId,
			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken) {
		roleService.deleteRole(roleId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@ApiOperation(value = "Retrieve all Roles by status", nickname = "Retrieve roles by status", notes = "Retrieve Roles from Record Admin", response = RoleModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "role" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = RoleModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@GetMapping("/byStatus/{status}")
	public ResponseEntity<List<RoleModel>> getRoles(
			@ApiParam(value = "User Token issued during login call") @PathVariable("status") String status,
			@RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken) {
		return new ResponseEntity<>(roleService.getRoles(status), HttpStatus.OK);
	}

	@ApiOperation(value = "Retrieve Roles by role id", nickname = "Retrieve role by role id ", notes = "Retrieve Roles from Record Admin", response = RoleModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "role" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = RoleModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@GetMapping(ROLE_ID)
	public ResponseEntity<RoleModel> getRole(
			@ApiParam(value = "Identifier of Lead to be updated") @PathVariable(ROLES_ID) Long roleId,
			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken)
			throws EntityNotFoundException {
		return new ResponseEntity<>(roleService.findByRoleId(roleId), HttpStatus.OK);
	}

	@ApiOperation(value = "Retrieve Roles by role code", nickname = "Retrieve role by role code", notes = "Retrieve Roles from Record Admin", response = RoleModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "role" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = RoleModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@GetMapping(ROLE_CODE)
	public ResponseEntity<RoleModel> getRoleByCode(
			@ApiParam(value = "Identifier of Lead to be updated") @PathVariable(ROLES_CODE) String roleCode,
			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken)
			throws EntityNotFoundException {
		return new ResponseEntity<>(roleService.findByRoleCode(roleCode), HttpStatus.OK);
	}

	/*
	 * @GetMapping(ROLE_BY_USER) public ResponseEntity<List<RoleModel>>
	 * getRoleByUser(
	 * 
	 * @ApiParam(value =
	 * "Identifier of Lead to be updated") @PathVariable(ROLE_USER) Long userId,
	 * 
	 * @ApiParam(value = "User Token issued during login call") @RequestHeader(value
	 * = RECORD_ADMIN_USER_TOKEN) String UserToken) throws EntityNotFoundException {
	 * return new ResponseEntity<>(roleService.findRoleByUser(userId),
	 * HttpStatus.OK); }
	 */
}
