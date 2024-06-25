package main.pattern.rest;

import lombok.RequiredArgsConstructor;
import main.notification.impl.TelegramBotMessages;
import main.pattern.dto.RsSignal;
import main.pattern.dto.RsSignal.Signal;
import main.pattern.enam.TypeSignalPassivity;
import main.pattern.service.PatternPrice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/ea")
@RequiredArgsConstructor
public class Controller {

  @Qualifier("activityPattern")
  private final PatternPrice activityPattern;

  @Qualifier("passivityPattern")
  private final PatternPrice passivityPattern;

  @Qualifier("multiPattern")
  private final PatternPrice multiPattern;

  @Qualifier("arrowPattern")
  private final  PatternPrice arrowPattern;

  private final TelegramBotMessages bot;

  private final Logger logger = LoggerFactory.getLogger(Controller.class);


  @GetMapping("/signal")
  public ResponseEntity<RsSignal> getPatterns(
      @RequestParam(name = "activity") Boolean activity,
      @RequestParam(name = "passivity") Boolean passivity,
      @RequestParam(name = "multi") Boolean multi,
      @RequestParam(name = "arrow") Boolean arrow) {

    RsSignal rsSignal = new RsSignal();
    StringBuilder sbLog = new StringBuilder();
    if (activity) {
      Signal res = activityPattern.getResponse();
      if (checkResponse(res.pattern())) {
        buildResponse(rsSignal, sbLog, res);
      }
    }
    if (passivity) {
      Signal res = passivityPattern.getResponse();
      if (checkResponse(res.pattern())) {
        buildResponse(rsSignal, sbLog, res);
      }
    }
    if (multi) {
      Signal res = multiPattern.getResponse();
      if (checkResponse(res.pattern())) {
        buildResponse(rsSignal, sbLog, res);
      }
    }
    if (arrow) {
      Signal res = arrowPattern.getResponse();
      if (checkResponse(res.pattern())) {
        buildResponse(rsSignal, sbLog, res);
      }
    }

    if (!sbLog.isEmpty()) {
      logger.info(sbLog.toString());
      bot.sendMessage(sbLog.toString());
    }
    return ResponseEntity.status(200).body(rsSignal);
  }

  private boolean checkResponse(int res) {
    return res != TypeSignalPassivity.NO_PATTERN.getResponseCode() && res != TypeSignalPassivity.ERROR.getResponseCode();
  }

  private void buildResponse(RsSignal rsSignal, StringBuilder sb, Signal signal) {
    rsSignal.addSignal(signal);
    sb.append(String.format("%s: %s trend: %s kRation: %s", signal.type(), signal.pattern(), signal.trend(), signal.kRatio() == null ? null : signal.kRatio())).append("\n");
  }

}
