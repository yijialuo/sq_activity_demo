package com.sq.demo.controller;

import com.sq.demo.mapper.ProjectMapper;
import com.sq.demo.pojo.Sgjdb;
import com.sq.demo.utils.ExcelUtil;
import com.sq.demo.utils.ReflectUtil;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/Bb")
public class BbController {
    @Autowired
    ProjectMapper projectMapper;

    @Autowired
    private SgjdbController sgjdbController;

    private static String OS = System.getProperty("os.name").toLowerCase();

    @RequestMapping("/get")
    public List<List<String>> get(String year, String month) {
//        System.out.println(month);
//        System.out.println(month.length());
//        if(month.length()==2){
//            if(month.charAt(0)=='0')
//                month=month.substring(1,1);
//        }

        List<List<String>> res = new ArrayList<>();
        Map yfmap = new HashMap();
        Map nfmap = new HashMap();
        yfmap.put("_year", year);
        yfmap.put("_month", month);
        projectMapper.yfbb(yfmap);
        //标题
        List<String> bt = new ArrayList<>();
        bt.add("");
        for (int i = 0; i < 5; i++) {
            bt.add("发标数量（项）");
            bt.add("结算数量（项）");
            bt.add("结算金额（万元）");
        }
        bt.add("");
        res.add(bt);

        List<String> yfbb = new ArrayList<>();
        yfbb.add("月份："+month);
        int zje=0;
        yfbb.add(yfmap.get("sb_fbsl").toString());
        yfbb.add(yfmap.get("sb_jssl").toString());
        if (yfmap.get("sb_jsje") == null)
            yfmap.put("sb_jsje", 0);
        zje=zje+Integer.valueOf(yfmap.get("sb_jsje").toString());
        yfbb.add(yfmap.get("sb_jsje").toString());

        yfbb.add(yfmap.get("jj_fbsl").toString());
        yfbb.add(yfmap.get("jj_jssl").toString());
        if (yfmap.get("jj_jsje") == null)
            yfmap.put("jj_jsje", 0);
        zje=zje+Integer.valueOf(yfmap.get("jj_jsje").toString());
        yfbb.add(yfmap.get("jj_jsje").toString());

        yfbb.add(yfmap.get("xx_fbsl").toString());
        yfbb.add(yfmap.get("xx_jssl").toString());
        if (yfmap.get("xx_jsje") == null)
            yfmap.put("xx_jsje", 0);
        zje=zje+Integer.valueOf(yfmap.get("xx_jsje").toString());
        yfbb.add(yfmap.get("xx_jsje").toString());

        yfbb.add(yfmap.get("wz_fbsl").toString());
        yfbb.add(yfmap.get("wz_jssl").toString());
        if (yfmap.get("wz_jsje") == null)
            yfmap.put("wz_jsje", 0);
        zje=zje+Integer.valueOf(yfmap.get("wz_jsje").toString());
        yfbb.add(yfmap.get("wz_jsje").toString());

        yfbb.add(yfmap.get("gdzc_fbsl").toString());
        yfbb.add(yfmap.get("gdzc_jssl").toString());
        if (yfmap.get("gdzc_jsje") == null)
            yfmap.put("gdzc_jsje", 0);
        zje=zje+Integer.valueOf(yfmap.get("gdzc_jsje").toString());
        yfbb.add(yfmap.get("gdzc_jsje").toString());
        yfbb.add(String.valueOf(zje));
        res.add(yfbb);

        List<String> nfbb = new ArrayList<>();
        nfmap.put("_year", year);
        nfmap.put("_month", month);
        projectMapper.nfbb(nfmap);
        zje=0;
        nfbb.add("年初累计：01-"+month);
        nfbb.add(nfmap.get("sb_fbsl").toString());
        nfbb.add(nfmap.get("sb_jssl").toString());
        if (nfmap.get("sb_jsje") == null)
            nfmap.put("sb_jsje", 0);
        zje=zje+Integer.valueOf(nfmap.get("sb_jsje").toString());
        nfbb.add(nfmap.get("sb_jsje").toString());

        nfbb.add(nfmap.get("jj_fbsl").toString());
        nfbb.add(nfmap.get("jj_jssl").toString());
        if (nfmap.get("jj_jsje") == null)
            nfmap.put("jj_jsje", 0);
        zje=zje+Integer.valueOf(nfmap.get("jj_jsje").toString());
        nfbb.add(nfmap.get("jj_jsje").toString());

        nfbb.add(nfmap.get("xx_fbsl").toString());
        nfbb.add(nfmap.get("xx_jssl").toString());
        if (nfmap.get("xx_jsje") == null)
            nfmap.put("xx_jsje", 0);
        zje=zje+Integer.valueOf(nfmap.get("xx_jsje").toString());
        nfbb.add(nfmap.get("xx_jsje").toString());

        nfbb.add(nfmap.get("wz_fbsl").toString());
        nfbb.add(nfmap.get("wz_jssl").toString());
        if (nfmap.get("wz_jsje") == null)
            nfmap.put("wz_jsje", 0);
        zje=zje+Integer.valueOf(nfmap.get("wz_jsje").toString());
        nfbb.add(nfmap.get("wz_jsje").toString());

        nfbb.add(nfmap.get("gdzc_fbsl").toString());
        nfbb.add(nfmap.get("gdzc_jssl").toString());
        if (nfmap.get("gdzc_jsje") == null)
            nfmap.put("gdzc_jsje", 0);
        zje=zje+Integer.valueOf(nfmap.get("gdzc_jsje").toString());
        nfbb.add(nfmap.get("gdzc_jsje").toString());
        nfbb.add(String.valueOf(zje));
        res.add(nfbb);
        return res;
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> download() throws IOException {
        String filePath = getCurrentUploadDirectory() + File.separator + "测试文件.xls";
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();

        ExcelUtil.createInformation(workbook);

        int[][] columnWidths = {
                {
                        0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19
                },
                {
                        18 * 256, 18 * 256, 18 * 256, 18 * 256, 18 * 256, 18 * 256, 18 * 256, 18 * 256, 18 * 256, 18 * 256, 18 * 256, 18 * 256, 18 * 256, 18 * 256, 18 * 256, 18 * 256, 18 * 256, 18 * 256, 18 * 256, 18 * 256
                }
        };
        ExcelUtil.setCloumnWitth(sheet, columnWidths);

        HSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);


        HSSFRow row2 = sheet.createRow(2);
        row2.setHeightInPoints(25);// 行高度
        ExcelUtil.createTempCell(row2, 1, 19, style);
        HSSFCell cell2_0 = ExcelUtil.createCell(row2, 0, "新沙公司工程项目实施进度表", style);
        ExcelUtil.createRange(sheet, 2, 2, 0, 19);

        String[] cloumnNames = {
                "序号", "项目计划号", "项目名称", "计划金额(万元)", "立项部门",
                "项目大类", "项目类别", "过会时间", "过总经办时间", "定标时间",
                "合同提交评审时间", "合同签订时间", "合同金额（万元）", "开工时间",
                "验收时间", "本年度结算进度（万元）", "总结算进度（万元）", "完成结算时间",
                "技术部主管", "施工单位"
        };
        HSSFRow row3 = sheet.createRow(3);
        ExcelUtil.createRowHeader(row3, cloumnNames, style);

        List<Sgjdb> sgjdbs = sgjdbController.getAllSgjdb();
        List<List<String>> values = new ArrayList<>();
        for (Sgjdb sgjdb : sgjdbs) {
            List<String> list= ReflectUtil.getAllDeclareField(sgjdb);
            values.add(list);
        }

        ExcelUtil.insertRow(sheet,4, values, style);

        try {
            java.io.File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }

            FileOutputStream out = new FileOutputStream(file);
            workbook.write(out);//保存Excel文件
            out.close();//关闭文件流
        } catch (IOException e) {
            e.printStackTrace();
        }

        File file = new File(filePath);
        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
        builder.contentLength(file.length());
        builder.contentType(MediaType.APPLICATION_OCTET_STREAM);
        //builder.header("Content-Disposition","attachment;filename*=UTF-8''"+file.getName());
        //中文文件名不行，需要转码
        String file_name = new String("项目实施进度表.xls".getBytes(), "ISO-8859-1");
        //res.setHeader("Content-Disposition", "attachment;filename=" + file_name);
        builder.header("Content-Disposition", "attachment;filename=" + file_name);
        return builder.body(FileUtils.readFileToByteArray(file));
    }

    public static String getCurrentUploadDirectory() {
        if (OS.indexOf("linux") >= 0)
            return "/upload";
        else if (OS.indexOf("windows") >= 0)
            return "D:\\upload";
        else
            return null;
    }
}
