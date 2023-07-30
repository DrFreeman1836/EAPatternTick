package main.notification.impl;

import main.notification.MessagesBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class TelegramBotMessages {

  private MessagesBot bot;

  private final Logger logger = LoggerFactory.getLogger(TelegramBotMessages.class);

  @Value("${bot.chatId}")
  private long chatId;

  @Autowired
  public TelegramBotMessages(MessagesBot bot) {
    this.bot = bot;
  }

  @Async
  public void sendMessage(String text) {
    try {
      bot.sendMessage(chatId, text);
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }

}
