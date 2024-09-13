package org.example.msauth.util;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.msauth.exception.AuthException;
import org.example.msauth.model.constants.AuthConstants;
import org.example.msauth.model.enums.ExceptionConstants;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import static org.example.msauth.model.enums.ExceptionConstants.USER_UNAUTHORIZED;
import static org.springframework.util.Base64Utils.decodeFromString;

@Slf4j
public enum CertificateKeyUtil {
    CERTIFICATE_KEY_UTIL;
    public KeyPair generateKeyPair() {
        try {
            var keyPairGen = KeyPairGenerator.getInstance(AuthConstants.RSA);
            keyPairGen.initialize(AuthConstants.KEY_SIZE);
            return keyPairGen.generateKeyPair();
        }
        catch (NoSuchAlgorithmException e) {
            log.error("ActionLog.generateKeyPair.error ", e);
            throw new AuthException(USER_UNAUTHORIZED.getCode(),USER_UNAUTHORIZED.getMessage(),401);
        }

    }
    @SneakyThrows
    public PublicKey getPublicKey(String publicKey) {
        var keyFactory = KeyFactory.getInstance(AuthConstants.RSA);
        var keySpec = new X509EncodedKeySpec(decodeFromString(publicKey));
        return keyFactory.generatePublic(keySpec);
    }
}
