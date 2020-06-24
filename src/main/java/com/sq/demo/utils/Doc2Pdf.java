package com.sq.demo.utils;

import com.aspose.words.Document;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;

import javax.servlet.http.HttpServletResponse;

import java.io.InputStream;
import java.io.OutputStream;

public class Doc2Pdf {


    public static boolean getLicense() throws Exception {
        boolean result = false;
        try {
            InputStream is = com.aspose.words.Document.class
                    .getResourceAsStream("/com.aspose.words.lic_2999.xml");
            License aposeLic = new License();
            aposeLic.setLicense(is);
            result = true;
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return result;
    }

    public static void doc2pdf(HttpServletResponse httpResponse, String fileName, Document  doc) throws Exception {
        if (!getLicense()) { // 验证License 若不验证则转化出的pdf文档有水印
            throw new Exception("com.aspose.words lic ERROR!");
        }
        httpResponse.setStatus(200);
        httpResponse.setHeader("content-type", "application/pdf");

        httpResponse.setContentType("application/pdf");

        httpResponse.setHeader("Content-Disposition", "attachment; filename=" + fileName);

        OutputStream os = httpResponse.getOutputStream();

        doc.save(os, SaveFormat.PDF);

        os.close();
    }

}
