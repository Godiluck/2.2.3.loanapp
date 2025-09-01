package com.example.loan.service;

import com.example.loan.model.Car;
import com.example.loan.model.User;
import com.example.loan.repository.CarRepository;
import com.example.loan.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final UserRepository userRepository;
    private final CarRepository carRepository;

    @Value("${loan.minimalIncome}")
    private int minimalIncome;

    @Value("${loan.minimalCarPrice}")
    private int minimalCarPrice;

    @Value("${loan.incomePercentage}")
    private double maxLoanPercentage;

    @Value("${loan.carPricePercentage}")
    private double maxCarLoanPercentage;

    public int getApprovedLoanAmount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь с ID " + userId + " не найден"));
        Car car = null;
        if (user.getCar() != null) {
            car = carRepository.findById(user.getCar().getId()).orElse(null);
        }
        return approveLoan(user, car);
    }

    public int approveLoan(User user, Car car) {
        int yearIncome = user.getIncome() * 12;
        boolean hasHighIncome = user.getIncome() > minimalIncome;
        boolean hasExpensiveCar = car != null && car.getPrice() != null && car.getPrice() > minimalCarPrice;

        if (!(hasHighIncome || hasExpensiveCar)) {
            return 0;
        }

        int maxByIncome = (int) (yearIncome * maxLoanPercentage);
        int maxByCar = hasExpensiveCar ? (int) (car.getPrice() * maxCarLoanPercentage) : 0;

        return Math.max(maxByIncome, maxByCar);
    }
}
