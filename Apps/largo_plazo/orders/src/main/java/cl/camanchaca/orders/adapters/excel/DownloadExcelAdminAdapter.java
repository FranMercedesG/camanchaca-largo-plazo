package cl.camanchaca.orders.adapters.excel;

import cl.camanchaca.business.repositories.DownloadAdminRepository;
import cl.camanchaca.domain.models.demand.UnrestrictedDemand;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@Service
public class DownloadExcelAdminAdapter implements DownloadAdminRepository {

    @Override
    public Mono<byte[]> downloadAdminFile(Flux<UnrestrictedDemand> demandFlux) {
        return demandFlux.collectList().flatMap(demandList -> {
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Unrestricted Demand");

                Row headerRow = sheet.createRow(0);

                XSSFCellStyle headerStyle = (XSSFCellStyle) workbook.createCellStyle();
                headerStyle.setFillForegroundColor(new XSSFColor(new byte[]{(byte) 10, (byte) 10, (byte) 59}, null));
                headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

                Font headerFont = workbook.createFont();
                headerFont.setBold(true);
                headerFont.setColor(IndexedColors.WHITE.getIndex());
                headerStyle.setFont(headerFont);


                String[] columnNames = {
                        "Especie", "Nombre Sector", "Pais Destino", "Destinatario", "Tipo Contrato", "DV", "Contrato",
                        "Calidad", "Puerto Destino", "Codigo", "Forma", "Calibre", "Familia", "Oficina", "Kilos Netos",
                        "Fob", "RMP", "Aux Op", "Rend", "KgWFE_demanda", "KgWFE_Plan", "Dif", "mes", "Precio cierre (USD/kg Neto)",
                        "Flete", "Libras", "Estatus", "mes carga", "Order_type", "Customer/Program", "Oficina-Sales Type",
                        "Family", "Ext RMP", "Trim D Lb WK"
                };

                for (int i = 0; i < columnNames.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(columnNames[i]);
                    cell.setCellStyle(headerStyle);
                    sheet.autoSizeColumn(i);
                }

                if (demandList != null) {
                    for (int i = 0; i < demandList.size(); i++) {
                        UnrestrictedDemand demand = demandList.get(i);
                        Row dataRow = sheet.createRow(i + 1);

                        setCellValue(dataRow.createCell(0), demand.getEspecie());
                        setCellValue(dataRow.createCell(1), demand.getNombreSector());
                        setCellValue(dataRow.createCell(2), demand.getPaisDestino());
                        setCellValue(dataRow.createCell(3), demand.getDestinatario());
                        setCellValue(dataRow.createCell(4), demand.getTipoContrato());
                        setCellValue(dataRow.createCell(5), demand.getDv());
                        setCellValue(dataRow.createCell(6), demand.getContrato());
                        setCellValue(dataRow.createCell(7), demand.getCalidad());
                        setCellValue(dataRow.createCell(8), demand.getPuertoDestino());
                        setCellValue(dataRow.createCell(9), demand.getProductsId());
                        setCellValue(dataRow.createCell(10), demand.getForma());
                        setCellValue(dataRow.createCell(11), demand.getCalibre());
                        setCellValue(dataRow.createCell(12), demand.getFamilia());
                        setCellValue(dataRow.createCell(13), demand.getOficina());
                        setCellValue(dataRow.createCell(14), demand.getKilosNetos());
                        setCellValue(dataRow.createCell(15), demand.getFob());
                        setCellValue(dataRow.createCell(16), demand.getRmp());
                        setCellValue(dataRow.createCell(17), demand.getAuxOp());
                        setCellValue(dataRow.createCell(18), demand.getRend());
                        setCellValue(dataRow.createCell(19), demand.getKgWFEDemanda());
                        setCellValue(dataRow.createCell(20), demand.getKgWFEPlan());
                        setCellValue(dataRow.createCell(21), demand.getDif());
                        setCellValue(dataRow.createCell(22), demand.getPeriodo());
                        setCellValue(dataRow.createCell(23), demand.getPrecioCierreUSDKgNeto());
                        setCellValue(dataRow.createCell(24), demand.getFlete());
                        setCellValue(dataRow.createCell(25), demand.getLibras());
                        setCellValue(dataRow.createCell(26), demand.getEstatus());
                        setCellValue(dataRow.createCell(27), demand.getPeriodoCarga());
                        setCellValue(dataRow.createCell(28), demand.getOrderType());
                        setCellValue(dataRow.createCell(29), demand.getCustomerProgram());
                        setCellValue(dataRow.createCell(30), demand.getOfficeSalesType());
                        setCellValue(dataRow.createCell(31), demand.getFamily());
                        setCellValue(dataRow.createCell(32), demand.getExtRmp());
                        setCellValue(dataRow.createCell(33), demand.getTrimDlbWK());
                    }
                }

                try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                    workbook.write(bos);
                    return Mono.just(bos.toByteArray());
                }
            } catch (IOException e) {
                return Mono.error(new RuntimeException("Error al generar el archivo Excel", e));
            }
        });
    }


    private void setCellValue(Cell cell, Object value) {
        if (value != null) {
            if (value instanceof String) {
                cell.setCellValue((String) value);
            } else if (value instanceof BigDecimal) {
                cell.setCellValue(((BigDecimal) value).doubleValue());
            } else if (value instanceof LocalDate) {
                cell.setCellValue(((LocalDate) value).toString());
            } else {
                cell.setCellValue(value.toString());
            }
        } else {
            cell.setCellValue("");
        }
    }

}
