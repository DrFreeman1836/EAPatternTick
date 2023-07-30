package patterns.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import main.pattern.service.impl.activity.ActivityPattern;
import main.price_storage.dto.TickDto;
import main.price_storage.storage.impl.StorageTickImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ActivityPatternTest {

  private StorageTickImpl storage = new StorageTickImpl();

  private ActivityPattern pattern = new ActivityPattern(storage);

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
  }

  @Test
  @Order(1)
  public void noPatternTestByTime() {
    pattern.setParams(new HashMap<>(Map.of(
        "time", 110,
        "count", 4,
        "deltaMaxAsk", new BigDecimal("0.005"),
        "deltaMinAsk", new BigDecimal("0.00001"),
        "deltaMaxBid", new BigDecimal("0.005"),
        "deltaMinBid", new BigDecimal("0.00001"))));
    Assert.assertEquals(404, pattern.getResponse());
  }

  @Test
  @Order(2)
  public void patternTestByTime() {
    pattern.setParams(new HashMap<>(Map.of(
        "time", 111,
        "count", 4,
        "deltaMaxAsk", new BigDecimal("0.005"),
        "deltaMinAsk", new BigDecimal("0.00001"),
        "deltaMaxBid", new BigDecimal("0.005"),
        "deltaMinBid", new BigDecimal("0.00001"))));
    Assert.assertEquals(202, pattern.getResponse());
  }

  @Test
  @Order(3)
  public void errorByCountTick() {
    pattern.setParams(new HashMap<>(Map.of(
        "time", 111,
        "count", 5,
        "deltaMaxAsk", new BigDecimal("0.005"),
        "deltaMinAsk", new BigDecimal("0.00001"),
        "deltaMaxBid", new BigDecimal("0.005"),
        "deltaMinBid", new BigDecimal("0.00001"))));
    Assert.assertEquals(400, pattern.getResponse());
  }

  @Test
  @Order(4)
  public void patternAsk() {
    pattern.setParams(new HashMap<>(Map.of(
        "time", 100,
        "count", 3,
        "deltaMaxAsk", new BigDecimal("0.00007"),
        "deltaMinAsk", new BigDecimal("0.00001"),
        "deltaMaxBid", new BigDecimal("0.00001"),
        "deltaMinBid", new BigDecimal("0.00001"))));
    Assert.assertEquals(200, pattern.getResponse());
  }

  @Test
  @Order(5)
  public void noPatternAskByMin() {
    pattern.setParams(new HashMap<>(Map.of(
        "time", 100,
        "count", 3,
        "deltaMaxAsk", new BigDecimal("0.00007"),
        "deltaMinAsk", new BigDecimal("0.00005"),
        "deltaMaxBid", new BigDecimal("0.00001"),
        "deltaMinBid", new BigDecimal("0.00001"))));
    Assert.assertEquals(404, pattern.getResponse());
  }

  @Test
  @Order(6)
  public void patternBid() {
    pattern.setParams(new HashMap<>(Map.of(
        "time", 100,
        "count", 3,
        "deltaMaxAsk", new BigDecimal("0.00001"),
        "deltaMinAsk", new BigDecimal("0.00001"),
        "deltaMaxBid", new BigDecimal("0.00005"),
        "deltaMinBid", new BigDecimal("0.00001"))));
    Assert.assertEquals(201, pattern.getResponse());
  }

  @Test
  @Order(7)
  public void noPatternBidByMax() {
    pattern.setParams(new HashMap<>(Map.of(
        "time", 100,
        "count", 3,
        "deltaMaxAsk", new BigDecimal("0.00001"),
        "deltaMinAsk", new BigDecimal("0.00001"),
        "deltaMaxBid", new BigDecimal("0.00003"),
        "deltaMinBid", new BigDecimal("0.00001"))));
    Assert.assertEquals(404, pattern.getResponse());
  }

  @Test
  @Order(8)
  public void noPattern() {
    pattern.setParams(new HashMap<>(Map.of(
        "time", 10,
        "count", 3,
        "deltaMaxAsk", new BigDecimal("0.00001"),
        "deltaMinAsk", new BigDecimal("0.00001"),
        "deltaMaxBid", new BigDecimal("0.00005"),
        "deltaMinBid", new BigDecimal("0.00001"))));
    Assert.assertEquals(404, pattern.getResponse());
  }

  @Test
  @Order(9)
  public void allPattern() {
    pattern.setParams(new HashMap<>(Map.of(
        "time", 100,
        "count", 3,
        "deltaMaxAsk", new BigDecimal("0.00005"),
        "deltaMinAsk", new BigDecimal("0.00001"),
        "deltaMaxBid", new BigDecimal("0.00005"),
        "deltaMinBid", new BigDecimal("0.00001"))));
    Assert.assertEquals(202, pattern.getResponse());
  }

  @Test
  @Order(10)
  public void noPatternByMaxAsk() {
    pattern.setParams(new HashMap<>(Map.of(
        "time", 100,
        "count", 3,
        "deltaMaxAsk", new BigDecimal("0.00003"),
        "deltaMinAsk", new BigDecimal("0.00001"),
        "deltaMaxBid", new BigDecimal("0.00001"),
        "deltaMinBid", new BigDecimal("0.00001"))));
    Assert.assertEquals(404, pattern.getResponse());
  }

}
