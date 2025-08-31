package com.example.loan.service;

import com.example.loan.model.Car;
import com.example.loan.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LoanService {

    @Value("${loan.minimalIncome}")
    private int minimalIncome;

    @Value("${loan.minimalCarPrice}")
    private int minimalCarPrice;

    @Value("${loan.incomePercentage}")
    private double maxLoanPercentage;

    @Value("${loan.carPricePercentage}")
    private double maxCarLoanPercentage;

    public int approveLoan(User user, Car car) {
        int yearIncome = user.getIncome() * 12;

        boolean hasHighIncome = user.getIncome() > minimalIncome;
        boolean hasExpensiveCar = car != null && car.getPrice() != null && car.getPrice() > minimalCarPrice;

        if (!(hasHighIncome || hasExpensiveCar)) {
            return 0;
        }

        int maxByIncome = (int)(yearIncome * maxLoanPercentage);

        int maxByCar = hasExpensiveCar ? (int)(car.getPrice() * maxCarLoanPercentage) : 0;

        int approvedAmount = Math.max(maxByIncome, maxByCar);

        return Math.max(approvedAmount, 0);
    }
}
