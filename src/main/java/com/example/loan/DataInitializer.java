package com.example.loan;

import com.example.loan.model.Car;
import com.example.loan.model.User;
import com.example.loan.repository.CarRepository;
import com.example.loan.repository.UserRepository;
import com.example.loan.service.CustomTableService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.incomestarter.starter.IncomeClient;
import com.example.incomestarter.model.IncomeRecord;

import java.util.Random;

@Component
public class DataInitializer {

    @Autowired
    private CustomTableService customTableService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CarRepository carRepository;

    @Value("${loan.carMinPriceRange}")
    private Integer carMinPriceRange;

    @Value("${loan.carMaxPriceRange}")
    private Integer carMaxPriceRange;

    @Autowired
    private IncomeClient incomeClient;


    @PostConstruct
    public void loadData() {
        customTableService.deleteAllAndResetAutoIncrement("users", "users_id_seq");
        customTableService.deleteAllAndResetAutoIncrement("cars", "cars_id_seq");

        IncomeRecord[] incomeRecords = incomeClient.fetchIncomeRecords();

        if (incomeRecords == null) {
            return;
        }

        for (IncomeRecord incomeRecord : incomeRecords) {
            Car car = new Car(new Random().nextInt(carMaxPriceRange - carMinPriceRange + 1) + carMinPriceRange);
            carRepository.save(car);

            User user = new User(incomeRecord.getIncome(), car);

            userRepository.save(user);
        }
    }
}
