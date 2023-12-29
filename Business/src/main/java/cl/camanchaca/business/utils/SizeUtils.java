package cl.camanchaca.business.utils;

import cl.camanchaca.business.responses.ProjectedBiomassResponse;
import cl.camanchaca.domain.models.Size;
import cl.camanchaca.domain.models.biomass.GroupSizeMaximum;
import cl.camanchaca.domain.models.biomass.GroupSizeMaximumDTO;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class SizeUtils {

    private static final Map<String, Integer> PIECE_TYPE_PRIORITY = new HashMap<>();
    static {
        PIECE_TYPE_PRIORITY.put("Piezas Premium", 1);
        PIECE_TYPE_PRIORITY.put("Piezas IND con piel", 2);
        PIECE_TYPE_PRIORITY.put("Piezas IND sin piel", 3);
    }

    public static Comparator<ProjectedBiomassResponse> sortProjected() {
        return Comparator.comparing((ProjectedBiomassResponse biomassResponse) -> PIECE_TYPE_PRIORITY.getOrDefault(biomassResponse.getQuality(), 4))
                .thenComparingDouble(biomassResponse -> Size
                        .ofString(biomassResponse.getSize())
                        .getInitialRange()
                        .doubleValue()
                );
    }

    public static Comparator<Size> sortSizes() {
        return Comparator.comparing((Size size) -> PIECE_TYPE_PRIORITY.getOrDefault(size.getPieceType(), 4))
                .thenComparing(size -> size.getInitialRange().doubleValue());
    }

    public static Comparator<GroupSizeMaximumDTO> sortGroups() {
        return Comparator.comparing((GroupSizeMaximumDTO groupMax) -> PIECE_TYPE_PRIORITY.getOrDefault(groupMax.getQualityName(), 4))
                .thenComparing(GroupSizeMaximumDTO::getSizeName);
    }

    public static Comparator<GroupSizeMaximum> sortGroupsMaximum() {
        return Comparator.comparing((GroupSizeMaximum groupMax) -> PIECE_TYPE_PRIORITY.getOrDefault(groupMax.getQualityName(), 4))
                .thenComparing(GroupSizeMaximum::getSizeName);
    }


    private SizeUtils(){}


}
