package com.record.service;

import static com.record.constant.LayerFieldEnum.INSURED_NAME;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;
import com.lowagie.text.alignment.HorizontalAlignment;
import com.lowagie.text.pdf.PdfWriter;
import com.record.constant.LayerFieldEnum;
import com.record.dto.LayerDto;
import com.record.dto.ParamFieldDto;
import com.record.dto.PpwDto;
import com.record.entity.BusinessEntityOp;
import com.record.entity.ParamEntityLink;
import com.record.mapper.LayerMapper;
import com.record.model.Layer;
import com.record.model.ParamField;
import com.record.model.PpwPremium;
import com.record.model.Risk;
@Service
public class PdfService {

	@Autowired
	private LayerMapper layerMapper;
	
	String insuredDetails="";
	String insuredNameValue = "";
	String addressValue = "";

	public void createEntityRiskLayerPdfDocument(BusinessEntityOp businessEntityOp, Risk risk, Layer layer, HttpServletResponse response,ParamEntityLink paramEntityLink,File file)
			throws DocumentException, IOException {
		Document document = new Document(PageSize.A4, 20, 20, 50, 25);
		PdfWriter.getInstance(document, response.getOutputStream());

//		File file = new File(System.getProperty("user.home") + System.getProperty("file.separator") + "RISlip_"+businessEntityOp.getId() + "<"
//				+ risk.getName() + ">" + ".pdf");
//		file.createNewFile();
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
		
		Rectangle rect = new Rectangle(30, 30, 550, 800);
		
        writer.setBoxSize("art", rect);
        HeaderFooterPageEvent event = new HeaderFooterPageEvent();
        
        writer.setPageEvent(event);
        
		document.open();

		Phrase phrase = createLogo();
		
		createPage1( businessEntityOp, risk, layer, document, phrase);
		document.newPage();
		
		createPage2(businessEntityOp, risk, layer, document, phrase,paramEntityLink);
		document.newPage();
		
		createPage3(risk,layer, document, phrase);
		
		document.newPage();
		
		createPage4(risk,document, phrase);
		
		document.newPage();
		
		createPage5(risk,document, phrase);
		
		document.newPage();
		createPage6(risk,document, phrase);
		
		document.newPage();
		createPage7(risk,document, phrase);
		
		document.newPage();
		createPage8(risk,document, phrase);
		
		document.newPage();
		createPage9(risk,document, phrase);
		
		document.newPage();
		createPage10(risk,document, phrase);
		
		document.newPage();
		createPage11(risk,document, phrase);
		document.newPage();
		createPage12(risk,document, phrase);
		
		document.newPage();
		createPage13(risk,document, phrase);
		
		document.newPage();
		createPage14(risk,document, phrase);
		
		document.close();
	
	//	mailService.sendEmail(layer,businessEntityOp, file);

	}

	private Phrase createLogo() throws IOException {
		Image img = Image.getInstance(ClassLoader.getSystemResource("Logo.jpg"));
		
		img.scaleAbsolute(80,55);
		
		Phrase phrase = new Phrase();
		//	phrase.add(new Chunk(img, 0,0));
		
		// -25 is to change the logo in x direction
		// -50 is to change the logo in y direction
		phrase.add(new Chunk(img, 420,-10));
		return phrase;
	}

	private void createPage14(Risk risk,Document document, Phrase phrase) {
		pageHeader(risk, document, phrase);
		
		document.add(createHeaderParagraph("\nBROKER REMUNERATION & DEDUCTIONS"));
		
		Table t15 = createTable();
		
		t15.addCell(createCell(createHeaderParagraph("FEE PAYABLE BY\n"
				+ "CLIENT?:\n"
				+ ""), 25));
		t15.addCell(createCell(createParagraph("No"), 75));
		t15.addCell(createCell(createHeaderParagraph("TOTAL\n"
				+ "BROKERAGE:\n"
				+ ""), 25));
		t15.addCell(createCell(createParagraph("10% (nil on Reinstatement Premiums)."), 75));
		t15.addCell(createCell(createHeaderParagraph("OTHER\n"
				+ "DEDUCTIONS\n"
				+ "FROM PREMIUM:\n"
				+ ""), 25));
		t15.addCell(createCell(createParagraph("None"), 75));
		t15.addCell(createCell(createHeaderParagraph("INTERMEDIARY\n"
				+ "CLAUSE:\n"
				+ ""), 25));
		t15.addCell(createCell(createParagraph("{Placing broker Name}, {Placing Broker address} are hereby recognised as the Intermediaries negotiating this Agreement for all business hereunder.\n"
				+ "	\n"
				+ "All communications (including but not limited to notices, statements, premium, return premium, commissions, taxes, losses, loss adjustment expense, salvages and loss settlements but excluding notices of termination) relating thereto shall be transmitted to the Intermediaries. Where the Intermediaries maintain risk and / or claim data / information / documents, the Intermediaries may hold such data / information / documents electronically.\n"
				+ ""), 75));
		
		document.add(t15);
	}

