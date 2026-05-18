package nexus.feedback.processor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import nexus.feedback.processor.dto.FeedbackRequestDto;
import nexus.feedback.processor.dto.FeedbackResponseDto;
import nexus.feedback.processor.model.FeedbackStatus;
import nexus.feedback.processor.service.FeedbackService;
import nexus.feedback.processor.service.FeedbackAnalysisService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/feedbacks")
@Tag(name = "Feedbacks", description = "Endpoints para gerenciamento, triagem e análise baseada em IA de feedbacks corporativos.")
public class FeedbackController {

    private final FeedbackService service;
    private final FeedbackAnalysisService analysisService;

    public FeedbackController(FeedbackService service, FeedbackAnalysisService analysisService) {
        this.service = service;
        this.analysisService = analysisService;
    }

    @PostMapping
    @Operation(summary = "Registrar novo feedback", description = "Executa a validação de entrada e armazena de forma persistente e thread-safe um novo feedback de cliente.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Feedback persistido com sucesso."),
        @ApiResponse(responseCode = "400", description = "Payload inválido ou quebra de restrições de validação.")
    })
    public ResponseEntity<FeedbackResponseDto> create(@RequestBody @Valid FeedbackRequestDto dto) {
        var response = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter feedback por ID", description = "Recupera os detalhes completos de um registro de feedback mapeado pelo seu UUID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Registro localizado com sucesso."),
        @ApiResponse(responseCode = "404", description = "Nenhum feedback com o identificador fornecido foi localizado.")
    })
    public ResponseEntity<FeedbackResponseDto> getById(
            @PathVariable @Parameter(description = "Identificador único (UUID) do feedback") UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    @Operation(summary = "Listar todos os feedbacks", description = "Lista de forma reativa os dados imutáveis contidos na base local em memória concorrente.")
    @ApiResponse(responseCode = "200", description = "Listagem realizada com sucesso.")
    public ResponseEntity<List<FeedbackResponseDto>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/analyze-words")
    @Operation(summary = "Extrair palavras-chave por IA", description = "Aciona o modelo generativo Gemini para varrer as descrições acumuladas e extrair termos semanticamente válidos.")
    @ApiResponse(responseCode = "200", description = "Análise processada e estruturada via LLM com sucesso.")
    public ResponseEntity<List<String>> analyzeWords() {
        return ResponseEntity.ok(analysisService.extractValidWords());
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar status de tramitação", description = "Altera o estado interno do pipeline (PENDING, PROCESSING, RESOLVED, IGNORED) de forma atômica e imutável.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso."),
        @ApiResponse(responseCode = "404", description = "Registro não encontrado.")
    })
    public ResponseEntity<FeedbackResponseDto> updateStatus(
            @PathVariable @Parameter(description = "UUID do feedback alvo") UUID id,
            @RequestParam @Parameter(description = "Novo status operacional a ser atribuído") FeedbackStatus status) {
        return ResponseEntity.ok(service.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Expurgar feedback", description = "Remove permanentemente um registro de feedback baseado no seu identificador.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Feedback removido com sucesso."),
        @ApiResponse(responseCode = "404", description = "Registro não encontrado para deleção.")
    })
    public ResponseEntity<Void> delete(
            @PathVariable @Parameter(description = "UUID do feedback a ser apagado") UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
