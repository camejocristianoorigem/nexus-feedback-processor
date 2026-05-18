package nexus.feedback.processor.service;

import nexus.feedback.processor.dto.FeedbackRequestDto;
import nexus.feedback.processor.dto.FeedbackResponseDto;
import nexus.feedback.processor.exception.ResourceNotFoundException;
import nexus.feedback.processor.model.Feedback;
import nexus.feedback.processor.model.FeedbackStatus;
import nexus.feedback.processor.repository.FeedbackRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class FeedbackService {

    private final FeedbackRepository repository;

    public FeedbackService(FeedbackRepository repository) {
        this.repository = repository;
    }

    public FeedbackResponseDto create(FeedbackRequestDto dto) {
        var feedback = new Feedback(
                UUID.randomUUID(),
                dto.title(),
                dto.description(),
                FeedbackStatus.PENDING,
                dto.customerEmail(),
                Instant.now(),
                Instant.now()
        );
        
        var saved = repository.save(feedback);
        return mapToResponse(saved);
    }

    public FeedbackResponseDto findById(UUID id) {
        return repository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback não encontrado com o ID: " + id));
    }

    public List<FeedbackResponseDto> findAll() {
        return repository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    public FeedbackResponseDto updateStatus(UUID id, FeedbackStatus newStatus) {
        var existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback não encontrado com o ID: " + id));

        var updated = new Feedback(
                existing.id(),
                existing.title(),
                existing.description(),
                newStatus,
                existing.customerEmail(),
                existing.createdAt(),
                Instant.now()
        );

        var saved = repository.save(updated);
        return mapToResponse(saved);
    }

    public void delete(UUID id) {
        if (repository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Feedback não encontrado com o ID: " + id);
        }
        repository.deleteById(id);
    }

    private FeedbackResponseDto mapToResponse(Feedback f) {
        return new FeedbackResponseDto(
                f.id(),
                f.title(),
                f.description(),
                f.status(),
                f.customerEmail(),
                f.createdAt(),
                f.updatedAt()
        );
    }
}
