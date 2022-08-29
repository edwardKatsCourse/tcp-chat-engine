package com.company.common;

import java.io.Serializable;

public record PayloadDTO(ClientType clientType, String message) implements Serializable {
}
