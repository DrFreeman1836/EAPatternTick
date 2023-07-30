package main.pattern.service.impl.multi;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import main.pattern.dto.RsSignal.Signal;
import main.pattern.enam.TypeSignalMulti;
import main.pattern.service.AbstractPatternActivity;
import main.price_storage.storage.impl.StorageTickImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MultiPattern extends AbstractPatternActivity {

  private Integer LOW_LEVEL = 15;
  private Integer MIDDLE_LEVEL = 20;
  private Integer HIGH_LEVEL = 25;
  private final HashSet<Integer> checkResponse = new HashSet<Integer>(Set.of(200, 201, 202));

  @Autowired
  public MultiPattern(StorageTickImpl tickManagerService) {
    super(tickManagerService);
  }

  @Override
  public Signal getResponse() {
    setLevelSettings(HIGH_LEVEL);
    if (checkResponse.contains(super.getResponse().pattern())) {
      return new Signal(lastPriceBid, getPatternName(), TypeSignalMulti.HIGH_LEVEL.getResponseCode(), null);
    }
    setLevelSettings(MIDDLE_LEVEL);
    if (checkResponse.contains(super.getResponse().pattern())) {
      return new Signal(lastPriceBid, getPatternName(), TypeSignalMulti.MIDDLE_LEVEL.getResponseCode(), null);
    }
    setLevelSettings(LOW_LEVEL);
    if (checkResponse.contains(super.getResponse().pattern())) {
      return new Signal(lastPriceBid, getPatternName(), TypeSignalMulti.LOW_LEVEL.getResponseCode(), null);
    }

    return new Signal(null, getPatternName(), TypeSignalMulti.NO_PATTERN.getResponseCode(), null);
  }

  @Override
  public String getPatternName() {
    return "multi";
  }

  private void setLevelSettings(Integer level) {
    super.setParams(new HashMap<>(Map.of(
        "time", getParams().get("time"),
        "count", level,
        "deltaMaxAsk", getParams().get("deltaMaxAsk"),
        "deltaMinAsk", getParams().get("deltaMinAsk"),
        "deltaMaxBid", getParams().get("deltaMaxBid"),
        "deltaMinBid", getParams().get("deltaMinBid")
    )));
  }

  @Override
  public void setParams(HashMap<String, Number> params) {
    this.HIGH_LEVEL = (Integer) params.get("highLevel");
    this.MIDDLE_LEVEL = (Integer) params.get("middleLevel");
    this.LOW_LEVEL = (Integer) params.get("lowLevel");
    super.setParams(params);
  }

  @Override
  public HashMap<String, Number> getParams() {
    HashMap<String, Number> params = new HashMap<>(super.getParams());
    params.put("highLevel", HIGH_LEVEL);
    params.put("middleLevel", MIDDLE_LEVEL);
    params.put("lowLevel", LOW_LEVEL);
    return params;
  }

}
