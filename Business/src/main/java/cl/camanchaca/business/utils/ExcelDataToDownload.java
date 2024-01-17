package cl.camanchaca.business.utils;

import lombok.Data;

import java.util.List;
import java.util.Map;
@Data
public class ExcelDataToDownload {

    private final Map<String, String> headers;
    private final List<DataRowExcel> rows;
    private final String sheetName;

    public static ExcelDataToDownload of(Map<String, String>  headers, List<DataRowExcel> rows, String sheetName) {
        return new ExcelDataToDownload(headers, rows, sheetName);
    }

    private ExcelDataToDownload(Map<String, String>  headers, List<DataRowExcel> rows, String sheetName) {
        this.headers = headers;
        this.rows = rows;
        this.sheetName = sheetName;
    }
}
