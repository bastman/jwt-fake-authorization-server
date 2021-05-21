package app.config

import app.util.jwt.RSAKeyFactory
import com.nimbusds.jose.jwk.RSAKey
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "app.auth")
@ConstructorBinding
data class AuthConfig(
    val rsaKeyB64: String
) {

    val rsaKey: RSAKey by lazy {
        RSAKeyFactory.rsaKeyOfB64String(rsaKeyB64 = rsaKeyB64)
    }
}
