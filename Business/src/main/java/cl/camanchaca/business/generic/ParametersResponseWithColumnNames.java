package cl.camanchaca.business.generic;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ParametersResponseWithColumnNames extends ParametersResponse {

    private List<Column> colunmNames;
    private ParametersResponseWithColumnNames(List<Column>  colunmNames, List data, Long total){
        super(data, total);
        this.colunmNames = colunmNames;
    }

    public static ParametersResponseWithColumnNames of(List data, List<Column>  colunmNames, Long total){
        return new ParametersResponseWithColumnNames(colunmNames, data, total);
    }

}
