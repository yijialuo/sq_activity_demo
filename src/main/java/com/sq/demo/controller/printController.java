package com.sq.demo.controller;

import cn.afterturn.easypoi.word.WordExportUtil;
import com.sq.demo.Entity.Return_Comments;
import com.sq.demo.mapper.*;

import com.sq.demo.pojo.*;


import com.sq.demo.utils.Doc2Pdf;
import com.sq.demo.utils.FileUtil;
import com.sq.demo.utils.NumberToCn;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.identity.User;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//文件模板下载controller
@RestController
@RequestMapping("/print")
public class printController {
    @Autowired
    ProjectMapper projectMapper;
    @Autowired
    DepartmentMapper departmentMapper;
    @Autowired
    ContractMapper contractMapper;
    @Autowired
    PayableMapper payableMapper;
    @Autowired
    ContractfileMapper contractfileMapper;


    @Value("classpath:license.xml")
    private org.springframework.core.io.Resource licensepath;

    //部门名字找经理
    public String dptnTojl(String dptn) {
        Department department = new Department();
        department.setdNam(dptn);
        department = departmentMapper.selectOne(department);
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = engine.getIdentityService();
        List<User> users = identityService.createUserQuery().list();
        for (User user : users) {
            //用户是这个部门
            if (identityService.getUserInfo(user.getId(), "departmentId").equals(department.getId())) {
                //用户是经理
                if (identityService.createGroupQuery().groupMember(user.getId()).singleResult().getId().equals("jl")) {
                    return user.getFirstName();
                }
            }
        }
        return "";
    }

    //合同id找项目名字
    public String cidToPnam(String cid) {
        Contract contract = new Contract();
        contract.setId(cid);
        String pid = contractMapper.selectOne(contract).getProjectId();
        Project project = new Project();
        project.setId(pid);
        String pnam = projectMapper.selectOne(project).getProjectNam();
        return pnam;
    }

    //合同id拿未已付
    public BigDecimal cidToYf(String cid) {
        Contract contract = new Contract();
        contract.setId(cid);
        String pid = contractMapper.selectOne(contract).getProjectId();
        BigDecimal zje = contractMapper.selectOne(contract).getPrice();
        Payable payable = payableMapper.selectlatest(pid);
        if (payable == null) {
            return new BigDecimal(0);
        }
        BigDecimal wf = payable.getWzf();
        BigDecimal yf = zje.subtract(wf);
        return yf;
    }

    //合同id拿未付
    public BigDecimal cidToWf(String cid) {
        Contract contract = new Contract();
        contract.setId(cid);
        String pid = contractMapper.selectOne(contract).getProjectId();
        BigDecimal zje = contractMapper.selectOne(contract).getPrice();
        Payable payable = payableMapper.selectlatest(pid);
        if (payable == null) {
            return zje;
        }
        BigDecimal wf = payable.getWzf();
        return wf;
    }

