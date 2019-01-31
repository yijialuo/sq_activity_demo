package com.sq.demo.controller;

import com.sq.demo.Entity.AttachmentReturn;
import com.sq.demo.Entity.Project_Receive;
import com.sq.demo.Entity.Return_Comments;
import com.sq.demo.Entity.Sqb;
import com.sq.demo.mapper.AttachmentlinkMapper;
import com.sq.demo.mapper.DepRankMapper;
import com.sq.demo.mapper.DepartmentMapper;
import com.sq.demo.mapper.ProjectMapper;
import com.sq.demo.pojo.Attachmentlink;
import com.sq.demo.pojo.DepRank;
import com.sq.demo.pojo.Department;
import com.sq.demo.pojo.Project;
import com.sq.demo.utils.IdCreate;
import com.sun.deploy.util.GeneralUtil;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by yijialuo on 2019/1/13.
 */

@RestController
@RequestMapping("/Attachment")
public class AttachmentController {
    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
    IdentityService identityService = processEngine.getIdentityService();

    @Autowired
    ProjectMapper projectMapper;
    @Autowired
    DepartmentMapper departmentMapper;
    @Autowired
    AttachmentlinkMapper  attachmentlinkMapper;
    @Autowired
    DepRankMapper depRankMapper;
    @Autowired
    private HttpServletResponse response;



    //删除附件
    @RequestMapping("/deletAttachment")
    public boolean deletAttachment(String userId,String attachment_id){
        if(getrank(userId,attachment_id)){
            TaskService taskService = processEngine.getTaskService();
            taskService.deleteAttachment(attachment_id);
            //删关联表
            Attachmentlink attachmentlink=new Attachmentlink();
            attachmentlink.setAttachment(attachment_id);
            attachmentlinkMapper.delete(attachmentlink);
            return true;
        }else{
            return false;
        }
    }

    //返回附件信息
   @RequestMapping("/getattachment")
    public List<AttachmentReturn> getattachment(String pid){
       TaskService taskService = processEngine.getTaskService();
       List<AttachmentReturn> attachmentReturns = new ArrayList<>();
       List<Attachment> atta = taskService.getProcessInstanceAttachments(pid);
       for(Attachment at : atta){
           AttachmentReturn attachmentReturn = new AttachmentReturn();
           attachmentReturn.setAttachment_id(at.getId());
           attachmentReturn.setAttachment_nam(at.getName());
           attachmentReturns.add(attachmentReturn);
       }
       return attachmentReturns;
   }



   //判断用户对附件是否有操作权限
   @RequestMapping("/getrank")
    public Boolean getrank(String userId,String attachment_id){
//       DepRank depRank = new DepRank();
//       depRank.setDepartmentNam(dnam);
//       String rank = depRankMapper.selectOne(depRank).getRank();
       TaskService taskService=processEngine.getTaskService();
       String rank = identityService.createGroupQuery().groupMember(userId).singleResult().getType();
       Attachment attachment = taskService.getAttachment(attachment_id);
       String aid = attachment.getId();
       Attachmentlink attachmentlink = new Attachmentlink();
       attachmentlink.setAttachment(aid);
       String upid = attachmentlinkMapper.selectOne(attachmentlink).getUserid();
       String rank1 = identityService.createGroupQuery().groupMember(upid).singleResult().getType();
       int a = Integer.valueOf(rank);
       int b = Integer.valueOf(rank1);
       if(a>=b){
           return true;
       }else{
           return false;
       }
   }


   //下载附件
   @RequestMapping("/getattachment1")
   public void getattachment1(HttpServletResponse res, String attachment_id) {

            TaskService taskService=processEngine.getTaskService();
            String fileName=taskService.getAttachment(attachment_id).getName();
            res.setHeader("content-type", "application/octet-stream");
            res.setContentType("application/octet-stream");
            res.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            byte[] buff = new byte[1024];
            BufferedInputStream bis = null;
            OutputStream os = null;
            try {
                os = res.getOutputStream();
                bis=new BufferedInputStream(processEngine.getTaskService().getAttachmentContent(attachment_id));
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
