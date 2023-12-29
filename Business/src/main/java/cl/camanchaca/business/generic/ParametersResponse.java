package cl.camanchaca.business.generic;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(toBuilder = true)

public class ParametersResponse<T> {

    protected List<T> data;
    protected Long total;

    protected ParametersResponse(List data, Long total){
        this.data = data;
        this.total = total;
    }

    public static ParametersResponse of(List data, Long total){
        return new ParametersResponse(data, total);
    }

}
