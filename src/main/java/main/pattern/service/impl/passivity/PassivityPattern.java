package main.pattern.service.impl.passivity;

import java.util.HashMap;
import main.pattern.dto.RsSignal.Signal;
import main.pattern.service.AbstractPatternPassivity;
import main.price_storage.storage.impl.StorageTickImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PassivityPattern extends AbstractPatternPassivity {


  @Autowired
  public PassivityPattern(StorageTickImpl tickManagerService) {
    super(tickManagerService);
  }

  @Override
  public void setParams(HashMap<String, Number> params) {
    super.setParams(params);
  }

  @Override
  public Signal getResponse() {
    return super.getResponse();
  }

  @Override
  public String getPatternName() {
    return "passivity";
  }

  @Override
  public HashMap<String, Number> getParams() {
    return super.getParams();
  }
}
