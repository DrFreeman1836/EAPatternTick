package main.service.impl.trend;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import main.enam.TypeSignalActivity;
import main.model.Tick;
import main.storage.tick.impl.TickManagerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatternDeltaPriceTrend  {

  private long time;//ms

  private int count;

  private BigDecimal deltaMaxAsk;

  private BigDecimal deltaMinAsk;

  private BigDecimal deltaMaxBid;

  private BigDecimal deltaMinBid;

  private List<Tick> listTicks;

  private long minTime;

  private long maxTime;

  private BigDecimal maxPriceAsk;

  private BigDecimal minPriceAsk;

  private BigDecimal maxPriceBid;

  private BigDecimal minPriceBid;

  private BigDecimal minDeltaTrend;

  private TickManagerServiceImpl tickManagerService;

  private Trend trend;

  @Autowired
  public void setTickManagerService(TickManagerServiceImpl tickManagerService) {
    this.tickManagerService = tickManagerService;
  }

  @Autowired
  public void setTrend(Trend trend) {
    this.trend = trend;
  }

  public void setParams(HashMap<String, Number> params) {
    this.time = params.get("time").intValue();
    this.count = params.get("count").intValue();
    this.deltaMaxAsk = new BigDecimal(params.get("deltaMaxAsk").toString());
    this.deltaMinAsk = new BigDecimal(params.get("deltaMinAsk").toString());
    this.deltaMaxBid = new BigDecimal(params.get("deltaMaxBid").toString());
    this.deltaMinBid = new BigDecimal(params.get("deltaMinBid").toString());
    this.minDeltaTrend = new BigDecimal(params.get("minDeltaTrend").toString());
  }

  public int getResponse() {
    if(tickManagerService.sizeStorageTicks() < count) {
      return TypeSignalActivity.ERROR.getResponseCode();
    }
    getSelection();
//    if (checkPatternAsk()) {
//      BigDecimal resultTrendAsk = trend.getTrend(listTicks.stream().map(Tick::getPriceAsk).toList());
//      if(resultTrendAsk.compareTo(new BigDecimal(0)) > 0){
//        return resultTrendAsk.compareTo(minDeltaTrend) >= 0 ? TypeSignalPassivity.BUY.getResponseCode() : TypeSignalActivity.NO_PATTERN.getResponseCode();
//      }
//      if(resultTrendAsk.compareTo(new BigDecimal(0)) < 0){
//        return resultTrendAsk.abs().compareTo(minDeltaTrend) >= 0 ? TypeSignalPassivity.SELL.getResponseCode() : TypeSignalActivity.NO_PATTERN.getResponseCode();
//      }
//    }
//    if (checkPatternBid()) {
//      BigDecimal resultTrendAsk = trend.getTrend(listTicks.stream().map(Tick::getPriceBid).toList());
//      if(resultTrendAsk.compareTo(new BigDecimal(0)) > 0){
//        return resultTrendAsk.compareTo(minDeltaTrend) >= 0 ? TypeSignalPassivity.BUY.getResponseCode() : TypeSignalActivity.NO_PATTERN.getResponseCode();
//      }
//      if(resultTrendAsk.compareTo(new BigDecimal(0)) < 0){
//        return resultTrendAsk.abs().compareTo(minDeltaTrend) >= 0 ? TypeSignalPassivity.SELL.getResponseCode() : TypeSignalActivity.NO_PATTERN.getResponseCode();
//      }
//    }
    return TypeSignalActivity.NO_PATTERN.getResponseCode();
  }

  private void getSelection() {
    listTicks = tickManagerService.getListTickByCount(count);
    maxPriceAsk = listTicks.stream().max(Comparator.comparing(Tick::getPriceAsk)).get().getPriceAsk();
    minPriceAsk = listTicks.stream().min(Comparator.comparing(Tick::getPriceAsk)).get().getPriceAsk();

    maxPriceBid = listTicks.stream().max(Comparator.comparing(Tick::getPriceBid)).get().getPriceBid();
    minPriceBid = listTicks.stream().min(Comparator.comparing(Tick::getPriceBid)).get().getPriceBid();

    maxTime = listTicks.stream().max(Comparator.comparing(Tick::getTimestamp)).get().getTimestamp();
    minTime = listTicks.stream().min(Comparator.comparing(Tick::getTimestamp)).get().getTimestamp();
  }

  private boolean checkPatternAsk() {
    if (maxPriceAsk.subtract(minPriceAsk).compareTo(deltaMaxAsk) <= 0
        && maxPriceAsk.subtract(minPriceAsk).compareTo(deltaMinAsk) >= 0
        && maxTime - minTime < time) {
      return true;
    }
    return false;
  }

  private boolean checkPatternBid() {
    if (maxPriceBid.subtract(minPriceBid).compareTo(deltaMaxBid) <= 0
        && maxPriceBid.subtract(minPriceBid).compareTo(deltaMinBid) >= 0
        && maxTime - minTime < time) {
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
