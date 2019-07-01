package com.sq.demo.controller;

import com.sq.demo.mapper.YscjdwjMapper;
import com.sq.demo.pojo.Contractfile;
import com.sq.demo.pojo.Yscjdwj;
import com.sq.demo.utils.IdCreate;
import com.sq.demo.utils.Time;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@RestController
public class fileController {
    @Autowired
    YscjdwjMapper yscjdwjMapper;


    //上传未申请时候附件上传
    @RequestMapping("/upload")
    @Transactional
    public boolean uploadHtfj(MultipartFile file, String id, String bcwjid, String userId) {
        try {
            ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
            RepositoryService repositoryService = engine.getRepositoryService();
            InputStream isq = file.getInputStream();
            DeploymentBuilder builder = repositoryService.createDeployment();
            builder.addInputStream(file.getOriginalFilename(), isq);
            Yscjdwj yscjdwj = new Yscjdwj();
            yscjdwj.setId(IdCreate.id());
            yscjdwj.setJlid(id);
            yscjdwj.setBcwjid(bcwjid);
            yscjdwj.setFid(builder.deploy().getId());
            yscjdwj.setFname(file.getOriginalFilename());
            yscjdwj.setScr(userId);
            yscjdwj.setY1(Time.getNow());
            yscjdwjMapper.insert(yscjdwj);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}