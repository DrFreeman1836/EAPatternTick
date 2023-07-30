package main.pattern.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import main.pattern.enam.TypeTrend;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RsSignal {

  private List<Signal> listSignal = new ArrayList<>();

  public void addSignal(BigDecimal price, String type, int pattern, TypeTrend trend) {
    listSignal.add(new Signal(price, type, pattern, trend));
  }

  public void addSignal(Signal signal) {
    listSignal.add(signal);
  }

  public List<Signal> getListSignal() {
    return listSignal;
  }

  public record Signal(BigDecimal price, String type, int pattern, TypeTrend trend){}

}
