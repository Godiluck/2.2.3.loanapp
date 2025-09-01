package com.example.loan;

import com.example.loan.model.Car;
import com.example.loan.model.User;
import com.example.loan.repository.CarRepository;
import com.example.loan.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.incomestarter.starter.IncomeClient;
import com.example.incomestarter.dto.IncomeResponseDto;

import java.util.Random;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final CarRepository carRepository;
    private final IncomeClient incomeClient;


    @Value("${loan.carMinPriceRange}")
    private Integer carMinPriceRange;

    @Value("${loan.carMaxPriceRange}")
    private Integer carMaxPriceRange;

    @PostConstruct
    public void loadData() {
        IncomeResponseDto[] incomeResponses = incomeClient.fetchIncomeResponse();

        if (incomeResponses == null) {
            return;
        }

        for (IncomeResponseDto incomeResponse : incomeResponses) {
            Car car = new Car(new Random().nextInt(carMaxPriceRange - carMinPriceRange + 1) + carMinPriceRange);
            carRepository.save(car);

            User user = new User(incomeResponse.getIncome(), car);

            userRepository.save(user);
        }
    }
}
