package main.storage.tick.impl;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import main.model.Tick;
import main.storage.tick.ManagerTicks;
import org.springframework.stereotype.Service;

@Service
public class TickManagerServiceImpl implements ManagerTicks {

  private Deque<Tick> listTicks = new ArrayDeque<>();

  private final int SIZE_LIST_TICKS = 5000;

  public void processingTick(BigDecimal priceAsk, BigDecimal priceBid, Long time) throws Exception {

    Tick tick = Tick.builder()
        .priceAsk(priceAsk)
        .priceBid(priceBid)
        .timestamp(time)
        .build();

    if (listTicks.size() >= SIZE_LIST_TICKS) {
      listTicks.pollFirst();
    }
    listTicks.addLast(tick);

  }

  public int sizeStorageTicks() {
    return listTicks.size();
  }

  /**
   * Возвращает указанное кол-во последних тиков
   * @param count
   * @return
   */
  public List<Tick> getListTickByCount(int count) {
    return new ArrayList<>(listTicks.stream().skip(listTicks.size() - count).collect(Collectors.toList()));
  }

  /**
   * Возвращает тики в указанном диапозоне времени
   * @param timeFrom
   * @param timeTo
   * @return
   */
  public List<Tick> getListTickByTime(Long timeFrom, Long timeTo) {
    return new ArrayList<>(listTicks.stream()
        .filter(t -> t.getTimestamp() <= timeFrom && t.getTimestamp() >= timeTo).toList());
  }


}
