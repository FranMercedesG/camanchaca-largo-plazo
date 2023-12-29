package cl.camanchaca.optimization.mapper;

import cl.camanchaca.domain.models.optimization.Optimization;
import cl.camanchaca.optimization.adapter.postgres.optimization.OptimizationData;

public class OptimizationMapper {

    public static Optimization toOptimization(OptimizationData data) {
        return Optimization.builder()
                .unrestricterDemandId(data.getUnrestricterDemandId())
                .sizeId(data.getSizeId())
                .period(data.getPeriod())
                .kiloWfe(data.getKiloWfe())
                .codigo(data.getCodigo())
                .orderMonthlyPlanificationId(data.getOrderMonthlyPlanificationId())
                .build();
    }

    public static OptimizationData toOptimizationData(Optimization data) {
        return OptimizationData.builder()
                .unrestricterDemandId(data.getUnrestricterDemandId())
                .sizeId(data.getSizeId())
                .period(data.getPeriod())
                .kiloWfe(data.getKiloWfe())
                .codigo(data.getCodigo())
                .orderMonthlyPlanificationId(data.getOrderMonthlyPlanificationId())
                .build();
    }

    private OptimizationMapper() {
    }
}
