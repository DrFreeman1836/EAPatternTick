package patterns.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import main.pattern.dto.RsSignal.Signal;
import main.pattern.enam.TypeSignalMulti;
import main.pattern.enam.TypeSignalPassivity;
import main.pattern.service.impl.multi.MultiPattern;
import main.pattern.service.impl.passivity.PassivityPattern;
import main.price_storage.dto.TickDto;
import main.price_storage.storage.impl.StorageTickImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PassivityPatternTest {

  private StorageTickImpl storage = new StorageTickImpl();

  private PassivityPattern pattern = new PassivityPattern(storage);

  @Before
  public void setData() throws Exception {
    TickDto tick00 = new TickDto();
    tick00.setAsk(new BigDecimal("0.00005"));
    tick00.setBid(new BigDecimal("0.00006"));
    tick00.setTimeMsc(-20L);
    storage.processingTick(tick00, -20L);

    TickDto tick0 = new TickDto();
    tick0.setAsk(new BigDecimal("0.00005"));
    tick0.setBid(new BigDecimal("0.00006"));
    tick0.setTimeMsc(0L);
    storage.processingTick(tick0, 0L);
    TickDto tick1 = new TickDto();
    tick1.setAsk(new BigDecimal("0.00007"));
    tick1.setBid(new BigDecimal("0.00008"));
    tick1.setTimeMsc(20L);
    storage.processingTick(tick1, 20L);
    TickDto tick2 = new TickDto();
    tick2.setAsk(new BigDecimal("0.00009"));
    tick2.setBid(new BigDecimal("0.00010"));
    tick2.setTimeMsc(40L);
    storage.processingTick(tick2, 40L);
    TickDto tick3 = new TickDto();
    tick3.setAsk(new BigDecimal("0.00011"));
    tick3.setBid(new BigDecimal("0.00012"));
    tick3.setTimeMsc(60L);
    storage.processingTick(tick3, 60L);
    TickDto tick4 = new TickDto();
    tick4.setAsk(new BigDecimal("0.00013"));
    tick4.setBid(new BigDecimal("0.00014"));
    tick4.setTimeMsc(80L);
    storage.processingTick(tick4, 80L);
    TickDto tick5 = new TickDto();
    tick5.setAsk(new BigDecimal("0.00014"));
    tick5.setBid(new BigDecimal("0.00015"));
    tick5.setTimeMsc(100L);
    storage.processingTick(tick5, 100L);
    TickDto tick6 = new TickDto();
    tick6.setAsk(new BigDecimal("0.00016"));
    tick6.setBid(new BigDecimal("0.00017"));
    tick6.setTimeMsc(120L);
    storage.processingTick(tick6, 120L);
  }

  /**
   залипание - Проверка на больше чем Ннное количество тиков в заданное колличество миллисекунд - если проверка пройдена
   проверяем новую переменную времени в которой было меньше чем заданное колличество тиков в ед времени.
   ставим разноцветные нолики - по аску по биду или вместе в настройках
   */

  @Test
  @Order(1)
  public void patternTest() {
    pattern.setParams(new HashMap<>(Map.of(
        "timeFirst", 80,
        "countFirst", 5,
        "timeSecond", 40,
        "countSecond", 2)));
    Signal signal = pattern.getResponse();
    Assert.assertEquals(TypeSignalPassivity.YES_PATTERN.getResponseCode(), signal.pattern());
    Assert.assertEquals(new BigDecimal("0.00015"), signal.price());
    Assert.assertEquals("passivity", signal.type());
  }

  @Test
  @Order(1)
  public void noPatternTestByFirstPeriod() {
    pattern.setParams(new HashMap<>(Map.of(
        "timeFirst", 80,
        "countFirst", 6,
        "timeSecond", 20,
        "countSecond", 1)));
    Signal signal = pattern.getResponse();
    Assert.assertEquals(TypeSignalPassivity.NO_PATTERN.getResponseCode(), signal.pattern());
    Assert.assertEquals(null, signal.price());
    Assert.assertEquals("passivity", signal.type());
  }

  @Test
  @Order(1)
  public void noPatternTestBySecondPeriod() {
    pattern.setParams(new HashMap<>(Map.of(
        "timeFirst", 80,
        "countFirst", 5,
        "timeSecond", 40,
        "countSecond", 1)));
    Signal signal = pattern.getResponse();
    Assert.assertEquals(TypeSignalPassivity.NO_PATTERN.getResponseCode(), signal.pattern());
    Assert.assertEquals(null, signal.price());
    Assert.assertEquals("passivity", signal.type());
  }

}
