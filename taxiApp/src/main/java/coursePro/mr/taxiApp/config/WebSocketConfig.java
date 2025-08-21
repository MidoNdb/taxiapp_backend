// ✅ WebSocketConfig.java - Version compatible sans setAllowedCredentials
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
        // ✅ Configuration simple sans credentials (compatible toutes versions)
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:3000"); // Origine spécifique
                //.withSockJS();
    }
}

// package coursePro.mr.taxiApp.config;

// import org.springframework.context.annotation.Configuration;
// import org.springframework.messaging.simp.config.MessageBrokerRegistry;
// import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
// import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
// import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

// @Configuration
// @EnableWebSocketMessageBroker
// public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

//     @Override
//     public void configureMessageBroker(MessageBrokerRegistry config) {
//         config.enableSimpleBroker("/topic");
//         config.setApplicationDestinationPrefixes("/app");
//     }

//     @Override
//     public void registerStompEndpoints(StompEndpointRegistry registry) {
//         // ✅ Configuration CORS spécifique (pas de wildcard avec credentials)
//         registry.addEndpoint("/ws")
//                 .setAllowedOrigins("http://localhost:3000") // Origine spécifique
//                 //.setAllowedCredentials(true) // Permet les credentials
//                 .withSockJS();
                
//         // ✅ Endpoint natif pour Flutter
//         registry.addEndpoint("/ws-native")
//                 .setAllowedOrigins("http://localhost:3000");
//                 //.setAllowedCredentials(true);
//     }
// }