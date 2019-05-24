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

    public List<Sgjdb> tcsj(List<Sgjdb> res) {
        for (int i = 0; i < res.size(); i++) {
            //列出所有审核意见
            Project project = projectMapper.selectByPrimaryKey(res.get(i).getId());
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
        }
        int i=1;
        for(Sgjdb sgjdb:res){
            sgjdb.setId(i+++"");
        }
        return res;
    }

    @RequestMapping("/getAllSgjdb")
    public List<Sgjdb> getAllSgjdb() {
        List<Sgjdb> res = sgjdbMapper.selectAll();
        return tcsj(res);
    }


    public void Tcjssj(Sgjdb2 sgjdb2) {
        if (!sgjdb2.getKslhsj().equals("") && sgjdb2.getJslhsj().equals("")) {
            sgjdb2.setJslhsj("9999-99-99");
        }
        if (!sgjdb2.getKszjhsj().equals("") && sgjdb2.getJszjhsj().equals("")) {
            sgjdb2.setJszjhsj("9999-99-99");
        }
        if (!sgjdb2.getKsdbsj().equals("") && sgjdb2.getJsdbsj().equals("")) {
             sgjdb2.setJsdbsj("9999-99-99");
        }
        if (!sgjdb2.getKshttjpssj().equals("") && sgjdb2.getJshttjpssj().equals("")) {
             sgjdb2.setJshttjpssj("9999-99-99");
        }
        if (!sgjdb2.getKshtqdsj().equals("") && sgjdb2.getJshtqdsj().equals("")) {
            sgjdb2.setJshtqdsj("9999-99-99");
        }
        if (!sgjdb2.getKskgsj().equals("") && sgjdb2.getJskgsj().equals("")) {
            sgjdb2.setJskgsj("9999-99-99");
        }
        if (!sgjdb2.getKsysrq().equals("") && sgjdb2.getJsysrq().equals("")) {
            sgjdb2.setJsysrq("9999-99-99");
        }
        if (!sgjdb2.getKswcjsjs().equals("") && sgjdb2.getJswcjsjs().equals("")) {
            sgjdb2.setJswcjsjs("9999-99-99");
        }
    }

    //实施进度报表搜索
    @RequestMapping("/select")
    public List<Sgjdb> select(@RequestBody Sgjdb2 sgjdb2) {
        Tcjssj(sgjdb2);
        List<Sgjdb> res = tcsj(sgjdbMapper.search(sgjdb2));
        //过滤技术部经理时间查询条件
        if ((sgjdb2.getKsjsbjlsj() != null && !sgjdb2.getKsjsbjlsj().equals("")) || (sgjdb2.getJsjsbjlsj() != null && !sgjdb2.getJsjsbjlsj().equals(""))) {
            if (!sgjdb2.getKsjsbjlsj().equals("") && sgjdb2.getJsjsbjlsj().equals(""))
                sgjdb2.setJsjsbjlsj("9999-99-99 00:00:00");

            for (int i = 0; i < res.size(); i++) {
                if (!(res.get(i).getJsbjlsj().compareTo(sgjdb2.getKsjsbjlsj()) >= 0 && res.get(i).getJsbjlsj().compareTo(sgjdb2.getJsjsbjlsj()) <= 0)) {
                    res.remove(i);
                    i--;
                }
            }
        }
        return res;
    }

//    public List<Sgjdb> select(Sgjdb2 sgjdb2) {
//        Tcjssj(sgjdb2);
//        List<Sgjdb> res = tcsj(sgjdbMapper.search(sgjdb2));
//        //过滤技术部经理时间查询条件
//        if ((sgjdb2.getKsjsbjlsj() != null && !sgjdb2.getKsjsbjlsj().equals("")) || (sgjdb2.getJsjsbjlsj() != null && !sgjdb2.getJsjsbjlsj().equals(""))) {
//            if (!sgjdb2.getKsjsbjlsj().equals("") && sgjdb2.getJsjsbjlsj().equals(""))
//                sgjdb2.setJsjsbjlsj("9999-99-99");
//            for (int i = 0; i < res.size(); i++) {
//                if (!(res.get(i).getJsbjlsj().compareTo(sgjdb2.getKsjsbjlsj()) >= 0 && res.get(i).getJsbjlsj().compareTo(sgjdb2.getJsjsbjlsj()) <= 0)) {
//                    res.remove(i);
//                    i--;
//                }
//            }
//        }
//        return res;
//    }
}
