package com.sq.demo.mapper;


import com.sq.demo.pojo.Project;
import com.sq.demo.utils.MyMapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;
import java.util.Map;


public interface ProjectMapper extends MyMapper<Project> {
    //搜索进行中项目
    @Select("SELECT * FROM project p WHERE p.DECLARATION_DEP=#{s} and p.ID  in (  SELECT DISTINCT project_id FROM  jindu) and( p.FINISH_DTE is null or p.FINISH_DTE='')")
    List<Project> zjssjxzxm(String s);

    //搜索未开工项目
    @Select("SELECT * FROM project p WHERE p.DECLARATION_DEP=#{s} and p.ID  not in (  SELECT DISTINCT project_id FROM  jindu)")
    List<Project> zjsswkgxm(String s);

    //搜索已完工项目
    @Select("SELECT * FROM project  WHERE DECLARATION_DEP=#{s} and FINISH_DTE is NOT null and FINISH_DTE!=''")
    List<Project> zjssywgxm(String s);

    //搜索进行中项目
    @Select("SELECT * FROM project p WHERE  p.ID  in (  SELECT DISTINCT project_id FROM  jindu) and( p.FINISH_DTE is null or p.FINISH_DTE='')")
    List<Project> ssjxzxm();

    //搜索未开工项目
    @Select("SELECT * FROM project p WHERE  p.ID  not in (  SELECT DISTINCT project_id FROM  jindu)")
    List<Project> sswkgxm();

    //搜索已完工项目
    @Select("SELECT * FROM project WHERE FINISH_DTE is NOT null and FINISH_DTE!=''")
    List<Project> ssywgxm();

    //工程技术部项目名称搜索
    @Select("select * from project where project_nam like CONCAT('%',#{s},'%')")
    List<Project> gcjsbxmmcss(@Param("s") String s);

    //工程技术部项目编号搜索
    @Select("select * from project where project_no like CONCAT('%',#{s},'%')")
    List<Project> gcjsbxmbhss(@Param("s") String s);

    @Select("select * from project where project_nam like CONCAT('%',#{s},'%') and DECLARATION_DEP=#{s2}")
    List<Project> xmmcss(@Param("s") String s, @Param("s2") String s2);

    @Select("select * from project where project_no like CONCAT('%',#{s},'%') and DECLARATION_DEP=#{s2}")
    List<Project> xmbhss(@Param("s") String s, @Param("s2") String s2);

    @Select("select * from project limit #{startNo},#{pageSize}")
    List<Project> fenye(@Param("startNo") Integer pageNo, @Param("pageSize") Integer pageSize);

    //工程技术部拿所有项目数
    @Select("select count(*) from project")
    int AllCounts();

    //其他部门拿自己项目数
    @Select("select count(*) from project where DECLARATION_DEP=#{s}")
    int selfAllCounts(String s);


    @Select("select * from project order by ENG_TECH_AUDIT_OPINION desc")
    List<Project> selectAll();

    //拿到还可以进行验收的数据
    @Select("SELECT ID,PROJECT_NAM FROM project WHERE ID not in (SELECT PROJECTID FROM yanshou )")
    List<Map> selectYsXm();

    //id拿名字和编号
    @Select("select project_nam,project_no from project where id = #{xmid}")
    Map selectNameAndNo(String xmid);

    //拿到技术部经办人的项目id和name
    @Select("select ID,PROJECT_NAM FROM project")
    List<Map> selectAllXmidAndXmname();

    //拿到技术部经办人的项目id和name
    @Select("select ID,PROJECT_NAM FROM project WHERE BIDER = #{Name}")
    List<Map> selectJsbjbrXmidAndXmname(String Name);

    //拿所有的项目id、项目编号、项目名字
    @Select("select ID,PROJECT_NO,PROJECT_NAM FROM project")
    List<Map> selectXmidAndXmnoAndXmname();

    //根据部门名称那项目
    @Select("select * from project where DECLARATION_DEP=#{departmentName} order by ENG_TECH_AUDIT_OPINION desc")
    List<Project> selectByDepartmentName(String departmentName);

