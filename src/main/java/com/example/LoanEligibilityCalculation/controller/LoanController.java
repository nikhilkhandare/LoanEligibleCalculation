package com.example.LoanEligibilityCalculation.controller;

import com.example.LoanEligibilityCalculation.Service.LoanService;
import com.nimbusds.jose.shaded.gson.JsonArray;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
public class LoanController {
    @Autowired
    LoanService loanService;

    @GetMapping(value = "/loan/eligibility", produces = "application/json")
    public String checkLoanEligibilty() {
        return loanService.LoanPayLoadApiCall();
    }

    @GetMapping(value = "/admin/loan-statistics", produces = "application/json")
    public String loanStatistics() {
        String response = loanService.LoanPayLoadApiCall();

        List<Double> approveLoanAmtList = new ArrayList<>();
        int csRejectCount = 0;
        int miRejectCount = 0;
        int miExccedRejectCount = 0;
        Double totalApproveLoanAmt = 0.0;
        JsonObject responseObj = new JsonObject();

        if (Objects.nonNull(response)) {
            JsonArray responseArray = JsonParser.parseString(response).getAsJsonObject().get("data").getAsJsonArray();
            for (int i = 0; i < responseArray.size(); i++) {
                if (responseArray.get(i).getAsJsonObject().get("Eligible").getAsBoolean() == true) {
                    approveLoanAmtList.add(responseArray.get(i).getAsJsonObject().get("Loan-Amount").getAsDouble());
                } else if (responseArray.get(i).getAsJsonObject().get("Reason").getAsString().contains("Credit score is below the required minimum")) {
                    csRejectCount++;
                } else if (responseArray.get(i).getAsJsonObject().get("Reason").getAsString().contains("Existing loan obligations exceed 40% of monthly income")) {
                    miExccedRejectCount++;
                } else if (responseArray.get(i).getAsJsonObject().get("Reason").getAsString().contains("Monthly income is below the required minimum")) {
                    miRejectCount++;
                }
            }

            for (int i = 0; i < approveLoanAmtList.size(); i++) {
                totalApproveLoanAmt += approveLoanAmtList.get(i);
            }
            double ApproveLoanAmt = totalApproveLoanAmt / approveLoanAmtList.size();
            JsonObject res = new JsonObject();
            res.addProperty("averageApprovedLoanAmount", String.format("%.2f", ApproveLoanAmt));
            JsonObject rejectionReason = new JsonObject();
            rejectionReason.addProperty("Credit score is below the required minimum", csRejectCount);
            rejectionReason.addProperty("Existing loan obligations exceed 40% of monthly income", miExccedRejectCount);
            rejectionReason.addProperty("Monthly income is below the required minimum", miRejectCount);
            res.add("rejectionsReasons", rejectionReason);
            responseObj.add("response", res);
        }
        return responseObj.toString();
    }
}

