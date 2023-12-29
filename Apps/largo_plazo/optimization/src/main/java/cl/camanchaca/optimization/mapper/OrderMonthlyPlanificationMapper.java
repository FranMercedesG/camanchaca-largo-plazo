package cl.camanchaca.optimization.mapper;

import cl.camanchaca.domain.models.optimization.OrderMonthlyPlanification;
import cl.camanchaca.optimization.adapter.postgres.ordermonthly.OrderMonthlyPlanificationData;

public class OrderMonthlyPlanificationMapper {

    public static OrderMonthlyPlanificationData toData(OrderMonthlyPlanification data){
        return OrderMonthlyPlanificationData
                .builder()
                .orderMonthlyPlanificationId(data.getOrderMonthlyPlanificationId())
                .monthlyPlanificationId(data.getMonthlyPlanificationId())
                .kiloWfe(data.getKiloWfe())
                .period(data.getPeriod())
                .sizeId(data.getSizeId())
                .unrestrictedDemandId(data.getUnrestrictedDemandId())
                .build();
    }

    public static OrderMonthlyPlanification toModel(OrderMonthlyPlanificationData data){
        return OrderMonthlyPlanification
                .builder()
                .orderMonthlyPlanificationId(data.getOrderMonthlyPlanificationId())
                .monthlyPlanificationId(data.getMonthlyPlanificationId())
                .kiloWfe(data.getKiloWfe())
                .period(data.getPeriod())
                .sizeId(data.getSizeId())
                .unrestrictedDemandId(data.getUnrestrictedDemandId())
                .build();
    }
}