	private void createPage13(Risk risk,Document document, Phrase phrase) {
		pageHeader(risk, document, phrase);
		
		document.add(createHeaderParagraph("\nFISCAL AND REGULATORY"));
		
		Table t14 = createTable();
		
		
		t14.addCell(createCell(createHeaderParagraph("TAX PAYABLE BY\n"
				+ "REINSURERS:\n"
				+ ""), 25));
		t14.addCell(createCell(createParagraph("As applicable."), 75));
		t14.addCell(createCell(createHeaderParagraph("COUNTRY OF \n"
				+ "ORIGIN:\n"
				+ ""), 25));
		t14.addCell(createCell(createParagraph("India."), 75));
		t14.addCell(createCell(createHeaderParagraph("OVERSEAS \n"
				+ "BROKER:\n"
				+ ""), 25));
		t14.addCell(createCell(createParagraph("None."), 75));
		t14.addCell(createCell(createHeaderParagraph("US \n"
				+ "CLASSIFICATION:\n"
				+ ""), 25));
		t14.addCell(createCell(createParagraph("Non-regulated."), 75));
		t14.addCell(createCell(createHeaderParagraph("ALLOCATION OF \n"
				+ "PREMIUM CODING:\n"
				+ ""), 25));
		t14.addCell(createCell(createParagraph(""), 75));
		t14.addCell(createCell(createHeaderParagraph("FCA CLIENT \n"
				+ "CLASSIFICATION:\n"
				+ ""), 25));
		t14.addCell(createCell(createParagraph("Reinsurance."), 75));
		
		document.add(t14);
	}

	private void createPage12(Risk risk,Document document, Phrase phrase) {
		pageHeader(risk, document, phrase);
		
		Table t13 = createTable();
		
		t13.addCell(createCell(createHeaderParagraph("\n"), 25));
		t13.addCell(createCell(createParagraph("Agree to sign any instalment premium as additional premium for In-sure purposes only.\n"
				+ "\n"
				+ "	If the Settlement Due Date falls on a Saturday, Sunday or a United Kingdom Bank Holiday, it is agreed that the Settlement Due Date shall be changed to the first following working day.\n"
				+ ""), 75));
		t13.addCell(createCell(createHeaderParagraph("NON-BUREAUX\n"
				+ "ARRANGEMENTS:\n"
				+ ""), 25));
		t13.addCell(createCell(createParagraph(" In the event that Premiums and Limits under this Contract are in currencies other than the Intermediary’s normal Banking and Settlement Currencies, then Premiums and Claims hereunder will be paid/collected in Settlement Currency (ies) at the rate(s) of exchange prevailing at the time of payment/collection. Rate of exchange adjustments will be effected where applicable.\n"
				+ "\n"
				+ "	Where Premium Payment Warranty (PPW) or Premium Payment Condition (PPC) due date falls on a weekend or public holiday, presentation to Reinsurers hereon on the next working will be deemed in compliance with the PPW or PPC date.\n"
				+ ""), 75));
		t13.addCell(createCell(createHeaderParagraph("NOTICE OF \n"
				+ "CANCELLATION \n"
				+ "PROVISIONS:\n"
				+ ""), 25));
		t13.addCell(createCell(createParagraph("The content and format of any such notice should be in accordance with the ‘Notice of Cancellation’ standard, as published by the London Market Group (LMG), or their successor body, on behalf of London Market Associations and participants.  However, failure to comply with this standard will not affect the validity of the notice given.\n"
				+ "\n"
				+ "	The notice shall be provided to the broker by the following means:\n"
				+ "\n"
				+ "	By e mail to treatynoc@uib.co.uk \n"
				+ "\n"
				+ "	Failure to comply with this delivery requirement will make the notice null and void.  Satisfactory delivery of the notice will cause it to be effective irrespective of whether the broker has acknowledged receipt. \n"
				+ ""), 75));
		
		document.add(t13);
	}

	private void createPage11(Risk risk,Document document, Phrase phrase) {
		pageHeader(risk, document, phrase);
		
		Table t12 = createTable();
		
		t12.addCell(createCell(createHeaderParagraph("\n"), 25));
		t12.addCell(createCell(createParagraph("iii)	Those companies that have specifically elected to agree claims in respect of their own participation\n"
				+ "\n"
				+ "iv)	All other subscribing insurers that are not party to the Lloyd’s/IUA claims agreement practices, each in respect of their own participation."), 75));
		t12.addCell(createCell(createHeaderParagraph("CLAIMS ADMINISTRATION:"), 25));
		t12.addCell(createCell(createParagraph("UIB and (Re)Insurers agree that claims hereunder (including any claims related costs/fees) may be notified and administered via ECF with any payment(s) processed via CLASS, unless both parties agree to do otherwise.        \n"
				+ "\n"
				+ "	All overseas markets to settle by telegraphic transfer.\n"
				+ ""), 75));
		t12.addCell(createCell(createHeaderParagraph("RULES AND EXTENT OF \n"
				+ "ANY OTHER DELEGATED AUTHORITY:"
				+ ""), 25));
		t12.addCell(createCell(createParagraph("None, unless specified here by any of the claims agreement parties shown above."), 75));
		t12.addCell(createCell(createHeaderParagraph("EXPERT(S) FEES COLLECTION:"), 25));
		t12.addCell(createCell(createParagraph("UIB to collect fees for all contract security, including overseas."), 75));
		t12.addCell(createCell(createHeaderParagraph("SETTLEMENT DUE DATE:"), 100));
		t12.addCell(createCell(createHeaderParagraph("INSTALMENT PREMIUM  \n"
				+ "PERIOD OF CREDIT:\n"
				+ ""), 100));
		t12.addCell(createCell(createHeaderParagraph("ADJUSTMENT PREMIUM\n"
				+ "PERIOD OF CREDIT:\n"
				+ ""), 100));
		t12.addCell(createCell(createHeaderParagraph("BUREAUX \n"
				+ "ARRANGEMENTS:\n"
				+ ""), 25));
		t12.addCell(createCell(createParagraph("Delinked accounts to be submitted to Xchanging Ins-Sure services, when possible.     \n"
				+ "\n"
				+ "	All financial transactions due under this Reinsurance between the IUA / XIS member(s) and United Insurance Brokers Limited shall be settled in the same currency as indicated in the slip.\n"
				+ "\n"
				+ "	However, in the event of:\n"
				+ "1)	one or more XIS members requesting an alternative settlement currency, at the time or writing their line, or\n"
				+ "\n"
				+ "2)	where the currency(ies) indicated on this slip is not a currency in which IUA / XIS settle,\n"
				+ "	then such settlement currency shall be GBP and/or USD and/or EUR.\n"
				+ "	Underwriters hereon authorise the IUA / XIS to accept United Insurance Brokers Limited’s certification of figures and take down accounts as presented.\n"
				+ ""), 75));
		
		document.add(t12);
	}

