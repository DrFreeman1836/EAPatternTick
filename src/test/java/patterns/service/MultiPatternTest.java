package patterns.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import main.pattern.enam.TypeSignalMulti;
import main.pattern.service.impl.multi.MultiPattern;
import main.price_storage.dto.TickDto;
import main.price_storage.storage.impl.StorageTickImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MultiPatternTest {

  private StorageTickImpl storage = new StorageTickImpl();

  private MultiPattern pattern = new MultiPattern(storage);

  @Before
  public void setData() throws Exception {
    TickDto tick0 = new TickDto();
    tick0.setAsk(new BigDecimal("0.00005"));
    tick0.setBid(new BigDecimal("0.00006"));
    storage.processingTick(tick0, 0L);
    TickDto tick1 = new TickDto();
    tick1.setAsk(new BigDecimal("0.00007"));
    tick1.setBid(new BigDecimal("0.00008"));
    storage.processingTick(tick1, 90L);
    TickDto tick2 = new TickDto();
    tick2.setAsk(new BigDecimal("0.00009"));
    tick2.setBid(new BigDecimal("0.00010"));
    storage.processingTick(tick2, 100L);
    TickDto tick3 = new TickDto();
    tick3.setAsk(new BigDecimal("0.00011"));
    tick3.setBid(new BigDecimal("0.00012"));
    storage.processingTick(tick3, 110L);
    TickDto tick4 = new TickDto();
    tick4.setAsk(new BigDecimal("0.00013"));
    tick4.setBid(new BigDecimal("0.00014"));
    storage.processingTick(tick4, 150L);
  }

  @Test
  @Order(1)
  public void noPatternTestByTime() {
    pattern.setParams(new HashMap<>(Map.of(
        "time", 30,
        "count", 1,
        "lowLevel", 2,
        "middleLevel", 3,
        "highLevel", 5,
        "deltaMaxAsk", new BigDecimal("0.005"),
        "deltaMinAsk", new BigDecimal("0.00001"),
        "deltaMaxBid", new BigDecimal("0.005"),
        "deltaMinBid", new BigDecimal("0.00001"))));
    Assert.assertEquals(TypeSignalMulti.NO_PATTERN.getResponseCode(), pattern.getResponse());
  }

  @Test
  @Order(2)
  public void patternLowLevel() {
    pattern.setParams(new HashMap<>(Map.of(
        "time", 41,
        "count", 1,
        "lowLevel", 2,
        "middleLevel", 3,
        "highLevel", 5,
        "deltaMaxAsk", new BigDecimal("0.005"),
        "deltaMinAsk", new BigDecimal("0.00001"),
        "deltaMaxBid", new BigDecimal("0.005"),
        "deltaMinBid", new BigDecimal("0.00001"))));
    Assert.assertEquals(TypeSignalMulti.LOW_LEVEL.getResponseCode(), pattern.getResponse());
  }

  @Test
  @Order(3)
  public void patternMiddleLevel() {
    pattern.setParams(new HashMap<>(Map.of(
        "time", 60,
        "count", 1,
        "lowLevel", 2,
        "middleLevel", 3,
        "highLevel", 5,
        "deltaMaxAsk", new BigDecimal("0.005"),
        "deltaMinAsk", new BigDecimal("0.00001"),
        "deltaMaxBid", new BigDecimal("0.005"),
        "deltaMinBid", new BigDecimal("0.00001"))));
    Assert.assertEquals(TypeSignalMulti.MIDDLE_LEVEL.getResponseCode(), pattern.getResponse());
  }

  @Test
  @Order(4)
  public void patternHighLevel() {
    pattern.setParams(new HashMap<>(Map.of(
        "time", 151,
        "count", 1,
        "lowLevel", 2,
        "middleLevel", 3,
        "highLevel", 5,
        "deltaMaxAsk", new BigDecimal("0.005"),
        "deltaMinAsk", new BigDecimal("0.00001"),
        "deltaMaxBid", new BigDecimal("0.005"),
        "deltaMinBid", new BigDecimal("0.00001"))));
    Assert.assertEquals(TypeSignalMulti.HIGH_LEVEL.getResponseCode(), pattern.getResponse());
  }

  @Test
  @Order(5)
  public void noPatternHighLevelByDeltaMax() {
    pattern.setParams(new HashMap<>(Map.of(
        "time", 151,
        "count", 1,
        "lowLevel", 2,
        "middleLevel", 3,
        "highLevel", 5,
        "deltaMaxAsk", new BigDecimal("0.00007"),
        "deltaMinAsk", new BigDecimal("0.00001"),
        "deltaMaxBid", new BigDecimal("0.00007"),
        "deltaMinBid", new BigDecimal("0.00001"))));
    Assert.assertEquals(TypeSignalMulti.MIDDLE_LEVEL.getResponseCode(), pattern.getResponse());
  }

}
