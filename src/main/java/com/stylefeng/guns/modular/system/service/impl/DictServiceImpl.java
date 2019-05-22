package com.stylefeng.guns.modular.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.stylefeng.guns.common.exception.BizExceptionEnum;
import com.stylefeng.guns.common.exception.BussinessException;
import com.stylefeng.guns.common.persistence.dao.DictMapper;
import com.stylefeng.guns.common.persistence.model.SecretDict;
import com.stylefeng.guns.modular.system.dao.DictDao;
import com.stylefeng.guns.modular.system.service.IDictService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static com.stylefeng.guns.common.constant.factory.MutiStrFactory.*;

@Service
@Transactional
public class DictServiceImpl implements IDictService {

    @Resource
    DictDao dictDao;

    @Resource
    DictMapper dictMapper;

    @Override
    public void addDict(String dictName, String dictValues) {
        //判断有没有该字典
        List<SecretDict> secretDicts = dictMapper.selectList(new EntityWrapper<SecretDict>().eq("name", dictName).and().eq("pid", 0));
        if(secretDicts != null && secretDicts.size() > 0){
            throw new BussinessException(BizExceptionEnum.DICT_EXISTED);
        }

        //解析dictValues
        List<Map<String, String>> items = parseKeyValue(dictValues);

        //添加字典
        SecretDict secretDict = new SecretDict();
        secretDict.setDictName(dictName);
        secretDict.setNum(0);
        secretDict.setPid(0);
        this.dictMapper.insert(secretDict);

        //添加字典条目
        for (Map<String, String> item : items) {
            String num = item.get(MUTI_STR_KEY);
            String name = item.get(MUTI_STR_VALUE);
            SecretDict itemSecretDict = new SecretDict();
            itemSecretDict.setPid(secretDict.getId());
            itemSecretDict.setDictName(name);
            itemSecretDict.setNum(Integer.valueOf(num));
            this.dictMapper.insert(itemSecretDict);
        }
    }

    @Override
    public void editDict(Integer dictId, String dictName, String dicts) {
        //删除之前的字典
        this.delteDict(dictId);

        //重新添加新的字典
        this.addDict(dictName,dicts);
    }

    @Override
    public void delteDict(Integer dictId) {
        //删除这个字典的子词典
        Wrapper<SecretDict> dictEntityWrapper = new EntityWrapper<>();
        dictEntityWrapper = dictEntityWrapper.eq("pid", dictId);
        dictMapper.delete(dictEntityWrapper);

        //删除这个词典
        dictMapper.deleteById(dictId);
    }
}
