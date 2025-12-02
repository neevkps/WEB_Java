package com.wearetrying.space_cats_market.service;
import com.wearetrying.space_cats_market.config.FeatureProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

@Configuration
@Service
public class FeatureToggleService {

    private final FeatureProperties properties;

    public FeatureToggleService(FeatureProperties properties) {
        this.properties = properties;
    }
    public boolean check(String featureName) {
        return properties.getToggles().getOrDefault(featureName, false);
    }
}
