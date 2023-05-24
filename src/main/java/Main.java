import model.PersonData;
import model.PlanData;

import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {

        List<PersonData> dataList = new ArrayList<>();
        dataList.add(new PersonData("1", "支队名称1", "", "一分队", "qxd1", "轻型队" ));
        dataList.add(new PersonData("6", "支队名称1", "", "一分队", "qxd1", "轻型队" ));
        dataList.add(new PersonData("7", "支队名称1", "", "一分队", "qxd1", "轻型队" ));
        dataList.add(new PersonData("3", "支队名称1", "", "一分队", "zxd1", "重型队" ));


        dataList.add(new PersonData("2", "支队名称1", "", "二分队", "qxd2", "轻型队" ));
        dataList.add(new PersonData("2", "支队名称1", "", "二分队", "tsd1", "特殊队" ));

        dataList.add(new PersonData("4", "支队名称1", "", "三分队", "zxd2", "重型队" ));
        dataList.add(new PersonData("5", "支队名称1", "", "三分队", "zxd2", "重型队" ));
        dataList.add(new PersonData("5", "支队名称1", "", "三分队", "zhd1", "灾害队" ));

        // 查数据写EXCEL
        List<List<String>> excelDataList = new ArrayList<>();

        // 1. 获取默认排序
        List<String> professionalNames = StreamUtil.distinctList(dataList, PersonData::getProfessionalName);
        Map<String, List<String>> sSortMap = StreamUtil.groupingThenDistinctList(dataList,
                PersonData::getProfessionalName, PersonData::getSpecialityName);


        // 1. 各分队数据提取
        Map<String, Long> pCountMap = StreamUtil.groupingCount(dataList, PersonData::getProfessionalName);
        Map<String, Long> sCountMap = StreamUtil.groupingCount(dataList, PersonData::getSpecialityName);
//        Set<String> professionalNames = pCountMap.keySet();

        // 各个分队下有几个专业
        Map<String, Integer> sCountWithPMap = StreamUtil.groupingThenDistinctCount(dataList, PersonData::getProfessionalName, PersonData::getSpecialityName);

        // 各个分队下 -> (各个专业队 ->有多少人)
        Map<String, Map<String, Long>> pAndSCountMap = StreamUtil.groupingThenGroupingCount(
                dataList, PersonData::getProfessionalName, PersonData::getSpecialityName);


        // pHeads 处理
        List<String> pHeads = new ArrayList<>(Arrays.asList("", ""));
        for (String pName : professionalNames) {

            String pValueStr = pName + pCountMap.getOrDefault(pName, 0L) + "人";
            int sCountWithP = sCountWithPMap.getOrDefault(pName, 0);
            for (int i = 0; i < (sCountWithP + 1); i++) {
                pHeads.add(pValueStr);
            }
        }

        System.out.println(pHeads);


        // 2. sHeads 处理
        List<String> sHeads = new ArrayList<>(Arrays.asList("序号", "单位"));
        for (String professionalName : professionalNames) {
            List<String> sNames = sSortMap.getOrDefault(professionalName, Collections.emptyList());
            Long sCoutSumWithP = 0L;
            for (String sName : sNames) {
                Long sCount = sCountMap.getOrDefault(sName, 0L);
                String sValueStr = sName + sCount + "人";
                sHeads.add(sValueStr);
            }

            sHeads.add("小计");
        }

        System.out.println(sHeads);

    }

}
