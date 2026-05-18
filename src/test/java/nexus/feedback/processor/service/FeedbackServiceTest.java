package nexus.feedback.processor.service;

import nexus.feedback.processor.dto.FeedbackRequestDto;
import nexus.feedback.processor.dto.FeedbackResponseDto;
import nexus.feedback.processor.exception.ResourceNotFoundException;
import nexus.feedback.processor.model.Feedback;
import nexus.feedback.processor.model.FeedbackStatus;
import nexus.feedback.processor.repository.FeedbackRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FeedbackServiceTest {

    private FeedbackRepository repository;
    private FeedbackService service;

    @BeforeEach
    void setUp() {
        repository = mock(FeedbackRepository.class);
        service = new FeedbackService(repository);
    }

    @Test
    void deveCriarFeedbackComSucesso() {
        var dto = new FeedbackRequestDto("Erro na API", "Descricao longa aqui", "dev@nexus.com");
        when(repository.save(any(Feedback.class))).thenAnswer(invocation -> invocation.getArgument(0));

        FeedbackResponseDto response = service.create(dto);

        assertNotNull(response.id());
        assertEquals("Erro na API", response.title());
        assertEquals(FeedbackStatus.PENDING, response.status());
        verify(repository, times(1)).save(any(Feedback.class));
    }

    @Test
    void deveLancarExcecaoAoBuscarIdInexistente() {
        UUID randomId = UUID.randomUUID();
        when(repository.findById(randomId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findById(randomId));
    }

    @Test
    void deveSuportarAltaConcorrenciaComVirtualThreads() throws InterruptedException {
        // Usando um repositório real baseado em ConcurrentHashMap para testar concorrência real
        FeedbackRepository realRepository = new FeedbackRepository();
        FeedbackService realService = new FeedbackService(realRepository);
        
        int totalThreads = 100;
        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
        CountDownLatch latch = new CountDownLatch(totalThreads);

        for (int i = 0; i < totalThreads; i++) {
            final int index = i;
            executor.submit(() -> {
                try {
                    var dto = new FeedbackRequestDto("Feedback #" + index, "Descricao concorrente", "user@test.com");
                    realService.create(dto);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        assertEquals(totalThreads, realRepository.findAll().size(), "O repositório concorrente deve conter todos os registros inseridos.");
    }
}