	private void createPage10(Risk risk,Document document, Phrase phrase) {
		pageHeader(risk, document, phrase);
		
		document.add(createHeaderParagraph("\nSUBSCRIPTION AGREEMENT"));
		
		Table t11 = createTable();
		
		t11.addCell(createCell(createHeaderParagraph("SLIP LEADER:"), 25));
		t11.addCell(createCell(createHeaderParagraph("Ascot Syndicate Lloyds"), 75));
		t11.addCell(createCell(createHeaderParagraph("BUREAU LEADER:"), 100));
		t11.addCell(createCell(createHeaderParagraph("BASIS OF AGREEMENT \n"
				+ "TO CONTRACT \n"
				+ "CHANGES:\n"
				+ ""), 25));
		t11.addCell(createCell(createParagraph("General Underwriters Agreement (Version 2.0 February 2014) with Excess of Loss and Treaty Reinsurance Schedule (October 2002)."), 75));
		t11.addCell(createCell(createHeaderParagraph("OTHER AGREEMENT \n"
				+ "PARTIES FOR \n"
				+ "CONTRACT CHANGES, \n"
				+ "FOR PART TWO GUA \n"
				+ "CHANGES ONLY:\n"
				+ ""), 25));
		t11.addCell(createCell(createParagraph("Slip Leader only to agree part two changes"), 75));
		t11.addCell(createCell(createHeaderParagraph("AGREEMENT PARTIES \n"
				+ "FOR CONTRACT \n"
				+ "CHANGES, FOR THEIR \n"
				+ "PROPORTION ONLY:\n"
				+ ""), 25));
		t11.addCell(createCell(createParagraph("All non-bureaux companies to agree contract changes for each of their respective shares by correspondence"), 75));
		t11.addCell(createCell(createHeaderParagraph("BASIS OF CLAIMS\n"
				+ "AGREEMENT:\n"
				+ ""), 25));
		t11.addCell(createCell(createParagraph("Claims to be managed in accordance with (as applicable):\n"
				+ "\n"
				+ "i) The Lloyd’s Claims Scheme (Combined), or as amended or any successor thereto\n"
				+ "\n"
				+ "ii) IUA claims agreement practices\n"
				+ "\n"
				+ "iii) The practices of any company(ies) electing to agree claims in respect of their own participation\n"
				+ "\n"
				+ "iv) Non-Bureau companies to agree claims subject to their own claims agreement procedures\n"
				+ ""), 75));
		t11.addCell(createCell(createHeaderParagraph("CLAIMS AGREEMENT\n"
				+ "PARTIES:\n"
				+ ""), 25));
		t11.addCell(createCell(createParagraph("i) 	For Lloyd’s syndicates \n"
				+ "\n"
				+ "	The leading Lloyd's syndicate and, where required by the applicable Lloyd's Claims Scheme, the second Lloyd's syndicate and/or the Scheme Service Provider.\n"
				+ "\n"
				+ "	The second Lloyd’s Syndicate is:\n"
				+ "\n"
				+ "\n"
				+ "\n"
				+ "ii)	Those companies acting in accordance with the IUA claims agreement practices, excepting those that may have opted out via iii below"), 75));
		
		document.add(t11);
	}

	private void createPage9(Risk risk,Document document, Phrase phrase) {
		pageHeader(risk, document, phrase);
		
		document.add(createHeaderParagraph("\nREINSURER SIGNING PAGE"));
		document.add(createHeaderParagraph("ATTACHING TO AND FORMING PART OF"));
		
		Table t10 = createTable();
		
		t10.addCell(createCell(createParagraph("Contract Number:"), 25));
		t10.addCell(createCell(createParagraph("21T491003003"), 75));
		t10.addCell(createCell(createParagraph("Reinsured:"), 25));
		t10.addCell(createCell(createParagraph("National Insurance Company Limited, India"), 75));
		t10.addCell(createCell(createParagraph("Type:"), 25));
		t10.addCell(createCell(createParagraph("Risk cum Catastrophe Excess of Loss protection of inward Treaty and Facultative Account."), 75));
		t10.addCell(createCell(createParagraph("Limits:"), 25));
		t10.addCell(createCell(createParagraph("INR 100,000,000 xs INR 250,000,000\n"
				+ "INR 250,000,000 xs INR 350,000,000\n"
				+ "INR 400,000,000 xs INR 600,000,000 (PML/CAT only)\n"
				+ "INR 400,000,000 xs INR 1,000,000,000 (CAT only)\n"
				+ ""), 75));
		t10.addCell(createCell(createParagraph("Period:"), 25));
		t10.addCell(createCell(createParagraph("Losses occurring during the period 1.5.2021 – 30.4.2022 b.d.i. Local Standard Time at place where the loss occurs."), 75));
		t10.addCell(createCell(createHeaderParagraph("Written line participation:"), 25));
		t10.addCell(createCell(createHeaderParagraph("%"), 75));
		t10.addCell(createCell(createHeaderParagraph("Signed line participation:"), 25));
		t10.addCell(createCell(createHeaderParagraph("%"), 75));
		t10.addCell(createCell(createHeaderParagraph(""), 100));
		t10.addCell(createCell(createHeaderParagraph("Signed in                           this                  day of                                       2021"), 100));
		t10.addCell(createCell(createHeaderParagraph("Signed for and on behalf of the Reinsurer: "), 100));
		
		document.add(t10);
	}

