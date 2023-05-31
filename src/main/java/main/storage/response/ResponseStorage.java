package main.storage.response;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Deque;
import main.model.Response;
import org.springframework.stereotype.Service;

@Service
public class ResponseStorage {

  private Deque<Response> listResponses = new ArrayDeque<>();

  private final int SIZE_LIST_RESPONSES = 100;

  public void addResponse(BigDecimal price, String typePrice) {

    Response res = Response.builder()
        .price(price)
        .time(System.currentTimeMillis())
        .typePrice(typePrice)
        .build();

    if (listResponses.size() >= SIZE_LIST_RESPONSES) {
      listResponses.pollFirst();
    }
    listResponses.addLast(res);

  }

  public Response getLastResponse() {
    return listResponses.getLast();
  }

  public void deleteLastResponse() {
    listResponses.removeLast();
  }

}
