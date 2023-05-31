package main.service.impl.activity;

import java.util.HashMap;
import main.service.AbstractPatternActivity;
import main.storage.tick.impl.TickManagerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivityPattern extends AbstractPatternActivity {

  @Autowired
  public ActivityPattern(TickManagerServiceImpl tickManagerService) {
    super(tickManagerService);
  }

  @Override
  public int getResponse() {
    return super.getResponse();
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
