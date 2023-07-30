package main.pattern.service.impl.activity;

import java.util.HashMap;
import main.pattern.dto.RsSignal.Signal;
import main.pattern.service.AbstractPatternActivity;
import main.price_storage.storage.impl.StorageTickImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivityPattern extends AbstractPatternActivity {

  @Autowired
  public ActivityPattern(StorageTickImpl tickManagerService) {
    super(tickManagerService);
  }

  @Override
  public Signal getResponse() {
    return super.getResponse();
  }

  @Override
  public String getPatternName() {
    return "activity";
  }

  @Override
  public void setParams(HashMap<String, Number> params) {
    super.setParams(params);
  }

  @Override
  public HashMap<String, Number> getParams() {
    return super.getParams();
  }
}
