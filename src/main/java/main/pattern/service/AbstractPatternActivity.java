package main.pattern.service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import main.pattern.enam.TypeSignalActivity;
import main.price_storage.model.Tick;
import main.price_storage.storage.impl.StorageTickImpl;

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

  private StorageTickImpl tickManagerService;

  public AbstractPatternActivity(StorageTickImpl tickManagerService) {
    this.tickManagerService = tickManagerService;
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

    if (maxTime - minTime >= time) {
      return TypeSignalActivity.NO_PATTERN.getResponseCode();
    }
    boolean patternAsk = checkPatternAsk();
    boolean patternBid = checkPatternBid();
    if (patternAsk && patternBid) {
      return TypeSignalActivity.ALL.getResponseCode();
    }
    if (patternAsk) {
      return TypeSignalActivity.ASK.getResponseCode();
    }
    if (patternBid) {
      return TypeSignalActivity.BID.getResponseCode();
    }
    return TypeSignalActivity.NO_PATTERN.getResponseCode();
  }

  private void getSelection() {
    listTicks = tickManagerService.getListTickByCount(count);//count
    maxPriceAsk = listTicks.stream()
        .max(Comparator.comparing(Tick::getAsk))
        .get().getAsk();
    minPriceAsk = listTicks.stream()
        .min(Comparator.comparing(Tick::getAsk))
        .get().getAsk();

    maxPriceBid = listTicks.stream()
        .max(Comparator.comparing(Tick::getBid))
        .get().getBid();
    minPriceBid = listTicks.stream()
        .min(Comparator.comparing(Tick::getBid))
        .get().getBid();

    maxTime = listTicks.stream()
        .max(Comparator.comparing(Tick::getTimestamp))
        .get().getTimestamp();
    minTime = listTicks.stream()
        .min(Comparator.comparing(Tick::getTimestamp))
        .get().getTimestamp();
  }

  private boolean checkPatternAsk() {
    BigDecimal diffPriceAsk = maxPriceAsk.subtract(minPriceAsk);
    return diffPriceAsk.compareTo(deltaMaxAsk) <= 0 && diffPriceAsk.compareTo(deltaMinAsk) >= 0;
  }

  private boolean checkPatternBid() {
    BigDecimal diffPriceBid = maxPriceBid.subtract(minPriceBid);
    return diffPriceBid.compareTo(deltaMaxBid) <= 0 && diffPriceBid.compareTo(deltaMinBid) >= 0;
  }

}