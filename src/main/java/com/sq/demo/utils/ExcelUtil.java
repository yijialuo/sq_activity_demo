package com.sq.demo.utils;

import org.apache.poi.hpsf.DocumentSummaryInformation;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.List;

/**
 * @Author: 13965
 * @Date: 2019/4/23 15:10
 * @Description:
 */
public class ExcelUtil {
    /**
     * 创建表格基本信息
     * @param workbook 表格
     */
    public static void createInformation(HSSFWorkbook workbook){
        workbook.createInformationProperties();
        DocumentSummaryInformation dsi = workbook.getDocumentSummaryInformation();
        dsi.setCategory("类别:Excel文件");
        dsi.setManager("管理者:易家洛");
        dsi.setCompany("公司:广州港数据科技有限公司");
        SummaryInformation si = workbook.getSummaryInformation();
        si.setSubject("主题:新沙工程项目管理系统报表");
        si.setTitle("标题:实施进度表、统计报表");
        si.setAuthor("作者:王伦辉");
        si.setComments("备注:可打印");
    }

    /**
     * 创建列
     * @param row 当前行
     * @param column 列号
     * @param name 列名
     * @param style 样式
     * @return 创建列
     */
    public static HSSFCell createCell(HSSFRow row, int column, Object name, HSSFCellStyle style){
        HSSFCell cell=row.createCell(column);
        cell.setCellValue(name!=null?name.toString():"");
        cell.setCellStyle(style);

        return cell;
    }

    /**
     * 创建合并单元格
     * @param sheet 工作表
     * @param firstRow 第一行
     * @param lastRow 最后一行
     * @param firstCol 第一列
     * @param lastCol 最后一列
     * @return
     */
    public static int createRange(HSSFSheet sheet,int firstRow, int lastRow, int firstCol, int lastCol){
        CellRangeAddress region=new CellRangeAddress(firstRow,lastRow,firstCol,lastCol);
        return sheet.addMergedRegion(region);
    }

    /**
     * 创建空列并填充样式，解决填充列样式没有的问题
     * @param row 行
     * @param columnStart 开始列
     * @param columnEnd 结束列
     * @param style 填充样式
     */
    public static void createTempCell(HSSFRow row,int columnStart,int columnEnd, HSSFCellStyle style){
        for (int i=columnStart;i<=columnEnd;i++){
            HSSFCell cell=row.createCell(i);
            cell.setCellStyle(style);
        }
    }

    /**
     * 设置列宽度
     * @param sheet 工作表
     * @param widths 宽度二维数组，一维列索引，二维宽度值
     */
    public static void setCloumnWitth(HSSFSheet sheet,int[][] widths){
        for (int i=0;i<widths[0].length;i++){
            sheet.setColumnWidth(widths[0][i],widths[1][i]);
        }
    }

    /**
     * 创建行表头
     * @param row 当前行
     * @param names 表头名称数组
     * @param style 每列风格
     */
    public static void createRowHeader(HSSFRow row,String[] names,HSSFCellStyle style){
        for (int i=0;i<names.length;i++){
            createCell(row,i,names[i],style);
        }
    }

    /**
     * 插入一行数据
     * @param beginRow 开始行
     * @param values 值数组
     * @param style 每列风格
     */
    public static void insertRow(HSSFSheet sheet,int beginRow, List<List<String>> values, HSSFCellStyle style){
        for (int i=0;i<values.size();i++){
            HSSFRow row = sheet.createRow(beginRow++);
            for (int ii=0;ii<values.get(i).size();ii++){
                createCell(row,ii,values.get(i).get(ii),style);
            }
        }
    }
}
