package com.example.app.Controllers;

import com.example.app.DTOs.ClientDTO;
import com.example.app.DTOs.ClientStatusDTO;
import com.example.app.Services.ClientService;
import com.example.app.Services.RegisterService;
import com.example.app.Services.ReputationService;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Controller for registering the clients
 */
@RestController
@RequestMapping("/client")
public class RegisterController {


    private final ReputationService reputationService;
    private final ClientService clientService;
    private final RegisterService registerService;

    @Autowired
    public RegisterController(ReputationService reputationService, ClientService clientService, RegisterService registerService) {
        this.reputationService = reputationService;
        this.clientService = clientService;
        this.registerService = registerService;
    }


    @PostMapping("/register-client")
    public ClientStatusDTO register(@RequestBody ClientDTO clientDTO) throws IOException, DocumentException {
        return registerService.register(clientDTO);

    }
}
