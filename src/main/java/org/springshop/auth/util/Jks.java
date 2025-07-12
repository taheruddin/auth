package org.springshop.auth.util;

import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.encrypt.KeyStoreKeyFactory;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

public class Jks {
    public static RSAKey toRsa(Resource jks, String password, String alias) {
        var factory = new KeyStoreKeyFactory(jks, password.toCharArray());
        var keyPair = factory.getKeyPair(alias);
        var rsaKey = new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                .privateKey((RSAPrivateKey) keyPair.getPrivate())
                .keyID(UUID.randomUUID().toString())
                .build();
        return rsaKey;
    }
}
