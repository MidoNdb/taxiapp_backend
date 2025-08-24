package coursePro.mr.taxiApp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // ✅ CORRECTION: Autoriser toutes les origines pour les tests
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*") // Autoriser toutes les origines
                .withSockJS(); // ✅ Activer SockJS pour une meilleure compatibilité
        
        // ✅ Endpoint sans SockJS aussi
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*"); // Pour les connexions WebSocket natives
    }
}