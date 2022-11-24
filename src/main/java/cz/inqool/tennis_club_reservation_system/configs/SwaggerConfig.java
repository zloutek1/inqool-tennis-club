package cz.inqool.tennis_club_reservation_system.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String securitySchemaName = "bearerAuth";

    @Bean
    public OpenApiCustomiser apiCustomizer() {
        return SwaggerConfig::addSecurity;
    }

    private static void addSecurity(OpenAPI openApi) {
        openApi.addSecurityItem(new SecurityRequirement().addList(securitySchemaName));
        openApi.getComponents()
                .addSecuritySchemes(securitySchemaName,
                        new SecurityScheme()
                                .name(securitySchemaName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                );
    }

}
