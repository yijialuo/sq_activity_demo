package com.sq.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sq.demo.mapper.OperateManualMapper;
import com.sq.demo.pojo.OperateManual;
import org.apache.commons.io.FileUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Author: 13965
 * @Date: 2019/4/19 17:04
 * @Description:
 */
@Controller
@RequestMapping("/operateManual")
public class OperateManualController {
    @Autowired
    private OperateManualMapper operateManualMapper;

    private static String OS = System.getProperty("os.name").toLowerCase();

    @PostMapping("/add")
    public ResponseEntity<OperateManual> add(HttpServletRequest request, @RequestParam("json") String json, @RequestParam("file")MultipartFile file) throws Exception{
        OperateManual operateManual=new ObjectMapper().readValue(json,OperateManual.class);
        operateManual.setCreateTime(new Timestamp(new Date().getTime()));


        String path=getCurrentUploadDirectory();
        String fileName= UUID.randomUUID().toString()+file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        File filePath=new File(path,fileName);
        if(!filePath.getParentFile().exists()){
            filePath.getParentFile().mkdirs();
        }

        file.transferTo(new File(path+File.separator+fileName));
        operateManual.setAddress(path+File.separator+fileName);
        operateManualMapper.insertByKey(operateManual);

        return ResponseEntity.ok().body(operateManual);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> delete(@RequestBody Long[] ids){
        Arrays.stream(ids)
                .forEach(id->{
                    File originFile=new File(operateManualMapper.selectByPrimaryKey(id).getAddress());
                    if(originFile.exists()){
                        originFile.delete();
                    }
                    operateManualMapper.deleteByPrimaryKey(id);
                });
        return ResponseEntity.ok().body(true);
    }

    @PutMapping("/update")
    public ResponseEntity<OperateManual> update(@RequestParam("json") String json, @RequestParam("file")MultipartFile file) throws Exception{
        // id name address uploader description file，其中id、address不变
        OperateManual operateManual=new ObjectMapper().readValue(json,OperateManual.class);

        File originFile=new File(operateManual.getAddress());
        if(originFile.exists()){
            originFile.delete();
        }

        file.transferTo(new File(operateManual.getAddress()));

        operateManualMapper.updateByPrimaryKey(operateManual);

        return ResponseEntity.ok().body(operateManual);
    }

    @GetMapping("/get")
    public ResponseEntity<List<OperateManual>> get(@RequestParam int page,@RequestParam int size){
        int allCount=operateManualMapper.getAllCount();// 行总数目
        int pageCount=allCount%size==0?allCount/size:allCount/size+1;// 页数

        int offset=(page-1)*size;
        int limit=size;

        RowBounds rowBounds=new RowBounds(offset,limit);
        List<OperateManual> operateManualList = operateManualMapper.getPage(rowBounds);

        ResponseEntity.BodyBuilder builder=ResponseEntity.ok();
        builder.header("pageCount",""+pageCount);
        builder.header("allCount",""+allCount);
        return builder.body(operateManualList);
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> download(@RequestParam("id") Long id) throws IOException {
        OperateManual operateManual=operateManualMapper.selectByPrimaryKey(id);

        if(operateManual==null){
            return ResponseEntity.badRequest().body(null);
        }

        File file=new File(operateManual.getAddress());
        ResponseEntity.BodyBuilder builder=ResponseEntity.ok();
        builder.contentLength(file.length());
        builder.contentType(MediaType.APPLICATION_OCTET_STREAM);
        //builder.header("Content-Disposition","attachment;filename*=UTF-8''"+file.getName());
        //中文文件名不行，需要转码
        String file_name = new String((operateManual.getName()+operateManual.getAddress().substring(operateManual.getAddress().lastIndexOf('.'))).getBytes(), "ISO-8859-1");
        //res.setHeader("Content-Disposition", "attachment;filename=" + file_name);
        builder.header("Content-Disposition","attachment;filename="+file_name);
        return builder.body(FileUtils.readFileToByteArray(file));
    }

    public static String getCurrentUploadDirectory(){
        if(OS.indexOf("linux")>=0)
            return "/upload";
        else if(OS.indexOf("windows")>=0)
            return "D:\\upload";
        else
            return null;
    }
}