	private void createPage8(Risk risk,Document document, Phrase phrase) {
		pageHeader(risk, document, phrase);
		
		Table t9 = createTable();
		
		t9.addCell(createCell(createHeaderParagraph("\nBASIS OF\n"
				+ "WRITTEN LINES:\n"
				+ ""), 25));
		t9.addCell(createCell(createParagraph("Percentage of whole."), 75));
		t9.addCell(createCell(createHeaderParagraph("BASIS OF\n"
				+ "SIGNED LINES:\n"
				+ ""), 25));
		t9.addCell(createCell(createParagraph("Percentage of whole."), 75));
		t9.addCell(createCell(createHeaderParagraph("SIGNING\n"
				+ "PROVISIONS:\n"
				+ ""), 25));
		t9.addCell(createCell(createHeaderParagraph("Disproportionate Signing"), 75));
		t9.addCell(createCell(createParagraph(""), 25));
		t9.addCell(createCell(createParagraph("In the event that the written lines hereon exceed 100% of the order, any lines written “to stand” will be allocated in full and all other lines will be signed down in equal proportions so that the aggregate signed lines are equal to 100% of the order without further agreement of any of the (re)insurers.\n"
				+ "\n"
				+ "	However:\n"
				+ "\n"
				+ "	1.	in the event that the placement of the order is not completed by the commencement date of the period of insurance then all lines written by that date will be signed in full;\n"
				+ "\n"
				+ "	2.	the (re)insured may elect for the disproportionate signing of (re)insurers’ lines, without further specific agreement of (re)insurers, providing that any such variation is made prior to the commencement date of the period of insurance, and that lines written “to stand” may not be varied without the documented agreement of those (re)insurers; \n"
				+ "\n"
				+ "	3.	the signed lines resulting from the application of the above provisions can be varied, before or after the commencement date of the period of insurance, by the documented agreement of the (re)insured and all (re)insurers whose lines are to be varied. The variation to the contracts will take effect only when all such (re)insurers have agreed, with the resulting variation in signed lines commencing from the date set out in that agreement."), 75));
		t9.addCell(createCell(createHeaderParagraph("WRITTEN LINES:"), 25));
		t9.addCell(createCell(createParagraph("As per the attached schedules."), 75));
		
		document.add(t9);
	}

	private void createPage7(Risk risk,Document document, Phrase phrase) {
		pageHeader(risk, document, phrase);
		
		document.add(createHeaderParagraph("\nSECURITY DETAILS"));
		
		Table t8 = createTable();
		
		t8.addCell(createCell(createHeaderParagraph("REINSURER'S \n"
				+ "LIABILITY:"), 25));
		t8.addCell(createCell(createHeaderParagraph("LMA3333 Reinsurer's Liability Several not Joint"), 75));
		t8.addCell(createCell(createParagraph(""), 25));
		t8.addCell(createCell(createParagraph("The liability of a reinsurer under this contract is several and not joint with other reinsurers party to this contract. A reinsurer is liable only for the proportion of liability it has underwritten. A reinsurer is not jointly liable for the proportion of liability underwritten by any other reinsurer. Nor is a reinsurer otherwise responsible for any liability of any other reinsurer that may underwrite this contract.\n"
				+ "\n"
				+ "	The proportion of liability under this contract underwritten by a reinsurer (or, in the case of a Lloyd’s syndicate, the total of the proportions underwritten by all the members of the syndicate taken together) is shown next to its stamp. This is subject always to the provision concerning “signing” below.\n"
				+ "\n"
				+ "	In the case of a Lloyd’s syndicate, each member of the syndicate (rather than the syndicate itself) is a reinsurer. Each member has underwritten a proportion of the total shown for the syndicate (that total itself being the total of the proportions underwritten by all the members of the syndicate taken together). The liability of each member of the syndicate is several and not joint with other members. A member is liable only for that member’s proportion. A member is not jointly liable for any other member’s proportion. Nor is any member otherwise responsible for any liability of any other reinsurer that may underwrite this contract. The business address of each member is Lloyd’s, One Lime Street, London EC3M 7HA. The identity of each member of a Lloyd’s syndicate and their respective proportion may be obtained by writing to Market Services, Lloyd’s, at the above address.\n"
				+ ""), 75));
		t8.addCell(createCell(createParagraph(""), 25));
		t8.addCell(createCell(createHeaderParagraph("\n"
				+ "	Proportion of liability\n"
				+ ""), 75));
		t8.addCell(createCell(createParagraph(""), 25));
		t8.addCell(createCell(createParagraph("	Unless there is “signing” (see below), the proportion of liability under this contract underwritten by each (re)insurer (or, in the case of a Lloyd’s syndicate, the total of the proportions underwritten by all the members of the syndicate taken together) is shown next to its stamp and is referred to as its “written line”.\n"
				+ "\n"
				+ "	Where this contract permits, written lines, or certain written lines, may be adjusted (“signed”). In that case a schedule is to be appended to this contract to show the definitive proportion of liability under this contract underwritten by each (re)insurer (or, in the case of a Lloyd’s syndicate, the total of the proportions underwritten by all the members of the syndicate taken together). A definitive proportion (or, in the case of a Lloyd’s syndicate, the total of the proportions underwritten by all the members of a Lloyd’s syndicate taken together) is referred to as a “signed line”. The signed lines shown in the schedule will prevail over the written lines unless a proven error in calculation has occurred. \n"
				+ "\n"
				+ "	Although reference is made at various points in this clause to “this contract” in the singular, where the circumstances so require this should be read as a reference to contracts in the plural.\n"
				+ ""), 75));
		t8.addCell(createCell(createHeaderParagraph("ORDER HEREON:"), 25));
		t8.addCell(createCell(createParagraph("% of 100%"), 75));
		
		document.add(t8);
	}

