package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonData {

    private String idNumber;

    private String unitName;

    private String planName;

    private String professionalName;

    private String specialityId;

    private String specialityName;

}
