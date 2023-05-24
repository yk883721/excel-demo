package model;

import lombok.Data;

import java.util.List;

/**
 * @author admin
 */
@Data
public class SpecialityData {

    private String specialityName;

    private Integer count;

    private List<Integer> values;

}