	private void createPage6(Risk risk,Document document, Phrase phrase) {
		pageHeader(risk, document, phrase);
		
		document.add(createHeaderParagraph("\nINFORMATION"));
		
		Table t7 = createTable();
		
		t7.addCell(createCell(createHeaderParagraph("LOSS HISTORY:"), 25));
		t7.addCell(createCell(createParagraph("{Loss History}"), 75));
		t7.addCell(createCell(createParagraph("COMMENTS"), 100));
		t7.addCell(createCell(createParagraph("NAME OF ATTACHMENT FILES"), 100));
		
		document.add(t7);
	}

	private void createPage5(Risk risk,Document document, Phrase phrase) {
		pageHeader(risk, document, phrase);
		
		document.add(createHeaderParagraph("\nREPOSITORY TITLE 1"));
		
		
		Table t6 = createTable();

		t6.addCell(createCell(createHeaderParagraph("{REPOSITORY CLAUSE HEADING}:"), 30));
		t6.addCell(createCell(createHeaderParagraph("{(Repository Remark)}"), 70));
		t6.addCell(createCell(createParagraph("{REPOSITORY DESCRIPTION}"), 100));
		t6.addCell(createCell(createHeaderParagraph("{REPOSITORY CLAUSE HEADING}:"), 30));
		t6.addCell(createCell(createHeaderParagraph("{(Repository Remark)}"), 70));
		t6.addCell(createCell(createParagraph("{REPOSITORY DESCRIPTION}"), 100));
		t6.addCell(createCell(createHeaderParagraph("{REPOSITORY CLAUSE HEADING}:"), 30));
		t6.addCell(createCell(createHeaderParagraph("{(Repository Remark)}"), 70));
		t6.addCell(createCell(createParagraph("{REPOSITORY DESCRIPTION}"), 100));
		document.add(t6);
	}

	private void createPage4(Risk risk,Document document, Phrase phrase) {
		pageHeader(risk, document, phrase);
		
		document.add(createHeaderParagraph("\nCONTRACTUAL WORDING\n\n"));
		
		document.add(createParagraph("Agreed Between\n"
				+ "\n"
				+ "THE REINSURED as specified in the attached Risk Details\n"
				+ "\n"
				+ "And\n"
				+ "\n"
				+ "The subscribing reinsures whose names and proportions hereof are set of in the attached Signing Schedule(s), (hereinafter called the “Reinsurer”)\n"
				+ "\n"
				+"\n"));
		
		document.add(createHeaderParagraph("REPOSITORY TITLE 1"));
		
		
		Table t5 = createTable();
		
		t5.addCell(createCell(createHeaderParagraph("{REPOSITORY CLAUSE HEADING}:"), 30));
		t5.addCell(createCell(createHeaderParagraph("{(Repository Remark)}"), 70));
		t5.addCell(createCell(createParagraph("{REPOSITORY DESCRIPTION}"), 100));
		t5.addCell(createCell(createHeaderParagraph("{REPOSITORY CLAUSE HEADING}:"), 30));
		t5.addCell(createCell(createHeaderParagraph("{(Repository Remark)}"), 70));
		t5.addCell(createCell(createParagraph("{REPOSITORY DESCRIPTION}"), 100));
		t5.addCell(createCell(createHeaderParagraph("{REPOSITORY CLAUSE HEADING}:"), 30));
		t5.addCell(createCell(createHeaderParagraph("{(Repository Remark)}"), 70));
		t5.addCell(createCell(createParagraph("{REPOSITORY DESCRIPTION}"), 100));
		

		document.add(t5);
	}

