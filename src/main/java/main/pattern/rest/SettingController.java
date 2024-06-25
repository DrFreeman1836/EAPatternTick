package main.pattern.rest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import lombok.RequiredArgsConstructor;
import main.pattern.dto.SettingDto;
import main.pattern.service.PatternPrice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/ea/params")
@RequiredArgsConstructor
public class SettingController {

  @Qualifier("activityPattern")
  private final PatternPrice activityPattern;

  @Qualifier("passivityPattern")
  private final PatternPrice passivityPattern;

  @Qualifier("multiPattern")
  private final  PatternPrice multiPattern;

  @Qualifier("arrowPattern")
  private final  PatternPrice arrowPattern;

  private final Logger logger = LoggerFactory.getLogger(SettingController.class);

  @PutMapping("/arrow")
  public ResponseEntity<?> setSettingArrow(
      @RequestParam(name = "deltaTimeArrow") Long deltaTimeArrow,
      @RequestParam(name = "kRatio") BigDecimal kRatio,
      @RequestParam(name = "minAverage") BigDecimal minAverage
  ) {
    try {
      arrowPattern.setParams(new HashMap<>(Map.of(
          "deltaTimeArrow", deltaTimeArrow == null ? arrowPattern.getParams().get("deltaTimeArrow") : deltaTimeArrow,
          "kRatio", kRatio == null ? arrowPattern.getParams().get("kRatio") : kRatio,
          "minAverage", minAverage == null ? arrowPattern.getParams().get("minAverage") : minAverage)));
    } catch (Exception e) {
      logger.error("arrow no set settings, error: " + e.getMessage());
      return ResponseEntity.status(400).build();
    }
    return ResponseEntity.ok().build();
  }

  @PutMapping("/activity")
  public ResponseEntity<?> setActivityPattern(
      @RequestParam(name = "time") Integer time,
      @RequestParam(name = "count") Long count,
      @RequestParam(name = "deltaMaxAsk") BigDecimal deltaMaxAsk,
      @RequestParam(name = "deltaMinAsk") BigDecimal deltaMinAsk,
      @RequestParam(name = "deltaMaxBid") BigDecimal deltaMaxBid,
      @RequestParam(name = "deltaMinBid") BigDecimal deltaMinBid
  ) {
    try {
      activityPattern.setParams(new HashMap<>(Map.of(
          "time", time == null ? activityPattern.getParams().get("time") : time,
          "count", count == null ? activityPattern.getParams().get("count") : count,
          "deltaMaxAsk", deltaMaxAsk == null ? activityPattern.getParams().get("deltaMaxAsk") : deltaMaxAsk,
          "deltaMinAsk", deltaMinAsk == null ? activityPattern.getParams().get("deltaMinAsk") : deltaMinAsk,
          "deltaMaxBid", deltaMaxBid == null ? activityPattern.getParams().get("deltaMaxBid") : deltaMaxBid,
          "deltaMinBid", deltaMinBid == null ? activityPattern.getParams().get("deltaMinBid") : deltaMinBid)));
    } catch (Exception e) {
      logger.error("activity no set settings, error: " + e.getMessage());
      return ResponseEntity.status(400).build();
    }
    return ResponseEntity.ok().build();
  }

  @PutMapping("/passivity")
  public ResponseEntity<?> setPassivityPattern(
      @RequestParam(name = "countFirst") Integer countFirst,
      @RequestParam(name = "timeFirst") Long timeFirst,
      @RequestParam(name = "countSecond") Integer countSecond,
      @RequestParam(name = "timeSecond") Long timeSecond) {
    try {
      passivityPattern.setParams(new HashMap<>(Map.of(
          "countFirst",
          countFirst == null ? passivityPattern.getParams().get("countFirst") : countFirst,
          "timeFirst",
          timeFirst == null ? passivityPattern.getParams().get("timeFirst") : timeFirst,
          "countSecond",
          countSecond == null ? passivityPattern.getParams().get("countSecond") : countSecond,
          "timeSecond",
          timeSecond == null ? passivityPattern.getParams().get("timeSecond") : timeSecond
      )));
    } catch (Exception e) {
      logger.error("passivity no set settings, error: " + e.getMessage());
      return ResponseEntity.status(400).build();
    }
    return ResponseEntity.ok().build();
  }

  @PutMapping("/multi")
  public ResponseEntity<?> setMultiPattern(
      @RequestParam(name = "lowLevel") Integer lowLevel,
      @RequestParam(name = "highLevel") Integer highLevel,
      @RequestParam(name = "middleLevel") Integer middleLevel,
      @RequestParam(name = "time") Integer time,
      @RequestParam(name = "deltaMaxAsk") BigDecimal deltaMaxAsk,
      @RequestParam(name = "deltaMinAsk") BigDecimal deltaMinAsk,
      @RequestParam(name = "deltaMaxBid") BigDecimal deltaMaxBid,
      @RequestParam(name = "deltaMinBid") BigDecimal deltaMinBid) {
    try {
      multiPattern.setParams(new HashMap<>(Map.of(
          "count", multiPattern.getParams().get("count"),
          "highLevel", highLevel == null ? multiPattern.getParams().get("highLevel") : highLevel,
          "middleLevel", middleLevel == null ? multiPattern.getParams().get("middleLevel") : middleLevel,
          "lowLevel", lowLevel == null ? multiPattern.getParams().get("lowLevel") : lowLevel,
          "time", time == null ? multiPattern.getParams().get("time") : time,
          "deltaMaxAsk", deltaMaxAsk == null ? multiPattern.getParams().get("deltaMaxAsk") : deltaMaxAsk,
          "deltaMinAsk", deltaMinAsk == null ? multiPattern.getParams().get("deltaMinAsk") : deltaMinAsk,
          "deltaMaxBid", deltaMaxBid == null ? multiPattern.getParams().get("deltaMaxBid") : deltaMaxBid,
          "deltaMinBid", deltaMinBid == null ? multiPattern.getParams().get("deltaMinBid") : deltaMinBid)));
    } catch (Exception e) {
      logger.error("multi no set settings, error: " + e.getMessage());
      return ResponseEntity.status(400).build();
    }
    return ResponseEntity.ok().build();
  }

  @GetMapping()
  public List<SettingDto> getParams() {
    List<SettingDto> list = new ArrayList<>();
    for(Entry<String, Number> set : activityPattern.getParams().entrySet()) {
      list.add(SettingDto.builder().pattern("activity").name(set.getKey()).value(set.getValue()).build());
    }
    for(Entry<String, Number> set : multiPattern.getParams().entrySet()) {
      list.add(SettingDto.builder().pattern("multi").name(set.getKey()).value(set.getValue()).build());
    }
    for(Entry<String, Number> set : passivityPattern.getParams().entrySet()) {
      list.add(SettingDto.builder().pattern("passivity").name(set.getKey()).value(set.getValue()).build());
    }
    return list;
  }

}
