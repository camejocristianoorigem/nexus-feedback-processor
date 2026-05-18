package nexus.feedback.processor.service;

import nexus.feedback.processor.repository.FeedbackRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedbackAnalysisService {

    private final FeedbackRepository repository;
    private final ChatClient chatClient;

    public FeedbackAnalysisService(FeedbackRepository repository, ChatClient.Builder chatClientBuilder) {
        this.repository = repository;
        this.chatClient = chatClientBuilder.build();
    }

    public List<String> extractValidWords() {
        String combinedDescriptions = repository.findAll().stream()
                .map(f -> f.description())
                .collect(Collectors.joining(" | "));

        if (combinedDescriptions.isBlank()) {
            return List.of("Nenhum feedback encontrado para analise.");
        }

        String prompt = """
                Voce e um analisador de texto linguistico de alta precisao.
                Abaixo esta uma sequencia de textos randomicos separados por '|'.
                Sua tarefa e escanear esses caracteres e identificar onde se formaram palavras reais e validas (em portugues ou ingles).
                
                Textos para analise: %s
                
                Regras estritas:
                1. Retorne APENAS uma lista das palavras encontradas, separadas por virgula.
                2. Nao de explicacoes, introducoes ou exemplos.
                3. Se nenhuma palavra real de pelo menos 3 letras for encontrada, responda apenas: Nenhuma.
                """.formatted(combinedDescriptions);

        String response = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

        return List.of(response.split(",\\s*"));
    }
}
