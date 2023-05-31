package main.service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import main.enam.TypeSignalActivity;
import main.model.Tick;
import main.storage.response.ResponseStorage;
import main.storage.tick.impl.TickManagerServiceImpl;

public abstract class AbstractPatternActivity implements PatternPrice {

  protected Long time = 260L;
  protected Integer count = 9;
  protected BigDecimal deltaMaxAsk = BigDecimal.valueOf(0.00010);
  protected BigDecimal deltaMinAsk = BigDecimal.valueOf(0.00003);
  protected BigDecimal deltaMaxBid = BigDecimal.valueOf(0.00010);
  protected BigDecimal deltaMinBid = BigDecimal.valueOf(0.00003);

  protected List<Tick> listTicks;

  private Long minTime;

  private Long maxTime;

  private BigDecimal maxPriceAsk;

  private BigDecimal minPriceAsk;

  private BigDecimal maxPriceBid;

  private BigDecimal minPriceBid;

  private TickManagerServiceImpl tickManagerService;

  protected ResponseStorage responseStorage;

  public AbstractPatternActivity(TickManagerServiceImpl tickManagerService, ResponseStorage responseStorage) {
    this.tickManagerService = tickManagerService;
    this.responseStorage = responseStorage;
  }

  public void setParams(HashMap<String, Number> params) {
    this.time = params.get("time").longValue();
    this.count = params.get("count").intValue();
    this.deltaMaxAsk = new BigDecimal(params.get("deltaMaxAsk").toString());
    this.deltaMinAsk = new BigDecimal(params.get("deltaMinAsk").toString());
    this.deltaMaxBid = new BigDecimal(params.get("deltaMaxBid").toString());
    this.deltaMinBid = new BigDecimal(params.get("deltaMinBid").toString());
  }

  public HashMap<String, Number> getParams() {
    return new HashMap<>(Map.of("time", time,
        "count", count,
        "deltaMaxAsk", deltaMaxAsk,
        "deltaMinAsk", deltaMinAsk,
        "deltaMaxBid", deltaMaxBid,
        "deltaMinBid", deltaMinBid));
  }

  public int getResponse() {
    if (tickManagerService.sizeStorageTicks() < count) {//count
      return TypeSignalActivity.ERROR.getResponseCode();
    }
    getSelection();
    if (checkPatternAll()) {
      return TypeSignalActivity.ALL.getResponseCode();
    }
    if (checkPatternAsk()) {
      return TypeSignalActivity.ASK.getResponseCode();
    }
    if (checkPatternBid()) {
      return TypeSignalActivity.BID.getResponseCode();
    }
    return TypeSignalActivity.NO_PATTERN.getResponseCode();
  }

  private void getSelection() {
    listTicks = tickManagerService.getListTickByCount(count);//count
    maxPriceAsk = listTicks.stream()
        .max(Comparator.comparing(Tick::getPriceAsk))
        .get().getPriceAsk();
    minPriceAsk = listTicks.stream()
        .min(Comparator.comparing(Tick::getPriceAsk))
        .get().getPriceAsk();

    maxPriceBid = listTicks.stream()
        .max(Comparator.comparing(Tick::getPriceBid))
        .get().getPriceBid();
    minPriceBid = listTicks.stream()
        .min(Comparator.comparing(Tick::getPriceBid))
        .get().getPriceBid();

    maxTime = listTicks.stream()
        .max(Comparator.comparing(Tick::getTimestamp))
        .get().getTimestamp();
    minTime = listTicks.stream()
        .min(Comparator.comparing(Tick::getTimestamp))
        .get().getTimestamp();
  }

  private boolean checkPatternAsk() {
    if (maxPriceAsk.subtract(minPriceAsk).compareTo(deltaMaxAsk) <= 0//deltaMaxAsk
        && maxPriceAsk.subtract(minPriceAsk).compareTo(deltaMinAsk) >= 0//deltaMinAsk
        && maxTime - minTime < time) {//time
      return true;
    }
    return false;
  }

  private boolean checkPatternBid() {
    if (maxPriceBid.subtract(minPriceBid).compareTo(deltaMaxBid) <= 0//deltaMaxBid
        && maxPriceBid.subtract(minPriceBid).compareTo(deltaMinBid) >= 0//deltaMinBid
        && maxTime - minTime < time) {//time
      return true;
    }
    return false;
  }

  private boolean checkPatternAll() {
    if (checkPatternAsk() && checkPatternBid()) {
      return true;
    }
    return false;
  }

}