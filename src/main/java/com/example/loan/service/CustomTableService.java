package com.example.loan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class CustomTableService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void deleteAllAndResetAutoIncrement(String tableName, String sequenceName) {
        jdbcTemplate.execute("DELETE FROM " + tableName);
        jdbcTemplate.execute("ALTER SEQUENCE " + sequenceName + " RESTART WITH 1");
    }
}
