package com.sq.demo.mapper;

import com.sq.demo.pojo.Sgjdb;
import com.sq.demo.utils.MyMapper;

import java.util.List;

public interface SgjdbMapper extends MyMapper<Sgjdb> {

    //搜索
    List<Sgjdb> search(Sgjdb sgjdb);
}