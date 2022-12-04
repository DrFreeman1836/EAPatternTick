package main.storage.impl;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import main.model.Tick;
import main.storage.ManagerTicks;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TickManagerServiceImpl implements ManagerTicks {

  private Deque<Tick> listTicks = new ArrayDeque<>();

  private final int SIZE_LIST_TICKS = 100;

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

  public List<Tick> getSortedListTicks(int count) {
    return new ArrayList<>(listTicks.stream().sorted(Comparator.comparingLong(Tick::getTimestamp))
        .collect(Collectors.toList()));
  }

  public List<Tick> getListTicks() {
    return new ArrayList<>(listTicks);
  }

  public int sizeStorageTicks() {
    return listTicks.size();
  }

  public List<Tick> getListTicks(int count) {
    return new ArrayList<>(listTicks.stream().skip(listTicks.size() - count).collect(Collectors.toList()));
  }

  public BigDecimal getSizeTick(BigDecimal priceAsk, BigDecimal priceBid) {
    return priceAsk.subtract(priceBid);
  }

}