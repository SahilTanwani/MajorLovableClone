package lovable_clone.mapper;


import lovable_clone.dto.subscription.PlanResponse;
import lovable_clone.dto.subscription.SubscriptionResponse;
import lovable_clone.entity.Plan;
import lovable_clone.entity.Subscription;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {
    SubscriptionResponse toSubscriptionResponse(Subscription subscription);

    PlanResponse toPlanResponse(Plan plan);
}
