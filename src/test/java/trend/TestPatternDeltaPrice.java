package trend;


import main.Application;
import main.service.impl.trend.PatternDeltaPriceTrend;
import main.telegram.impl.TelegramBotMessages;
import main.storage.tick.impl.TickManagerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class TestPatternDeltaPrice {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private TelegramBotMessages botMessages;

  @Autowired
  private PatternDeltaPriceTrend patternDeltaPrice;

  @Autowired
  private TickManagerServiceImpl tickManagerService;

//  @Test
//  public void getResponseTest1() throws Exception {
//
//    HashMap<String, Number> params = new HashMap<>(Map.of(
//        "time", 1000,
//        "count", 3,
//        "minDeltaTrend", BigDecimal.valueOf(0.00003),
//        "deltaMaxAsk", BigDecimal.valueOf(0.00075),
//        "deltaMinAsk", BigDecimal.valueOf(0.00009),
//        "deltaMaxBid", BigDecimal.valueOf(0.00075),
//        "deltaMinBid", BigDecimal.valueOf(0.00009)));
//    patternDeltaPrice.initParams(params);
//
//    tickManagerService.processingTick(BigDecimal.valueOf(0.00025), BigDecimal.valueOf(0.00028), 0L);
//    tickManagerService.processingTick(BigDecimal.valueOf(0.00028), BigDecimal.valueOf(0.00035), 350L);
//    tickManagerService.processingTick(BigDecimal.valueOf(0.00035), BigDecimal.valueOf(0.00038), 550L);
//    int res = patternDeltaPrice.getResponse();
//    //Assertions.assertEquals(TypeSignalPassivity.BUY.getResponseCode(), res);
//
//  }
//
//  @Test
//  public void getResponseTest2() throws Exception {
//
//    HashMap<String, Number> params = new HashMap<>(Map.of(
//        "time", 1000,
//        "count", 3,
//        "minDeltaTrend", BigDecimal.valueOf(0.00003),
//        "deltaMaxAsk", BigDecimal.valueOf(0.00075),
//        "deltaMinAsk", BigDecimal.valueOf(0.00009),
//        "deltaMaxBid", BigDecimal.valueOf(0.00075),
//        "deltaMinBid", BigDecimal.valueOf(0.00009)));
//    patternDeltaPrice.initParams(params);
//
//    tickManagerService.processingTick(BigDecimal.valueOf(0.00025), BigDecimal.valueOf(0.00028), 0L);
//    tickManagerService.processingTick(BigDecimal.valueOf(0.00023), BigDecimal.valueOf(0.00024), 550L);
//    tickManagerService.processingTick(BigDecimal.valueOf(0.00035), BigDecimal.valueOf(0.00037), 950L);
//    int res = patternDeltaPrice.getResponse();
//    //Assertions.assertEquals(TypeSignalPassivity.BUY.getResponseCode(), res);
//
//  }
//  @Test
//  public void getResponseTest3() throws Exception {
//
//    HashMap<String, Number> params = new HashMap<>(Map.of(
//        "time", 1000,
//        "count", 3,
//        "minDeltaTrend", BigDecimal.valueOf(0.00003),
//        "deltaMaxAsk", BigDecimal.valueOf(0.00075),
//        "deltaMinAsk", BigDecimal.valueOf(0.00009),
//        "deltaMaxBid", BigDecimal.valueOf(0.00075),
//        "deltaMinBid", BigDecimal.valueOf(0.00009)));
//    patternDeltaPrice.initParams(params);
//
//    tickManagerService.processingTick(BigDecimal.valueOf(0.00025), BigDecimal.valueOf(0.00028), 0L);
//    tickManagerService.processingTick(BigDecimal.valueOf(0.00023), BigDecimal.valueOf(0.00027), 150L);
//    tickManagerService.processingTick(BigDecimal.valueOf(0.00014), BigDecimal.valueOf(0.00016), 250L);
//    int res = patternDeltaPrice.getResponse();
//    //Assertions.assertEquals(TypeSignalPassivity.SELL.getResponseCode(), res);
//
//  }
//  @Test
//  public void getResponseTest4() throws Exception {
//
//    HashMap<String, Number> params = new HashMap<>(Map.of(
//        "time", 1000,
//        "count", 3,
//        "minDeltaTrend", BigDecimal.valueOf(0.00003),
//        "deltaMaxAsk", BigDecimal.valueOf(0.00075),
//        "deltaMinAsk", BigDecimal.valueOf(0.00009),
//        "deltaMaxBid", BigDecimal.valueOf(0.00075),
//        "deltaMinBid", BigDecimal.valueOf(0.00009)));
//    patternDeltaPrice.initParams(params);
//
//    tickManagerService.processingTick(BigDecimal.valueOf(0.00025), BigDecimal.valueOf(0.00028), 0L);
//    tickManagerService.processingTick(BigDecimal.valueOf(0.00028), BigDecimal.valueOf(0.00032), 750L);
//    tickManagerService.processingTick(BigDecimal.valueOf(0.00014), BigDecimal.valueOf(0.00016), 975L);
//    int res = patternDeltaPrice.getResponse();
//    //Assertions.assertEquals(TypeSignalPassivity.SELL.getResponseCode(), res);
//
//  }
//  @Test
//  public void getResponseTest5() throws Exception {
//
//    HashMap<String, Number> params = new HashMap<>(Map.of(
//        "time", 1000,
//        "count", 3,
//        "minDeltaTrend", BigDecimal.valueOf(0.00003),
//        "deltaMaxAsk", BigDecimal.valueOf(0.00075),
//        "deltaMinAsk", BigDecimal.valueOf(0.00009),
//        "deltaMaxBid", BigDecimal.valueOf(0.00075),
//        "deltaMinBid", BigDecimal.valueOf(0.00009)));
//    patternDeltaPrice.initParams(params);
//
//    tickManagerService.processingTick(BigDecimal.valueOf(0.00025), BigDecimal.valueOf(0.00028), 0L);
//    tickManagerService.processingTick(BigDecimal.valueOf(0.00028), BigDecimal.valueOf(0.00035), 350L);
//    tickManagerService.processingTick(BigDecimal.valueOf(0.00035), BigDecimal.valueOf(0.00038), 1150L);
//    int res = patternDeltaPrice.getResponse();
//    Assertions.assertEquals(TypeSignalActivity.NO_PATTERN.getResponseCode(), res);
//
//  }
//  @Test
//  public void getResponseTest6() throws Exception {
//
//    HashMap<String, Number> params = new HashMap<>(Map.of(
//        "time", 1000,
//        "count", 3,
//        "minDeltaTrend", BigDecimal.valueOf(0.00003),
//        "deltaMaxAsk", BigDecimal.valueOf(0.00075),
//        "deltaMinAsk", BigDecimal.valueOf(0.00009),
//        "deltaMaxBid", BigDecimal.valueOf(0.00075),
//        "deltaMinBid", BigDecimal.valueOf(0.00009)));
//    patternDeltaPrice.initParams(params);
//
//    tickManagerService.processingTick(BigDecimal.valueOf(0.00025), BigDecimal.valueOf(0.00028), 0L);
//    tickManagerService.processingTick(BigDecimal.valueOf(0.00035), BigDecimal.valueOf(0.00040), 450L);
//    tickManagerService.processingTick(BigDecimal.valueOf(0.00027), BigDecimal.valueOf(0.00030), 650L);
//    int res = patternDeltaPrice.getResponse();
//    //Assertions.assertEquals(TypeSignalActivity.NO_PATTERN.getResponseCode(), res);
//
//  }
//  @Test
//  public void getResponseTest7() throws Exception {
//
//    HashMap<String, Number> params = new HashMap<>(Map.of(
//        "time", 1000,
//        "count", 3,
//        "minDeltaTrend", BigDecimal.valueOf(0.00003),
//        "deltaMaxAsk", BigDecimal.valueOf(0.00075),
//        "deltaMinAsk", BigDecimal.valueOf(0.00009),
//        "deltaMaxBid", BigDecimal.valueOf(0.00075),
//        "deltaMinBid", BigDecimal.valueOf(0.00009)));
//    patternDeltaPrice.initParams(params);
//
//    tickManagerService.processingTick(BigDecimal.valueOf(0.00025), BigDecimal.valueOf(0.00028), 0L);
//    tickManagerService.processingTick(BigDecimal.valueOf(0.00028), BigDecimal.valueOf(0.00032), 150L);
//    tickManagerService.processingTick(BigDecimal.valueOf(0.00033), BigDecimal.valueOf(0.00035), 300L);
//    int res = patternDeltaPrice.getResponse();
//    //Assertions.assertEquals(TypeSignalActivity.NO_PATTERN.getResponseCode(), res);
//
//  }


}
/**
 *
 */