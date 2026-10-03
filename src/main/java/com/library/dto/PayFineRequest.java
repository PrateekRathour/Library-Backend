package com.library.dto;

import jakarta.validation.constraints.NotBlank;

public class PayFineRequest {
    @NotBlank(message = "Payment method is required")
    private String paymentMethod; // CASH, CARD, UPI, ONLINE

    private String transactionId;

    public PayFineRequest() {}

    public PayFineRequest(String paymentMethod, String transactionId) {
        this.paymentMethod = paymentMethod;
        this.transactionId = transactionId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
