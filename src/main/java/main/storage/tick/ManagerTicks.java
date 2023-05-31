package main.storage.tick;

import java.math.BigDecimal;
import java.util.List;
import main.model.Tick;

/**
 * Сервис для работы с тиками
 */
public interface ManagerTicks {

  void processingTick(BigDecimal priceAsk, BigDecimal priceBid, Long time) throws Exception;

}
