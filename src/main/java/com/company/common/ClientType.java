package com.company.common;

import java.util.Arrays;

public enum ClientType {

    READ(1),
    WRITE(2),
    ADMIN(3)
    ;

    private final int typeId;

    ClientType(int typeId) {
        this.typeId = typeId;
    }

    public int getTypeId() {
        return typeId;
    }

    public static ClientType findByTypeId(int typeId) {
        return Arrays.stream(ClientType.values())
                .filter(x -> x.getTypeId() == typeId)
                .findFirst()
                .orElse(null);
    }

}
