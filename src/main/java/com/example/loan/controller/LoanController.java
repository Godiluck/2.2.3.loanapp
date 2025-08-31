package com.example.loan.controller;

import com.example.loan.model.Car;
import com.example.loan.model.User;
import com.example.loan.repository.CarRepository;
import com.example.loan.repository.UserRepository;
import com.example.loan.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/loan")
public class LoanController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private LoanService loanService;

    @GetMapping
    public ResponseEntity<?> getUserMaxLoan(@RequestParam Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Пользователь с ID " + userId + " не найден");
        }
        User user = userOpt.get();
        Car car = null;
        if (user.getCar() != null) {
            Optional<Car> carOpt = carRepository.findById(user.getCar().getId());
            if (carOpt.isPresent()) {
                car = carOpt.get();
            }
        }

        int approvedAmount = loanService.approveLoan(user, car);
        return ResponseEntity.ok(approvedAmount);
    }
}
