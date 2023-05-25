package handler;


import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.ArrayList;
import java.util.List;



@NoArgsConstructor
@AllArgsConstructor
/**
 *  自定义 easyExcel拦截器
 */
public class CustomSheetWriteHandler implements SheetWriteHandler {

    private List<Integer> mergeCountList = new ArrayList<>();

    @Override
    public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {

    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {

        Sheet sheet = writeSheetHolder.getSheet();
        Integer startIndex = 1;
        for (Integer mergeCount : mergeCountList) {
            CellRangeAddress region = new CellRangeAddress(0, 0, startIndex + 1, startIndex + mergeCount);
            sheet.addMergedRegionUnsafe(region);

            startIndex += mergeCount;
        }


    }



}
