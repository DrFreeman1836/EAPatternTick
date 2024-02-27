package main.pattern.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import main.pattern.dto.RsSignal.Signal;
import main.pattern.enam.TypeSignalArrow;
import main.price_storage.model.Tick;
import main.price_storage.storage.impl.StorageTickImpl;

public abstract class AbstractPatternArrow implements PatternPrice {

  protected Long deltaTimeArrow;

  protected BigDecimal kRatio;

  protected BigDecimal minAverage;

  private final StorageTickImpl tickManagerService;

  protected AbstractPatternArrow(StorageTickImpl tickManagerService) {
    this.tickManagerService = tickManagerService;
  }

  @Override
  public void setParams(HashMap<String, Number> params) {
    this.deltaTimeArrow = params.get("deltaTimeArrow").longValue();
    this.kRatio = new BigDecimal(params.get("kRatio").toString());
    this.minAverage = new BigDecimal(params.get("minAverage").toString());
  }

  @Override
  public Signal getResponse() {
    try {
      List<Tick> listTick = tickManagerService.getListTickByTimeFromLastTickArrow(deltaTimeArrow);
      BigDecimal averageAsk = calculationAverage(listTick.stream().map(Tick::getSizeAsk).toList());
      BigDecimal averageBid = calculationAverage(listTick.stream().map(Tick::getSizeBid).toList());
      if (averageBid.compareTo(minAverage) < 0 || averageAsk.compareTo(minAverage) < 0)
        return new Signal(null, getPatternName(), TypeSignalArrow.NO_PATTERN.getResponseCode(), null);

      if (averageAsk.compareTo(averageBid) > 0 && averageAsk.divide(averageBid).compareTo(kRatio) > 0) {
        return new Signal(listTick.get(0).getAsk(), getPatternName(), TypeSignalArrow.ASK.getResponseCode(), null, averageAsk.divide(averageBid));
      }
      if (averageBid.compareTo(averageAsk) > 0 && averageBid.divide(averageAsk).compareTo(kRatio) > 0) {
        return new Signal(listTick.get(0).getBid(), getPatternName(), TypeSignalArrow.BID.getResponseCode(), null, averageBid.divide(averageAsk));
      }

    } catch (Exception e) {
      return new Signal(null, getPatternName(), TypeSignalArrow.ERROR.getResponseCode(), null);
    }
    return new Signal(null, getPatternName(), TypeSignalArrow.NO_PATTERN.getResponseCode(), null);
  }


  private BigDecimal calculationAverage(List<BigDecimal> listPrice) {
    BigDecimal sum = listPrice.stream()
        .map(BigDecimal::abs)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    return sum.multiply(BigDecimal.ONE.divide(BigDecimal.valueOf(listPrice.size()), 2, RoundingMode.HALF_UP));
  }

  @Override
  public String getPatternName() {
    return "abstractArrowPattern";
  }

  @Override
  public HashMap<String, Number> getParams() {
    return new HashMap<>(Map.of(
        "deltaTimeArrow", deltaTimeArrow,
        "kRatio", kRatio,
        "minAverage", minAverage));
  }
}
