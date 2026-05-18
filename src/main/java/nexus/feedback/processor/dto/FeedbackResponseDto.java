package nexus.feedback.processor.dto;

import nexus.feedback.processor.model.FeedbackStatus;
import java.time.Instant;
import java.util.UUID;

public record FeedbackResponseDto(
    UUID id,
    String title,
    String description,
    FeedbackStatus status,
    String customerEmail,
    Instant createdAt,
    Instant updatedAt
) {}
