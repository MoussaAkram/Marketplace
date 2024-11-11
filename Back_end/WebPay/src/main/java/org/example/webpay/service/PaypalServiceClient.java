package org.example.webpay.service;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@Transactional
@RequiredArgsConstructor
public class PaypalServiceClient implements PaypalServiceClientInter {

    private final APIContext apiContext;  // APIContext injecté pour la communication PayPal

    public Payment createPayment(
            Double total,
            String currency,
            String method,
            String intent,
            String description,
            String cancelUrl,
            String successUrl
    ) throws PayPalRESTException {
        // Montant du paiement
        Amount amount = new Amount();
        amount.setCurrency(currency);
        amount.setTotal(String.format(Locale.forLanguageTag(currency), "%.2f", total));  // Format montant

        // Crée une transaction
        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);

        // Liste de transactions
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        // Crée un payer (utilisateur) avec le mode de paiement
        Payer payer = new Payer();
        payer.setPaymentMethod(method);

        // Crée le paiement
        Payment payment = new Payment();
        payment.setIntent(intent);  // Intent: sale ou authorize
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        // Redirections
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);

        return payment.create(apiContext);  // Crée le paiement via PayPal API
    }

    public Payment executePayment(
            String paymentId,
            String payerId
    ) throws PayPalRESTException {
        // Exécute un paiement avec l'ID de paiement et l'ID du payeur
        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        return payment.execute(apiContext, paymentExecution);  // Exécute le paiement via PayPal API
    }
}
