package com.sq.demo.mapper;

import com.sq.demo.pojo.Xmcxb;
import com.sq.demo.utils.MyMapper;

import java.util.List;

public interface XmcxbMapper extends MyMapper<Xmcxb> {

    List<Xmcxb> select(Xmcxb xmcxb);
}