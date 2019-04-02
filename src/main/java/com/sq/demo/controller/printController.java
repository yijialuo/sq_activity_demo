package com.sq.demo.controller;

import com.sq.demo.Entity.Return_Comments;
import com.sq.demo.mapper.DepartmentMapper;
import com.sq.demo.mapper.ProjectMapper;

import com.sq.demo.pojo.Department;
import com.sq.demo.pojo.Project;


import com.sq.demo.utils.FileUtil;
import com.sq.demo.utils.WordUtils;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/print")
public class printController {
    @Autowired
    ProjectMapper projectMapper;
    @Autowired
    DepartmentMapper departmentMapper;

    public String dptnTojl(String dptn){
        Department department=new Department();
        department.setdNam(dptn);
        department=departmentMapper.selectOne(department);
        ProcessEngine engine= ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService=engine.getIdentityService();
        List<User> users=identityService.createUserQuery().list();
        for(User user:users){
            //用户是这个部门
            if(identityService.getUserInfo(user.getId(),"departmentId").equals(department.getId())){
                //用户是经理
                if(identityService.createGroupQuery().groupMember(user.getId()).singleResult().getId().equals("jl")){
                    return user.getFirstName();
                }
            }
        }
        return "";
    }


//    @RequestMapping("export")
//    public void export(String id,HttpServletRequest request, HttpServletResponse response){
//        Project project=projectMapper.selectByPrimaryKey(id);
//        Map<String, Object> mmap = new HashMap<String, Object>();
//        mmap.put("declarationDep", project.getDeclarationDep());
//        mmap.put("projectNo", project.getProjectNo()==null?"   ":project.getProjectNo());
//        mmap.put("projectNam", project.getProjectNam());
//        mmap.put("projectType",project.getProjectType());
//        mmap.put("investmentEstimate", project.getInvestmentEstimate()==null?"  ":project.getInvestmentEstimate());
//        mmap.put("personInCharge", project.getPersonInCharge()==null?" ":project.getPersonInCharge());
//        mmap.put("establishReason",project.getEstablishReason()==null?" ":project.getEstablishReason());
//        mmap.put("scale", project.getScale()==null?" ":project.getScale());
//        mmap.put("illustration", project.getIllustration()==null?" ":project.getIllustration());
//        //拿到该部门的部门经理名字
//        String jl=dptnTojl(project.getDeclarationDep());
//        ProjectController projectController=new ProjectController();
//        //部门所有审核意见
//        List<Return_Comments> return_comments = projectController.projecttocomment(project.getPid());
//        System.out.println(return_comments.size());
//        for(Return_Comments return_comment:return_comments){
//            if(return_comment.getUsernam().equals(jl)){
//                mmap.put("bmshyj",return_comment.getComment());
//                mmap.put("jl",jl);
//                mmap.put("bmspsj",return_comment.getTime().substring(0,10));
//                break;
//            }
//            if(return_comment.getUsernam().equals("元少麟")){
//                mmap.put("sjbjlyj",return_comment.getComment());
//                mmap.put("jsbjl","元少麟");
//                mmap.put("jsbspsj",return_comment.getTime().substring(0,10));
//                break;
//            }
//        }
//        if(mmap.get("bmshyj")==null){
//            mmap.put("bmshyj"," ");
//            mmap.put("jl"," ");
//            mmap.put("bmspsj"," ");
//        }
//        if(mmap.get("sjbjlyj")==null){
//            mmap.put("sjbjlyj"," ");
//            mmap.put("jsbjl"," ");
//            mmap.put("jsbspsj"," ");
//        }
//        //FileUtil.exportWord("word/xmlxb.doc","D:/test",project.getProjectNam()+"立项申请表.docx",mmap,request,response);
//    }

//    public void download(String id, HttpServletResponse response, HttpServletRequest request)
//    {
//        Project project=projectMapper.selectByPrimaryKey(id);
//        Map<String, Object> mmap = new HashMap<String, Object>();
//        mmap.put("declarationDep", project.getDeclarationDep());
//        mmap.put("projectNo", project.getProjectNo()==null?" ":project.getProjectNo());
//        mmap.put("projectNam", project.getProjectNam());
//        mmap.put("investmentEstimate", project.getInvestmentEstimate()==null?" ":project.getInvestmentEstimate());
//        mmap.put("personInCharge", project.getPersonInCharge()==null?" ":project.getPersonInCharge());
//        mmap.put("establishReason",project.getEstablishReason()==null?" ":project.getEstablishReason());
//        mmap.put("scale", project.getScale()==null?" ":project.getScale());
//        mmap.put("illustration", project.getIllustration()==null?" ":project.getIllustration());
//
//        //拿到该部门的部门经理名字
//        String jl=departmentController.dptnTojl(project.getDeclarationDep());
//        ProjectController projectController=new ProjectController();
//        //部门所有审核意见
//        List<Return_Comments> return_comments = projectController.projecttocomment(project.getPid());
//        for(Return_Comments return_comment:return_comments){
//            if(return_comment.getUsernam().equals(jl)){
//                mmap.put("bmshyj",return_comment.getComment());
//                mmap.put("jl",jl);
//                mmap.put("bmspsj",return_comment.getTime().substring(0,10));
//                break;
//            }
//            if(return_comment.getUsernam().equals("元少麟")){
//                mmap.put("sjbjlyj",return_comment.getComment());
//                mmap.put("jsbjl","元少麟");
//                mmap.put("jsbspsj",return_comment.getTime().substring(0,10));
//                break;
//            }
//        }
//        UUID uuid = UUID.randomUUID();
//        try {
//            //生成
//            File file = new File(Global.getWordPath(), uuid + "_sqb.doc");
//            if (!file.createNewFile()) {
//                System.out.println("生成失败");
//            }
//        } catch (Exception e) {
//            System.out.println("异常");
//        }
//        String xmlPath = "xmlxb.xml";
//        int generatedWordResult = new DynamicallyGeneratedWordService().genWordFile(Global.getWordPath() + uuid + "_tjb.doc", xmlPath, mmap, "xmlxb.xml");
//        if (generatedWordResult == 1)
//        {
//            try {
//                String filePath = Global.getWordPath() + uuid + "_tjb.doc";
//                response.setCharacterEncoding("utf-8");
//                response.setContentType("multipart/form-data");
//                response.setHeader("Content-Disposition",
//                        "attachment;fileName=" + setFileDownloadHeader(request, "推荐表.doc"));
//                FileUtils.writeBytes(filePath, response.getOutputStream());
//            } catch (Exception e) {
//                System.out.println("下载文件失败"+e);
//            }
//        }
//    }
//
//    //设置下载头文件
//    public String setFileDownloadHeader(HttpServletRequest request, String fileName) throws UnsupportedEncodingException {
//        final String agent = request.getHeader("USER-AGENT");
//        String filename = fileName;
//        if (agent.contains("MSIE")) {
//            // IE浏览器
//            filename = URLEncoder.encode(filename, "utf-8");
//            filename = filename.replace("+", " ");
//        } else if (agent.contains("Firefox")) {
//            // 火狐浏览器
//            filename = new String(fileName.getBytes(), "ISO8859-1");
//        } else if (agent.contains("Chrome")) {
//            // google浏览器
//            filename = URLEncoder.encode(filename, "utf-8");
//        } else {
//            // 其它浏览器
//            filename = URLEncoder.encode(filename, "utf-8");
//        }
//        return filename;
//    }

}
