package com.example.LoanEligibilityCalculation.Bean;

import java.util.List;

public class LoanEligibilityArray {
    List<LoanEligibilityRequest> LoanEligibilityArray;

    public List<LoanEligibilityRequest> getLoanEligibilityArray() {
        return LoanEligibilityArray;
    }

    public void setLoanEligibilityArray(List<LoanEligibilityRequest> loanEligibilityArray) {
        LoanEligibilityArray = loanEligibilityArray;
    }

    @Override
    public String toString() {
        return "LoanEligibilityArray{" +
                "LoanEligibilityArray=" + LoanEligibilityArray +
                '}';
    }
}
