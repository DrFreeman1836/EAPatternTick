package main.pattern.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import main.pattern.dto.RsSignal.Signal;
import main.pattern.enam.TypeSignalPassivity;
import main.price_storage.model.Tick;
import main.price_storage.storage.impl.StorageTickImpl;

public abstract class AbstractPatternPassivity implements PatternPrice {

  protected Integer countFirst = 100;
  protected Long timeFirst = 10000L;
  protected Integer countSecond = 1;
  protected Long timeSecond = 1000L;

  protected List<Tick> listTicksFirstPeriod;
  protected List<Tick> listTicksSecondPeriod;

  protected BigDecimal lastPrice;

  private final StorageTickImpl tickManagerService;

  public AbstractPatternPassivity(StorageTickImpl tickManagerService) {
    this.tickManagerService = tickManagerService;
  }

  @Override
  public void setParams(HashMap<String, Number> params) {
    this.countFirst = params.get("countFirst").intValue();
    this.timeFirst = params.get("timeFirst").longValue();
    this.countSecond = params.get("countSecond").intValue();
    this.timeSecond = params.get("timeSecond").longValue();
  }

  @Override
  public Signal getResponse() {
    getSelection();
    return getPattern().equals(Boolean.TRUE)
        ? new Signal(lastPrice, getPatternName(), TypeSignalPassivity.YES_PATTERN.getResponseCode(), null)
        : new Signal(null, getPatternName(), TypeSignalPassivity.NO_PATTERN.getResponseCode(), null);
  }

  private void getSelection() {
    listTicksSecondPeriod = tickManagerService.getListTickByTimeFromLastTick(timeSecond);

    Long timeStartByFirstList = listTicksSecondPeriod.get(0).getTimestamp() - 1;
    listTicksFirstPeriod = tickManagerService.getListTickByTime
        (timeStartByFirstList, timeStartByFirstList - timeFirst);
    this.lastPrice = listTicksSecondPeriod.get(listTicksSecondPeriod.size()-1).getBid();
  }

  private Boolean getPattern() {
    return listTicksFirstPeriod.size() >= countFirst && listTicksSecondPeriod.size() <= countSecond;
  }

  public HashMap<String, Number> getParams() {
    return new HashMap<>(Map.of(
        "countFirst", countFirst,
        "timeFirst", timeFirst,
        "countSecond", countSecond,
        "timeSecond", timeSecond
    ));
  }

  @Override
  public String getPatternName() {
    return "abstractPassivity";
  }
}
/**
 залипание - Проверка на больше чем Ннное количество тиков в заданное колличество миллисекунд - если проверка пройдена
 проверяем новую переменную времени в которой было не больше чем заданное колличество тиков в ед времени.
 ставим разноцветные нолики - по аску по биду или вместе в настройках
 */
