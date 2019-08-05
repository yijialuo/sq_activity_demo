package com.sq.demo.controller;

import com.sq.demo.Entity.AttachmentReturn;

import com.sq.demo.mapper.*;
import com.sq.demo.pojo.*;

import com.sq.demo.utils.Time;
import org.activiti.engine.*;

import org.activiti.engine.task.Attachment;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.HttpServletResponse;
import java.io.*;

import java.util.ArrayList;

import java.util.List;


/**
 * Created by yijialuo on 2019/1/13.
 */

@RestController
@RequestMapping("/Attachment")
public class AttachmentController {

    @Autowired
    ProjectMapper projectMapper;
    @Autowired
    DepartmentMapper departmentMapper;
    @Autowired
    AttachmentlinkMapper attachmentlinkMapper;
    @Autowired
    DepRankMapper depRankMapper;
    @Autowired
    YscjdwjMapper yscjdwjMapper;
    @Autowired
    ProjectController projectController;
    @Autowired
    BcwjMapper bcwjMapper;
    @Autowired
    ZhaobiaoMapper zhaobiaoMapper;
    @Autowired
    ContractMapper contractMapper;
    @Autowired
    XxmglMapper xxmglMapper;
    @Autowired
    XxmcbMapper xxmcbMapper;
    @Autowired
    ContractfileMapper contractfileMapper;
    @Autowired
    YanshouMapper yanshouMapper;
    @Autowired
    PayableMapper payableMapper;

