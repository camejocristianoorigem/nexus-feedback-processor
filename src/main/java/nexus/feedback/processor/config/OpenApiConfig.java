package nexus.feedback.processor.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Nexus Feedback Processor API")
                        .version("1.0.0")
                        .description("Plataforma assíncrona de ingestão de feedbacks operando sob Virtual Threads e enriquecida com análise semântica estruturada via LLM (Gemini 2.5 Flash).")
                        .contact(new Contact()
                                .name("Cristiano Camejo Origem")
                                .url("https://github.com/camejocristianoorigem")));
    }
}
