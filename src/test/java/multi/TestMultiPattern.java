package multi;

import java.math.BigDecimal;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import main.Application;
import main.service.impl.multi.MultiPattern;
import main.storage.tick.impl.TickManagerServiceImpl;
import main.telegram.impl.TelegramBotMessages;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class TestMultiPattern {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private TelegramBotMessages botMessages;

  @Autowired
  private MultiPattern multiPattern;

  @Autowired
  private TickManagerServiceImpl tickManagerService;

  @Test
  public void getResponseTest() throws Exception {

    HashMap<String, Number> params = new HashMap<>(Map.of(
        "time", 1000,
        "count", 3,
        "lowLevel", 4,
        "middleLevel",4,
        "highLevel",5,
        "deltaMaxAsk", BigDecimal.valueOf(0.00075),
        "deltaMinAsk", BigDecimal.valueOf(0.00009),
        "deltaMaxBid", BigDecimal.valueOf(0.00075),
        "deltaMinBid", BigDecimal.valueOf(0.00009)));
    multiPattern.setParams(params);

    tickManagerService.processingTick(BigDecimal.valueOf(0.00025), BigDecimal.valueOf(0.00025), System.currentTimeMillis());
    tickManagerService.processingTick(BigDecimal.valueOf(0.00028), BigDecimal.valueOf(0.00028), System.currentTimeMillis());

    String requestString1 = "http://localhost:80/api/v1/ea/signal?priceAsk=0.00035&priceBid=0.00035&pattern=multi";
    MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(URI.create(requestString1));
    ResultActions result = mockMvc.perform(request);
    result.andExpect(MockMvcResultMatchers.status().is(404));

    String requestString2 = "http://localhost:80/api/v1/ea/signal?priceAsk=0.00038&priceBid=0.00035&pattern=multi";
    request = MockMvcRequestBuilders.post(URI.create(requestString2));
    result = mockMvc.perform(request);
    result.andExpect(MockMvcResultMatchers.status().is(201));

    String requestString3 = "http://localhost:80/api/v1/ea/signal?priceAsk=0.00040&priceBid=0.00044&pattern=multi";
    request = MockMvcRequestBuilders.post(URI.create(requestString3));
    result = mockMvc.perform(request);
    result.andExpect(MockMvcResultMatchers.status().is(202));

    String requestString4 = "http://localhost:80/api/v1/ea/signal?priceAsk=0.00244&priceBid=0.00235&pattern=multi";
    request = MockMvcRequestBuilders.post(URI.create(requestString4));
    result = mockMvc.perform(request);
    result.andExpect(MockMvcResultMatchers.status().is(404));

  }

}
