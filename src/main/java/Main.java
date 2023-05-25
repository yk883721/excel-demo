import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import handler.CustomCellWriteHandler;
import handler.CustomSheetWriteHandler;
import handler.MatchColumnWidthHandler;
import model.SpeData;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) {

        List<SpeData> dataList = new ArrayList<>();
        dataList.add(new SpeData("1", "专业队1", "qx1", "轻型队", 60, "zd1", "支队名称1", "fd1", "一分队" ));
        dataList.add(new SpeData("2", "专业队2", "qx1", "轻型队", 60, "zd2", "支队名称2", "fd1", "一分队" ));
        dataList.add(new SpeData("3", "专业队3", "qx1", "轻型队", 60, "zd3", "支队名称3", "fd1", "一分队" ));
        dataList.add(new SpeData("4", "专业队4", "zx1", "重型队", 75, "zd1", "支队名称1", "fd1", "一分队" ));

        dataList.add(new SpeData("5", "专业队5", "qx1", "轻型队", 60, "zd1", "支队名称1", "fd2", "二分队" ));
        dataList.add(new SpeData("6", "专业队6", "ts1", "特殊队", 40, "zd1", "支队名称1", "fd2", "二分队" ));

        dataList.add(new SpeData("7", "专业队7", "zx1", "重型队", 75, "zd1", "支队名称1", "fd3","三分队" ));
        dataList.add(new SpeData("8", "专业队8", "zx1", "重型队", 75, "zd2", "支队名称2", "fd3","三分队" ));
        dataList.add(new SpeData("9", "专业队9", "zh1", "灾害队", 20, "zd1", "支队名称1", "fd3","三分队" ));

        // 查数据写EXCEL
        List<List<String>> excelDataList = new ArrayList<>();

        // 1. 获取默认排序
        List<String> professionalIds = StreamUtil.distinctList(dataList, SpeData::getProfessionalId);
        List<String> unitIds = StreamUtil.distinctList(dataList, SpeData::getUnitId);
        Map<String, List<String>> sSortMap = StreamUtil.groupingThenDistinctList(dataList,
                SpeData::getProfessionalId, SpeData::getTypeId);


        // 2. 各分队、各专业队 数据提取
        Map<String, Integer> proPlanCountMap = StreamUtil.groupingSum(dataList, SpeData::getProfessionalId, SpeData::getPlanCount);
        Map<String, Long> unitAndProAndTypeCountMap = StreamUtil.groupingCount(dataList, p -> p.getProfessionalId() + p.getUnitId() + p.getTypeId());
        Map<String, Integer> proAndTypeSumMap = StreamUtil.groupingSum(dataList, v -> v.getProfessionalId() + v.getTypeId(), SpeData::getPlanCount);

        Map<String, Integer> sTypeCountMap = StreamUtil.listToMap(dataList, SpeData::getTypeId, SpeData::getPlanCount);


        // 3. 获取 分队、各专业队 Id -> Name 映射
        Map<String, String> pId2NameMap = StreamUtil.listToMap(dataList, SpeData::getProfessionalId, SpeData::getProfessionalName);
        Map<String, String> typeNameMap = StreamUtil.listToMap(dataList, SpeData::getTypeId, SpeData::getTypeName);
        Map<String, String> uId2NameMap = StreamUtil.listToMap(dataList, SpeData::getUnitId, SpeData::getUnitName);


        // pHeads 处理
        List<Integer> mergeCountList = new ArrayList<>();
        List<String> pHeads = new ArrayList<>(Arrays.asList("", ""));
        for (String pId : professionalIds) {
            String pName = pId2NameMap.getOrDefault(pId, " - ");

            String pValueStr = pName + proPlanCountMap.getOrDefault(pId, 0) + "人";
            int sCountWithP = sSortMap.getOrDefault(pId, Collections.emptyList()).size();

            mergeCountList.add(sCountWithP + 1);
            for (int i = 0; i < (sCountWithP + 1); i++) {
                pHeads.add(pValueStr);
            }
        }

        // 2. sHeads 处理
        List<String> sHeads = new ArrayList<>(Arrays.asList("序号", "单位"));
        for (String professionalId : professionalIds) {
            List<String> sTypeIds = sSortMap.getOrDefault(professionalId, Collections.emptyList());
            for (String typeId : sTypeIds) {

                String typeName = typeNameMap.getOrDefault(typeId, " - ");
                int sCount = sTypeCountMap.getOrDefault(typeId, 0);

                String sValueStr = typeName + sCount + "人";
                sHeads.add(sValueStr);
            }

            sHeads.add("小计");
        }


        // 各单位数据处理
        List<List<String>> dataRows = new ArrayList<>();
        Integer index = 2;
        for (String unitId : unitIds) {

            String uName = uId2NameMap.getOrDefault(unitId, " - ");

            List<String> dataRow = new ArrayList<>();
            dataRow.add(String.valueOf(index));
            dataRow.add(uName);

            for (String pId : professionalIds) {
                List<String> typeIds = sSortMap.getOrDefault(pId, Collections.emptyList());

                Long pSum = 0L;
                for (String typeId : typeIds) {

                    String key = pId + unitId + typeId;

                    Long unitAndProAndSpeCount = unitAndProAndTypeCountMap.getOrDefault(key, 0L);
                    Integer typePlanCount = sTypeCountMap.getOrDefault(typeId, 0);

                    dataRow.add(unitAndProAndSpeCount == 0 ? "" : String.valueOf(unitAndProAndSpeCount));

                    pSum += unitAndProAndSpeCount * typePlanCount;
                }

                dataRow.add(pSum == 0 ?"":String.valueOf(pSum));
            }
            index++;
            dataRows.add(dataRow);
        }

        // 合计
        List<String> sumRow = new ArrayList<>(Arrays.asList(String.valueOf(index), "合计"));
        for (String professionalId : professionalIds) {
            List<String> typeIds = sSortMap.getOrDefault(professionalId, Collections.emptyList());
            int sSum = 0;
            for (String typeId : typeIds) {

                String key = professionalId + typeId;
                int proAndTypeCount = proAndTypeSumMap.getOrDefault(key, 0);
                sumRow.add(proAndTypeCount == 0 ? "" : String.valueOf(proAndTypeCount));
                sSum += proAndTypeCount;
            }

            sumRow.add(sSum == 0 ? "" : String.valueOf(sSum));
        }

        // 可调派合计
//        List<String> availableSumRow = new ArrayList<>(Arrays.asList(String.valueOf(index), "可调派合计"));
//        for (String professionalId : professionalIds) {
//            List<String> sIds = sSortMap.getOrDefault(professionalId, Collections.emptyList());
//
//            int proCount = 0;
//            int proDumpCount = 0;
//            for (String sId : sIds) {
//
//                Set<String> thisSpeIdSet = sCountMap.getOrDefault(sId, Collections.emptySet());
//                int speCount = thisSpeIdSet.size();
//
//                int speDumpCount = 0;
//                for (String perDumpId : perDumpIds) {
//                    if (thisSpeIdSet.contains(perDumpId)) {
//                        speDumpCount++;
//                    }
//                }
//
//                int speAvailableCount = speCount - speDumpCount;
//
//                String thisValueStr = String.format("%d (%d)", speAvailableCount, speDumpCount);
//                availableSumRow.add(thisValueStr);
//
//                proCount += speCount;
//                proDumpCount += speDumpCount;
//            }
//
//            String thisProValueStr = String.format("%d (%d)", proCount, proDumpCount);
//            availableSumRow.add(thisProValueStr);
//
//        }

        List<String> remarkRow = Collections.singletonList("备注：这里是备注文字这里是备注文字这里是备注文字这里是备注文字这里是备注");
        List<String> circleRow = Collections.singletonList("○: 非备勤状态  ○: 同时隶属多个专业队");

        excelDataList.add(pHeads);
        excelDataList.add(sHeads);
        excelDataList.addAll(dataRows);
        excelDataList.add(sumRow);
//        excelDataList.add(availableSumRow);
        excelDataList.add(Collections.emptyList());
        excelDataList.add(remarkRow);
        excelDataList.add(circleRow);

        excelDataList.forEach(System.out::println);

        Integer mergeCount = mergeCountList.stream().mapToInt(Integer::intValue).sum();

        // 写法1
        String fileName = "out.xlsx";
//        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        EasyExcel.write(fileName)
                .inMemory(true)
                .registerWriteHandler(new MatchColumnWidthHandler())
                .registerWriteHandler(new CustomSheetWriteHandler(mergeCountList))
                .registerWriteHandler(new CustomCellWriteHandler(mergeCount))
                .sheet("模板").doWrite(excelDataList);

    }

}
