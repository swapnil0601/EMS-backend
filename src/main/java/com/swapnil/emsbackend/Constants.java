package com.swapnil.emsbackend;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;
import io.jsonwebtoken.security.Keys;

public class Constants {
    public static final byte[] API_SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded();
    public static final long API_TOKEN_VALIDITY = 30 * 60 * 1000;

    public static Map<String, Object> validateToken(String token) {

        Map<String, Object> map = new HashMap<>();

        if (token == null || token.trim().isEmpty()) {
            map.put("valid", false);
            return map;
        }

        String[] chunks = token.split("\\.");
        String tokenWithoutSignature = chunks[0] + "." + chunks[1];
        String signature = chunks[2];
        SignatureAlgorithm sa = SignatureAlgorithm.HS256;

        SecretKeySpec secretKeySpec = new SecretKeySpec(Constants.API_SECRET_KEY, sa.getJcaName());
        DefaultJwtSignatureValidator validator = new DefaultJwtSignatureValidator(sa, secretKeySpec);

        if (!validator.isValid(tokenWithoutSignature, signature)) {
            map.put("valid", false);
            return map;
        }

        Base64.Decoder decoder = Base64.getUrlDecoder();
        String header = new String(decoder.decode(chunks[0]));
        String payload = new String(decoder.decode(chunks[1]));

        map.put("valid", true);

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode claimsNode = objectMapper.readTree(payload);

            int userId = claimsNode.get("userId").asInt();
            String email = claimsNode.get("email").asText();
            String firstName = claimsNode.get("firstName").asText();
            String lastName = claimsNode.get("lastName").asText();
            String role = claimsNode.get("role").asText();

            map.put("userId", userId);
            map.put("email", email);
            map.put("firstName", firstName);
            map.put("lastName", lastName);
            map.put("role", role);

            if (claimsNode.has("studentId")) {
                map.put("studentId", claimsNode.get("studentId").asInt());
            }
            if (claimsNode.has("childId")) {
                map.put("childId", claimsNode.get("childId").asInt());
            }

        } catch (Exception e) {

        }
        return map;
    }
}
