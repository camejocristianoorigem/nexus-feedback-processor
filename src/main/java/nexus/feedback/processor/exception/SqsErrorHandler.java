package nexus.feedback.processor.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SqsErrorHandler {

    private static final Logger log = LoggerFactory.getLogger(SqsErrorHandler.class);

    public void handle(Object message, Throwable exception) {
        log.error("💥 Erro capturado no processamento de feedback.");
    }
}
