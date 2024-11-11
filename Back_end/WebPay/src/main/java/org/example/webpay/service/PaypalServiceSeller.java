package org.example.webpay.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class PaypalServiceSeller implements PaypalServiceSellerInter {

    @Value("${paypal.client-id}")
    private String clientId;  // ID client PayPal

    @Value("${paypal.client-secret}")
    private String clientSecret;  // Secret client PayPal

    @Value("${paypal.mode}")
    private String mode;  // Mode (sandbox ou live)

    private final RestTemplate restTemplate;

    public PaypalServiceSeller(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;  // Injecte RestTemplate pour les appels HTTP
    }

    public String getAccessToken() {
        // Génère l'authentification en Base64 (clientId:clientSecret)
        String auth = clientId + ":" + clientSecret;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

        // Crée les en-têtes HTTP avec l'authentification et le type de contenu
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + encodedAuth);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Crée la requête pour obtenir le token d'accès
        HttpEntity<String> request = new HttpEntity<>("grant_type=client_credentials", headers);

        // Détermine l'URL en fonction du mode (sandbox ou live)
        String url = mode.equals("live") ?
                "https://api.paypal.com/v1/oauth2/token" :
                "https://api.sandbox.paypal.com/v1/oauth2/token";

        // Envoie la requête et récupère le token
        Map<String, String> response = restTemplate.postForObject(url, request, Map.class);
        return response.get("access_token");  // Retourne le token d'accès
    }

    public void sendPayout(String recipientEmail, String amount, String currency, String note) {
        // Récupère le token d'accès
        String accessToken = getAccessToken();

        // Crée les en-têtes HTTP avec le token d'accès
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Crée le corps de la requête pour le paiement
        Map<String, Object> body = new HashMap<>();
        Map<String, String> senderBatchHeader = new HashMap<>();
        senderBatchHeader.put("sender_batch_id", "batch_" + System.currentTimeMillis());
        senderBatchHeader.put("email_subject", "You have a payment");

        Map<String, Object> payoutItem = new HashMap<>();
        payoutItem.put("recipient_type", "EMAIL");
        payoutItem.put("amount", Map.of("value", amount, "currency", currency));
        payoutItem.put("receiver", recipientEmail);
        payoutItem.put("note", note);
        payoutItem.put("sender_item_id", "item_" + System.currentTimeMillis());

        body.put("sender_batch_header", senderBatchHeader);
        body.put("items", new Object[] { payoutItem });

        // Détermine l'URL pour envoyer le paiement (sandbox ou live)
        String url = mode.equals("live") ?
                "https://api.paypal.com/v1/payments/payouts" :
                "https://api.sandbox.paypal.com/v1/payments/payouts";

        // Envoie la requête pour le paiement
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        restTemplate.postForObject(url, request, String.class);  // Effectue le paiement via l'API PayPal
    }
}
