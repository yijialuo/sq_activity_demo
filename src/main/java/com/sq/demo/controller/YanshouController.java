package com.sq.demo.controller;

import com.github.pagehelper.PageHelper;

import com.sq.demo.mapper.ContractfileMapper;
import com.sq.demo.mapper.ProjectMapper;
import com.sq.demo.mapper.YanshouMapper;
import com.sq.demo.pojo.Contractfile;
import com.sq.demo.pojo.Project;
import com.sq.demo.pojo.Yanshou;
import com.sq.demo.utils.IdCreate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yijialuo on 2019/1/13.
 */

@RestController
@RequestMapping("/yanshou")
public class YanshouController {

    @Autowired
    YanshouMapper yanshouMapper;
    @Autowired
    ContractfileMapper contractfileMapper ;
    @Autowired
    ProjectMapper projectMapper;

    @RequestMapping("/search")
    public List<Yanshou> search(String ysNo,String projectName,String projectNo,String kgrq,String sjjgrq,String ysrq){
        List<Yanshou> yanshous=yanshouMapper.search(ysNo,kgrq,sjjgrq,ysrq);
        if((projectName==null||projectName.equals(""))&&(projectNo==null||projectNo.equals(""))){//项目名称和项目编号没搜索
            return yanshous;
        }else if((projectName==null||projectName.equals(""))&&(projectNo!=null&&!projectNo.equals(""))){//项目名称不搜索，项目编号搜索
            List<Yanshou> res=new ArrayList<>();
            for(Yanshou yanshou:yanshous){
                Project project=projectMapper.selectByPrimaryKey(yanshou.getProjectid());
                if(project.getProjectNo().contains(projectNo)){
                    res.add(yanshou);
                }
            }
            return res;
        }else if((projectName!=null&&!projectName.equals(""))&&(projectNo==null||projectNo.equals(""))){//项目名称搜索，项目编号不搜索
            List<Yanshou> res=new ArrayList<>();
            for(Yanshou yanshou:yanshous){
                Project project=projectMapper.selectByPrimaryKey(yanshou.getProjectid());
                if(project.getProjectNam().contains(projectName)){
                    res.add(yanshou);
                }
            }
            return res;
        }else {//项目名称，项目编号都搜索
            List<Yanshou> res=new ArrayList<>();
            for (Yanshou yanshou:yanshous){
                Project project=projectMapper.selectByPrimaryKey(yanshou.getProjectid());
                if(project.getProjectNam().contains(projectName)&&project.getProjectNo().contains(projectNo)){
                    res.add(yanshou);
                }
            }
            return res;
        }
    }

    //归档
    @Transactional
    @RequestMapping("/guidang")
    public boolean guidang(String id){
        Yanshou yanshou=new Yanshou();
        yanshou.setId(id);
        yanshou.setGd("1");
        return yanshouMapper.updateByPrimaryKeySelective(yanshou)==1;
    }

    //增加验收单
    @Transactional
    @RequestMapping("/addYanshou")
    public String addYanshou(@RequestBody Yanshou ys){
        ys.setId(IdCreate.id());
        yanshouMapper.insert(ys);
        return ys.getId();
    }

    //修改验收单
    @Transactional
    @RequestMapping("/updateYanshou")
    public boolean updateYanshou(@RequestBody Yanshou ys){
        if(yanshouMapper.updateByPrimaryKeySelective(ys)==1){
            return true;
        }else {
            return false;
        }
    }

    //删除验收单
    @Transactional
    @RequestMapping("/deleteYanshou")
    public boolean deleteYanshou(String yid){
        if(yid!=null&&!yid.equals("")){
            Contractfile contractfile=new Contractfile();
            contractfile.setCid(yid);
            //删文件关联表
            contractfileMapper.delete(contractfile);
            if(yanshouMapper.deleteByPrimaryKey(yid)==1){
                return true;
            }else{
                return false;
            }
        }else {
            return false;
        }

    }

    //查询所有验收单
    @RequestMapping("/getAllYanshou")
    public List<Yanshou> getAllYanshou(int pageNum){
        PageHelper.startPage(pageNum,10);
        return yanshouMapper.selectAll();
    }

    //数据条数
    @RequestMapping("/AllCounts")
    public int AllCounts(){
        return yanshouMapper.AllCounts();
    }

    //验收编号模糊搜索
    @RequestMapping("/selectYSbyNo")
    List<Yanshou> selectYSbyNo(String ysNo){
        return yanshouMapper.yanshouSSbyNo(ysNo);
    }

    //项目id查询
    @RequestMapping("/selectYSbyprojectid")
    List<Yanshou> selectYSbyprojectid(String projectid){
        Yanshou yanshou=new Yanshou();
        yanshou.setProjectid(projectid);
        return yanshouMapper.select(yanshou);
    }
}
