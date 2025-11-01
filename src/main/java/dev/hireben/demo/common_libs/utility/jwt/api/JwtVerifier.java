package dev.hireben.demo.common_libs.utility.jwt.api;

import io.jsonwebtoken.Claims;

public interface JwtVerifier {

  Claims verifyToken(String token);

}
