package lovable_clone.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lovable_clone.dto.chat.StreamResponse;
import lovable_clone.entity.*;
import lovable_clone.enums.ChatEventType;
import lovable_clone.enums.MessageRole;
import lovable_clone.error.ResourceNotFoundException;
import lovable_clone.llm.LlmResponseParser;
import lovable_clone.llm.advisors.FileTreeContextAdvisor;
import lovable_clone.llm.PromptUtils;
import lovable_clone.llm.tools.CodeGenerationTools;
import lovable_clone.repository.*;
import lovable_clone.security.AuthUtil;
import lovable_clone.service.AiGenerationService;
import lovable_clone.service.ProjectFileService;
import lovable_clone.service.UsageService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiGenerationServiceImpl implements AiGenerationService {
    private final ChatClient chatClient;
    private final AuthUtil authUtil;
    private final UsageService usageService;
    private final ChatSessionRepository chatSessionRepository;
    private final FileTreeContextAdvisor fileTreeContextAdvisor;
    private final ProjectFileService projectFileService;
    private final LlmResponseParser llmResponseParser;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatEventRepository chatEventRepository;
    private final UsageLogRepository usageLogRepository;

    private static final Pattern FILE_TAG_PATTERN = Pattern.compile("<file path=\"([^\"]+)\">(.*?)</file>", Pattern.DOTALL);


//    @Override
//    @PreAuthorize("@security.canEditProject(#projectId)")
//    public Flux<StreamResponse> streamResponse(String userMessage, Long projectId) {
//
////        usageService.checkDailyTokensUsage();
//
//        Long userId = authUtil.getCurrentUserId();
//        ChatSession chatSession = createChatSessionIfNotExists(projectId, userId);
//
//        Map<String, Object> advisorParams = Map.of(
//                "userId", userId,
//                "projectId", projectId
//        );
//
//        StringBuilder fullResponseBuffer = new StringBuilder();
//        CodeGenerationTools codeGenerationTools = new CodeGenerationTools(projectFileService, projectId);
//
//        AtomicReference<Long> startTime = new AtomicReference<>(System.currentTimeMillis());
//        AtomicReference<Long> endTime = new AtomicReference<>(0L);
//        AtomicReference<Usage> usageRef = new AtomicReference<>();
//
//        return chatClient.prompt()
//                .system(PromptUtils.CODE_GENERATION_SYSTEM_PROMPT)
//                .user(userMessage)
//                .tools(codeGenerationTools)
//                .advisors(advisorSpec -> {
//                            advisorSpec.params(advisorParams);
//                            advisorSpec.advisors(fileTreeContextAdvisor);
//                        }
//                )
//                .stream()
//                .chatResponse()
//                .doOnNext(response -> {
//                    String content = response.getResult().getOutput().getText();
//
//                    if(content != null && !content.isEmpty() && endTime.get() == 0) { // first non-empty chunk received
//                        endTime.set(System.currentTimeMillis());
//                    }
//
//                    if(response.getMetadata().getUsage() != null) {
//                        usageRef.set(response.getMetadata().getUsage());
//                    }
//                    fullResponseBuffer.append(content);
//                })
//                .doOnComplete(() -> {
//                    Schedulers.boundedElastic().schedule(() -> {
////                        parseAndSaveFiles(fullResponseBuffer.toString(), projectId);
//
//                        long duration = (endTime.get() - startTime.get()) /  1000;
//                        finalizeChats(userMessage, chatSession, fullResponseBuffer.toString(), duration, usageRef.get());
//                    });
//                })
//                .doOnError(error -> log.error("Error during streaming for projectId: {}", projectId))
//                .map(response -> {
//                    String text = response.getResult().getOutput().getText();
//                    return new StreamResponse(text != null ? text : "");
//                });
//    }
    @Override
    @PreAuthorize("@security.canEditProject(#projectId)")
    public Flux<StreamResponse> streamResponse(String userMessage, Long projectId) {

        usageService.checkDailyTokensUsage();

        Long userId = authUtil.getCurrentUserId();
        ChatSession chatSession = createChatSessionIfNotExists(projectId, userId);

        Map<String, Object> advisorParams = Map.of(
                "userId", userId,
                "projectId", projectId
        );

        StringBuilder fullResponseBuffer = new StringBuilder();
        CodeGenerationTools codeGenerationTools = new CodeGenerationTools(projectFileService, projectId);

        AtomicReference<Long> startTime = new AtomicReference<>(System.currentTimeMillis());
        AtomicReference<Long> endTime = new AtomicReference<>(0L);
        AtomicReference<Usage> usageRef = new AtomicReference<>();

        // 1. Capture the security context from the main Spring Web thread
        org.springframework.security.core.context.SecurityContext securityContext =
                org.springframework.security.core.context.SecurityContextHolder.getContext();

        return chatClient.prompt()
                .system(PromptUtils.CODE_GENERATION_SYSTEM_PROMPT)
                .user(userMessage)
                .tools(codeGenerationTools)
                .advisors(advisorSpec -> {
                            advisorSpec.params(advisorParams);
                            advisorSpec.advisors(fileTreeContextAdvisor);
                        }
                )
                .stream()
                .chatResponse()
                .doOnNext(response -> {
                    String content = response.getResult().getOutput().getText();

                    if(content != null && !content.isEmpty() && endTime.get() == 0) { // first non-empty chunk received
                        endTime.set(System.currentTimeMillis());
                    }

                    if(response.getMetadata().getUsage() != null) {
                        usageRef.set(response.getMetadata().getUsage());
                    }

                    fullResponseBuffer.append(content);
                })
                .doOnComplete(() -> {
                    Schedulers.boundedElastic().schedule(() -> {
                        // 2. Set the context on the background thread before executing secured methods
                        org.springframework.security.core.context.SecurityContextHolder.setContext(securityContext);

                        try {
                            long duration = (endTime.get() - startTime.get()) /  1000;
                            finalizeChats(userMessage, chatSession, fullResponseBuffer.toString(), duration, usageRef.get());
                        } finally {
                            // 3. Always clean up the context to prevent memory leaks in thread pools
                            org.springframework.security.core.context.SecurityContextHolder.clearContext();
                        }
                    });
                })
                .doOnError(error -> log.error("Error during streaming for projectId: {}", projectId, error))
                .map(response -> {
                    String text = response.getResult().getOutput().getText();
                    return new StreamResponse(text != null ? text : "");
                });
    }





//    private void finalizeChats(String userMessage, ChatSession chatSession, String fullText, Long duration, Usage usage, Long userId) {
//        Long projectId = chatSession.getProject().getId();
//
//        if(usage != null) {
//            int totalTokens = usage.getTotalTokens();
//            usageService.recordTokenUsage(chatSession.getUser().getId(), totalTokens);
//        }
//
//        // Save the User message
//        chatMessageRepository.save(
//                ChatMessage.builder()
//                        .chatSession(chatSession)
//                        .role(MessageRole.USER)
//                        .content(userMessage)
//                        .tokensUsed(usage.getPromptTokens())
//                        .build()
//        );
//
//        ChatMessage assistantChatMessage = ChatMessage.builder()
//                .role(MessageRole.ASSISTANT)
//                .content("Assistant Message here...")
//                .chatSession(chatSession)
//                .tokensUsed(usage.getCompletionTokens())
//                .build();
//
//        assistantChatMessage = chatMessageRepository.save(assistantChatMessage);
//
//        List<ChatEvent> chatEventList = llmResponseParser.parseChatEvents(fullText, assistantChatMessage);
//        chatEventList.addFirst(ChatEvent.builder()
//                .type(ChatEventType.THOUGHT)
//                .chatMessage(assistantChatMessage)
//                .content("Thought for "+duration+"s")
//                .sequenceOrder(0)
//                .build());
//
//        chatEventList.stream()
//                .filter(e -> e.getType() == ChatEventType.FILE_EDIT)
//                .forEach(e -> projectFileService.saveFile(projectId, e.getFilePath(), e.getContent()));
//
//        chatEventRepository.saveAll(chatEventList);
//    }
    private void finalizeChats(String userMessage, ChatSession chatSession, String fullText, Long duration, Usage usage) {
        Long projectId = chatSession.getProject().getId();

        // 1. Safely extract tokens with fallbacks (0 if null)
        int totalTokens = usage != null && usage.getTotalTokens() != null ? usage.getTotalTokens() : 0;
        int promptTokens = usage != null && usage.getPromptTokens() != null ? usage.getPromptTokens() : 0;
        int completionTokens = usage != null && usage.getCompletionTokens() != null ? usage.getCompletionTokens() : 0;

        if(totalTokens > 0) {
            usageService.recordTokenUsage(chatSession.getUser().getId(), totalTokens);
        }

        // Save the User message (Using the safe promptTokens variable)
        chatMessageRepository.save(
                ChatMessage.builder()
                        .chatSession(chatSession)
                        .role(MessageRole.USER)
                        .content(userMessage)
                        .tokensUsed(promptTokens) // ✅ SAFE
                        .build()
        );

        ChatMessage assistantChatMessage = ChatMessage.builder()
                .role(MessageRole.ASSISTANT)
                .content("Assistant Message here...")
                .chatSession(chatSession)
                .tokensUsed(completionTokens) // ✅ SAFE
                .build();

        assistantChatMessage = chatMessageRepository.save(assistantChatMessage);

        List<ChatEvent> chatEventList = llmResponseParser.parseChatEvents(fullText, assistantChatMessage);
        chatEventList.addFirst(ChatEvent.builder()
                .type(ChatEventType.THOUGHT)
                .chatMessage(assistantChatMessage)
                .content("Thought for " + duration + "s")
                .sequenceOrder(0)
                .build());

        chatEventList.stream()
                .filter(e -> e.getType() == ChatEventType.FILE_EDIT)
                .forEach(e -> projectFileService.saveFile(projectId, e.getFilePath(), e.getContent()));

        chatEventRepository.saveAll(chatEventList);
    }

    private ChatSession createChatSessionIfNotExists(Long projectId, Long userId) {
        ChatSessionId chatSessionId = new ChatSessionId(projectId, userId);
        ChatSession chatSession = chatSessionRepository.findById(chatSessionId).orElse(null);

        if(chatSession == null) {
            Project project = projectRepository.findById(projectId)
                    .orElseThrow(() -> new ResourceNotFoundException("Project", projectId.toString()));
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User", userId.toString()));

            chatSession = ChatSession.builder()
                    .id(chatSessionId)
                    .project(project)
                    .user(user)
                    .build();

            chatSession = chatSessionRepository.save(chatSession);
        }
        return chatSession;
//        return null;
    }
}
