package com.sq.demo.mapper;

import com.sq.demo.pojo.OperateManual;
import com.sq.demo.utils.MyMapper;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * @Author: 13965
 * @Date: 2019/4/19 16:49
 * @Description:
 */
public interface OperateManualMapper extends MyMapper<OperateManual> {
    public int insertByKey(OperateManual operateManual);
    public int getAllCount();
    public List<OperateManual> getPage(RowBounds rowBounds);
}
