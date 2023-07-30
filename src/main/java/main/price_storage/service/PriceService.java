package main.price_storage.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import main.price_storage.dto.TickDto;
import main.price_storage.model.Tick;
import main.price_storage.repository.TickRepository;
import main.price_storage.storage.StorageTick;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PriceService {

  private final ObjectMapper mapper = new ObjectMapper();

  private final static Integer COUNT_TICK = 5000;

  @Qualifier("storageTickImpl")
  private final StorageTick storageTickImpl;
  private final List<Tick> listTick = new ArrayList<>();
  private final TickRepository tickRepository;


  public void addPrice(byte[] msg) throws Exception {

    TickDto tick = mapper.readValue(msg, TickDto.class);
    storageTickImpl.processingTick(tick, System.currentTimeMillis());
    addTick(tick);

    if (listTick.size() >= COUNT_TICK) {
      tickRepository.saveAll(listTick);
      listTick.clear();
    }

  }

  private void addTick(TickDto tickDto) {
    Tick tick = Tick.builder()
        .ask(tickDto.getAsk())
        .bid(tickDto.getBid())
        .timestamp(System.currentTimeMillis())
        .time(tickDto.getTime())
        .timeMsc(tickDto.getTimeMsc())
        .flags(tickDto.getFlags())
        .last(tickDto.getLast())
        .volume(tickDto.getVolume())
        .volumeReal(tickDto.getVolumeReal())
        .build();
    listTick.add(tick);
  }

  @PreDestroy
  private void flushTick() {
    tickRepository.saveAll(listTick);
    listTick.clear();
  }

}
