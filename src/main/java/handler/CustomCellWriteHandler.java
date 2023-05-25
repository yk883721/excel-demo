package handler;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;

import java.util.List;

public class CustomCellWriteHandler implements CellWriteHandler {


    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {

        String stringCellValue = cell.getStringCellValue();


        if (stringCellValue.contains("(")) {
            int startIndex = stringCellValue.indexOf("(");
            int endIndex = stringCellValue.indexOf(")");

            //todo 核心代码  真正上下标起作用的

            Font font = cell.getSheet().getWorkbook().createFont();
            font.setTypeOffset(XSSFFont.SS_SUPER);
            font.setColor(IndexedColors.DARK_RED.getIndex());

            RichTextString richStringCellValue = cell.getRichStringCellValue();
            richStringCellValue.applyFont(startIndex, (endIndex + 1), font);

            cell.setCellValue(richStringCellValue);
        }else if (stringCellValue.contains("○")) {

            int indexOne = stringCellValue.indexOf("○");
            Font font1 = cell.getSheet().getWorkbook().createFont();
            font1.setColor(IndexedColors.DARK_RED.getIndex());

            int indexTwo = stringCellValue.indexOf("○", indexOne + 1);
            Font font2 = cell.getSheet().getWorkbook().createFont();
            font2.setColor(IndexedColors.DARK_YELLOW.getIndex());


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
