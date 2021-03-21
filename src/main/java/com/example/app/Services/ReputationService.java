package com.example.app.Services;

import com.example.app.DTOs.ReputationDTO;
import com.example.app.Enums.ReputationEnum;
import com.example.app.Utils.Utils;
import org.apache.http.HttpException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class ReputationService {


    /**
     * This method gets the client reputation from an external system (online mock server : Mockable.io)
     *
     * @param clientId the client personalIdentityCode
     * @return ReputationDTO if exists else null
     * @throws IOException
     * @throws HttpException
     */
    ReputationDTO getReputation(String clientId) throws IOException, HttpException {
        URL url = new URL("http://demo7164456.mockable.io/get-reputation/" + clientId);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        Utils.prepareRequest(connection, "GET", "application/json");
        connection.connect();
        try {

            return Utils.handleResponse(connection, ReputationDTO.class);
        } catch (Exception e) {
            return null;
        } finally {
            connection.disconnect();
        }
    }

    /**
     * This method checks the clinet reputations using the below rules
     *
     * @param reputationValue
     * @return
     */
    ReputationEnum checkReputation(int reputationValue) {

        if (reputationValue >= 0 && reputationValue <= 20) {
            return ReputationEnum.CANDIDATE_WITH_NO_RISK;
        }

        if (reputationValue >= 21 && reputationValue <= 99) {
            return ReputationEnum.CANDIDATE_WITH_MEDIUM_RISK_BUT_ENROLLMENT_STILL_POSSIBLE;
        }

        if (reputationValue > 99) {
            return ReputationEnum.RISKY_CANDIDATE_ENROLLMENT_NOT_ACCEPTABLE;
        }

        return null;
    }
}
