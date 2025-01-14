package com.example.LoanEligibilityCalculation.Bean;

import jakarta.validation.constraints.Min;
import org.springframework.lang.NonNull;

public class LoanEligibilityRequest {
    private String userId;
    @NonNull
    @Min(30000)
    private long monthlyIncome;
    @NonNull
    private long existingLoanObligations;

    @NonNull
    @Min(700)
    private int creditScore;
    @NonNull
    private long requestedLoanAmount;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(long monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public long getExistingLoanObligations() {
        return existingLoanObligations;
    }

    public void setExistingLoanObligations(long existingLoanObligations) {
        this.existingLoanObligations = existingLoanObligations;
    }

    public int getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(int creditScore) {
        this.creditScore = creditScore;
    }

    public long getRequestedLoanAmount() {
        return requestedLoanAmount;
    }

    public void setRequestedLoanAmount(long requestedLoanAmount) {
        this.requestedLoanAmount = requestedLoanAmount;
    }

    @Override
    public String toString() {
        return "TransactionBean{" +
                "userId='" + userId + '\'' +
                ", monthlyIncome=" + monthlyIncome +
                ", existingLoanObligations=" + existingLoanObligations +
                ", creditScore=" + creditScore +
                ", requestedLoanAmount=" + requestedLoanAmount +
                '}';
    }
}
