package patterns.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import main.Application;
import main.notification.impl.TelegramBotMessages;
import main.pattern.dto.RsSignal;
import main.pattern.service.impl.activity.ActivityPattern;
import main.pattern.service.impl.multi.MultiPattern;
import main.pattern.service.impl.passivity.PassivityPattern;
import main.price_storage.dto.TickDto;
import main.price_storage.storage.impl.StorageTickImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
public class PatternIntegrationTest {

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private ActivityPattern activityPattern;

  @Autowired
  private PassivityPattern passivityPattern;

  @Autowired
  private MultiPattern multiPattern;

  @Autowired
  private StorageTickImpl storage;

  @MockBean
  private TelegramBotMessages bot;

  @Before
  public void initTicks() throws Exception {
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
    tick5.setAsk(new BigDecimal("0.00013"));
    tick5.setBid(new BigDecimal("0.00014"));
    tick5.setTimeMsc(100L);
    storage.processingTick(tick5, 100L);
    TickDto tick6 = new TickDto();
    tick6.setAsk(new BigDecimal("0.00013"));
    tick6.setBid(new BigDecimal("0.00014"));
    tick6.setTimeMsc(120L);
    storage.processingTick(tick6, 120L);
    passivityPattern.setParams(new HashMap<>(Map.of(
        "timeFirst", 100,
        "countFirst", 5,
        "timeSecond", 20,
        "countSecond", 2)));
  }

  @Test
  public void testGetPatterns() {

    ResponseEntity<RsSignal> response = restTemplate.postForEntity("/api/v1/ea/signal?activity=false&passivity=true&multi=false", null, RsSignal.class);
    RsSignal signals = response.getBody();
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(1, signals.getListSignal().size());


    verify(bot, times(1)).sendMessage(anyString());
  }


}
