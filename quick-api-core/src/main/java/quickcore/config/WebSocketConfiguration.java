package quickcore.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
@Component
public class WebSocketConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketConfiguration.class);

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        logger.info("[QuickApi] Starting Websocket......");
        return new ServerEndpointExporter();
    }
}
