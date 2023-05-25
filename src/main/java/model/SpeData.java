package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpeData {

    private String specialityId;

    private String specialityName;

    private String typeId;

    private String typeName;

    private Integer planCount;

    private String unitId;

    private String unitName;

    private String professionalId;

    private String professionalName;

}
