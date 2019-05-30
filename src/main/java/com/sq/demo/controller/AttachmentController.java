package com.sq.demo.controller;

import com.sq.demo.Entity.AttachmentReturn;

import com.sq.demo.mapper.*;
import com.sq.demo.pojo.Attachmentlink;

import com.sq.demo.pojo.Yscjdwj;
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

    //删除附件
    @RequestMapping("/deletAttachment")
    @Transactional
    public boolean deletAttachment(String userId, String attachment_id) {
        try {
            if(attachment_id!=null){
                //删关联表
                Attachmentlink attachmentlink = new Attachmentlink();
                attachmentlink.setAttachment(attachment_id);
                attachmentlinkMapper.delete(attachmentlink);
                ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
                TaskService taskService = processEngine.getTaskService();
                taskService.deleteAttachment(attachment_id);
                //删除已上传节点文件表
                Yscjdwj yscjdwj=new Yscjdwj();
                yscjdwj.setFid(attachment_id);
                yscjdwjMapper.delete(yscjdwj);
                return true;
            }else {
                return false;
            }
        }catch (Exception e){
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


    //返回附件信息
    @RequestMapping("/getattachment")
    public List<AttachmentReturn> getattachment(String pid) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        List<AttachmentReturn> attachmentReturns = new ArrayList<>();
        List<Attachment> atta = taskService.getProcessInstanceAttachments(pid);
        for (Attachment at : atta) {
            AttachmentReturn attachmentReturn = new AttachmentReturn();
            attachmentReturn.setAttachment_id(at.getId());
            attachmentReturn.setAttachment_nam(at.getName());
            attachmentReturns.add(attachmentReturn);
        }
        return attachmentReturns;
    }


    //返回附件信息
    @RequestMapping("/getattachment2")
    public List<AttachmentReturn> getattachment2(String id,String bcwjid) {
        List<AttachmentReturn> attachmentReturns = new ArrayList<>();
        Yscjdwj yscjdwj=new Yscjdwj();
        yscjdwj.setBcwjid(bcwjid);
        yscjdwj.setJlid(id);
        List<Yscjdwj> yscjdwjs=yscjdwjMapper.select(yscjdwj);
        for (Yscjdwj yscjdwj1 : yscjdwjs) {
            AttachmentReturn attachmentReturn = new AttachmentReturn();
            attachmentReturn.setAttachment_id(yscjdwj1.getFid());
            attachmentReturn.setAttachment_nam(yscjdwj1.getFname());
            attachmentReturns.add(attachmentReturn);
        }
        return attachmentReturns;
    }

    //判断用户对附件是否有操作权限
    @RequestMapping("/getrank")
    public Boolean getrank(String userId, String attachment_id) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = processEngine.getIdentityService();
        TaskService taskService = processEngine.getTaskService();
        String rank = identityService.createGroupQuery().groupMember(userId).singleResult().getType();
        Attachment attachment = taskService.getAttachment(attachment_id);
        String aid = attachment.getId();
        Attachmentlink attachmentlink = new Attachmentlink();
        attachmentlink.setAttachment(aid);
        String upid = attachmentlinkMapper.selectOne(attachmentlink).getUserid();
        String rank1 = identityService.createGroupQuery().groupMember(upid).singleResult().getType();
        int a = Integer.valueOf(rank);
        int b = Integer.valueOf(rank1);
        if (a >= b) {
            return true;
        } else {
            return false;
        }
    }

    //下载附件
    @RequestMapping("/getattachment1")
    public void getattachment1(HttpServletResponse res, String attachment_id) throws UnsupportedEncodingException {
        if(attachment_id!=null&&!attachment_id.equals("")){
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            TaskService taskService = processEngine.getTaskService();
            System.out.println(Time.getNow()+"  attachment_id: "+attachment_id);
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
