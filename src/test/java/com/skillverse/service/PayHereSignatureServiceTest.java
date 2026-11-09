package com.skillverse.service;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PayHereSignatureServiceTest {

    private static final String MERCHANT_ID = "121XXXX";
    private static final String SECRET = "secret-value";

    private final PayHereSignatureService service = new PayHereSignatureService(MERCHANT_ID, SECRET);

    @Test
    void acceptsTheDocumentedChecksum() {
        String sig = md5Upper(MERCHANT_ID + "SV-1" + "1000.00" + "LKR" + "2" + md5Upper(SECRET));
        assertTrue(service.verify(MERCHANT_ID, "SV-1", "1000.00", "LKR", "2", sig));
        assertTrue(service.verify(MERCHANT_ID, "SV-1", "1000.00", "LKR", "2", sig.toLowerCase()));
    }

    @Test
    void rejectsTamperedFields() {
        String sig = md5Upper(MERCHANT_ID + "SV-1" + "1000.00" + "LKR" + "2" + md5Upper(SECRET));
        assertFalse(service.verify(MERCHANT_ID, "SV-1", "1.00", "LKR", "2", sig));
        assertFalse(service.verify(MERCHANT_ID, "SV-2", "1000.00", "LKR", "2", sig));
        assertFalse(service.verify(MERCHANT_ID, "SV-1", "1000.00", "LKR", "-2", sig));
        assertFalse(service.verify("other-merchant", "SV-1", "1000.00", "LKR", "2", sig));
        assertFalse(service.verify(MERCHANT_ID, "SV-1", "1000.00", "LKR", "2", null));
    }

    @Test
    void failsClosedWhenNotConfigured() {
        PayHereSignatureService unconfigured = new PayHereSignatureService("", "");
        String sig = md5Upper("" + "SV-1" + "1000.00" + "LKR" + "2" + md5Upper(""));
        assertFalse(unconfigured.isConfigured());
        assertFalse(unconfigured.verify("", "SV-1", "1000.00", "LKR", "2", sig));
    }

    private static String md5Upper(String value) {
        try {
            byte[] hash = MessageDigest.getInstance("MD5").digest(value.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().withUpperCase().formatHex(hash);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