	@SuppressWarnings("unlikely-arg-type")
	private void createPage3(Risk risk,Layer layer, Document document, Phrase phrase) {
		pageHeader(risk, document, phrase);
		
		document.add(createHeaderParagraph("\nPREMIUM PAYMENT TERMS & SCHEDULE"));
		
		Table t3 = createTable();
		LayerDto layerDto = layerMapper.mapToDto(layer);
//		layerDto.getParamFieldDtos().get(0).getPpwDto().get(0).getDepositPremiumPercent();
		Optional<ParamFieldDto> paramFieldDto= layerDto.getParamFieldDtos().stream().filter(paramField->paramField.getFieldKey().equalsIgnoreCase("ppw")).findFirst();
	//	paramFieldDto.get().getPpwDto().get(0).getDepositPremiumPercent();
		
		t3.addCell(createCell(createHeaderParagraph("PREMIUM PAYMENT TERM:"), 25));
		t3.addCell(createCell(createParagraph("{Give value of field}"), 75));
		t3.addCell(createCell(createHeaderParagraph("DEPOSIT PREMIUM %:"), 25));
	//	t3.addCell(createCell(createParagraph(layerDto.getParamFieldDtos().stream().filter(paramField->paramField.getPpwDto().contains("depositPremiumPercent")).findAny().toString() ), 75));
	//	t3.addCell(createCell(createParagraph("60.00"), 75));
		t3.addCell(createCell(createParagraph(String.valueOf(layer.getPpws().get(0).getDepositPremiumPercent())), 75));
		t3.addCell(createCell(createHeaderParagraph("ADJUSTMENT PREMIUM%:"), 25));
		layer.getPpws().stream().forEach(ppws->{
			ppws.getPpwPremiums().stream().forEach(premium->{
				if(premium.isAdjustedPremium()) {
					t3.addCell(createCell(createParagraph(String.valueOf(premium.getPercentage())), 75));
					
				}
			
		});
		});
//		layer.getPpws().get(0).getPpwPremiums().stream().forEach(premium->{
//			if(!premium.isAdjustedPremium()){
//				t3.addCell(createCell(createParagraph(String.valueOf(premium.getPercentage())), 75));
//			}
//		});
//		t3.addCell(createCell(createParagraph(String.valueOf(layer.getPpws().get(0).getNumberOfInstallments())), 75));
		t3.addCell(createCell(createHeaderParagraph("NUMBER OF INSTALLMENTS:"), 25));
		t3.addCell(createCell(createParagraph(String.valueOf(layer.getPpws().get(0).getNumberOfInstallments())), 75));
		document.add(t3);
		document.add(createHeaderParagraph("SCHEDULE-"));
		
		Table t4 = new Table(80);
		t4.setPadding(2);
		t4.setWidth(100);
		
		t4.addCell(createCell1(createHeaderParagraph("INSTALLMENT"), 20));
		t4.addCell(createCell2(createHeaderParagraph("PREMIUM "), 40));
		t4.addCell(createCell1(createHeaderParagraph("SETTLEMENT DUE"), 20));
		t4.addCell(createCell1(createHeaderParagraph("NUMBER"),20));
		t4.addCell(createCell2(createHeaderParagraph("%"), 15));
		t4.addCell(createCell2(createHeaderParagraph("Amount{Currency}"), 25));
		t4.addCell(createCell1(createHeaderParagraph("DATE"),20));
		
		layer.getPpws().stream().forEach(ppws->{
			ppws.getPpwPremiums().stream().forEach(premium->{
				if(premium.isAdjustedPremium()) {
			t4.addCell(createCell2(createHeaderParagraph("ADJUSTMENT PREMIUM"),20));
			}else {
				t4.addCell(createCell2(createHeaderParagraph(premium.getInstallmentNum().toString()),20));
			}
				
			t4.addCell(createCell2(createHeaderParagraph(premium.getPercentage().toString()), 15));
			t4.addCell(createCell2(createHeaderParagraph(premium.getAmount().toString()), 25));
		//	t4.addCell(createCell2(createHeaderParagraph(String.valueOf(premium.getPremiumDate().getMonth()+"/"+premium.getPremiumDate().getDate()+"/"+premium.getPremiumDate().getYear())),20));
			t4.addCell(createCell2(createHeaderParagraph(premium.getPremiumDate().toString()), 20));
			});
		});

		document.add(t4);
	}

