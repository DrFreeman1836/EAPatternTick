package storage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import main.price_storage.dto.TickDto;
import main.price_storage.model.Tick;
import main.price_storage.storage.impl.StorageTickImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StorageTickTest {

  private StorageTickImpl storage = new StorageTickImpl();

  @Before
  public void initData() throws Exception {
    TickDto tick0 = new TickDto();
    tick0.setAsk(new BigDecimal("0.00005"));
    tick0.setBid(new BigDecimal("0.00006"));
    storage.processingTick(tick0, 0L);
    TickDto tick1 = new TickDto();
    tick1.setAsk(new BigDecimal("0.00007"));
    tick1.setBid(new BigDecimal("0.00008"));
    storage.processingTick(tick1, 20L);
    TickDto tick2 = new TickDto();
    tick2.setAsk(new BigDecimal("0.00009"));
    tick2.setBid(new BigDecimal("0.00010"));
    storage.processingTick(tick2, 40L);
    TickDto tick3 = new TickDto();
    tick3.setAsk(new BigDecimal("0.00011"));
    tick3.setBid(new BigDecimal("0.00012"));
    storage.processingTick(tick3, 60L);
    TickDto tick4 = new TickDto();
    tick4.setAsk(new BigDecimal("0.00013"));
    tick4.setBid(new BigDecimal("0.00014"));
    storage.processingTick(tick4, 80L);
    TickDto tick5 = new TickDto();
    tick5.setAsk(new BigDecimal("0.00013"));
    tick5.setBid(new BigDecimal("0.00014"));
    storage.processingTick(tick5, 100L);
    TickDto tick6 = new TickDto();
    tick6.setAsk(new BigDecimal("0.00013"));
    tick6.setBid(new BigDecimal("0.00014"));
    storage.processingTick(tick6, 120L);
  }

  @Test
  @Order(1)
  public void testProcessingTick() throws Exception {
    TickDto tick = new TickDto();
    tick.setAsk(new BigDecimal("0.00015"));
    tick.setBid(new BigDecimal("0.00016"));
    storage.processingTick(tick, 140L);

    List<Tick> lastTicks = storage.getListTickByCount(5);
    Assert.assertEquals(5, lastTicks.size());

    Assert.assertEquals(new BigDecimal("0.00015"), lastTicks.get(0).getAsk());
    Assert.assertEquals(new BigDecimal("0.00016"), lastTicks.get(0).getBid());
    Assert.assertEquals(Long.valueOf(140), lastTicks.get(0).getTimestamp());
  }

  @Test
  @Order(2)
  public void testCountTick() throws Exception {
    Assert.assertEquals(7, storage.sizeStorageTicks());
  }

  @Test
  @Order(3)
  public void testGetListTickByCount() {
    List<Tick> actualList = storage.getListTickByCount(2);
    List<Tick> expectedList = new ArrayList<>();
    Tick tick2 = new Tick();
    tick2.setAsk(new BigDecimal("0.00013"));
    tick2.setBid(new BigDecimal("0.00014"));
    tick2.setTimestamp(120L);
    expectedList.add(tick2);
    Tick tick = new Tick();
    tick.setAsk(new BigDecimal("0.00013"));
    tick.setBid(new BigDecimal("0.00014"));
    tick.setTimestamp(100L);
    expectedList.add(tick);
    Assert.assertEquals(expectedList, actualList);
  }

}