    //下载支付审批表
    @RequestMapping("/zfspd")
    public void zfspd(String id, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Payable payable = payableMapper.selectByPrimaryKey(id);
        Map<String, Object> map = new HashMap<>();
        map.put("jbbm", payable.getJbbm());
        map.put("jbr", payable.getJbr());
        map.put("yszmr", payable.getYszmr());
        Contractfile contractfile = new Contractfile();
        contractfile.setCid(id);
        map.put("fjzs", contractfileMapper.select(contractfile).size());
        map.put("invoiceNo", payable.getInvoiceNo());
        Contract contract = contractMapper.selectByPrimaryKey(payable.getContractId());
        map.put("skdw", contract.getDfdsr());
        map.put("yhzh", payable.getAccount());
        map.put("khyh", payable.getBank());
        String zfxm = cidToPnam(contract.getId());
        map.put("zfxm", zfxm);
        map.put("hth", contract.getContractNo());
        map.put("htrq", contract.getRq());
        map.put("htzje", contract.getPrice());
        map.put("yzfje", cidToYf(contract.getId()).subtract(payable.getBqyf()));
        map.put("wzf", cidToWf(contract.getId()).add(payable.getBqyf()));
        map.put("bqyf", payable.getBqyf().toString());
        //FileUtil.exportWord("word/zfspd.docx", zfxm + "支付审批单.docx", map, request, response);
        //FileUtil.exportWord("word/htpsb.docx", project.getProjectNam() + "合同评审表.docx", map, request, response);
        String OS = System.getProperty("os.name").toLowerCase();
        //生成word的路径
        String lj = "";
        if (OS.indexOf("linux") >= 0)
            lj = "/upload/";
        else if (OS.indexOf("windows") >= 0)
            lj = "D:\\upload\\";
        XWPFDocument doc = WordExportUtil.exportWord07("word/zfspd.docx", map);
        String fileName = zfxm + "支付审批单.docx";
        FileOutputStream fos = new FileOutputStream(lj + fileName);
        doc.write(fos);
        FileInputStream fileInputStream = new FileInputStream(lj + fileName);
        com.aspose.words.Document doc1 = new com.aspose.words.Document(fileInputStream);
        fileInputStream.close();
        fos.close();
        doc.close();
        //刪除文件
        File file = new File(lj + fileName);
        if (file.exists() && file.isFile())
            file.delete();
        fileName = fileName.split(".docx")[0] + ".pdf";
        String userAgent = request.getHeader("user-agent").toLowerCase();
        if (userAgent.contains("msie") || userAgent.contains("like gecko")) {
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } else {
            fileName = new String(fileName.getBytes("utf-8"), "ISO-8859-1");
        }
        // word转pdf的工具类
        Doc2Pdf.doc2pdf(response, fileName, doc1);
    }

    //下载合同审批表
    @RequestMapping("/ht")
    public void ht(String id, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Contract contract = contractMapper.selectByPrimaryKey(id);
        Map<String, Object> map = new HashMap<>();
        Project project = projectMapper.selectByPrimaryKey(contract.getProjectId());
        String projectname = project.getProjectNam();
        map.put("projectName", projectname);
        map.put("contractNo", contract.getContractNo() == null ? " " : contract.getContractNo());
        map.put("dfdsr", contract.getDfdsr() == null ? " " : contract.getDfdsr());
        map.put("tzwh", contract.getTzwh() == null ? " " : contract.getTzwh());
        map.put("price", contract.getPrice() == null ? " " : contract.getPrice());
        map.put("dx", NumberToCn.number2CNMontrayUnit(contract.getPrice()));
        map.put("jbr", contract.getJbr() == null ? " " : contract.getJbr());
        ProjectController projectController = new ProjectController();
        List<Return_Comments> return_comments = projectController.projecttocomment(contract.getDwyj());
        for (Return_Comments return_comment : return_comments) {
            if (return_comment.getUsernam().equals("元少麟") && map.get("zbdwyj") == null) {
                map.put("zbdwyj", return_comment.getComment());
                String sj = return_comment.getTime().substring(0, 4) + "年" + return_comment.getTime().substring(5, 7) + "月" + return_comment.getTime().substring(8, 10) + "日";
                map.put("sj", sj);
                map.put("jsbjl","元少麟");
                break;
            }
        }
        if (map.get("zbdwyj") == null || map.get("zbdwyj").equals("")) {
            map.put("zbdwyj", " ");
            map.put("sj", "年    月    日");
        }

        //FileUtil.exportWord("word/htpsb.docx", project.getProjectNam() + "合同评审表.docx", map, request, response);
        String OS = System.getProperty("os.name").toLowerCase();
        //生成word的路径
        String lj = "";
        if (OS.indexOf("linux") >= 0)
            lj = "/upload/";
        else if (OS.indexOf("windows") >= 0)
            lj = "D:\\upload\\";
        XWPFDocument doc = WordExportUtil.exportWord07("word/htpsb.docx", map);
        String fileName = project.getProjectNam() + "合同评审表.docx";
        FileOutputStream fos = new FileOutputStream(lj + fileName);
        doc.write(fos);
        FileInputStream fileInputStream = new FileInputStream(lj + fileName);
        com.aspose.words.Document doc1 = new com.aspose.words.Document(fileInputStream);
        fileInputStream.close();
        fos.close();
        doc.close();
        //刪除文件
        File file = new File(lj + fileName);
        if (file.exists() && file.isFile())
            file.delete();
        fileName = fileName.split(".docx")[0] + ".pdf";
        String userAgent = request.getHeader("user-agent").toLowerCase();
        if (userAgent.contains("msie") || userAgent.contains("like gecko")) {
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } else {
            fileName = new String(fileName.getBytes("utf-8"), "ISO-8859-1");
        }
        // word转pdf的工具类
        Doc2Pdf.doc2pdf(response, fileName, doc1);
    }

