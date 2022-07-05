package com.record.controller;

import static com.record.util.Constants.RECORD_ADMIN_USER_TOKEN;
import static com.record.util.Constants.USEREMIL_ID;
import static com.record.util.Constants.USERS;
import static com.record.util.Constants.USERSEMIL_ID;
import static com.record.util.Constants.USERS_ID;
import static com.record.util.Constants.USER_ID;

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
import com.record.model.UserModel;
import com.record.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

@RestController
@RequestMapping(USERS)
@Api(value = "User Controller", tags = { "user" })
public class UserController {

	@Autowired
	public UserService userService;
	
	@ApiOperation(value = "Create user in system", nickname = "CreateUser", notes = "Create user for Record Admin", response = UserModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "user" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = UserModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@PostMapping
	public ResponseEntity<UserModel> create(
			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken,
			@ApiParam(value = "Create user", required = true) @Valid @RequestBody UserModel user)
			throws BadRequestException {
		return new ResponseEntity<>(userService.createUser(user), HttpStatus.CREATED);
	}
	
	
	@ApiOperation(value = "Update User", nickname = "updateUser", notes = "Update User for  Record Admin", response = UserModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "user" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = UserModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@PutMapping(USER_ID)
	public ResponseEntity<UserModel> update(
			@ApiParam(value = "Identifier of User to be updated") @PathVariable(USERS_ID) Long userId,
			@ApiParam(value = "Request User information", required = true) @Valid @RequestBody UserModel user,
			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken)
			throws EntityNotFoundException, BadRequestException {
		return new ResponseEntity<>(userService.updateUser(userId, user), HttpStatus.OK);
	}
	
	@ApiOperation(value = "Delete User", nickname = "Delete User ", notes = "Delete user from Record Admin", response = UserModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "user" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = UserModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@DeleteMapping(USER_ID)
	public ResponseEntity<Void> deleteUser(
			@ApiParam(value = "Identifier of User to be delete") @PathVariable(USERS_ID) Long userId,
			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken)
			{
		userService.deleteUser(userId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@ApiOperation(value = "Retrieve Users by status", nickname = "Retrieve Users by status", notes = "Retrieve Users from Record Admin", response = UserModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "user" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = UserModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@GetMapping("/byStatus/{status}")
	public ResponseEntity<List<UserModel>> getUsers(
			@ApiParam(value = "User Token issued during login call") @PathVariable("status") String status,@RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken) {
		return new ResponseEntity<>(userService.getUsers(status), HttpStatus.OK);
	}
	
	@ApiOperation(value = "Retrieve Users by user Id", nickname = "Retrieve Users by user Id ", notes = "Retrieve Users from Record Admin", response = UserModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "user" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = UserModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@GetMapping(USER_ID)
	public ResponseEntity<UserModel> getUser(
			@ApiParam(value = "Identifier of Lead to be updated") @PathVariable(USERS_ID) Long userId,
			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken)
			throws EntityNotFoundException {
		return new ResponseEntity<>(userService.findByUserId(userId), HttpStatus.OK);
	}
	
	@ApiOperation(value = "Retrieve Users by user email ", nickname = "Retrieve Users by email", notes = "Retrieve Users from Record Admin", response = UserModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "user" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = UserModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@GetMapping(USEREMIL_ID)
	public ResponseEntity<UserModel> getUserByEmail(
			@ApiParam(value = "Identifier of Lead to be updated") @PathVariable(USERSEMIL_ID) String email,
			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken)
			throws EntityNotFoundException {
		return new ResponseEntity<>(userService.getUserByEmail(email), HttpStatus.OK);
	}



	
}
