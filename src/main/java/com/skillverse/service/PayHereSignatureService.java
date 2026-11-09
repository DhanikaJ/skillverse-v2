package com.skillverse.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/**
 * Verifies the {@code md5sig} checksum that PayHere attaches to payment notifications.
 *
 * <pre>
 * md5sig = UPPER( MD5( merchant_id + order_id + payhere_amount + payhere_currency
 *                      + status_code + UPPER( MD5( merchant_secret ) ) ) )
 * </pre>
 *
 * When no merchant credentials are configured, verification always fails (fail closed).
 */
@Service
public class PayHereSignatureService {

    private final String merchantId;
    private final String merchantSecret;

    public PayHereSignatureService(
            @Value("${payhere.merchant-id:}") String merchantId,
            @Value("${payhere.merchant-secret:}") String merchantSecret
    ) {
        this.merchantId = merchantId == null ? "" : merchantId.trim();
        this.merchantSecret = merchantSecret == null ? "" : merchantSecret.trim();
    }

    /** True when both the merchant ID and secret are configured. */
    public boolean isConfigured() {
        return !merchantId.isEmpty() && !merchantSecret.isEmpty();
    }

    /**
     * Checks that the notification belongs to this merchant and that its checksum matches.
     * Values must be passed exactly as received (the amount string is part of the checksum).
     */
    public boolean verify(String receivedMerchantId, String orderId, String amount,
                          String currency, String statusCode, String receivedSignature) {
        if (!isConfigured()
                || receivedMerchantId == null || orderId == null || amount == null
                || currency == null || statusCode == null || receivedSignature == null) {
            return false;
        }
        if (!merchantId.equals(receivedMerchantId)) {
            return false;
        }

        String expected = md5Upper(receivedMerchantId + orderId + amount + currency + statusCode
                + md5Upper(merchantSecret));
        return MessageDigest.isEqual(
                expected.getBytes(StandardCharsets.UTF_8),
                receivedSignature.trim().toUpperCase().getBytes(StandardCharsets.UTF_8));
    }

    private static String md5Upper(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] hash = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().withUpperCase().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("MD5 algorithm not available", e);
        }
    }
}
