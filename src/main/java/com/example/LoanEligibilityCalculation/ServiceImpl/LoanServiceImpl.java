package com.example.LoanEligibilityCalculation.ServiceImpl;

import com.example.LoanEligibilityCalculation.Bean.LoanEligibilityResponse;
import com.example.LoanEligibilityCalculation.Service.LoanService;
import com.nimbusds.jose.shaded.gson.JsonArray;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LoanServiceImpl implements LoanService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${LoanAPIUrl}")
    String loanApiUrl;

    private static final double MAX_LOAN_MULTIPLIER = 10.0;
    private static final double MAX_DEBT_TO_INCOME_RATIO = 0.4;

    @Override
    public String LoanPayLoadApiCall() {
        String responsObject = restTemplate.getForObject(loanApiUrl, String.class);
        JsonArray responseArray = JsonParser.parseString(responsObject).getAsJsonArray();
        if (responseArray.size() == 0) {
            throw new RuntimeException("Fail to fetch the data");
        }
        return calculateLoanEligibility(responseArray).toString();
    }


    public JsonObject calculateLoanEligibility(JsonArray loanEligibilityRequest) {
        JsonObject responseObj = new JsonObject();


        double approvedLoanAmount = 0.0;
        JsonArray jsonArray = new JsonArray();
        Map<String, String> emiBreakdown = new HashMap<>();
        JsonObject json;

        for (int i = 0; i < loanEligibilityRequest.getAsJsonArray().size(); i++) {
            if (loanEligibilityRequest.get(i).getAsJsonObject().get("monthlyIncome").getAsInt() < 30000) {
                json = new JsonObject();
                json.addProperty("Eligible", false);
                json.addProperty("Reason", "Monthly income is below the required minimum.");
                String userId = loanEligibilityRequest.get(i).getAsJsonObject().get("userId").getAsString();
                json.addProperty("UserId", userId);
                jsonArray.add(json);
            } else if (loanEligibilityRequest.get(i).getAsJsonObject().get("existingLoanObligations").getAsInt() > loanEligibilityRequest.get(i).getAsJsonObject().get("monthlyIncome").getAsInt() * MAX_DEBT_TO_INCOME_RATIO) {
                json = new JsonObject();
                json.addProperty("Eligible", false);
                json.addProperty("Reason", "Existing loan obligations exceed 40% of monthly income.");
                String userId = loanEligibilityRequest.get(i).getAsJsonObject().get("userId").getAsString();
                json.addProperty("UserId", userId);
                jsonArray.add(json);
            } else if (loanEligibilityRequest.get(i).getAsJsonObject().get("creditScore").getAsInt() < 700) {
                json = new JsonObject();
                json.addProperty("Eligible", false);
                json.addProperty("Reason", "Credit score is below the required minimum.");
                String userId = loanEligibilityRequest.get(i).getAsJsonObject().get("userId").getAsString();
                json.addProperty("UserId", userId);
                jsonArray.add(json);
            } else {
                json = new JsonObject();
                approvedLoanAmount = Math.min(loanEligibilityRequest.get(i).getAsJsonObject().get("requestedLoanAmount").getAsInt(), loanEligibilityRequest.get(i).getAsJsonObject().get("monthlyIncome").getAsInt() * MAX_LOAN_MULTIPLIER);
                double principal = approvedLoanAmount;
                int tenureInYears = 12;
                int tenureInMonths = tenureInYears * 12;
                String emiFor8 = calculateEMI(principal, tenureInMonths, 8.0);
                String emiFor10 = calculateEMI(principal, tenureInMonths, 10.0);
                String emiFor12 = calculateEMI(principal, tenureInMonths, 12.0);
                emiBreakdown.put("8%", emiFor8);
                emiBreakdown.put("10%", emiFor10);
                emiBreakdown.put("12%", emiFor12);
                json.addProperty("Eligible", true);
                json.addProperty("Loan-Amount", approvedLoanAmount);
                json.addProperty("EMI-Breakdown", emiBreakdown.toString());

                String userId = loanEligibilityRequest.get(i).getAsJsonObject().get("userId").getAsString();
                json.addProperty("UserId", userId);
                jsonArray.add(json);
            }
        }
        responseObj.add("data", jsonArray);
        return responseObj;
    }

    public String calculateEMI(double principal, int tenureInMonths, double interest) {
        double monthlyInterestRate = (interest / 100) / 12;
        double ans = (principal * monthlyInterestRate * Math.pow(1 + monthlyInterestRate, tenureInMonths)) / (Math.pow(1 + monthlyInterestRate, tenureInMonths) - 1);
        return String.format("%.2f", ans);
    }


}
