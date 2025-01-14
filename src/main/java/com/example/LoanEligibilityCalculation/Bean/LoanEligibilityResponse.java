package com.example.LoanEligibilityCalculation.Bean;

import java.util.Map;

public class LoanEligibilityResponse {
    private boolean eligible;
    private Double approvedLoanAmount;
    private Map<String, Double> emiBreakdown;
    private String reason;



    public boolean isEligible() {
        return eligible;
    }

    public void setEligible(boolean eligible) {
        this.eligible = eligible;
    }

    public Double getApprovedLoanAmount() {
        return approvedLoanAmount;
    }

    public void setApprovedLoanAmount(Double approvedLoanAmount) {
        this.approvedLoanAmount = approvedLoanAmount;
    }

    public Map<String, Double> getEmiBreakdown() {
        return emiBreakdown;
    }

    public void setEmiBreakdown(Map<String, Double> emiBreakdown) {
        this.emiBreakdown = emiBreakdown;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "LoanEligibilityResponse{" +
                "eligible=" + eligible +
                ", approvedLoanAmount=" + approvedLoanAmount +
                ", emiBreakdown=" + emiBreakdown +
                ", reason='" + reason + '\'' +
                '}';
    }
}
