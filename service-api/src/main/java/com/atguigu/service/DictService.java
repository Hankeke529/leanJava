package com.atguigu.service;

import com.atguigu.entity.Dict;

import java.util.List;
import java.util.Map;

public interface DictService {
    //查询数据字典的数据,通过ztree进行渲染
    List<Map<String, Object>> findZnodes(long id);


    List<Dict> findListByParentId(long parentId);
    //根据节点名字获取节点下的数据
    List<Dict> findListByDictCode(String dictCode);


    String getNameById(Long id);
}
