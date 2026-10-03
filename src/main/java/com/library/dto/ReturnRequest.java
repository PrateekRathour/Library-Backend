package com.library.dto;

public class ReturnRequest {
    private String remarks;

    public ReturnRequest() {}

    public ReturnRequest(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
