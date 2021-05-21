package app.rest

import app.config.AuthConfig
import app.util.jwt.*
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import mu.KLogging
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.time.Duration
import java.time.Instant

@RestController
class AuthorizationServerApiController(
    private val authConfig: AuthConfig
) {
    companion object : KLogging()

    private val jwksJSON: String = authConfig.rsaKey.toPublicJWKSetJSONString()

    /**
     * authorization server
     * see: https://github.com/spring-projects-experimental/spring-authorization-server/blob/main/oauth2-authorization-server/src/main/java/org/springframework/security/oauth2/server/authorization/web/NimbusJwkSetEndpointFilter.java
     */
    @GetMapping("/.well-known/jwks.json")
    fun getJWKS(): String = jwksJSON

    @PostMapping("/oauth/sign-token")
    fun signToken(
        @RequestBody payload: Map<String, Any?>
    ): Any? {
        val claimsSet: JWTClaimsSet = jwtClaimSet {
            claims(payload)
            expirationTime(Instant.now() + Duration.ofDays(10 * 365))
        }
        val signedJwt: SignedJWT = signedJwtRS256(authConfig.rsaKey, claimsSet, checkDecdingWorks = false)
        val signedJwtSerialized: String = signedJwt.serialize()
        return mapOf(
            "token" to "Bearer $signedJwtSerialized"
        )
    }

    @PostMapping("/oauth/generate-rsa-keys")
    fun generateRSAKeys(): Any? {
        val newRSAKey: RSAKey = RSAKeyFactory.generateRSAKey()
        return mapOf(
            "rsaKeyB64" to newRSAKey.toRSAKeyB64String(),
            "rsaKey" to newRSAKey.toJSONObject()
        )
    }

    private fun signedJwtRS256(rsaKey: RSAKey, claimsSet: JWTClaimsSet, checkDecdingWorks: Boolean): SignedJWT {
        val rs256 = JwtRS256(rsaKey)
        val header: JWSHeader = rs256.jwsHeader() {}
        val signedJwt: SignedJWT = rs256.signedJwt(header, claimsSet)
        val signedJwtSerialized: String = signedJwt.serialize()
        logger.info { "signed fake jwt (RSA56) ... $signedJwtSerialized" }
        logger.info { "=====================" }
        logger.info { "Bearer $signedJwtSerialized" }

        // check decoding works ...
        if (checkDecdingWorks) {
            val decoder: NimbusJwtDecoder = rs256.jwtDecoder() {}
            val decoded: Jwt = decoder.decode(signedJwtSerialized)
            logger.info { "decoding works. claims: ${decoded.claims}" }
            logger.info { "=====================" }
        }


        return signedJwt
    }
}
