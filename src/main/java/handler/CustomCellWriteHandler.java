package handler;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;

import java.util.ArrayList;
import java.util.List;

import static org.apache.poi.hssf.record.cf.BorderFormatting.BORDER_THIN;

@NoArgsConstructor
@AllArgsConstructor
public class CustomCellWriteHandler implements CellWriteHandler {

    private Integer mergeCount;

    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {

        CellStyle cellStyle = writeSheetHolder.getSheet().getWorkbook().createCellStyle();

        int rowIndex = cell.getRowIndex();
        int columnIndex = cell.getColumnIndex();

        // 1.分队行设置水平居中
        if (rowIndex == 0) {
            cellStyle.setAlignment(HorizontalAlignment.CENTER); //水平居中
        }

        // 2. 每一行设置边框
        if (columnIndex == 0) {
            cellStyle.setBorderBottom(BorderStyle.THIN); //下边框
            cellStyle.setBorderLeft(BorderStyle.THIN);//左边框
            cellStyle.setBorderTop(BorderStyle.THIN);//上边框
            cellStyle.setBorderRight(BorderStyle.THIN);//右边框

            Row row = cell.getRow();

            for (int i = 0; i < (2 + mergeCount); i++) {
                Cell indexedCell = row.getCell(i);
                if (indexedCell == null) {
                    indexedCell = row.createCell(i);
                }
                indexedCell.setCellStyle(cellStyle);
            }
        }



        String stringCellValue = cell.getStringCellValue();
        if (stringCellValue.contains("(")) {
            int startIndex = stringCellValue.indexOf("(");
            int endIndex = stringCellValue.indexOf(")");

            //todo 核心代码  真正上下标起作用的
            Font font = cell.getSheet().getWorkbook().createFont();
            font.setTypeOffset(XSSFFont.SS_SUPER);
            font.setColor(IndexedColors.RED.getIndex());

            RichTextString richStringCellValue = cell.getRichStringCellValue();
            richStringCellValue.applyFont(startIndex, (endIndex + 1), font);

            cell.setCellValue(richStringCellValue);
        }else if (stringCellValue.contains("○")) {

            int indexOne = stringCellValue.indexOf("○");
            Font font1 = cell.getSheet().getWorkbook().createFont();
            font1.setColor(IndexedColors.DARK_RED.getIndex());

            int indexTwo = stringCellValue.indexOf("○", indexOne + 1);
            Font font2 = cell.getSheet().getWorkbook().createFont();
            font2.setColor(IndexedColors.ORANGE.getIndex());

            RichTextString richValue = cell.getRichStringCellValue();

            richValue.applyFont(indexOne, (indexOne + 1), font1);
            richValue.applyFont(indexTwo, (indexTwo + 1), font2);

            cell.setCellValue(richValue);
        }

//
//        //背景色
//        CellStyle style = writeSheetHolder.getSheet().getWorkbook().createCellStyle();
//        style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
//        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        //水平垂直居中
//        CellStyle horiStyle = writeSheetHolder.getSheet().getWorkbook().createCellStyle();
//        horiStyle.setAlignment(HorizontalAlignment.CENTER);
//
//
//        if (!isHead && (cell.getColumnIndex() == 0 || cell.getColumnIndex() == 1)) {
//            cell.setCellStyle(horiStyle);
//        }

//        if (!isHead && cell.getColumnIndex() == 7) {
//            if(cell.getStringCellValue() != null){
//                if (cell.getStringCellValue().contains("迟到") || cell.getStringCellValue().contains("早退")){
//                    cell.setCellStyle(style);
//                }
//            }
//        }
    }
}
