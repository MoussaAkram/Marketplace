package org.example.webpay.Config;

import com.paypal.base.rest.APIContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class PaypalConfig {

    @Value("${paypal.client-id}")
    private String clientId;  // ID client PayPal

    @Value("${paypal.client-secret}")
    private String clientSecret;  // Secret client PayPal

    @Value("${paypal.mode}")
    private String mode;  // Mode (sandbox ou live)

    @Bean
    public APIContext apiContext() {
        return new APIContext(clientId, clientSecret, mode);  // Crée un APIContext avec les infos PayPal
    }
}

