package com.sq.demo.controller;

import com.github.pagehelper.PageHelper;
import com.sq.demo.mapper.ProjectMapper;
import com.sq.demo.mapper.XxmcbMapper;
import com.sq.demo.mapper.XxmglMapper;
import com.sq.demo.pojo.Project;
import com.sq.demo.pojo.Xxmcb;
import com.sq.demo.pojo.Xxmgl;
import com.sq.demo.utils.IdCreate;
import com.sq.demo.utils.Time;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/xxmgl")
public class XxmglController {

    @Autowired
    XxmglMapper xxmglMapper;
    @Autowired
    XxmcbMapper xxmcbMapper;
    @Autowired
    ProjectMapper projectMapper;


    @RequestMapping("/getProjects")
    public List<Xxmgl> getProjects(String userName) {
        Project project = new Project();
        project.setBider(userName);
        project.setDepAuditOpinion("股份项目");
        List<Project> projects = projectMapper.select(project);
        List<Xxmgl> res = new ArrayList<>();
        for (Project project1 : projects) {
            Xxmgl xxmgl = new Xxmgl();
            xxmgl.setSqr(project1.getProposer());
            xxmgl.setXmbh(project1.getProjectNo());
            xxmgl.setXmmc(project1.getProjectNam());
            xxmgl.setLxbm(project1.getDeclarationDep());
            xxmgl.setY1(project1.getId());
            res.add(xxmgl);
        }
        //过滤已经新建了的
        List<String> xmids=xxmglMapper.getAllXmid();
        if(xmids.size()==0)
            return res;
        List<Xxmgl> res2=new ArrayList<>();
        for(Xxmgl xxmgl:res){
            if(!xmids.contains(xxmgl.getY1())){
                res2.add(xxmgl);
            }
        }
        return res2;
    }

    @Transactional
    @RequestMapping("/insert")
    public boolean insert(String xmbh, String xmmc, String lxbm, String sqr, String y1,String y2) {
        try {
            Xxmgl xxmgl = new Xxmgl();
            xxmgl.setId(IdCreate.id());
            xxmgl.setCjsj(Time.getNow());
            xxmgl.setXmbh(xmbh);
            xxmgl.setXmmc(xmmc);
            xxmgl.setLxbm(lxbm);
            xxmgl.setSqr(sqr);
            xxmgl.setY1(y1);
            xxmgl.setY2(y2);
            xxmglMapper.insert(xxmgl);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    @RequestMapping("/delete")
    public boolean delete(String id) {
        try {
            //删主表
            xxmglMapper.deleteByPrimaryKey(id);
            //删从表
            Xxmcb xxmcb = new Xxmcb();
            xxmcb.setXxmid(id);
            xxmcbMapper.delete(xxmcb);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    @RequestMapping("/updata")
    public boolean update(String id, String xmbh, String xmmc, String lxbm, String sqr, String y1) {
        Xxmgl xxmgl = new Xxmgl();
        xxmgl.setId(id);
        if (xmbh == null)
            xxmgl.setXmbh("");
        else
            xxmgl.setXmbh(xmbh);
        if (xmmc != null && !xmmc.equals(""))
            xxmgl.setXmmc(xmmc);
        if (lxbm != null && !lxbm.equals(""))
            xxmgl.setLxbm(lxbm);
        if (sqr != null && !sqr.equals(""))
            xxmgl.setSqr(sqr);
        if (y1 != null && !y1.equals(""))
            xxmgl.setY1(y1);
        return xxmglMapper.updateByPrimaryKeySelective(xxmgl) == 1 ? true : false;
    }

    @RequestMapping("/select")
    public List<Xxmgl> select(String xmbh, String xmmc, String lxbm, String sqr) {
        Xxmgl xxmgl = new Xxmgl();
        if (xmbh != null && !xmbh.equals(""))
            xxmgl.setXmbh(xmbh);
        if (xmmc != null && !xmmc.equals(""))
            xxmgl.setXmmc(xmmc);
        if (lxbm != null && !lxbm.equals(""))
            xxmgl.setLxbm(lxbm);
        if (sqr != null && !sqr.equals(""))
            xxmgl.setSqr(sqr);
        return xxmglMapper.select(xxmgl);
    }

    @RequestMapping("/selectAll")
    public List<Xxmgl> selectAll(HttpServletResponse response, int pageNum) {
        response.setHeader("allcount", "" + xxmglMapper.selectCount(null));
        PageHelper.startPage(pageNum, 10);
        return xxmglMapper.selectAll();
    }
}
