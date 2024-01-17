package cl.camanchaca.optimization.mapper;

import cl.camanchaca.domain.models.optimization.MonthlyPlanification;
import cl.camanchaca.optimization.adapter.postgres.monthly.MonthlyPlanificationData;

public class MonthlyPlanificationMapper {
    public static MonthlyPlanification toModel(MonthlyPlanificationData data){
     return  MonthlyPlanification
                .builder()
             .monthlyPlanificationId(data.getMonthlyPlanificationId())
             .creationDate(data.getCreationDate())
             .finalPeriod(data.getFinalPeriod())
             .initialPeriod(data.getInitialPeriod())
             .active(data.getActive())
             .version(data.getVersion())
                .build();
    }

    public static MonthlyPlanificationData toData(MonthlyPlanification data){
        return  MonthlyPlanificationData
                .builder()
                .monthlyPlanificationId(data.getMonthlyPlanificationId())
                .creationDate(data.getCreationDate())
                .finalPeriod(data.getFinalPeriod())
                .initialPeriod(data.getInitialPeriod())
                .active(data.getActive())
                .version(data.getVersion())
                .build();
    }
    private MonthlyPlanificationMapper(){}
}
