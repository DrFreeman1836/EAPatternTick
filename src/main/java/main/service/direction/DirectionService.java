package main.service.direction;

import java.lang.invoke.TypeDescriptor;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import main.enam.TypeDirection;
import main.model.Response;
import main.storage.response.ResponseStorage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DirectionService {

  private final ResponseStorage responseStorage;

  public TypeDirection getDirection(BigDecimal currentPriceAsk, BigDecimal currentPriceBid, Long diffTime) {
    Response res = responseStorage.getLastResponse();
    //
    return null;
  }

}
