package ma.ensa.rag_ollama.Web;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chat")
public class ChatRestController {
    
    private VectorStore vectorStore;
    private ChatClient chatClient;

    @Value("classpath:/prompts/prompt.st")
    private Resource prompt;

    public ChatRestController(VectorStore vectorStore,ChatClient.Builder builder) {
        this.vectorStore = vectorStore;
        this.chatClient = builder.build();
    }

    // Serve the home.html page on root path (GET request)
    @GetMapping("/")
    public String index() {
        return "login"; // Thymeleaf will resolve this to src/main/resources/templates/home.html
    }

    private String formatResponseAsHtml(String response) {
        return response
                .replaceAll("\\*\\*(.*?)\\*\\*", "<strong>$1</strong>")  // Convert bold markdown to <strong>
                .replaceAll("### (.*?)\\n", "<h3>$1</h3>")               // Convert headers to <h3>
                .replaceAll("- (.*?)\\n", "<li>$1</li>")                 // Convert lists to <li>
                .replaceAll("```(.*?)```", "<pre><code>$1</code></pre>")  // Convert code blocks to <pre><code>
                .replaceAll("\\n", "<br>");                              // Convert new lines to <br>
    }

    // Handle POST request from the front-end
    @PostMapping("/sendMessage")
    public ResponseEntity<Map<String, String>> sendMessage(@RequestBody Map<String, String> request) {
        // Extract user message from the request
        String userMessage = request.get("message");
        List<Document> documents = vectorStore.similaritySearch(SearchRequest.query(userMessage).withTopK(2));
        List<String> context = documents.stream().map(d -> d.getContent()).toList();

        PromptTemplate promptTemplate = new PromptTemplate(prompt);
        Prompt finalprompt = promptTemplate.create(Map.of("context", context, "question", userMessage));

        String botResponse = chatClient.prompt(finalprompt).call().content();
        botResponse = formatResponseAsHtml(botResponse);

        // Create a response to send back to the frontend
        Map<String, String> response = new HashMap<>();
        response.put("response", botResponse);

        return ResponseEntity.ok(response); // Return the bot's response as JSON
    }
}
