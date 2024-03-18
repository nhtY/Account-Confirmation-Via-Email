package com.nihat.loginregister.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final DataSource dataSource;

    @GetMapping
    public String test() {

        try {
            DatabaseMetaData metaData =  dataSource.getConnection().getMetaData();
            return "Connected to the database. URL:" + metaData.getURL();
        } catch (Exception e) {
            return "Error: " + e;
        }

    }
}
