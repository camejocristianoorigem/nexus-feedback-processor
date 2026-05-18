package nexus.feedback.processor.model;

import java.time.Instant;
import java.util.UUID;

public record Feedback(
    UUID id,
    String title,
    String description,
    FeedbackStatus status,
    String customerEmail,
    Instant createdAt,
    Instant updatedAt
) {
    public Feedback {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("O título não pode ser vazio.");
        }
        if (customerEmail == null || !customerEmail.contains("@")) {
            throw new IllegalArgumentException("Email do cliente inválido.");
        }
        if (status == null) {
            status = FeedbackStatus.PENDING;
        }
        if (createdAt == null) {
            createdAt = Instant.now();
        }
        if (updatedAt == null) {
            updatedAt = Instant.now();
        }
    }
}