	private void createPage2(BusinessEntityOp businessEntityOp, Risk risk, Layer layer, Document document,
			Phrase phrase,ParamEntityLink paramEntityLink) {
		
		pageHeader(risk, document, phrase);
		
		document.add(createHeaderParagraph("\nRISK DETAILS"));
		
		Table t2 = createTable();
		if(!StringUtils.isEmpty(risk.getRiskId())) {
		t2.addCell(createCell(createHeaderParagraph("UNIQUE MARKET REFERENCE:"),25));
		t2.addCell(createCell(createParagraph(risk.getRiskId()), 75));
		}
//		//t2.addCell(createCell(createHeaderParagraph(RecordConstants.INSURED), 10));
////		t2.addCell(createCell(createHeaderParagraph(INSURED), 25));
////		t2.addCell(createCell(createParagraph(layer.getParamFields().get(0).getFieldValue()), 75));
//		layer.getParamFields().stream().filter(paramFld->paramFld.getFieldKey().equalsIgnoreCase("insuredName") && (!StringUtils.isEmpty(paramFld.getFieldValue()))).findFirst().ifPresent(paramField->{
//			t2.addCell(createCell(createHeaderParagraph(INSURED_NAME.getLabel()), 25));
//			t2.addCell(createCell(createParagraph(paramField.getFieldValue()), 75));
//		});
//		t2.addCell(createCell(createHeaderParagraph("REINSURED:"), 25));
//		t2.addCell(createCell(createParagraph(businessEntityOp.getEntityShortName()), 75));
//		t2.addCell(createCell(createHeaderParagraph("REINSURER(S):"), 25));
//		t2.addCell(createCell(createParagraph("The subscribing Insurance and/or Reinsurance Companies and/or Underwriting Members of Lloyd's (hereinafter referred to as the Reinsurer), for a participation as stated in the individual signing pages."), 75));
//		t2.addCell(createCell(createHeaderParagraph("FORM:"), 25));
//		t2.addCell(createCell(createParagraph(risk.getForm()), 75));
//		t2.addCell(createCell(createHeaderParagraph("COVERAGE:"), 25));
//		t2.addCell(createCell(createParagraph("{coverage}"), 75));
//		t2.addCell(createCell(createHeaderParagraph(PERILS_INSURED.getLabel()), 25));
//		t2.addCell(createCell(createParagraph("{Perils insured}"), 75));
		t2.addCell(createCell(createHeaderParagraph("PERIOD:"), 25));
		t2.addCell(createCell(createParagraph(risk.getPolicyInceptionDate()+" to "+risk.getPolicyEndDate()), 75));
//		t2.addCell(createCell(createHeaderParagraph(OCCUPANCY.getLabel()), 25));
//		t2.addCell(createCell(createParagraph("{Occupancy/Interest}"), 75));
//		t2.addCell(createCell(createHeaderParagraph(CURRENCY.getLabel()), 25));
//		t2.addCell(createCell(createParagraph("{Currency}"), 75));
//		t2.addCell(createCell(createHeaderParagraph("DEDUCTIBLE:"), 25));
//		t2.addCell(createCell(createParagraph("{Deductible}"), 75));
//		t2.addCell(createCell(createHeaderParagraph(TOTAL_SUM_INSURED.getLabel()), 25));
//		t2.addCell(createCell(createParagraph("{Total sum insured}"), 75));
//		t2.addCell(createCell(createHeaderParagraph(LIMIT_OF_LIABILITY.getLabel()), 25));
//		t2.addCell(createCell(createParagraph("{limit of liability}"), 75));
//		t2.addCell(createCell(createHeaderParagraph("{SAL TITLE}:"), 25));
//		t2.addCell(createCell(createParagraph("{SAL Value}"), 75));
//		t2.addCell(createCell(createHeaderParagraph("{CUSTOM FIELD TITLE IN PREMIUM TAB}:"), 25));
//		t2.addCell(createCell(createParagraph("{Field Value}"), 75));
//		t2.addCell(createCell(createHeaderParagraph(PREMIUM_RATE.getLabel()), 25));
//		t2.addCell(createCell(createParagraph("{Premium Rate} "), 75));
//		t2.addCell(createCell(createHeaderParagraph(TOTAL_PREMIUM.getLabel()), 25));
//		t2.addCell(createCell(createParagraph("{Total Premium} "), 75));
//		t2.addCell(createCell(createHeaderParagraph("CEDING COMMISSION RATE:"), 25));
//		t2.addCell(createCell(createParagraph("{ceding commission rate}  "), 75));
//		t2.addCell(createCell(createHeaderParagraph(CEDING_COMMISSION.getLabel()), 25));
//		t2.addCell(createCell(createParagraph("{ceding commission}"), 75));
//		
//		layer.getParamFields().stream().forEach(paramField->{
////			if(paramField.getFieldKey().equals(LayerFieldEnum)) {
////				
////			}
//		//	t2.addCell(createCell(createHeaderParagraph(paramField.getFieldKey().toUpperCase()), 25));
//			if(LayerFieldEnum.getLabelByFieldName(paramField.getFieldKey())!=null){
//				t2.addCell(createCell(createHeaderParagraph(LayerFieldEnum.getLabelByFieldName(paramField.getFieldKey()).toUpperCase()), 25));
//			
//			}else {
//				t2.addCell(createCell(createHeaderParagraph(paramField.getFieldKey().toUpperCase()), 25));
//			
//			}
//			t2.addCell(createCell(createParagraph(paramField.getFieldValue()), 75));
//		});
		//layer.getParamFields().stream().filter(field->(!field.getFieldKey().equals("addressOfInsured"))).forEach(paramField->{
		
		
		Optional<ParamField> insuredNameField = layer.getParamFields().stream().filter(paramfld->paramfld.getFieldKey().equalsIgnoreCase("insuredName")).findAny();
				if(insuredNameField.isPresent()) {
					insuredNameValue=insuredNameField.get().getFieldValue();
				}
				
				Optional<ParamField> addressField = layer.getParamFields().stream().filter(paramfld->paramfld.getFieldKey().equalsIgnoreCase("addressOfInsured")).findAny();
						if(addressField.isPresent()) {
							addressValue=addressField.get().getFieldValue();
						}
			
			layer.getParamFields().stream().filter(field->paramEntityLink.getFieldIds().contains(field.getId().toString())).forEach(paramField->{

				if(!paramField.getFieldKey().equalsIgnoreCase("addressOfInsured")) {
					
					
				if(LayerFieldEnum.getLabelByFieldName(paramField.getFieldKey())!=null){
				t2.addCell(createCell(createHeaderParagraph(LayerFieldEnum.getLabelByFieldName(paramField.getFieldKey()).toUpperCase()), 25));
			
			}else {
				t2.addCell(createCell(createHeaderParagraph(paramField.getFieldKey().toUpperCase()), 25));
			
			}
				}
				
			if(!paramField.getFieldKey().equalsIgnoreCase("addressOfInsured")) {
				if(paramField.getFieldKey().equalsIgnoreCase("insuredName")) {
					t2.addCell(createCell(createParagraph(insuredNameValue+"\n"+addressValue), 75));
					
				}else {
					t2.addCell(createCell(createParagraph(paramField.getFieldValue()), 75));
				}
				}
				
			
			});
		
		document.add(t2);
	}

