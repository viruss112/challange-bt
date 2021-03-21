package com.example.app.Services;

import com.example.app.DTOs.ClientDTO;
import com.example.app.DTOs.ClientStatusDTO;
import com.example.app.Enums.ClientStatusEnum;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Service
public class PdfService extends PdfPageEventHelper {


    /**
     * @param clientDTO client info
     * @return
     */
    String getFileName(ClientDTO clientDTO) {

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm-ss");
        String date = now.format(format);

        return clientDTO.getLastName() + "-" + clientDTO.getFirstName() + "-" + "report" + "-" + date + ".pdf";

    }

    /**
     * @param content   pdf content
     * @param clientDTO client indo
     * @return
     * @throws FileNotFoundException
     * @throws DocumentException
     */
    private InputStream createPdf(String content, ClientDTO clientDTO) throws FileNotFoundException, DocumentException {

        Document document = new Document();

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm-ss");
        String date = now.format(format);
        String path = "./src/main/java/com/example/app/reports/" + getFileName(clientDTO);

        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path));

        document.open();
        onStartPage(writer);
        onEndPage(writer);
        Paragraph paragraph = new Paragraph(content);

        document.addTitle("Client enrolment");
        document.addAuthor("BT software team");
        document.add(paragraph);

        document.close();

        return new FileInputStream(path);
    }

    /**
     * This method sets the pdf page title
     *
     * @param pdfWriter
     */
    private void onStartPage(PdfWriter pdfWriter) {
        Phrase phrase = new Phrase("Client enrolment");
        ColumnText.showTextAligned(pdfWriter.getDirectContent(), Element.ALIGN_CENTER, phrase, 297, 800, 0);
    }

    /**
     * This method sets the pdf page signatures
     *
     * @param pdfWriter
     */
    private void onEndPage(PdfWriter pdfWriter) {
        ColumnText.showTextAligned(pdfWriter.getDirectContent(), Element.ALIGN_CENTER, new Phrase("Client signature"), 110, 700, 0);
        ColumnText.showTextAligned(pdfWriter.getDirectContent(), Element.ALIGN_CENTER, new Phrase("Frontdesk employee"), 450, 700, 0);
    }


    /**
     * This method sets the pdf document content
     *
     * @param clientStatusDTO
     * @param clientDTO
     * @param failReason
     * @return
     * @throws FileNotFoundException
     * @throws DocumentException
     */
    InputStream setPdfContent(ClientStatusDTO clientStatusDTO, ClientDTO clientDTO, String failReason) throws FileNotFoundException, DocumentException {

        String content;

        if (clientStatusDTO.getStatus() == ClientStatusEnum.ELIGIBLE_FOR_ENROLMENT) {
            content = "The client" + " " + clientDTO.getLastName() + " " + clientDTO.getFirstName() + " " + "has been successfully registered in our system. Please sign the document";
        } else {
            content = "The client" + " " + clientDTO.getLastName() + " " + clientDTO.getFirstName() + " " + "is not eligible for registration because: " + failReason;
        }
        return createPdf(content, clientDTO);
    }
}
