package main.price_storage.storage;

import main.price_storage.dto.TickDto;

/**
 * Сервис для работы с тиками
 */
public interface StorageTick {

  void processingTick(TickDto tickDto, Long timestamp) throws Exception;

}
