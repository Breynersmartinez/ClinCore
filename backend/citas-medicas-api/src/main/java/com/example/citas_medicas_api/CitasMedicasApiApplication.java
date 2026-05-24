package com.example.citas_medicas_api;


import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@RequiredArgsConstructor
public class CitasMedicasApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CitasMedicasApiApplication.class, args);
    }


}