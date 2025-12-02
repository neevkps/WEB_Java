package com.wearetrying.space_cats_market.aspect;
import com.wearetrying.space_cats_market.annotation.FeatureToggle;
import com.wearetrying.space_cats_market.service.exception.FeatureNotAvailableException;
import com.wearetrying.space_cats_market.service.FeatureToggleService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class FeatureToggleAspect {

    private final FeatureToggleService featureToggleService;

    public FeatureToggleAspect(FeatureToggleService featureToggleService) {
        this.featureToggleService = featureToggleService;
    }

    @Around("@annotation(featureToggle)")
    public Object checkFeature(ProceedingJoinPoint joinPoint, FeatureToggle featureToggle) throws Throwable {

        String featureName = featureToggle.value();

        if (featureToggleService.check(featureName)) {
            return joinPoint.proceed();
        } else {
            throw new FeatureNotAvailableException("Фіча '" + featureName + "' зараз вимкнена! 😿");
        }
    }
}