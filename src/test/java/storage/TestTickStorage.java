package storage;

import java.math.BigDecimal;
import java.util.List;
import main.model.Tick;
import main.storage.tick.impl.TickManagerServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestTickStorage {

  TickManagerServiceImpl tickService = new TickManagerServiceImpl();

  private Tick tick1 = Tick.builder().priceAsk(BigDecimal.valueOf(0.00025)).priceBid(BigDecimal.valueOf(0.00025)).timestamp(0L).build();
  private Tick tick2 = Tick.builder().priceAsk(BigDecimal.valueOf(0.00028)).priceBid(BigDecimal.valueOf(0.00028)).timestamp(300L).build();
  private Tick tick3 = Tick.builder().priceAsk(BigDecimal.valueOf(0.00028)).priceBid(BigDecimal.valueOf(0.00028)).timestamp(500L).build();
  private Tick tick4 = Tick.builder().priceAsk(BigDecimal.valueOf(0.00028)).priceBid(BigDecimal.valueOf(0.00028)).timestamp(700L).build();


  @Before
  public void initData() throws Exception {
    tickService.processingTick(BigDecimal.valueOf(0.00025), BigDecimal.valueOf(0.00025), 0L);
    tickService.processingTick(BigDecimal.valueOf(0.00028), BigDecimal.valueOf(0.00028), 300L);
    tickService.processingTick(BigDecimal.valueOf(0.00028), BigDecimal.valueOf(0.00028), 500L);
    tickService.processingTick(BigDecimal.valueOf(0.00028), BigDecimal.valueOf(0.00028), 700L);
  }

  @Test
  public void getListTickByCountTest() {
    List<Tick> actual = tickService.getListTickByCount(2);
    List<Tick> expected = List.of(tick3, tick4);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getListTickByTimeTest() {
    int actual = tickService.getListTickByTime(1100L, 600L).size();
    int expected = 1;
    Assert.assertEquals(expected, actual);
  }

}
