package com.record.controller;

import static com.record.util.Constants.RECORD_ADMIN_USER_TOKEN;
import static com.record.util.Constants.REPOSITORY;

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
import com.record.model.RepositoryModel;
import com.record.model.RoleModel;
import com.record.service.RepositoryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

@RestController
@RequestMapping(REPOSITORY)
@Api(value = "Repository Controller", tags = { "repository" })
public class RepositoryController {

	@Autowired
	public RepositoryService repositoryService;
	
	@ApiOperation(value = "Create repository in system", nickname = "CreateRepository", notes = "Create repository for Record Admin", response = RepositoryModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "repository" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = RoleModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@PostMapping
	public ResponseEntity<RepositoryModel> create(
			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken,
			@ApiParam(value = "Create role", required = true) @Valid @RequestBody RepositoryModel repo)
			throws BadRequestException {
		return new ResponseEntity<>(repositoryService.createRepository(repo), HttpStatus.CREATED);
	}
	
	@ApiOperation(value = "Retrieve Repository by repository id", nickname = "Retrieve Repository by repository id", notes = "Retrieve Repository from Record Admin", response = RepositoryModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "repository" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = RoleModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@GetMapping("/{repositoryId}")
	public ResponseEntity<RepositoryModel> getRepository(
			@ApiParam(value = "Identifier of Lead to be updated") @PathVariable("repositoryId") Long repositoryId,
			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken)
			throws EntityNotFoundException {
		return new ResponseEntity<>(repositoryService.findByRepoId(repositoryId), HttpStatus.OK);
	}
	
	@ApiOperation(value = "Update Repository by repository id", nickname = "Update Repository by repository id", notes = "Update Repository from Record Admin", response = RepositoryModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "repository" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = RoleModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@PutMapping("/{repositoryId}")
	public ResponseEntity<RepositoryModel> update(
			@ApiParam(value = "Identifier of Role to be updated") @PathVariable("repositoryId") Long repoId,
			@ApiParam(value = "Request User information", required = true) @Valid @RequestBody RepositoryModel repoModel,
			@ApiParam(value = "Role Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken)
			throws EntityNotFoundException {
		return new ResponseEntity<>(repositoryService.updateRepository(repoId, repoModel), HttpStatus.OK);
	}
	
	@ApiOperation(value = "Delete Repository by repository id", nickname = "Delete Repository by repository id", notes = "Delete Repository from Record Admin", response = RepositoryModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "repository" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = RoleModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@DeleteMapping("/{repositoryId}")
	public ResponseEntity<Void> deleteRepository(
			@ApiParam(value = "Identifier of User to be delete") @PathVariable("repositoryId") Long repoId,
			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken) {
		repositoryService.deleteRepository(repoId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@ApiOperation(value = "Delete Repository by repository id", nickname = "Delete Repository by repository id", notes = "Delete Repository from Record Admin", response = RepositoryModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "repository" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = RoleModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@DeleteMapping("byRepositoryCode/{repositoryCode}")
	public ResponseEntity<Void> deleteRepositoryByCode(
			@ApiParam(value = "Identifier of User to be delete") @PathVariable("repositoryCode") String repositoryCode,
			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken) {
		repositoryService.deleteRepositoryByCode(repositoryCode);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	
	@ApiOperation(value = "Retrieve Repository by repository code", nickname = "Retrieve Repository by repository code", notes = "Retrieve Repository from Record Admin", response = RepositoryModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "repository" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = RepositoryModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@GetMapping("byRepositoryCode/{repositoryCode}")
	public ResponseEntity<RepositoryModel> getRepositoryByCode(
			@ApiParam(value = "Identifier of Lead to be updated") @PathVariable("repositoryCode") String repoCode,
			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken)
			throws EntityNotFoundException {
		return new ResponseEntity<>(repositoryService.findByRepoCode(repoCode), HttpStatus.OK);
	}
	
	@ApiOperation(value = "Update Repository by repository id", nickname = "Update Repository by repository id", notes = "Update Repository from Record Admin", response = RepositoryModel.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "repository" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = RoleModel.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@PutMapping("byRepositoryCode/{repositoryCode}")
	public ResponseEntity<RepositoryModel> updateRepositoryByCode(
			@ApiParam(value = "Identifier of Role to be updated") @PathVariable("repositoryCode") String repositoryCode,
			@ApiParam(value = "Request User information", required = true) @Valid @RequestBody RepositoryModel repoModel,
			@ApiParam(value = "Role Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken)
			throws EntityNotFoundException {
		return new ResponseEntity<>(repositoryService.updateRepositoryByCode(repositoryCode, repoModel), HttpStatus.OK);
	}
	
}
