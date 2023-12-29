package cl.camanchaca.orders.adapters.excel;

import cl.camanchaca.business.repositories.DownloadOfficeRepository;
import cl.camanchaca.business.repositories.PeriodRepository;
import cl.camanchaca.domain.models.Period;
import cl.camanchaca.domain.models.demand.*;
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
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DownloadExcelOfficeAdapter implements DownloadOfficeRepository {

    private final PeriodRepository periodRepository;

    @Override
    public Mono<byte[]> downloadOfficeFile(Flux<UnrestrictedDemandOffice> demandFlux, Map<String, String> headerInfo) {

        return getPeriod(headerInfo)
                .flatMap(period -> demandFlux.collectList().flatMap(demandList -> {
                    try (Workbook workbook = new XSSFWorkbook()) {
                        Sheet sheet = workbook.createSheet("Unrestricted Demand");
                        Row beforeheaderRow = sheet.createRow(0);
                        Row headerRow = sheet.createRow(1);

                        XSSFCellStyle headerStyle = (XSSFCellStyle) workbook.createCellStyle();
                        headerStyle.setFillForegroundColor(new XSSFColor(new byte[]{(byte) 249, (byte) 221, (byte) 116}, null));
                        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

                        Font headerFont = workbook.createFont();
                        headerFont.setBold(true);
                        headerFont.setColor(IndexedColors.BLACK.getIndex());
                        headerStyle.setFont(headerFont);

                        XSSFCellStyle purchPriceStyle = (XSSFCellStyle) workbook.createCellStyle();
                        purchPriceStyle.setFillForegroundColor(new XSSFColor(new byte[]{(byte) 12, (byte) 0, (byte) 200}, null));
                        purchPriceStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

                        Font purchPriceFont = workbook.createFont();
                        purchPriceFont.setBold(true);
                        purchPriceFont.setColor(IndexedColors.WHITE.getIndex());
                        purchPriceStyle.setFont(purchPriceFont);

                        XSSFCellStyle prodFwStyle = (XSSFCellStyle) workbook.createCellStyle();
                        prodFwStyle.setFillForegroundColor(new XSSFColor(new byte[]{(byte) 33, (byte) 129, (byte) 13}, null));
                        prodFwStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

                        Font prodFwFont = workbook.createFont();
                        prodFwFont.setBold(true);
                        prodFwFont.setColor(IndexedColors.WHITE.getIndex());
                        prodFwStyle.setFont(prodFwFont);

                        XSSFCellStyle fobStyle = (XSSFCellStyle) workbook.createCellStyle();
                        fobStyle.setFillForegroundColor(new XSSFColor(new byte[]{(byte) 240, (byte) 214, (byte) 0}, null));
                        fobStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

                        Font fobFont = workbook.createFont();
                        fobFont.setBold(true);
                        fobFont.setColor(IndexedColors.WHITE.getIndex());
                        fobStyle.setFont(fobFont);

                        XSSFCellStyle rmpStyle = (XSSFCellStyle) workbook.createCellStyle();
                        rmpStyle.setFillForegroundColor(new XSSFColor(new byte[]{(byte) 240, (byte) 105, (byte) 0}, null));
                        rmpStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

                        Font rmpFont = workbook.createFont();
                        rmpFont.setBold(true);
                        rmpFont.setColor(IndexedColors.WHITE.getIndex());
                        rmpStyle.setFont(rmpFont);

                        Map<LocalDate, Integer> purchPricePosition = new HashMap<>();
                        Map<LocalDate, Integer> prodFwPosition = new HashMap<>();
                        Map<LocalDate, Integer> fobPosition = new HashMap<>();
                        Map<LocalDate, Integer> rmpPosition = new HashMap<>();


                        List<LocalDate> dates = getMonthsBetweenTwoDates(period.getInitialPeriod(), period.getFinalPeriod());

                        int dateSize = dates.size();

                        String[] columnNames = {
                                "Oficina", "Sales Type", "Customer Program", "Country", "Sale Status", "Codigo", "Material Description", "Especie",
                                "Family", "Type", "Forma", "Calidad", "Rend", "Unit", "Inco Term", "Puerto Destino"
                        };

                        createHeaders(columnNames, headerRow, headerStyle, sheet, dateSize,
                                purchPriceStyle, beforeheaderRow, dates,
                                prodFwStyle, fobStyle, rmpStyle,
                                purchPricePosition, prodFwPosition, fobPosition, rmpPosition);


                        if (demandList != null && !demandList.isEmpty()) {
                            int increment = 0;
                            int columnSize = 16;
                            for (UnrestrictedDemandOffice demand : demandList) {

                                int rowN = 2 + increment;

                                insertDemandUnrestricted(demand, sheet, rowN);

                                Row dataRow = sheet.getRow(rowN) == null ? sheet.createRow(rowN) : sheet.getRow(rowN);

                                List<FobDetail> fob = Optional.ofNullable(demand.getFob())
                                        .filter(list -> !list.isEmpty())
                                        .map(list -> list.stream()
                                                .sorted(Comparator.comparing(FobDetail::getDate))
                                                .map(detail -> {
                                                    LocalDate date = detail.getDate();
                                                    LocalDate firstDayOfMonth = date.withDayOfMonth(1);
                                                    return FobDetail.builder().date(firstDayOfMonth).value(detail.getValue()).build();
                                                })
                                                .collect(Collectors.toList()))
                                        .orElse(Collections.emptyList());

                                List<LibrasProdFwDetail> prodFw = Optional.ofNullable(demand.getProdFw())
                                        .filter(list -> !list.isEmpty())
                                        .map(list -> list.stream()
                                                .sorted(Comparator.comparing(LibrasProdFwDetail::getDate))
                                                .map(detail -> {
                                                    LocalDate date = detail.getDate();
                                                    LocalDate firstDayOfMonth = date.withDayOfMonth(1);
                                                    return LibrasProdFwDetail.builder().date(firstDayOfMonth).value(detail.getValue()).build();
                                                })
                                                .collect(Collectors.toList()))
                                        .orElse(Collections.emptyList());


                                List<PrecioCierreUsdKgNetoDetail> purchPrice = Optional.ofNullable(demand.getPurchPrice())
                                        .filter(list -> !list.isEmpty())
                                        .map(list -> list.stream()
                                                .sorted(Comparator.comparing(PrecioCierreUsdKgNetoDetail::getDate))
                                                .map(detail -> {
                                                    LocalDate date = detail.getDate();
                                                    LocalDate firstDayOfMonth = date.withDayOfMonth(1);
                                                    return PrecioCierreUsdKgNetoDetail.builder().date(firstDayOfMonth).value(detail.getValue()).build();
                                                })
                                                .collect(Collectors.toList()))
                                        .orElse(Collections.emptyList());

                                List<RmpDetail> rmp = Optional.ofNullable(demand.getRmp())
                                        .filter(list -> !list.isEmpty())
                                        .map(list -> list.stream()
                                                .sorted(Comparator.comparing(RmpDetail::getDate))
                                                .map(detail -> {
                                                    LocalDate date = detail.getDate();
                                                    LocalDate firstDayOfMonth = date.withDayOfMonth(1);
                                                    return RmpDetail.builder().date(firstDayOfMonth).value(detail.getValue()).build();
                                                })
                                                .collect(Collectors.toList()))
                                        .orElse(Collections.emptyList());


                                for (int i = 0; i < dateSize; i++) {

                                    PrecioCierreUsdKgNetoDetail p;
                                    if (i < purchPrice.size()) {
                                        p = purchPrice.get(i);
                                    } else {
                                        p = PrecioCierreUsdKgNetoDetail.builder().date(null).value(BigDecimal.ZERO).build();
                                    }

                                    if (areInSameMonthAndYear(p.getDate(), getCellValueAsLocalDate(beforeheaderRow.getCell(columnSize)))) {
                                        setCellValue(dataRow.createCell(columnSize), p.getValue() == null ? 0 : p.getValue());
                                    } else {
                                        int position = getPosition(purchPricePosition, p.getDate(), columnSize);
                                        setCellValue(dataRow.createCell(columnSize), 0);
                                        setCellValue(dataRow.createCell(position), p.getValue());
                                    }
                                    columnSize++;
                                }


                                for (int i = 0; i < dateSize; i++) {

                                    LibrasProdFwDetail p;
                                    if (i < prodFw.size()) {
                                        p = prodFw.get(i);
                                    } else {
                                        p = LibrasProdFwDetail.builder().date(null).value(BigDecimal.ZERO).build();
                                    }

                                    if (areInSameMonthAndYear(p.getDate(), getCellValueAsLocalDate(beforeheaderRow.getCell(columnSize)))) {
                                        setCellValue(dataRow.createCell(columnSize), p.getValue() == null ? 0 : p.getValue());
                                    } else {
                                        int position = getPosition(prodFwPosition, p.getDate(), columnSize);
                                        setCellValue(dataRow.createCell(columnSize), 0);
                                        setCellValue(dataRow.createCell(position), p.getValue());
                                    }
                                    columnSize++;
                                }

                                for (int i = 0; i < dateSize; i++) {

                                    FobDetail p;
                                    if (i < fob.size()) {
                                        p = fob.get(i);
                                    } else {
                                        p = FobDetail.builder().date(null).value(BigDecimal.ZERO).build();
                                    }

                                    if (areInSameMonthAndYear(p.getDate(), getCellValueAsLocalDate(beforeheaderRow.getCell(columnSize)))) {
                                        setCellValue(dataRow.createCell(columnSize), p.getValue() == null ? 0 : p.getValue());
                                    } else {
                                        int position = getPosition(fobPosition, p.getDate(), columnSize);
                                        setCellValue(dataRow.createCell(columnSize), 0);
                                        setCellValue(dataRow.createCell(position), p.getValue());
                                    }
                                    columnSize++;
                                }

                                for (int i = 0; i < dateSize; i++) {

                                    RmpDetail p;
                                    if (i < rmp.size()) {
                                        p = rmp.get(i);
                                    } else {
                                        p = RmpDetail.builder().date(null).value(BigDecimal.ZERO).build();
                                    }

                                    if (areInSameMonthAndYear(p.getDate(), getCellValueAsLocalDate(beforeheaderRow.getCell(columnSize)))) {
                                        setCellValue(dataRow.createCell(columnSize), p.getValue() == null ? 0 : p.getValue());
                                    } else {
                                        int position = getPosition(rmpPosition, p.getDate(), columnSize);
                                        setCellValue(dataRow.createCell(columnSize), 0);
                                        setCellValue(dataRow.createCell(position), p.getValue());
                                    }
                                    columnSize++;
                                }

                                increment++;
                                columnSize = 16;
                                fobPosition.clear();
                                prodFwPosition.clear();
                                purchPricePosition.clear();
                                rmpPosition.clear();
                            }
                        }

                        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                            workbook.write(bos);
                            return Mono.just(bos.toByteArray());
                        }
                    } catch (IOException e) {
                        return Mono.error(new RuntimeException("Error al generar el archivo Excel", e));
                    } catch (IndexOutOfBoundsException e) {
                        System.err.println("Error occurred: " + e.getMessage());
                        e.printStackTrace();
                        return Mono.error(new RuntimeException("Error al generar el archivo Excel", e));
                    }

                }));
    }

    private void createHeaders(String[] columnNames, Row headerRow, XSSFCellStyle headerStyle, Sheet sheet, int dateSize, XSSFCellStyle purchPriceStyle,
                               Row beforeheaderRow, List<LocalDate> dates, XSSFCellStyle prodFwStyle, XSSFCellStyle fobStyle, XSSFCellStyle styleRmp,
                               Map<LocalDate, Integer> purchPricePosition, Map<LocalDate, Integer> prodFwPosition, Map<LocalDate, Integer> fobPosition, Map<LocalDate, Integer> rmpPosition
    ) {

        for (int i = 0; i < columnNames.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columnNames[i]);
            cell.setCellStyle(headerStyle);
            sheet.autoSizeColumn(i);
        }

        int month = 1;
        for (int i = 0; i < dateSize; i++) {
            setCellValue(headerRow.createCell(i + 16), "CIF/CFR Purch Price - Month " + month, purchPriceStyle);
            setCellValue(beforeheaderRow.createCell(i + 16), dates.get(i), purchPriceStyle);
            purchPricePosition.put(dates.get(i), i + 16);
            sheet.autoSizeColumn(i);
            month++;
        }

        month = 1;
        int prodFwInt = 16 + dateSize;
        for (int i = 0; i < dateSize; i++) {
            setCellValue(headerRow.createCell(i + prodFwInt), "Prod FW - Month" + month, prodFwStyle);
            setCellValue(beforeheaderRow.createCell(i + prodFwInt), dates.get(i), prodFwStyle);
            prodFwPosition.put(dates.get(i), i + prodFwInt);
            sheet.autoSizeColumn(i);
            month++;
        }

        month = 1;
        int fobInt = prodFwInt + dateSize;
        for (int i = 0; i < dateSize; i++) {
            setCellValue(headerRow.createCell(i + fobInt), "Fob - Month" + month, fobStyle);
            setCellValue(beforeheaderRow.createCell(i + fobInt), dates.get(i), fobStyle);
            fobPosition.put(dates.get(i), i + fobInt);
            sheet.autoSizeColumn(i);
            month++;
        }

        month = 1;
        int rmpInt = fobInt + dateSize;
        for (int i = 0; i < dateSize; i++) {
            setCellValue(headerRow.createCell(i + rmpInt), "Rmp - Month" + month, styleRmp);
            setCellValue(beforeheaderRow.createCell(i + rmpInt), dates.get(i), styleRmp);
            rmpPosition.put(dates.get(i), i + rmpInt);
            sheet.autoSizeColumn(i);
            month++;
        }
    }


    private Mono<Period> getPeriod(Map<String, String> header) {
        return periodRepository.getSelectedPeriodByUser(header.get("user"))
                .collectList()
                .flatMap(periods -> {
                    if (periods.isEmpty()) {
                        return Mono.empty();
                    }
                    return Mono.just(periods.get(0));
                });
    }


    private void insertDemandUnrestricted(UnrestrictedDemandOffice demand, Sheet sheet, int i) {

        Row dataRow = sheet.getRow(i) == null ? sheet.createRow(i) : sheet.getRow(i);

        setCellValue(dataRow.createCell(0), demand.getOficina());
        setCellValue(dataRow.createCell(1), demand.getOfficeSalesType());
        setCellValue(dataRow.createCell(2), demand.getCustomerProgram());
        setCellValue(dataRow.createCell(3), demand.getPaisDestino());
        setCellValue(dataRow.createCell(4), demand.getEstatus());
        setCellValue(dataRow.createCell(5), demand.getProductsId());
        setCellValue(dataRow.createCell(6), demand.getDestinatario());
        setCellValue(dataRow.createCell(7), demand.getEspecie());
        setCellValue(dataRow.createCell(8), demand.getFamily());
        setCellValue(dataRow.createCell(9), demand.getOrderType());
        setCellValue(dataRow.createCell(10), demand.getForma());
        setCellValue(dataRow.createCell(11), demand.getCalidad());
        setCellValue(dataRow.createCell(12), demand.getRend());
        setCellValue(dataRow.createCell(13), demand.getSize());
        setCellValue(dataRow.createCell(14), demand.getIncoterm());
        setCellValue(dataRow.createCell(15), demand.getPuertoDestino());
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

    private void setCellValue(Cell cell, Object value, CellStyle cellStyle) {
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
        cell.setCellStyle(cellStyle);
    }

    private List<LocalDate> getMonthsBetweenTwoDates(LocalDate initialDate, LocalDate finalDate) {
        List<LocalDate> months = new ArrayList<>();
        YearMonth currentMonth = YearMonth.from(initialDate);

        while (!currentMonth.isAfter(YearMonth.from(finalDate))) {
            months.add(currentMonth.atDay(1));
            currentMonth = currentMonth.plusMonths(1);
        }
        return months;
    }

    private LocalDate getCellValueAsLocalDate(Cell cell) {
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case STRING:
                try {
                    return LocalDate.parse(cell.getStringCellValue());
                } catch (Exception e) {
                    return null;
                }
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    try {
                        Date date = cell.getDateCellValue();
                        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    } catch (Exception e) {
                        return null;
                    }
                }
                break;
            default:
                return null;
        }
        return null;
    }

    private boolean areInSameMonthAndYear(LocalDate date1, LocalDate date2) {
        if (date1 == null || date2 == null) {
            return false;
        }

        return date1.getMonth() == date2.getMonth() && date1.getYear() == date2.getYear();
    }

    private int getPosition(Map<LocalDate, Integer> positionMap, LocalDate date, int fallbackValue) {

        if (date == null) {
            return fallbackValue;
        }

        Integer position = positionMap.get(date);

        if (position != null) {
            return position;
        }

        LocalDate firstDayOfMonth = date.withDayOfMonth(1);
        position = positionMap.get(firstDayOfMonth);
        if (position != null) {
            return position;
        }

        return fallbackValue;
    }


}
