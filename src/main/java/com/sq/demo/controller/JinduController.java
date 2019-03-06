package com.sq.demo.controller;

import com.sq.demo.mapper.JinduMapper;
import com.sq.demo.mapper.ProjectMapper;
import com.sq.demo.pojo.Jindu;
import com.sq.demo.pojo.Payable;
import com.sq.demo.pojo.Project;
import com.sq.demo.utils.IdCreate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
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

    //修改进度
    @RequestMapping("/updataJindu")
    public boolean updataJindu(@RequestBody Jindu jin){
        if(jinduMapper.updateByPrimaryKeySelective(jin)==1)
            return true;
        return false;
    }

    //完成项目
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
        if(finishdte==null||finishdte==""){
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
        String starDte = null;
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
