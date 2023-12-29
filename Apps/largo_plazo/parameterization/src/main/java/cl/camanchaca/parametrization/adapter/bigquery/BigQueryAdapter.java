package cl.camanchaca.parametrization.adapter.bigquery;

import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryException;
import com.google.cloud.bigquery.QueryJobConfiguration;
import com.google.cloud.bigquery.TableResult;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BigQueryAdapter {

    private final BigQuery bigQuery;

    public TableResult runQuery(String sqlQuery) {
        try {
            QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(sqlQuery).build();
            return bigQuery.query(queryConfig);
        } catch (BigQueryException e) {
            throw e;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
