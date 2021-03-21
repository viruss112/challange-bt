package com.example.app.Services;

import com.example.app.DTOs.ClientDTO;
import com.example.app.DTOs.ClientStatusDTO;
import com.example.app.DTOs.ReputationDTO;
import com.example.app.Entities.Report;
import com.example.app.Enums.ClientStatusEnum;
import com.example.app.Enums.ReputationEnum;
import com.example.app.Repositories.ReportRepository;
import com.itextpdf.text.DocumentException;
import org.apache.pdfbox.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

/**
 * Here it is the register logic
 */

@Service
public class RegisterService {


    private final ClientService clientService;
    private final ReputationService reputationService;
    private final PdfService pdfService;
    private final ReportRepository reportRepository;


    @Autowired
    public RegisterService(ClientService clientService, ReputationService reputationService, PdfService pdfService, ReportRepository reportRepository) {
        this.clientService = clientService;
        this.reputationService = reputationService;
        this.pdfService = pdfService;
        this.reportRepository = reportRepository;
    }

    /**
     * @param clientDTO: the client information from the UI
     * @return ClientStatusDTO
     * @throws IOException
     * @throws DocumentException
     */

    public ClientStatusDTO register(ClientDTO clientDTO) throws IOException, DocumentException {

        ClientStatusDTO clientStatusDTO = new ClientStatusDTO();
        clientStatusDTO.setStatus(ClientStatusEnum.NOT_ELIGIBLE_FOR_ENROLMENT);

        String failReason = "";

        try {
            //get the client from the external system
            ClientDTO client = clientService.getClient(clientDTO.getPersonalIdentityCode());

            //check if client is already registered
            if (client == null) {

                //check the client's id card validity
                if (clientService.isValid(clientDTO.getValidTo())) {
                    ReputationDTO reputation = reputationService.getReputation(clientDTO.getPersonalIdentityCode());

                    //check the client's reputation
                    if (reputation != null) {
                        ReputationEnum rep = reputationService.checkReputation(Integer.parseInt(reputation.getReputation()));
                        if (rep != null) {
                            if (rep != ReputationEnum.RISKY_CANDIDATE_ENROLLMENT_NOT_ACCEPTABLE) {
                                clientStatusDTO.setStatus(ClientStatusEnum.ELIGIBLE_FOR_ENROLMENT);
                            } else {
                                failReason = "client is a risky candidate";
                            }
                        }
                    }
                } else {
                    failReason = "card is expired";
                }

            } else {
                failReason = "client is already registered";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        //create the pdf report for that client
        InputStream documentCreated = pdfService.setPdfContent(clientStatusDTO, clientDTO, failReason);

        //normally in the problem it says that after the client and frontdesk employee signs, the document should be submitted back into the system,
        //in this case after the register method is called into the controller, the frontdesk employee must upload the pdf report signed and
        //upload it => make another rest call to save it in the DB.

        // In this case that I don't have the client implemented, I can't send the byte array from swagger, so for test , after the client status is returned in this method,
        //I will save the document into the DB
        String fileName = pdfService.getFileName(clientDTO);
        saveReport(fileName, convertInputStreamToByteArray(documentCreated));
        return clientStatusDTO;
    }


    /**
     * @param fileName the pdf report name
     * @param document document that will be saved into the DB
     */
    private void saveReport(String fileName, byte[] document) {

        Report report = new Report();

        report.setDate(LocalDateTime.now());
        report.setFileName(fileName);
        report.setInfo(document);

        reportRepository.save(report);

    }

    /**
     * @param report report that will be converted into byte array
     * @return
     * @throws IOException
     */
    private byte[] convertInputStreamToByteArray(InputStream report) throws IOException {

        return IOUtils.toByteArray(report);
    }
}
