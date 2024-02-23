package main.pattern.service;

import java.util.HashMap;
import main.pattern.dto.RsSignal.Signal;

public abstract class AbstractPatternArrow implements PatternPrice {

  @Override
  public void setParams(HashMap<String, Number> params) {

  }

  @Override
  public Signal getResponse() {
    return null;
  }

  @Override
  public String getPatternName() {
    return null;
  }

  @Override
  public HashMap<String, Number> getParams() {
    return null;
  }
}
