package main.model;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Response {

  private BigDecimal price;

  private Long time;

  private String typePrice;

}
