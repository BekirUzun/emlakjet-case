package com.emlakjet.ecommerce.config;

import com.emlakjet.ecommerce.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomAuditorAware implements AuditorAware<String> {

    private final AuthUtil authUtil;

    @Override
    public Optional<String> getCurrentAuditor() {
        var userId = authUtil.getUserId();
        if (StringUtils.isEmpty(userId)) {
            return Optional.empty();
        }
        return Optional.of(userId);
    }

}