package com.sq.demo.controller;

import com.sq.demo.mapper.JinduMapper;
import com.sq.demo.mapper.ProjectMapper;
import com.sq.demo.mapper.ZhaobiaoMapper;
import com.sq.demo.pojo.Jindu;
import com.sq.demo.pojo.Project;
import com.sq.demo.pojo.Zhaobiao;
import com.sq.demo.utils.IdCreate;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/jindu")
public class JinduController {
    @Autowired
    ProjectMapper projectMapper;
    @Autowired
    JinduMapper jinduMapper;
    @Autowired
    ZhaobiaoMapper zhaobiaoMapper;

    //判断该项目是否可以
    @RequestMapping("/canAddjd")
    public boolean canAddjd(String projectId){
        Zhaobiao zhaobiao=new Zhaobiao();
        zhaobiao.setXmid(projectId);
        zhaobiao=zhaobiaoMapper.selectOne(zhaobiao);
        if(zhaobiao!=null&&zhaobiao.getZbpid()!=null&&!zhaobiao.getZbpid().equals("")){
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            TaskService taskService = processEngine.getTaskService();
            String node = taskService.createTaskQuery().processInstanceId(zhaobiao.getZbpid()).singleResult().getName();
            if(node.equals("定标")||node.equals("招标结束"))
                return true;
            else
                return false;
        }
        return false;
    }

    //拿项目施工状态
    @RequestMapping("/getSgzt")
    public String getSgzt(String projectId){
        Jindu jindu=new Jindu();
        jindu.setProjectId(projectId);
        return jinduMapper.select(jindu).size()==0?"未开工":"进行中";
    }

    //修改进度
    @Transactional
    @RequestMapping("/updataJindu")
    public boolean updataJindu(@RequestBody Jindu jin){
        if(jinduMapper.updateByPrimaryKeySelective(jin)==1)
            return true;
        return false;
    }

    //完成项目
    @Transactional
    @RequestMapping("/projectFinish")
    public String projectFinish(String pid){
        String dte= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        Project project = new Project();
        project.setId(pid);
        Project p1 = projectMapper.selectOne(project);
        p1.setFinishDte(dte);
        if(projectMapper.updateByPrimaryKeySelective(p1)==1){
            return dte;
        }else{
            return null;
        }
    }

    //添加节点
    @Transactional
    @RequestMapping("/addJindu")
    public boolean addJindu(@RequestBody Jindu jin){
        Jindu jindu = new Jindu();
        jindu.setId(IdCreate.id());
        jindu.setFinishDate(jin.getFinishDate());
        jindu.setIsFinish(jin.getIsFinish());
        jindu.setPoint(jin.getPoint());
        jindu.setProjectId(jin.getProjectId());
        if(jinduMapper.insert(jindu)==1){
            return true;
        }else {
            return false;
        }
    }

    //删除进度节点
    @Transactional
    @RequestMapping("/deleteJindu")
    public boolean deleteJindu(String jid){
        if(jinduMapper.deleteByPrimaryKey(jid)==1){
            return true;
        }else {
            return false;
        }
    }

    //查询项目进度
    @RequestMapping("/cxJindu")
    public List<Jindu> cxJindu(String pid){
        List<Jindu> jindus = jinduMapper.pidcx(pid);
        return jindus;
    }

    //判断项目是否完成
    @RequestMapping("/isfinish")
    public boolean isfinish(String pid){
        Project project = new Project();
        project.setId(pid);
        String finishdte = projectMapper.selectOne(project).getFinishDte();
        if(finishdte==null||finishdte.equals("")){
            return false;
        }else {
            return true;
        }
    }

    //获取开工完工时间
    @RequestMapping("/getSFdte")
    public String getSFdte(String pid){
        Project project = new Project();
        project.setId(pid);
        String finishDte = projectMapper.selectOne(project).getFinishDte();
        Jindu jindu = jinduMapper.selectfirst(pid);
        String starDte;
        if(jindu==null){
            starDte=null;
        }else {
            starDte = jindu.getFinishDate();
        }
        StringBuffer da = new StringBuffer();
        da.append(starDte);
        da.append("/");
        da.append(finishDte);
        return da.toString();
    }
}
