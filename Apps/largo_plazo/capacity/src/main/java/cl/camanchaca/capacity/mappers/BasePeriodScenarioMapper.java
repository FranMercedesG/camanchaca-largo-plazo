package cl.camanchaca.capacity.mappers;

import cl.camanchaca.capacity.adapters.postgres.baseperiodscenario.BasePeriodScenarioData;
import cl.camanchaca.domain.models.capacity.BasePeriodScenario;

public class BasePeriodScenarioMapper {
    public static BasePeriodScenario toBasePeriodScenario(BasePeriodScenarioData data) {
        return BasePeriodScenario.builder()
                .period(data.getPeriod())
                .extraDays(data.getExtraDays())
                .workDays(data.getWorkDays())
                .build();
    }

    public static BasePeriodScenarioData toBasePeriodScenarioData(BasePeriodScenario data) {
        return BasePeriodScenarioData.builder()
                .period(data.getPeriod())
                .extraDays(data.getExtraDays())
                .workDays(data.getWorkDays())
                .build();
    }
}
