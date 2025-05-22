package com.multitenancy.back.common.model;

import lombok.Data;

@Data
public class RequestModel {
    private String userId;
    private int type;
}