    //拿到项目的其他关联表的项目id
    @Select("SELECT xmid FROM zhaobiao \n" +
            "UNION \n" +
            "SELECT PROJECT_ID FROM contract\n" +
            "UNION\n" +
            "SELECT PROJECT_ID FROM jindu\n" +
            "UNION\n" +
            "SELECT PROJECT FROM payable\n" +
            "UNION\n" +
            "SELECT PROJECTID FROM yanshou\n" +
            "UNION\n" +
            "SELECT xmid FROM zhongbiao")
    List<String> getGlXmid();

    //拿到未申请的项目
    @Select("SELECT * FROM project WHERE PID is NULL or PID=''")
    List<Project> selectAllWsq();

    //项目分类搜索项目
    @Select("SELECT * FROM project WHERE REVISER=#{s}")
    List<Project> selectByXmfl(@Param("s") String s);

    //项目类别搜索
    @Select("SELECT * FROM project WHERE PROJECT_TYPE=#{s}")
    List<Project> selectByXmlb(@Param("s") String s);

    List<Project> search(@Param("select_code") String select_code, @Param("select_xmmc") String select_xmmc, @Param("select_dptnm") String select_dptnm, @Param("select_fqr") String[] select_fqr, @Param("select_jbr") String[] select_jbr, @Param("select_jd") String select_jd, @Param("select_xmfl") String[] select_xmfl, @Param("select_xmlb") String[] select_xmlb);

    List<Project> sgSearch(String projectName, String departmentName, String fzr, String xmlb, String yjgq);

    //拿到有流程的项目
    @Select("select * from project where pid is not null and pid!=''")
    List<Project> getPidProject();

    @Options(statementType = StatementType.CALLABLE)
    @Select("call yfbb(#{ _year,mode=IN,jdbcType=VARCHAR},#{_month,mode=IN,jdbcType=VARCHAR}," +
            "#{sb_fbsl,mode=OUT,jdbcType=INTEGER},#{sb_jssl,mode=OUT,jdbcType=INTEGER},#{sb_jsje,mode=OUT,jdbcType=INTEGER},#{sb_htje,mode=OUT,jdbcType=INTEGER}," +
            "#{jj_fbsl,mode=OUT,jdbcType=INTEGER},#{jj_jssl,mode=OUT,jdbcType=INTEGER},#{jj_jsje,mode=OUT,jdbcType=INTEGER},#{jj_htje,mode=OUT,jdbcType=INTEGER}," +
            "#{xx_fbsl,mode=OUT,jdbcType=INTEGER},#{xx_jssl,mode=OUT,jdbcType=INTEGER},#{xx_jsje,mode=OUT,jdbcType=INTEGER},#{xx_htje,mode=OUT,jdbcType=INTEGER}," +
            "#{wz_fbsl,mode=OUT,jdbcType=INTEGER},#{wz_jssl,mode=OUT,jdbcType=INTEGER},#{wz_jsje,mode=OUT,jdbcType=INTEGER},#{wz_htje,mode=OUT,jdbcType=INTEGER}," +
            "#{gdzc_fbsl,mode=OUT,jdbcType=INTEGER},#{gdzc_jssl,mode=OUT,jdbcType=INTEGER},#{gdzc_jsje,mode=OUT,jdbcType=INTEGER},#{gdzc_htje,mode=OUT,jdbcType=INTEGER})")
    void yfbb(Map<String, String> params);

    @Options(statementType = StatementType.CALLABLE)
    @Select("call nfbb(#{ _year,mode=IN,jdbcType=VARCHAR},#{_month,mode=IN,jdbcType=VARCHAR}," +
            "#{sb_fbsl,mode=OUT,jdbcType=INTEGER},#{sb_jssl,mode=OUT,jdbcType=INTEGER},#{sb_jsje,mode=OUT,jdbcType=INTEGER},#{sb_htje,mode=OUT,jdbcType=INTEGER}," +
            "#{jj_fbsl,mode=OUT,jdbcType=INTEGER},#{jj_jssl,mode=OUT,jdbcType=INTEGER},#{jj_jsje,mode=OUT,jdbcType=INTEGER},#{jj_htje,mode=OUT,jdbcType=INTEGER}," +
            "#{xx_fbsl,mode=OUT,jdbcType=INTEGER},#{xx_jssl,mode=OUT,jdbcType=INTEGER},#{xx_jsje,mode=OUT,jdbcType=INTEGER},#{xx_htje,mode=OUT,jdbcType=INTEGER}," +
            "#{wz_fbsl,mode=OUT,jdbcType=INTEGER},#{wz_jssl,mode=OUT,jdbcType=INTEGER},#{wz_jsje,mode=OUT,jdbcType=INTEGER},#{wz_htje,mode=OUT,jdbcType=INTEGER}," +
            "#{gdzc_fbsl,mode=OUT,jdbcType=INTEGER},#{gdzc_jssl,mode=OUT,jdbcType=INTEGER},#{gdzc_jsje,mode=OUT,jdbcType=INTEGER},#{gdzc_htje,mode=OUT,jdbcType=INTEGER})")
    void nfbb(Map<String, String> params);

