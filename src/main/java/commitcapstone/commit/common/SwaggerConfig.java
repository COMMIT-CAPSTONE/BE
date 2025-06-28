package commitcapstone.commit.common;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.providers.ObjectMapperProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = " API 명세서",
                description = "API 명세서",
                version = "v1"))
@Configuration
public class SwaggerConfig   {

    @Bean
    public ModelResolver modelResolver(ObjectMapperProvider objectMapperProvider) {
        ModelResolver modelResolver = new ModelResolver(objectMapperProvider.jsonMapper()
                .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE));

        // 기존에 등록된 ModelConverters 클리어 후 다시 등록 (중복 방지)
        ModelConverters.getInstance().addConverter(modelResolver);

        return modelResolver;
    }

    @Bean
    public OpenAPI api() {
        SecurityScheme apiKey = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .in(SecurityScheme.In.HEADER)
                .name("Authorization")
                .scheme("bearer")
                .bearerFormat("JWT");

        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("Bearer Token");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("Bearer Token", apiKey))
                .addSecurityItem(securityRequirement);
    }
}