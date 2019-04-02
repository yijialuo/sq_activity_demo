package com.sq.demo.utils;

import org.apache.poi.hpsf.DocumentSummaryInformation;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hwpf.usermodel.Range;
import org.springframework.util.ResourceUtils;


import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class WordUtils {

    public static void main(String[] args) throws Exception {
        testExportWord2();
    }

    //生成固定格式的word
    public static void testExportWord2() throws Exception {
        String tmpFile = "classpath:xmlxb.doc";
        String expFile = "D:/test/xmlxb.doc";
        Map<String, String> datas = new HashMap<String, String>();
        datas.put("declarationDep", "标题部份");
        datas.put("projectNo", "这里是内容，测试使用POI导出到Word的内容！");
        datas.put("projectNam", "");
        datas.put("projectType", "http://www.zslin.com");


        datas.put("investmentEstimate", "");
        datas.put("personInCharge", "http://ww");
        datas.put("establishReason", "h");
        datas.put("scale", "h");
        datas.put("illustration", "");
        datas.put("bmshyj", "");
        datas.put("jl", "h");
        datas.put("bmspsj", "h");
        datas.put("sjbjlyj", "");
        datas.put("jsbjl", "http://");
        datas.put("jsbspsj", "httpom");

        build(ResourceUtils.getFile(tmpFile), datas, expFile);
    }


    private static void build(File tmpFile, Map<String, String> contentMap, String exportFile) throws Exception {
        FileInputStream tempFileInputStream = new FileInputStream(tmpFile);
        HWPFDocument document = new HWPFDocument(tempFileInputStream);
        // 读取文本内容
        Range bodyRange = document.getRange();
        // 替换内容
        for (Map.Entry<String, String> entry : contentMap.entrySet()) {
            bodyRange.replaceText("{{" + entry.getKey() + "}}", entry.getValue());
        }

        //导出到文件
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        document.write(byteArrayOutputStream);
        OutputStream outputStream = new FileOutputStream(exportFile);
        outputStream.write(byteArrayOutputStream.toByteArray());
        outputStream.close();
    }


    public void readByText() throws Exception {
        InputStream in = new FileInputStream("D:\\test\\附件2：答题卡.doc");
        WordExtractor extractor = new WordExtractor(in);

        //输出word文档所有的文本
        System.out.println(extractor.getText());

        System.out.println(extractor.getTextFromPieces());
        //输出页眉的内容
        System.out.println("页眉：" + extractor.getHeaderText());
        //输出页脚的内容
        System.out.println("页脚：" + extractor.getFooterText());
        //输出当前word文档的元数据信息，包括作者、文档的修改时间等。
        System.out.println(extractor.getMetadataTextExtractor().getText());
        //获取各个段落的文本
        String paraTexts[] = extractor.getParagraphText();
        for (int i=0; i<paraTexts.length; i++) {
            System.out.println("Paragraph " + (i+1) + " : " + paraTexts[i]);
        }
        //输出当前word的一些信息
        printInfo(extractor.getSummaryInformation());
        //输出当前word的一些信息
        this.printInfo(extractor.getDocSummaryInformation());
        this.closeStream(in);
    }

    /**
     * 输出SummaryInfomation
     * @param info
     */
    private void printInfo(SummaryInformation info) {
        //作者
        System.out.println(info.getAuthor());
        //字符统计
        System.out.println(info.getCharCount());
        //页数
        System.out.println(info.getPageCount());
        //标题
        System.out.println(info.getTitle());
        //主题
        System.out.println(info.getSubject());
    }

    /**
     * 输出DocumentSummaryInfomation
     * @param info
     */
    private void printInfo(DocumentSummaryInformation info) {
        //分类
        System.out.println(info.getCategory());
        //公司
        System.out.println(info.getCompany());
    }

    /**
     * 关闭输入流
     * @param is
     */
    private void closeStream(InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
