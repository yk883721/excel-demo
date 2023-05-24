package model;

import lombok.Data;

import java.util.List;

/**
 * @author admin
 */
@Data
public class PlanData {

    private String planName;

    private List<String> unitNames;

    List<ProfessionalData> professionalDataList;

}
