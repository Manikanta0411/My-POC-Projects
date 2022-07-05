package com.record.controller;

import static com.record.util.Constants.RECORD_ADMIN_USER_TOKEN;
import static org.springframework.http.ResponseEntity.created;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.record.dto.BusinessEntityDto;
import com.record.dto.LayerDto;
import com.record.mapper.LayerMapper;
import com.record.model.Layer;
import com.record.service.BusinessEntityService;
import com.record.service.LayerService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

@RestController
@Api(value = "Layer Controller", tags = { "layer" })
public class LayerController {

	@Autowired
	private LayerService layerService;

	@Autowired
	private LayerMapper layerMapper;

	@Autowired
	private LayerValidator layerValidator;

	@Autowired
	private BusinessEntityService businessEntityService;

	private static final Logger LOGGER = LoggerFactory.getLogger(LayerController.class);

	@ApiOperation(value = "Create layer in system", nickname = "CreateLayer", notes = "Create layer for Record Admin", response = LayerDto.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "layer" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = LayerDto.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@PostMapping("/risk/{riskId}/layer")
	public ResponseEntity<LayerDto> createLayer(@Valid @RequestBody LayerDto layerDto,
			@PathVariable(name = "riskId") Long riskId,
			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken)
			throws URISyntaxException {
		layerValidator.validateLayerFields(layerDto);
		Layer layer = layerMapper.mapToModel(layerDto);
		Layer newLayer = layerService.createLayer(riskId, layer);
//		newLayer.setOriginId(newLayer.getId());

		return created(new URI("/risk/" + riskId + "/Layer/" + newLayer.getId())).body(layerMapper.mapToDto(newLayer));
	}

	@ApiOperation(value = "Create circulate layer in system", nickname = "CreateCirculatelayer", notes = "Create circulate layer for Record Admin", response = LayerDto.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "layer" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no+ technical or validation errors", response = LayerDto.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@PostMapping("/layer/{layerId}/circulate")
	public ResponseEntity circulateLayer(@PathVariable(name = "layerId") Long layerId,
			@RequestBody List<BusinessEntityDto> ReqEntity,
			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken)
			throws URISyntaxException {
		layerService.circulateLayer(layerId, ReqEntity);
		return ResponseEntity.ok().build();
	}

	@ApiOperation(value = "Retrieve layer in system by riskId", nickname = "Retrievelayer", notes = "Retrieve layer for Record Admin by riskId", response = LayerDto.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "layer" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no+ technical or validation errors", response = LayerDto.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@GetMapping("/risk/{riskId}/layer")
	public ResponseEntity<List<LayerDto>> getLayers(@PathVariable(name = "riskId") Long riskId,
			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken) {
		List<Layer> layers = layerService.getLayers(riskId);
		List<LayerDto> layerDtos = new ArrayList<>();
		layers.forEach(layer -> {
			LayerDto layerDto = layerMapper.mapToDto(layer);
			layerDtos.add(layerDto);
		});
		return ResponseEntity.ok(layerDtos);
	}

	@ApiOperation(value = "Retrieve layer in system by layer Id ", nickname = "Retrievelayer", notes = "Retrieve layer for Record Admin by layer Id", response = LayerDto.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "layer" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no+ technical or validation errors", response = LayerDto.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@GetMapping("/layer/{layerId}")
	public ResponseEntity<LayerDto> getLayer(@PathVariable(name = "layerId") Long layerId,
			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken) {
		Layer layer = layerService.getLayer(layerId);
		LayerDto layerDto = layerMapper.mapToDto(layer);
		return ResponseEntity.ok(layerDto);
	}

	@ApiOperation(value = "update layer in system by layer Id ", nickname = "Updatelayer", notes = "Update layer for Record Admin by layer Id", response = LayerDto.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "layer" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no+ technical or validation errors", response = LayerDto.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@PutMapping("layer/{layerId}")
	public ResponseEntity updateLayer(@PathVariable(name = "layerId") Long layerId,
			@Valid @RequestBody LayerDto layerDto,
			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken) throws URISyntaxException {
		Layer layer = layerMapper.mapToModel(layerDto);
		layerValidator.validateLayerFields(layerDto);
		layerService.updateLayer(layer, layerId);
		return ResponseEntity.ok().build();
	}

	private Set<Long> getEntityIds(com.record.model.ParamField paramField) {
		return paramField.getBusinessEntities().stream().map(entity -> entity.getId()).collect(Collectors.toSet());
	}

	/*

	@ApiOperation(value = "Create PPW", nickname = "Createlayer", notes = "Create circulate layer for Record Admin", response = PpwDto.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "layer" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = PpwDto.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@PostMapping("/savePpw/{layerId}")
	public ResponseEntity<Void> savePpw(@RequestBody List<PpwDto> reqPpw, @PathVariable("layerId") Long layerId) {
		// Layer layer = layerMapper.NewmapToModel(reqPpw);
		layerService.savePPW(reqPpw, layerId);
		return ResponseEntity.ok().build();
	}
	
	*/

}
