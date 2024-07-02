package main.price_storage.controller;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import main.price_storage.service.PriceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TcpInputService {

  private final PriceService priceService;

  private final Logger logger = LoggerFactory.getLogger(TcpInputService.class);

  @ServiceActivator(inputChannel = "EUR_USD")
  public void handleMessage1(byte[] msg) {
    long currentTime = System.currentTimeMillis();
    try {
      priceService.addPrice(msg, currentTime);
    } catch (IOException e) {
      logger.error("вход: " + new String(msg) + "\n выход: " + e.getMessage());
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }

  @ServiceActivator(inputChannel = "USD_JPA")
  public void handleMessage2(byte[] msg) {
    long currentTime = System.currentTimeMillis();
    try {
      priceService.addPrice(msg, currentTime);
    } catch (IOException e) {
      logger.error("вход: " + new String(msg) + "\n выход: " + e.getMessage());
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }

  @ServiceActivator(inputChannel = "AUD_USD")
  public void handleMessage3(byte[] msg) {
    long currentTime = System.currentTimeMillis();
    try {
      priceService.addPrice(msg, currentTime);
    } catch (IOException e) {
      logger.error("вход: " + new String(msg) + "\n выход: " + e.getMessage());
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }

}