	private void createPage1(BusinessEntityOp businessEntityOp, Risk risk,
			Layer layer, Document document, Phrase phrase) {
		document.add(new Paragraph(phrase));
		
		Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		fontTitle.setSize(25);
	
		Paragraph paragraph = new Paragraph("REINSURANCE CONTRACT", fontTitle);
		paragraph.setAlignment(Paragraph.ALIGN_CENTER);
		paragraph.setSpacingAfter(25);
		
		Table t = createTable();
		
		t.addCell(createCell1(createParagraph(""), 50));
		t.addCell(createCell1(createParagraph(businessEntityOp.getEntityAddress()), 50));
		
		Table t1 = createTable();
		
		t1.addCell(createCell(createHeaderParagraph("UMR:"), 25));
		t1.addCell(createCell(createParagraph(risk.getRiskId()), 75));
		t1.addCell(createCell(createHeaderParagraph("RISK NAME:"), 25));
		t1.addCell(createCell(createParagraph(risk.getName()), 75));
		layer.getParamFields().stream().filter(paramFld->paramFld.getFieldKey().equalsIgnoreCase("insuredName") && (!StringUtils.isEmpty(paramFld.getFieldValue()))).findFirst().ifPresent(paramField->{
			t1.addCell(createCell(createHeaderParagraph(INSURED_NAME.getLabel()), 25));
			t1.addCell(createCell(createParagraph(paramField.getFieldValue()), 75));
		});
		layer.getParamFields().stream().filter(paramFld->paramFld.getFieldKey().equalsIgnoreCase("addressOfInsured") && (!StringUtils.isEmpty(paramFld.getFieldValue()))).findFirst().ifPresent(paramField->{
			t1.addCell(createCell(createHeaderParagraph(""), 25));
			t1.addCell(createCell(createParagraph(paramField.getFieldValue()), 75));
		});
			t1.addCell(createCell(createHeaderParagraph("REINSURED:"), 25));
			t1.addCell(createCell(createParagraph(businessEntityOp.getEntityShortName()), 75));
		if(!StringUtils.isEmpty(risk.getForm())) {
			t1.addCell(createCell(createHeaderParagraph("FORM:"), 25));
			t1.addCell(createCell(createParagraph(risk.getForm()), 75));
		}
		t1.addCell(createCell(createHeaderParagraph("PERIOD:"), 25));
		t1.addCell(createCell(createParagraph(risk.getPolicyInceptionDate()+" to "+risk.getPolicyEndDate()), 75));
		
		document.add(t);
		document.add(paragraph);
		document.add(t1);
	}

	private void pageHeader(Risk risk, Document document, Phrase phrase) {
		document.add(new Paragraph(phrase));
		
		Table t = new Table(100);
		t.setPadding(0);
		t.setBorder(0);
		t.setWidth(100);
		
		t.addCell(createCell(createHeaderParagraph2("UMR:"), 25));
		t.addCell(createCell(createHeaderParagraph2(risk.getRiskId()), 75));
		t.addCell(createCell(createHeaderParagraph2("Reinsured:"), 25));
		t.addCell(createCell(createHeaderParagraph2("{Cedent Name}"), 75));
		t.addCell(createCell(createHeaderParagraph2("REF: Reins \' MERGEFORMAT REF Reins \' MERGEFORMAT Type:"), 40));
		t.addCell(createCell(createHeaderParagraph2(risk.getForm()+"\n"), 60));
		 
		document.add(t);
	}
	
	private Table createTable() {
		Table t = new Table(100);
		t.setPadding(5);
		t.setBorder(0);
		t.setWidth(100);
		return t;
	}
	
	private Cell createCell(Paragraph pHeader, int colspan) {
		Cell c = new Cell();
		c.setBorder(0);
		c.addElement(pHeader);
		c.setColspan(colspan);
		c.setHorizontalAlignment(HorizontalAlignment.JUSTIFIED);
		return c;
	}
	
	private Cell createCell1(Paragraph pHeader, int colspan) {
		Cell c = new Cell();
		c.setBorder(0);
		c.addElement(pHeader);
		c.setColspan(colspan);
		c.setHorizontalAlignment(HorizontalAlignment.CENTER);
		return c;
	}
	
	private Cell createCell2(Paragraph pHeader, int colspan) {
		Cell c = new Cell();
		c.addElement(pHeader);
		c.setColspan(colspan);
		c.setHorizontalAlignment(HorizontalAlignment.CENTER);
		return c;
	}
	
	private Paragraph createHeaderParagraph(String paragraph) {
		
		Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		fontTitle.setSize(8);
		
		Paragraph pHeader = new Paragraph(paragraph, fontTitle);
		pHeader.setAlignment(Paragraph.ALIGN_CENTER);
		return pHeader;
	}
	
	private Paragraph createHeaderParagraph2(String paragraph) {
		
		Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		fontTitle.setSize(6);
		fontTitle.setColor(66, 73, 73 );
		
		Paragraph pHeader = new Paragraph(paragraph, fontTitle);
		pHeader.setAlignment(Paragraph.ALIGN_LEFT);
		
		return pHeader;
	}
	
	private Paragraph createParagraph(String paragraph) {
		Font fontTitle1 = FontFactory.getFont(FontFactory.HELVETICA);
		fontTitle1.setSize(8);
		Paragraph p = new Paragraph(paragraph,fontTitle1);
		p.setAlignment(Paragraph.ALIGN_CENTER);

		return p;

	}
}