    @Options(statementType = StatementType.CALLABLE)
    @Select("call bmyfbb(#{ _year,mode=IN,jdbcType=VARCHAR},#{_month,mode=IN,jdbcType=VARCHAR},#{ _bm,mode=IN,jdbcType=VARCHAR}," +
            "#{sb_fbsl,mode=OUT,jdbcType=INTEGER},#{sb_jssl,mode=OUT,jdbcType=INTEGER},#{sb_jsje,mode=OUT,jdbcType=INTEGER},#{sb_htje,mode=OUT,jdbcType=INTEGER}," +
            "#{jj_fbsl,mode=OUT,jdbcType=INTEGER},#{jj_jssl,mode=OUT,jdbcType=INTEGER},#{jj_jsje,mode=OUT,jdbcType=INTEGER},#{jj_htje,mode=OUT,jdbcType=INTEGER}," +
            "#{xx_fbsl,mode=OUT,jdbcType=INTEGER},#{xx_jssl,mode=OUT,jdbcType=INTEGER},#{xx_jsje,mode=OUT,jdbcType=INTEGER},#{xx_htje,mode=OUT,jdbcType=INTEGER}," +
            "#{wz_fbsl,mode=OUT,jdbcType=INTEGER},#{wz_jssl,mode=OUT,jdbcType=INTEGER},#{wz_jsje,mode=OUT,jdbcType=INTEGER},#{wz_htje,mode=OUT,jdbcType=INTEGER}," +
            "#{gdzc_fbsl,mode=OUT,jdbcType=INTEGER},#{gdzc_jssl,mode=OUT,jdbcType=INTEGER},#{gdzc_jsje,mode=OUT,jdbcType=INTEGER},#{gdzc_htje,mode=OUT,jdbcType=INTEGER})")
    void bmyfbb(Map<String, String> params);

    @Options(statementType = StatementType.CALLABLE)
    @Select("call bmnfbb(#{ _year,mode=IN,jdbcType=VARCHAR},#{_month,mode=IN,jdbcType=VARCHAR},#{ _bm,mode=IN,jdbcType=VARCHAR}," +
            "#{sb_fbsl,mode=OUT,jdbcType=INTEGER},#{sb_jssl,mode=OUT,jdbcType=INTEGER},#{sb_jsje,mode=OUT,jdbcType=INTEGER},#{sb_htje,mode=OUT,jdbcType=INTEGER}," +
            "#{jj_fbsl,mode=OUT,jdbcType=INTEGER},#{jj_jssl,mode=OUT,jdbcType=INTEGER},#{jj_jsje,mode=OUT,jdbcType=INTEGER},#{jj_htje,mode=OUT,jdbcType=INTEGER}," +
            "#{xx_fbsl,mode=OUT,jdbcType=INTEGER},#{xx_jssl,mode=OUT,jdbcType=INTEGER},#{xx_jsje,mode=OUT,jdbcType=INTEGER},#{xx_htje,mode=OUT,jdbcType=INTEGER}," +
            "#{wz_fbsl,mode=OUT,jdbcType=INTEGER},#{wz_jssl,mode=OUT,jdbcType=INTEGER},#{wz_jsje,mode=OUT,jdbcType=INTEGER},#{wz_htje,mode=OUT,jdbcType=INTEGER}," +
            "#{gdzc_fbsl,mode=OUT,jdbcType=INTEGER},#{gdzc_jssl,mode=OUT,jdbcType=INTEGER},#{gdzc_jsje,mode=OUT,jdbcType=INTEGER},#{gdzc_htje,mode=OUT,jdbcType=INTEGER})")
    void bmnfbb(Map<String, String> params);

    @Select("SELECT ID FROM project WHERE PID=#{pid}")
    String pidToXmid(String pid);
}