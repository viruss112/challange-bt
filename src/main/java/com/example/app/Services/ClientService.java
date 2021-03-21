package com.example.app.Services;

import com.example.app.DTOs.ClientDTO;
import com.example.app.Utils.Utils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

@Service
public class ClientService {

    /**
     * This method gets the client from an external system (online mock server : MockLab.io)
     *
     * @param clientId: the client personalIdentityCode
     * @throws IOException
     * @return: ClientDTO if exists else null
     */
    ClientDTO getClient(String clientId) throws IOException {
        URL url = new URL("http://8g2de.mocklab.io/client/" + clientId);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        Utils.prepareRequest(connection, "GET", "application/json");
        connection.connect();
        try {

            return Utils.handleResponse(connection, ClientDTO.class);
        } catch (Exception e) {
            return null;
        } finally {
            connection.disconnect();
        }
    }

    boolean isValid(Date validTo) {
        return !new Date().after(validTo);
    }
}
