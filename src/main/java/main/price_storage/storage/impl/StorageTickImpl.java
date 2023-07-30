package main.price_storage.storage.impl;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import main.price_storage.dto.TickDto;
import main.price_storage.model.Tick;
import main.price_storage.storage.StorageTick;
import org.springframework.stereotype.Service;

@Service
public class StorageTickImpl implements StorageTick {

  private final Deque<Tick> listTicks = new ArrayDeque<>();

  private final int SIZE_LIST_TICKS = 5000;

  public void processingTick(TickDto tickDto, Long timestamp) throws Exception {

    Tick tick = Tick.builder()
        .ask(tickDto.getAsk())
        .bid(tickDto.getBid())
        //.timestamp(timestamp)
        .time(tickDto.getTime())
        .timeMsc(tickDto.getTimeMsc())
        .flags(tickDto.getFlags())
        .last(tickDto.getLast())
        .volume(tickDto.getVolume())
        .volumeReal(tickDto.getVolumeReal())
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
    //TODO: Тут возникал ConcurrentModificationException, исправить синхронизацию
    synchronized (listTicks) {
      return new ArrayList<>(
          listTicks.stream().skip(listTicks.size() - count).collect(Collectors.toList()));
    }
  }

  /**
   * Возвращает тики в указанном диапозоне времени
   * @param timeFrom
   * @param timeTo
   * @return
   */
  public List<Tick> getListTickByTime(Long timeFrom, Long timeTo) {
    synchronized (listTicks) {
      return new ArrayList<>(listTicks.stream()
          .filter(t -> t.getTimeMsc() <= timeFrom && t.getTimeMsc() >= timeTo).toList());
    }
  }

  /**
   * Возвращает тики от последнего до указанной дельты
   * @param timeTo
   * @return
   */
  public List<Tick> getListTickByTimeFromLastTick(Long timeTo) {
    synchronized (listTicks) {
      Long timeStartSelection = listTicks.getLast().getTimeMsc();
      return new ArrayList<>(listTicks.stream()
          .filter(t -> t.getTimeMsc() <= timeStartSelection
              && t.getTimeMsc() >= timeStartSelection - timeTo).toList());
    }
  }

  //@PostConstruct
  private void initTick() throws Exception {
    TickDto tick0 = new TickDto();
    tick0.setAsk(new BigDecimal("0.00005"));
    tick0.setBid(new BigDecimal("0.00006"));
    tick0.setTimeMsc(0L);
    processingTick(tick0, 0L);
    TickDto tick1 = new TickDto();
    tick1.setAsk(new BigDecimal("0.00007"));
    tick1.setBid(new BigDecimal("0.00008"));
    tick1.setTimeMsc(20L);
    processingTick(tick1, 20L);
    TickDto tick2 = new TickDto();
    tick2.setAsk(new BigDecimal("0.00009"));
    tick2.setBid(new BigDecimal("0.00010"));
    tick2.setTimeMsc(40L);
    processingTick(tick2, 40L);
    TickDto tick3 = new TickDto();
    tick3.setAsk(new BigDecimal("0.00011"));
    tick3.setBid(new BigDecimal("0.00012"));
    tick3.setTimeMsc(60L);
    processingTick(tick3, 60L);
    TickDto tick4 = new TickDto();
    tick4.setAsk(new BigDecimal("0.00013"));
    tick4.setBid(new BigDecimal("0.00014"));
    tick4.setTimeMsc(80L);
    processingTick(tick4, 80L);
    TickDto tick5 = new TickDto();
    tick5.setAsk(new BigDecimal("0.00013"));
    tick5.setBid(new BigDecimal("0.00014"));
    tick5.setTimeMsc(100L);
    processingTick(tick5, 100L);
    TickDto tick6 = new TickDto();
    tick6.setAsk(new BigDecimal("0.00013"));
    tick6.setBid(new BigDecimal("0.00014"));
    tick6.setTimeMsc(120L);
    processingTick(tick6, 120L);
  }

}
