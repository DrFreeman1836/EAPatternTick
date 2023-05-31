package passivity;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import main.model.Tick;
import main.service.impl.passivity.PassivityPattern;
import main.storage.tick.impl.TickManagerServiceImpl;
import org.junit.Before;

public class TestPatternPassivity {

  private TickManagerServiceImpl tickService = new TickManagerServiceImpl();

  private PassivityPattern passivityPattern = new PassivityPattern(tickService);


  private Tick tick1 = Tick.builder().priceAsk(BigDecimal.valueOf(0.00025)).priceBid(BigDecimal.valueOf(0.00025)).timestamp(0L).build();
  private Tick tick2 = Tick.builder().priceAsk(BigDecimal.valueOf(0.00028)).priceBid(BigDecimal.valueOf(0.00028)).timestamp(300L).build();
  private Tick tick3 = Tick.builder().priceAsk(BigDecimal.valueOf(0.00028)).priceBid(BigDecimal.valueOf(0.00028)).timestamp(500L).build();
  private Tick tick4 = Tick.builder().priceAsk(BigDecimal.valueOf(0.00028)).priceBid(BigDecimal.valueOf(0.00028)).timestamp(700L).build();
  private Tick tick5 = Tick.builder().priceAsk(BigDecimal.valueOf(0.00025)).priceBid(BigDecimal.valueOf(0.00025)).timestamp(750L).build();
  private Tick tick6 = Tick.builder().priceAsk(BigDecimal.valueOf(0.00028)).priceBid(BigDecimal.valueOf(0.00028)).timestamp(800L).build();
  private Tick tick7 = Tick.builder().priceAsk(BigDecimal.valueOf(0.00028)).priceBid(BigDecimal.valueOf(0.00028)).timestamp(900L).build();
  private Tick tick8 = Tick.builder().priceAsk(BigDecimal.valueOf(0.00028)).priceBid(BigDecimal.valueOf(0.00028)).timestamp(1000L).build();

  @Before
  public void initData() throws Exception {
    tickService.processingTick(BigDecimal.valueOf(0.00025), BigDecimal.valueOf(0.00025), 0L);
    tickService.processingTick(BigDecimal.valueOf(0.00028), BigDecimal.valueOf(0.00028), 300L);
    tickService.processingTick(BigDecimal.valueOf(0.00028), BigDecimal.valueOf(0.00028), 500L);
    tickService.processingTick(BigDecimal.valueOf(0.00028), BigDecimal.valueOf(0.00028), 700L);
    tickService.processingTick(BigDecimal.valueOf(0.00025), BigDecimal.valueOf(0.00025), 750L);
    tickService.processingTick(BigDecimal.valueOf(0.00028), BigDecimal.valueOf(0.00028), 800L);
    tickService.processingTick(BigDecimal.valueOf(0.00028), BigDecimal.valueOf(0.00028), 900L);
    tickService.processingTick(BigDecimal.valueOf(0.00028), BigDecimal.valueOf(0.00028), 1000L);
    passivityPattern.setParams(new HashMap<>(Map.of(
        "countFirst", 6,
        "timeFirst", 800L,
        "countSecond", 3,
        "timeSecond", 200L
    )));
  }

//  @Test
//  public void getSignalTest() {// для тестов нужно переделывать под передачу текущего времени
//    int expected = 300;
//    int actual = passivityPattern.getResponse();
//    Assert.assertEquals(expected, actual);
//  }

}
