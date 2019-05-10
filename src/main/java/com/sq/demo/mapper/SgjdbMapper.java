package com.sq.demo.mapper;

import com.sq.demo.Entity.Sgjdb2;
import com.sq.demo.pojo.Sgjdb;
import com.sq.demo.utils.MyMapper;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface SgjdbMapper extends MyMapper<Sgjdb> {

    //搜索
    List<Sgjdb> search(Sgjdb2 sgjdb);
}