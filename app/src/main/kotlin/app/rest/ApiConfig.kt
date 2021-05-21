package app.rest

import org.springframework.stereotype.Component

@Component
class ApiConfig(
) {
    val title: String
        get() = "jwt fake authorization server"
}


