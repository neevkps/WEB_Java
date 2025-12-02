package com.wearetrying.space_cats_market;


import com.wearetrying.space_cats_market.service.FeatureToggleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class SpaceCatsMarketApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpaceCatsMarketApplication.class, args);

    }
    @Bean
    public CommandLineRunner testToggles(FeatureToggleService service) {
        return args -> {
            System.out.println("--- ПЕРЕВІРКА ФІЧ ---");
            System.out.println("cosmoProducts активна? " + service.check("cosmoProducts"));
            System.out.println("blackHoleDiscounts активна? " + service.check("blackHoleDiscounts"));
            System.out.println("---------------------");
        };
    }

}
