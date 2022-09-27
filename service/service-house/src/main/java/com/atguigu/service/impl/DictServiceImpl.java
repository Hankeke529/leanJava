package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.DictDao;
import com.atguigu.entity.Dict;
import com.atguigu.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = DictService.class)
@Transactional
public class DictServiceImpl implements DictService{
    @Autowired
    private DictDao dictDao;

    @Override
    public List<Map<String, Object>> findZnodes(long id) {
        List<Dict> dictList=dictDao.findListByParentId(id);

        List<Map<String, Object>> zNodes=new ArrayList<>();

        for (Dict dict : dictList) {
            Integer count=dictDao.countIsParent(dict.getId());
            Map map = new HashMap<>();
            map.put("id",dict.getId());
            map.put("isParent",count>0?true:false);

            map.put("name",dict.getName());


            zNodes.add(map);
        }
        return zNodes;
    }

    @Override
    public List<Dict> findListByParentId(long parentId) {
        return dictDao.findListByParentId(parentId);
    }

    @Override
    public List<Dict> findListByDictCode(String dictCode) {
        Dict dict = dictDao.findDictByDictCode(dictCode);
        if(dict==null) return null;

        return this.findListByParentId(dict.getId());
    }

    @Override
    public String getNameById(Long id) {
        return dictDao.getNameId(id);
    }
}