    //下载申请表
    @RequestMapping("/sqb")
    public void export(String id, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Project project = projectMapper.selectByPrimaryKey(id);
        Map<String, Object> mmap = new HashMap<String, Object>();
        mmap.put("declarationDep", project.getDeclarationDep());
        mmap.put("projectNo", project.getProjectNo() == null || project.getProjectNo().equals("") ? " " : project.getProjectNo());
        mmap.put("projectNam", project.getProjectNam());
        mmap.put("projectType", project.getProjectType());
        mmap.put("investmentEstimate", project.getInvestmentEstimate() == null ? "  " : project.getInvestmentEstimate());
        mmap.put("personInCharge", project.getPersonInCharge() == null ? " " : project.getPersonInCharge());
        mmap.put("establishReason", project.getEstablishReason() == null ? " " : project.getEstablishReason());
        mmap.put("scale", project.getScale() == null ? " " : project.getScale());
        mmap.put("illustration", project.getIllustration() == null ? " " : project.getIllustration());
        //拿到该部门的部门经理名字
        String jl = dptnTojl(project.getDeclarationDep());
        ProjectController projectController = new ProjectController();
        //列出所有审核意见
        List<Return_Comments> return_comments = projectController.projecttocomment(project.getPid());
        for (Return_Comments return_comment : return_comments) {
            if (return_comment.getUsernam().equals(jl) && mmap.get("jl") == null) {
                mmap.put("bmshyj", return_comment.getComment());
                mmap.put("jl", jl);
                mmap.put("bmspsj", return_comment.getTime().substring(0, 10));
                continue;
            }
            if (return_comment.getUsernam().equals("元少麟") && mmap.get("jsbjl") == null) {
                mmap.put("sjbjlyj", return_comment.getComment());
                mmap.put("jsbjl", "元少麟");
                mmap.put("jsbspsj", return_comment.getTime().substring(0, 10));
                continue;
            }
        }
        if (mmap.get("bmshyj") == null) {
            mmap.put("bmshyj", " ");
            mmap.put("jl", " ");
            mmap.put("bmspsj", " ");
        }
        if (mmap.get("sjbjlyj") == null) {
            mmap.put("sjbjlyj", " ");
            mmap.put("jsbjl", " ");
            mmap.put("jsbspsj", " ");
        }

        String OS = System.getProperty("os.name").toLowerCase();
        //生成word的路径
        String lj = "";
        if (OS.indexOf("linux") >= 0)
            lj = "/upload/";
        else if (OS.indexOf("windows") >= 0)
            lj = "D:\\upload\\";
        XWPFDocument doc = WordExportUtil.exportWord07("word/xmlxb.docx", mmap);
        String fileName = project.getProjectNam() + "立项申请表.docx";
        FileOutputStream fos = new FileOutputStream(lj + fileName);
        doc.write(fos);
        FileInputStream fileInputStream = new FileInputStream(lj + fileName);
        com.aspose.words.Document doc1 = new com.aspose.words.Document(fileInputStream);
        fileInputStream.close();
        fos.close();
        doc.close();
        //刪除文件
        File file = new File(lj + fileName);
        if (file.exists() && file.isFile())
            file.delete();
        fileName = fileName.split(".docx")[0] + ".pdf";
        String userAgent = request.getHeader("user-agent").toLowerCase();
        if (userAgent.contains("msie") || userAgent.contains("like gecko")) {
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } else {
            fileName = new String(fileName.getBytes("utf-8"), "ISO-8859-1");
        }
        // word转pdf的工具类
        Doc2Pdf.doc2pdf(response, fileName, doc1);
    }

}
