package com.record.service;

import java.awt.Color;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;
import com.record.controller.RiskController;
import com.record.model.Layer;
import com.record.model.Risk;
import com.record.repository.RiskRepository;

@Service
public class HeaderFooterPageEvent extends PdfPageEventHelper {
	
	    public void onEndPage(PdfWriter writer,Document document) {
	    	Phrase phrase = new Phrase(24,"Slip Leader", FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL, new Color(39, 55, 70)));
	    	
	    	Rectangle rect = writer.getBoxSize("art");
	        ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_CENTER, phrase, 300, rect.getBottom(), 0);
	//        ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_CENTER, new Phrase("Bottom Right"), rect.getRight(), rect.getBottom(), 0);
	    }
	

}

