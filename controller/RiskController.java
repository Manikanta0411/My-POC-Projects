package com.record.controller;

import static com.record.util.Constants.RECORD_ADMIN_USER_TOKEN;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.record.dto.LayerDto;
import com.record.dto.RiskDto;
import com.record.entity.BusinessEntityOp;
import com.record.entity.ParamEntityLink;
import com.record.exception.BadRequestException;
import com.record.mapper.LayerMapper;
import com.record.mapper.RiskMapper;
import com.record.model.Layer;
import com.record.model.Risk;
import com.record.repository.ParamEntityLinkRepository;
import com.record.service.BusinessService;
import com.record.service.LayerService;
import com.record.service.MailService;
import com.record.service.MessageTransService;
import com.record.service.PdfService;
import com.record.service.RiskService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

@RestController
@Api(value = "Risk Controller", tags = { "risk" })
public class RiskController {

	@Autowired
	private RiskMapper riskMapper;

	@Autowired
	private LayerMapper layerMapper;

	@Autowired
	private RiskService riskService;
	
	@Autowired
	private LayerService layerService;
	
	@Autowired
	private ParamEntityLinkRepository paramEntityLinkRepository;
	
	@Autowired
	private BusinessService businessService;
	
	@Autowired
	private MessageTransService messageTransService;
	
	@Autowired
	private PdfService pdfService;
	
	@Autowired
	private MailService mailService;

	@ApiOperation(value = " Create Risk entity in system", nickname = "CreateRisk", notes = "Create Risk entity for Record Admin", response = RiskDto.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "risk" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no+ technical or validation errors", response = RiskDto.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@PostMapping("/risk")
	public ResponseEntity<RiskDto> addRisk(@Valid @RequestBody RiskDto riskDto,
			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken)
			throws URISyntaxException {
		Risk risk = riskMapper.mapToModel(riskDto);
		Risk newRisk = riskService.createRisk(risk);
		RiskDto newDto = riskMapper.mapToDto(newRisk);
		return ResponseEntity.created(new URI("/risk/" + newRisk.getRiskId())).body(newDto);
	}

	@ApiOperation(value = "Retrieve Risk entity", nickname = "RetrieveRisk", notes = "Retrieve Risk entity for Record Admin", response = RiskDto.class, authorizations = {
			@Authorization("xCheckClientId"), @Authorization("jwtBearerAuth") }, tags = { "risk" })
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK in case there is no technical or validation errors", response = RiskDto.class),
			@ApiResponse(code = 400, message = "Bad Requests", response = Error.class),
			@ApiResponse(code = 401, message = "Un Authorised user see error object for details", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class) })
	@GetMapping("/risk/{riskId}")
	public ResponseEntity<RiskDto> getRisk(@PathVariable(name = "riskId") Long riskId,
			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken) throws BadRequestException {
		Risk risk = riskService.getRisk(riskId);
		if (risk != null) {
			List<LayerDto> layerDtos = new ArrayList<>();
			risk.getLayers().forEach(layer -> {
				LayerDto layerDto = layerMapper.mapToDto(layer);
				layerDtos.add(layerDto);
			});
			RiskDto riskDto = riskMapper.mapToDto(risk);
			riskDto.setLayerDtos(layerDtos);
			return ResponseEntity.ok(riskDto);
		}
		return ResponseEntity.notFound().build();
	}
	
//	@GetMapping(value = "/entity/{entityId}/layer/{layerId}")
//	public void getEntityRiskLayerPDF2(
//			@PathVariable(name = "layerId") Long layerId,
//			@PathVariable(name = "entityId") Long entityId, HttpServletResponse response,
//			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken)
//			throws DocumentException, IOException {
//
//		Layer layer = layerService.getLayer(layerId);
//		List<ParamEntityLink> paramEntityLinkList = layerService.getParamEntityLinks(layer);
//		
//		Risk risk = layer.getRisk();
//
//		Optional<ParamEntityLink> paramEntityLink = paramEntityLinkRepository
//				.findByLayerAndBusinessEntityAndRisk(layerId, entityId, risk.getId());
//
//		BusinessEntityOp businessEntityOp = businessService.findById(entityId);
////		MessageTrans messageTrans = messageTransService.getMessageByLayerId(layerId);
////		
////		ObjectMapper mapper = new ObjectMapper();
////	    String message = messageTrans.getMessage();
////	        
////	    EntityMessageDto entityMessageDto =	mapper.readValue(message, EntityMessageDto.class);
//		
//		if (paramEntityLink.isPresent()) {
//		//	pdfService.createEntityRiskLayerPdfDocument(entityMessageDto,businessEntityOp, risk, layer, response);
//			pdfService.createEntityRiskLayerPdfDocument(businessEntityOp, risk, layer, response,paramEntityLink);
//		}
//	}
	
	@GetMapping(value = "/layer/{layerId}/pdf")
	public void getEntityRiskLayerPDF2(
			@PathVariable(name = "layerId") Long layerId,
			 HttpServletResponse response,
			@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken)
			throws DocumentException, IOException {

		Layer layer = layerService.getLayer(layerId);
		List<ParamEntityLink> paramEntityLinkList = layerService.getParamEntityLinks(layer);
		
		Risk risk = layer.getRisk();
		
		for (ParamEntityLink paramEntityLink : paramEntityLinkList) {
			BusinessEntityOp businessEntityOp = businessService.findById(paramEntityLink.getBusinessEntity().getId());
			File file = new File(System.getProperty("user.home") + System.getProperty("file.separator") + "RISlip_"+businessEntityOp.getId() + "<"
					+ risk.getName() + ">" + ".pdf");
			file.createNewFile();
					pdfService.createEntityRiskLayerPdfDocument(businessEntityOp, risk, layer, response,paramEntityLink,file);
					
				mailService.sendEmail(layer, businessEntityOp, file);
		}
		
//		MessageTrans messageTrans = messageTransService.getMessageByLayerId(layerId);
//		
//		ObjectMapper mapper = new ObjectMapper();
//	    String message = messageTrans.getMessage();
//	        
//	    EntityMessageDto entityMessageDto =	mapper.readValue(message, EntityMessageDto.class);
		
		
	}
}
