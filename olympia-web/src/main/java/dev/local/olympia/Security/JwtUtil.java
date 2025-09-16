package dev.local.olympia.Security;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    private final String jwtSecret = "ReallySecureSecretKeyForJwtSigning09023890134901239081234535183974501239847502983745134870561345";
    private final long jwtExpirationMs = 86400000; // 1 day

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUsernameFromJwt(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).build().parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).build().parseClaimsJws(authToken);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
