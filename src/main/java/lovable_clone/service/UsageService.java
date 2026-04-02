package lovable_clone.service;

import lovable_clone.dto.subscription.PlanLimitResponse;
import lovable_clone.dto.subscription.UsageTodayResponse;


public interface UsageService {
    PlanLimitResponse getCurrentSubscriptionLimitsOfUser(Long userId);

    UsageTodayResponse getTodayUsageOfUser(Long userId);

    void recordTokenUsage(Long id, int actualTokens);
    void checkDailyTokenUsage();
}
