package cl.camanchaca.business.generic;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ParametersResponseWithColumnNames that = (ParametersResponseWithColumnNames) o;
        return Objects.equals(colunmNames, that.colunmNames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), colunmNames);
    }
}
