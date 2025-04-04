package kz.aitu.corporationy.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        servers = @Server(
                url = "${server.servlet.context-path:/}",
                description = "Default server URL"
        )
)
@SecurityScheme(
        name = OpenApiConfig.SECURITY_SCHEME_NAME,
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {

    public static final String SECURITY_SCHEME_NAME = "bearerAuth";
}