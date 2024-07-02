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

  private static final int TCP_EUR_USR = 8081;
  private static final int TCP_USD_JPA = 8082;
  private static final int TCP_AUD_USD = 8083;

  @Bean
  public IntegrationFlow tcpInboundFlow1() {
    return IntegrationFlows.from(Tcp.inboundAdapter(Tcp.netServer(TCP_EUR_USR)))
        .channel("application.EUR_USD")
        .handle("tcpInputService", "handleMessage1")
        .get();
  }

  @Bean
  public IntegrationFlow tcpInboundFlow2() {
    return IntegrationFlows.from(Tcp.inboundAdapter(Tcp.netServer(TCP_USD_JPA)))
            .channel("application.USD_JPA")
            .handle("tcpInputService", "handleMessage2")
            .get();
  }

  @Bean
  public IntegrationFlow tcpInboundFlow3() {
    return IntegrationFlows.from(Tcp.inboundAdapter(Tcp.netServer(TCP_AUD_USD)))
            .channel("application.AUD_USD")
            .handle("tcpInputService", "handleMessage3")
            .get();
  }


}