    //删除附件
    @RequestMapping("/deletAttachment")
    @Transactional
    public boolean deletAttachment(String userId, String attachment_id) {
        try {
            if (attachment_id != null) {
                //删关联表
                Attachmentlink attachmentlink = new Attachmentlink();
                attachmentlink.setAttachment(attachment_id);
                attachmentlinkMapper.delete(attachmentlink);
                ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
                TaskService taskService = processEngine.getTaskService();
                taskService.deleteAttachment(attachment_id);
                //删除已上传节点文件表
                Yscjdwj yscjdwj = new Yscjdwj();
                yscjdwj.setFid(attachment_id);
                yscjdwjMapper.delete(yscjdwj);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
//
//        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        if (getrank(userId, attachment_id)) {
//            TaskService taskService = processEngine.getTaskService();
//            taskService.deleteAttachment(attachment_id);
//            //删关联表
//            Attachmentlink attachmentlink = new Attachmentlink();
//            attachmentlink.setAttachment(attachment_id);
//            attachmentlinkMapper.delete(attachmentlink);
//            return true;
//        } else {
//            return false;
//        }
    }


    //检查流程是否有附件
    @RequestMapping("/checkLcFj")
    public boolean checkLcFj(String pid) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        List<Attachment> atta = taskService.getProcessInstanceAttachments(pid);
        return atta.size()!=0;
    }

    //拿项目所有结算的附加
    @RequestMapping("getJsfj")
    public List<AttachmentReturn> getJsfj(String xmid) {
        Payable payable = new Payable();
        payable.setProject(xmid);
        List<Payable> payables = payableMapper.select(payable);
        List<AttachmentReturn> attachmentReturns = new ArrayList<>();
        for (Payable payable1 : payables) {
            Contractfile contractfile = new Contractfile();
            contractfile.setCid(payable1.getId());
            for (Contractfile contractfile1 : contractfileMapper.select(contractfile)) {
                AttachmentReturn attachmentReturn = new AttachmentReturn();
                attachmentReturn.setAttachment_id(contractfile1.getFid());
                attachmentReturn.setAttachment_nam(contractfile1.getFname());
                attachmentReturn.setScsj(contractfile1.getScsj());
                attachmentReturn.setScr(contractfile1.getScr());
                attachmentReturns.add(attachmentReturn);
            }
        }
        tcscr(attachmentReturns);
        return attachmentReturns;
    }

    //拿所有验收的附件
    @RequestMapping("getYsfj")
    public List<AttachmentReturn> getYsfj(String xmid) {
        Yanshou yanshou = new Yanshou();
        yanshou.setProjectid(xmid);
        yanshou = yanshouMapper.selectOne(yanshou);
        List<AttachmentReturn> attachmentReturns = new ArrayList<>();
        if (yanshou != null) {
            Contractfile contractfile = new Contractfile();
            contractfile.setCid(yanshou.getId());
            for (Contractfile contractfile1 : contractfileMapper.select(contractfile)) {
                AttachmentReturn attachmentReturn = new AttachmentReturn();
                attachmentReturn.setAttachment_id(contractfile1.getFid());
                attachmentReturn.setAttachment_nam(contractfile1.getFname());
                attachmentReturn.setScsj(contractfile1.getScsj());
                attachmentReturn.setScr(contractfile1.getScr());
                attachmentReturns.add(attachmentReturn);
            }

        }
        tcscr(attachmentReturns);
        return attachmentReturns;
    }

    //拿到股份小项目的所有附件
    @RequestMapping("getXxmfj")
    public List<AttachmentReturn> getXxmfj(String xmid) {
        Xxmgl xxmgl = new Xxmgl();
        xxmgl.setY1(xmid);
        xxmgl = xxmglMapper.selectOne(xxmgl);
        List<AttachmentReturn> attachmentReturns = new ArrayList<>();
        if (xxmgl != null) {
            Xxmcb xxmcb = new Xxmcb();
            xxmcb.setXxmid(xxmgl.getId());
            List<Xxmcb> xxmcbs = xxmcbMapper.select(xxmcb);
            for (Xxmcb xxmcb1 : xxmcbs) {
                Contractfile contractfile = new Contractfile();
                contractfile.setCid(xxmcb1.getId());
                for (Contractfile contractfile1 : contractfileMapper.select(contractfile)) {
                    AttachmentReturn attachmentReturn = new AttachmentReturn();
                    attachmentReturn.setAttachment_id(contractfile1.getFid());
                    attachmentReturn.setAttachment_nam(contractfile1.getFname());
                    attachmentReturn.setScsj(contractfile1.getScsj());
                    attachmentReturn.setScr(contractfile1.getScr());
                    attachmentReturns.add(attachmentReturn);
                }
            }
        }
        tcscr(attachmentReturns);
        return attachmentReturns;
    }

    //返回记录各个环节的附件信息（前期、招标、合同）
    @RequestMapping("/getHjFj")
    public List<AttachmentReturn> getHjFj(String xmid, String hj) {
        Bcwj bcwj = new Bcwj();
        bcwj.setLc(hj);
        List<AttachmentReturn> attachmentReturns = new ArrayList<>();
        for (Bcwj bcwj1 : bcwjMapper.select(bcwj)) {
            Yscjdwj yscjdwj = new Yscjdwj();
            yscjdwj.setBcwjid(bcwj1.getId());
            if (hj.equals("前期")) {
                yscjdwj.setJlid(xmid);
            } else if (hj.endsWith("招标")) {
                Zhaobiao zhaobiao = new Zhaobiao();
                zhaobiao.setXmid(xmid);
                zhaobiao = zhaobiaoMapper.selectOne(zhaobiao);
                if (zhaobiao == null)
                    return attachmentReturns;
                yscjdwj.setJlid(zhaobiao.getId());
            } else {//合同
                Contract contract = new Contract();
                contract.setProjectId(xmid);
                contract = contractMapper.selectOne(contract);
                if (contract == null)
                    return attachmentReturns;
                yscjdwj.setJlid(contract.getId());
            }
            List<Yscjdwj> yscjdwjs = yscjdwjMapper.select(yscjdwj);
            for (Yscjdwj yscjdwj1 : yscjdwjs) {
                AttachmentReturn attachmentReturn = new AttachmentReturn();
                attachmentReturn.setAttachment_id(yscjdwj1.getFid());
                attachmentReturn.setAttachment_nam(yscjdwj1.getFname());
                attachmentReturn.setScr(yscjdwj1.getScr());
                attachmentReturn.setScsj(yscjdwj1.getY1());
                attachmentReturns.add(attachmentReturn);
            }
        }
        tcscr(attachmentReturns);
        return attachmentReturns;
    }


    //填充附件信息的上传人，上传人的账号变为上传人的姓名
    void tcscr(List<AttachmentReturn> res) {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = engine.getIdentityService();
        for (AttachmentReturn attachmentReturn : res) {
            if (attachmentReturn.getScr() != null && !attachmentReturn.getScr().equals("")) {
                String scr = identityService.createUserQuery().userId(attachmentReturn.getScr()).singleResult().getFirstName();
                attachmentReturn.setScr(scr);
                attachmentReturn.setAttachment_nam(attachmentReturn.getAttachment_nam() + "----------上传人：" + scr);
            }
        }
    }


    //返回附件信息 已上传节点文件去找（yscjdwj）
    @RequestMapping("/getattachment2")
    public List<AttachmentReturn> getattachment2(String id, String bcwjid) {
        List<AttachmentReturn> attachmentReturns = new ArrayList<>();
        Yscjdwj yscjdwj = new Yscjdwj();
        yscjdwj.setBcwjid(bcwjid);
        yscjdwj.setJlid(id);
        List<Yscjdwj> yscjdwjs = yscjdwjMapper.select(yscjdwj);
        for (Yscjdwj yscjdwj1 : yscjdwjs) {
            AttachmentReturn attachmentReturn = new AttachmentReturn();
            attachmentReturn.setAttachment_id(yscjdwj1.getFid());
            attachmentReturn.setAttachment_nam(yscjdwj1.getFname());
            attachmentReturn.setScr(yscjdwj1.getScr());
            attachmentReturn.setScsj(yscjdwj1.getY1());
            attachmentReturns.add(attachmentReturn);
        }
        tcscr(attachmentReturns);
        return attachmentReturns;
    }


    //报表附件的下载
    @RequestMapping("/Download")
    public void Download(HttpServletResponse res, String fid, String fname) throws UnsupportedEncodingException {
        if (fid != null && !fid.equals("")) {
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            TaskService taskService = processEngine.getTaskService();
            System.out.println(Time.getNow() + "  attachment_id: " + fid);
            //直接资源表找到，流程下载
            if (taskService.getAttachment(fid) != null) {
                String fileName = taskService.getAttachment(fid).getName();
                res.setHeader("content-type", "application/octet-stream");
                res.setContentType("application/octet-stream");
                //中文文件名不行，需要转码
                String file_name = new String(fileName.getBytes(), "ISO-8859-1");
                res.setHeader("Content-Disposition", "attachment;filename=" + file_name);
                byte[] buff = new byte[1024];
                BufferedInputStream bis = null;
                OutputStream os = null;
                try {
                    os = res.getOutputStream();
                    bis = new BufferedInputStream(processEngine.getTaskService().getAttachmentContent(fid));
                    int i = bis.read(buff);
                    while (i != -1) {
                        os.write(buff, 0, buff.length);
                        os.flush();
                        i = bis.read(buff);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {//找不到，部署下载
                RepositoryService repositoryService = processEngine.getRepositoryService();
                res.setHeader("content-type", "application/octet-stream");
                res.setContentType("application/octet-stream");
                if (fname.contains("----------"))
                    fname = fname.split("----------")[0];
                //中文文件名不行，需要转码
                String file_name = new String(fname.getBytes(), "ISO-8859-1");
                res.setHeader("Content-Disposition", "attachment;filename=" + file_name);
                byte[] buff = new byte[1024];
                BufferedInputStream bis = null;
                OutputStream os = null;
                try {
                    os = res.getOutputStream();
                    bis = new BufferedInputStream(repositoryService.getResourceAsStream(fid, fname));
                    int i = bis.read(buff);
                    while (i != -1) {
                        os.write(buff, 0, buff.length);
                        os.flush();
                        i = bis.read(buff);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        }
    }

    //下载附件(流程附件)
    @RequestMapping("/download")
    public void getattachment1(HttpServletResponse res, String attachment_id) throws UnsupportedEncodingException {
        if (attachment_id != null && !attachment_id.equals("")) {
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            TaskService taskService = processEngine.getTaskService();
            System.out.println(Time.getNow() + "  attachment_id: " + attachment_id);
            String fileName = taskService.getAttachment(attachment_id).getName();
            res.setHeader("content-type", "application/octet-stream");
            res.setContentType("application/octet-stream");
            //中文文件名不行，需要转码
            String file_name = new String(fileName.getBytes(), "ISO-8859-1");
            res.setHeader("Content-Disposition", "attachment;filename=" + file_name);
            byte[] buff = new byte[1024];
            BufferedInputStream bis = null;
            OutputStream os = null;
            try {
                os = res.getOutputStream();
                bis = new BufferedInputStream(processEngine.getTaskService().getAttachmentContent(attachment_id));
                int i = bis.read(buff);
                while (i != -1) {
                    os.write(buff, 0, buff.length);
                    os.flush();
                    i = bis.read(buff);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
