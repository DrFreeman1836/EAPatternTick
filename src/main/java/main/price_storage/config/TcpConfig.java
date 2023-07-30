package main.price_storage.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.ip.dsl.Tcp;

@Configuration
@EnableIntegration
@IntegrationComponentScan
public class TcpConfig {

  private static final int TCP_SERVER_PORT = 8081;

  @Bean
  public IntegrationFlow tcpInboundFlow() {
    return IntegrationFlows.from(Tcp.inboundAdapter(Tcp.netServer(TCP_SERVER_PORT)))
        .channel("application.fromTcp")
        .handle("tcpInputService", "handleMessage")
        .get();
  }


}
