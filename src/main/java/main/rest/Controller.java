package main.rest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import main.dto.SettingDto;
import main.dto.SettingPatternsDto;
import main.service.PatternPrice;
import main.service.direction.DirectionService;
import main.storage.tick.ManagerTicks;
import main.telegram.impl.TelegramBotMessages;
import main.storage.tick.impl.TickManagerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/ea")
public class Controller {

  private ManagerTicks tickManagerService;

  private PatternPrice activityPattern;

  private PatternPrice passivityPattern;

  private PatternPrice multiPattern;

  private DirectionService directionService;

  private TelegramBotMessages bot;

  @Autowired
  @Qualifier("tickManagerServiceImpl")
  public void setTickManagerService(TickManagerServiceImpl tickManagerService) {
    this.tickManagerService = tickManagerService;
  }

  @Autowired
  public void setDirectionService(DirectionService directionService) {
    this.directionService = directionService;
  }

  @Autowired
  @Qualifier("multiPattern")
  public void setMultiPattern(PatternPrice multiPattern) {
    this.multiPattern = multiPattern;
  }

  @Autowired
  @Qualifier("passivityPattern")
  public void setPassivityPattern(PatternPrice passivityPattern) {
    this.passivityPattern = passivityPattern;
  }

  @Autowired
  @Qualifier("activityPattern")
  public void setActivityPattern(PatternPrice activityPattern) {
    this.activityPattern = activityPattern;
  }

  @Autowired
  public void setBot(TelegramBotMessages bot) {
    this.bot = bot;
  }

  @PutMapping("/params")
  public ResponseEntity<?> setParams(@RequestBody SettingPatternsDto setting) {
    System.out.println(setting);
    try {
      if (setting.getPattern().equals("activity")) {
        activityPattern.setParams(new HashMap<>(Map.of(
            "time", setting.getTime() == null ? activityPattern.getParams().get("time") : setting.getTime(),
            "count", setting.getCount() == null ? activityPattern.getParams().get("count") : setting.getCount(),
            "deltaMaxAsk", setting.getDeltaMaxAsk() == null ? activityPattern.getParams().get("deltaMaxAsk") : setting.getDeltaMaxAsk(),
            "deltaMinAsk", setting.getDeltaMinAsk() == null ? activityPattern.getParams().get("deltaMinAsk") : setting.getDeltaMinAsk(),
            "deltaMaxBid", setting.getDeltaMaxBid() == null ? activityPattern.getParams().get("deltaMaxBid") : setting.getDeltaMaxBid(),
            "deltaMinBid", setting.getDeltaMinBid() == null ? activityPattern.getParams().get("deltaMinBid") : setting.getDeltaMinBid())));

        multiPattern.setParams(new HashMap<>(Map.of(
            "highLevel", setting.getHighLevel() == null ? multiPattern.getParams().get("highLevel") : setting.getHighLevel(),
            "middleLevel", setting.getMiddleLevel() == null ? multiPattern.getParams().get("middleLevel") : setting.getMiddleLevel(),
            "lowLevel", setting.getLowLevel() == null ? multiPattern.getParams().get("lowLevel") : setting.getLowLevel(),
            "time", setting.getTime() == null ? multiPattern.getParams().get("time") : setting.getTime(),
            "count", setting.getCount() == null ? multiPattern.getParams().get("count") : setting.getCount(),
            "deltaMaxAsk", setting.getDeltaMaxAsk() == null ? multiPattern.getParams().get("deltaMaxAsk") : setting.getDeltaMaxAsk(),
            "deltaMinAsk", setting.getDeltaMinAsk() == null ? multiPattern.getParams().get("deltaMinAsk") : setting.getDeltaMinAsk(),
            "deltaMaxBid", setting.getDeltaMaxBid() == null ? multiPattern.getParams().get("deltaMaxBid") : setting.getDeltaMaxBid(),
            "deltaMinBid", setting.getDeltaMinBid() == null ? multiPattern.getParams().get("deltaMinBid") : setting.getDeltaMinBid())));
        return ResponseEntity.ok("Настройки установлены");
      }

      if (setting.getPattern().equals("passivity")) {
        passivityPattern.setParams(new HashMap<>(Map.of(
            "countFirst", setting.getCountFirst() == null ? passivityPattern.getParams().get("countFirst") : setting.getCountFirst(),
            "timeFirst", setting.getTimeFirst() == null ? passivityPattern.getParams().get("timeFirst") : setting.getTimeFirst(),
            "countSecond", setting.getCountSecond() == null ? passivityPattern.getParams().get("countSecond") : setting.getCountSecond(),
            "timeSecond", setting.getTimeSecond() == null ? passivityPattern.getParams().get("timeSecond") : setting.getTimeSecond()
        )));
        return ResponseEntity.ok("Настройки установлены");
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      return ResponseEntity.status(404).body("Настройки не установлены");
    }
    return ResponseEntity.status(400).body("Передано неверное имя паттерна");
  }

  @GetMapping("/params")
  public List<SettingDto> getParams() {
    List<SettingDto> list = new ArrayList<>();
    for(Entry<String, Number> set : activityPattern.getParams().entrySet()) {
      list.add(SettingDto.builder().pattern("activity").name(set.getKey()).value(set.getValue()).build());
    }
    list.add(SettingDto.builder().pattern("activity").name("highLevel").value(multiPattern.getParams()
        .get("highLevel")).build());
    list.add(SettingDto.builder().pattern("activity").name("middleLevel").value(multiPattern.getParams()
        .get("middleLevel")).build());
    list.add(SettingDto.builder().pattern("activity").name("lowLevel").value(multiPattern.getParams()
        .get("lowLevel")).build());
    for(Entry<String, Number> set : passivityPattern.getParams().entrySet()) {
      list.add(SettingDto.builder().pattern("passivity").name(set.getKey()).value(set.getValue()).build());
    }
    return list;
  }

  @PostMapping("/signal")
  public ResponseEntity<?> addTick(
      @RequestParam(name = "priceAsk") BigDecimal priceAsk,
      @RequestParam(name = "priceBid") BigDecimal priceBid,
      @RequestParam(name = "pattern") String pattern) {

    try {
      tickManagerService.processingTick(priceAsk, priceBid, System.currentTimeMillis());

      if (pattern.equals("activity")) {
        int res = activityPattern.getResponse();
        if (res != 404) bot.sendMessage(String.valueOf(res) + " новый активный");
        return ResponseEntity.status(res).build();
      }
      if (pattern.equals("passivity")) {
        int res = passivityPattern.getResponse();
        if (res != 404) bot.sendMessage(String.valueOf(res) + " новый пассивный");
        return ResponseEntity.status(res).build();
      }
      if (pattern.equals("multi")) {
        int res = multiPattern.getResponse();
        if (res != 404) bot.sendMessage(String.valueOf(res) + " новый мульти");
        return ResponseEntity.status(res).build();
      }

    } catch (Exception ex) {
      ex.printStackTrace();
      bot.sendMessage(ex.toString());
      return ResponseEntity.status(500).build();
    }
    return ResponseEntity.status(405).build();
  }

  @GetMapping("/signal")
  public ResponseEntity<?> getDirection(
      @RequestParam(name = "priceAsk") BigDecimal priceAsk,
      @RequestParam(name = "priceBid") BigDecimal priceBid,
      @RequestParam(name = "diffTime") Long diffTime) {
    int res = directionService.getDirection(priceAsk, priceBid, diffTime).getResponseCode();
    if (res != 404) bot.sendMessage(String.valueOf(res) + " новый активный");
    return ResponseEntity.status(res).build();
  }

}
