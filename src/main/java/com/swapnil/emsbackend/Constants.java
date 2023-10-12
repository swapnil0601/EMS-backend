package com.swapnil.emsbackend;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;
import io.jsonwebtoken.security.Keys;

public class Constants {
    public static final byte[] API_SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded();

    public static final long TOKEN_VALIDITY = 60*30*1000;

    public static Map<String,Object> validateToken(String token){
        Map<String,Object> claims = new HashMap<>();

        String [] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();

        String header = new String(decoder.decode(chunks[0]));
        String payload = new String(decoder.decode(chunks[1]));

        SignatureAlgorithm sa = SignatureAlgorithm.HS256;
        SecretKeySpec secretKeySpec = new SecretKeySpec(API_SECRET_KEY, sa.getJcaName());
        String tokenWithoutSignature = chunks[0] + "." + chunks[1];
        String signature = chunks[2];

        DefaultJwtSignatureValidator validator = new DefaultJwtSignatureValidator(sa, secretKeySpec);

        if (!validator.isValid(tokenWithoutSignature, signature)) {
            claims.put("valid",false);
            return claims;
        }

        claims.put("valid",true);

        String[] headerParts = header.split(",");
        String[] payloadParts = payload.split(",");
        for (String part : headerParts) {
            String[] kv = part.split(":");
            claims.put(kv[0].trim(), kv[1].trim());
        }
        for (String part : payloadParts) {
            String[] kv = part.split(":");
            claims.put(kv[0].trim(), kv[1].trim());
        }

        return claims;
    }
}
