package main.pattern.service;

import java.util.HashMap;
import main.pattern.dto.RsSignal.Signal;

public interface PatternPrice {

  void setParams(HashMap<String, Number> params);

  Signal getResponse();

  String getPatternName();

  HashMap<String, Number> getParams();

}
