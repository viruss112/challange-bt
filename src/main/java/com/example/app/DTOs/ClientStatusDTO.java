package com.example.app.DTOs;

import com.example.app.Enums.ClientStatusEnum;
import lombok.Data;

/**
 * This class represents the client's status
 */

@Data
public class ClientStatusDTO {

    private ClientStatusEnum status;

}
