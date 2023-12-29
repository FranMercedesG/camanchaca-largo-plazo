package cl.camanchaca.biomass.adapter.excel;

import cl.camanchaca.business.repositories.ExcelRepository;
import cl.camanchaca.business.repositories.GroupRepository;
import cl.camanchaca.business.repositories.SizeRepository;
import cl.camanchaca.business.utils.SizeUtils;
import cl.camanchaca.domain.models.Group;
import cl.camanchaca.domain.models.Size;
import cl.camanchaca.domain.models.biomass.GroupSizeMaxValue;
import cl.camanchaca.domain.models.biomass.GroupSizeMaximum;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
@AllArgsConstructor
public class GroupMaxExcelAdapter implements ExcelRepository<GroupSizeMaximum> {

    private final GroupRepository groupRepository;
    private final SizeRepository sizeRepository;

    @Override
    public Flux<GroupSizeMaximum> readFile(InputStream file) throws IOException {
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(0);

        Iterator<Row> iterator = sheet.iterator();
        List<GroupSizeMaximum> groupSizeMaximums = new ArrayList<>();

        if (iterator.hasNext()) {
            iterator.next();
        }

        while (iterator.hasNext()) {

            Row currentRow = iterator.next();

            if (isRowEmpty(currentRow)) {
                break;
            }
            GroupSizeMaximum unrestrictedDemand = createGroupSizeMaximum(currentRow, sheet.getRow(0));
            groupSizeMaximums.add(unrestrictedDemand);
        }

        workbook.close();

        return getGroupCompletes(groupSizeMaximums)
                .flatMapMany(Flux::fromIterable)
                .sort((group1, group2) -> SizeUtils.sortGroupsMaximum().compare(group1, group2));
    }

    private Mono<List<GroupSizeMaximum>> getGroupCompletes(List<GroupSizeMaximum> groupSizeMaximums) {

        return Mono.zip(groupRepository.getAll()
                        .collectMap(Group::getName, Group::getId)
                        ,sizeRepository.getAll()
                                .collectMap(size -> size.nameColunm(), Size::getId),
                        sizeRepository.getAll()
                                .collectMap(size -> size.nameColunm(), Size::getPieceType)
                        )
                .map(tuple -> {
                    Map<String, UUID> groupsMap = tuple.getT1();
                    Map<String, UUID> sizesMap = tuple.getT2();
                    Map<String, String> qualityMap = tuple.getT3();

                    groupSizeMaximums.forEach(groupSizeMaximum -> {
                        UUID groupId = groupsMap.getOrDefault(groupSizeMaximum.getGroupName(), null);
                        UUID sizeId = sizesMap.getOrDefault(groupSizeMaximum.getSizeName(), null);
                        String qualityName = qualityMap.getOrDefault(groupSizeMaximum.getSizeName(), null);

                        groupSizeMaximum.setGroupId(groupId);
                        groupSizeMaximum.setSizeId(sizeId);
                        groupSizeMaximum.setQualityName(qualityName);
                    });

                    return groupSizeMaximums;
                });
    }


    private boolean isRowEmpty(Row row) {
        if (row == null) {
            return true;
        }

        for (int i = 0; i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }

        return true;
    }

    private GroupSizeMaximum createGroupSizeMaximum(Row currentRow, Row header) {
        GroupSizeMaximum.GroupSizeMaximumBuilder builder = GroupSizeMaximum.builder();

        builder.groupName(getCellValueAsString(currentRow.getCell(0)));
        builder.sizeName(getCellValueAsString(currentRow.getCell(1)));
        builder.specieName(getCellValueAsString(currentRow.getCell(2)));
        List<GroupSizeMaxValue> value = new ArrayList<>();

        for (int i = 3; i < currentRow.getLastCellNum(); i++) {
            Cell cell = currentRow.getCell(i);
            if (cell != null) {
                GroupSizeMaxValue group = convertCellToGroupSizeMaxValue(cell, header.getCell(i));
                value.add(group);
            }
        }
        builder.value(value);

        return builder.build();
    }

    private GroupSizeMaxValue convertCellToGroupSizeMaxValue(Cell cell, Cell headerCell) {
        GroupSizeMaxValue maxValue = new GroupSizeMaxValue();
        maxValue.setPeriod(getCellValueAsLocalDate(headerCell));
        maxValue.setMaxLimit(getCellValueAsBigDecimal(cell));
        return maxValue;
    }

    public static UUID convertStringToUUID(String uuidStr) {
        try {
            return UUID.fromString(uuidStr);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            default:
                return "";
        }
    }

    private BigDecimal getCellValueAsBigDecimal(Cell cell) {
        if (cell == null) {
            return BigDecimal.ZERO;
        }
        switch (cell.getCellType()) {
            case NUMERIC:
                return BigDecimal.valueOf(cell.getNumericCellValue());
            case STRING:
                try {
                    return new BigDecimal(cell.getStringCellValue());
                } catch (NumberFormatException e) {
                    return BigDecimal.ZERO;
                }
            default:
                return BigDecimal.ZERO;
        }
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
}
