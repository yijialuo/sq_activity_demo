package com.sq.demo.controller;

import com.sq.demo.Entity.Return_Comments;
import com.sq.demo.Entity.Sgjdb2;
import com.sq.demo.mapper.ProjectMapper;
import com.sq.demo.mapper.SgjdbMapper;
import com.sq.demo.pojo.Project;
import com.sq.demo.pojo.Sgjdb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/sgjdb")
public class SgjdbController {
    @Autowired
    SgjdbMapper sgjdbMapper;
    @Autowired
    ProjectController projectController;
    @Autowired
    ProjectMapper projectMapper;

    public List<Sgjdb> tcsj(List<Sgjdb> res){
        for (int i = 0; i < res.size(); i++) {
            //列出所有审核意见
            Project project=projectMapper.selectByPrimaryKey(res.get(i).getId());
            List<Return_Comments> return_comments = projectController.projecttocomment(project.getPid());
            for (Return_Comments return_comment : return_comments) {
                if (return_comment.getUsernam().equals("元少麟")) {
                    res.get(i).setJsbjlsj(return_comment.getTime());
                    continue;
                }
            }
            if (res.get(i).getJsbjbr() == null || res.get(i).getJsbjlsj().equals(res.get(i).getJsbjbr())) {//没找到时间，设为空
                res.get(i).setJsbjlsj("");
            }
            res.get(i).setId(String.valueOf(i + 1));
        }
        return res;
    }

    @RequestMapping("/getAllSgjdb")
    public List<Sgjdb> getAllSgjdb() {
        List<Sgjdb> res = sgjdbMapper.selectAll();
        return tcsj(res);
    }

    //实施进度报表搜索
    @RequestMapping("/select")
    public List<Sgjdb> select(@RequestBody Sgjdb2 sgjdb2){
        List<Sgjdb> res= tcsj(sgjdbMapper.search(sgjdb2));
        if(sgjdb2.getJsbjlsj()==null||sgjdb2.getJsbjlsj().equals("")){
            return res;
        }else {
            List<Sgjdb> res2=new ArrayList<>();
            for(Sgjdb sgjdb1:res){
                if(sgjdb1.getJsbjlsj().contains(sgjdb2.getJsbjlsj())){
                    res2.add(sgjdb1);
                }
            }
            return res2;
        }
    }
}
