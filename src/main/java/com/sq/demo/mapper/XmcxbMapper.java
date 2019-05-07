package com.sq.demo.mapper;

import com.sq.demo.Entity.Xmcxb2;
import com.sq.demo.pojo.Xmcxb;
import com.sq.demo.utils.MyMapper;

import java.util.List;

public interface XmcxbMapper extends MyMapper<Xmcxb> {

    List<Xmcxb> search(Xmcxb2 xmcxb2);
}