package model;

import lombok.Data;

import java.util.List;

/**
 * @author admin
 */
@Data
public class ProfessionalData {

    private String professionalName;

    private Integer count;

    private List<String> specialityNames;

    private List<SpecialityData> specialityDataList;

}
