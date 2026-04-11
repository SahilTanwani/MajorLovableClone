package lovable_clone.service;

import lovable_clone.dto.subscription.PlanLimitResponse;
import lovable_clone.dto.subscription.UsageTodayResponse;


public interface UsageService {


    void recordTokenUsage(Long id, int actualTokens);
    void checkDailyTokensUsage();


}
