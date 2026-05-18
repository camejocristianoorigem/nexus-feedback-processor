package nexus.feedback.processor.repository;

import nexus.feedback.processor.model.Feedback;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class FeedbackRepository {

    private final Map<UUID, Feedback> datasource = new ConcurrentHashMap<>();

    public Feedback save(Feedback feedback) {
        if (feedback.id() == null) {
            throw new IllegalArgumentException("O feedback deve possuir um ID válido.");
        }
        datasource.put(feedback.id(), feedback);
        return feedback;
    }

    public Optional<Feedback> findById(UUID id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(datasource.get(id));
    }

    public List<Feedback> findAll() {
        return new ArrayList<>(datasource.values());
    }

    public void deleteById(UUID id) {
        if (id != null) {
            datasource.remove(id);
        }
    }
}
