package lovable_clone.service.impl;

import lovable_clone.dto.subscription.PlanLimitResponse;
import lovable_clone.dto.subscription.UsageTodayResponse;
import lovable_clone.service.UsageService;
import org.springframework.stereotype.Service;

@Service
public class UsageServiceImpl implements UsageService {
    @Override
    public PlanLimitResponse getCurrentSubscriptionLimitsOfUser(Long userId) {
        return null;
    }

    @Override
    public UsageTodayResponse getTodayUsageOfUser(Long userId) {
        return null;
    }

    @Override
    public void recordTokenUsage(Long id, int actualTokens) {

    }

    @Override
    public void checkDailyTokenUsage() {

    }
}
