package com.atguigu.dao;

import com.atguigu.entity.Dict;
import com.atguigu.dao.BaseDao;

import java.util.List;

public interface DictDao{

    List<Dict> findListByParentId(Long parentId);

    Integer countIsParent(Long id);

    Dict findDictByDictCode(String dictCode);

    String getNameId(Long id);
}
