package com.example.baro;

import com.example.baro.common.entity.Promise;
import com.example.baro.common.entity.PromisePersonal;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
public class BaroApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaroApplication.class, args);
    }

}
