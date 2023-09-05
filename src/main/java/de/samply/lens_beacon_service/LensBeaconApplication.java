package de.samply.lens_beacon_service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import de.samply.lens_beacon_service.Configuration;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootApplication
@Slf4j
public class LensBeaconApplication {
    public static void main(String[] args) {
        var context = SpringApplication.run(LensBeaconApplication.class, args);
        Configuration configuration = context.getBean(Configuration.class);
        GlobalVariables.configuration = configuration;
    }
}
