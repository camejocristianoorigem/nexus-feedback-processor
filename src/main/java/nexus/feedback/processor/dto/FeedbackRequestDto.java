package nexus.feedback.processor.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record FeedbackRequestDto(
    @NotBlank(message = "O título é obrigatório")
    @Size(max = 100, message = "O título deve ter no máximo {max} caracteres")
    String title,

    @NotBlank(message = "A descrição é obrigatória")
    @Size(max = 2000, message = "A descrição deve ter no máximo {max} caracteres")
    String description,

    @NotBlank(message = "O email do cliente é obrigatório")
    @Email(message = "Formato de email inválido")
    String customerEmail
) {}
