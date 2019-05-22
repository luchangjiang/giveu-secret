package com.stylefeng.guns.system;

import com.stylefeng.guns.base.BaseJunit;
import com.stylefeng.guns.common.persistence.dao.DeptMapper;
import com.stylefeng.guns.common.persistence.model.SecretDept;
import com.stylefeng.guns.modular.system.dao.DeptDao;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * 字典服务测试
 *
 * @author fengshuonan
 * @date 2017-04-27 17:05
 */
public class SecretDeptTest extends BaseJunit {

    @Resource
    DeptDao deptDao;

    @Resource
    DeptMapper deptMapper;

    @Test
    public void addDeptTest() {
        SecretDept secretDept = new SecretDept();
        secretDept.setFullName("测试fullname");
        secretDept.setNum(5);
        secretDept.setPid(1);
        secretDept.setSimpleName("测试");
        secretDept.setTips("测试tips");
        secretDept.setVersion(1);
        Integer insert = deptMapper.insert(secretDept);
        assertEquals(insert, new Integer(1));
    }

    @Test
    public void updateTest() {
        SecretDept secretDept = this.deptMapper.selectById(24);
        secretDept.setTips("哈哈");
        boolean flag = secretDept.updateById();
        assertTrue(flag);
    }

    @Test
    public void deleteTest() {
        SecretDept secretDept = this.deptMapper.selectById(24);
        Integer integer = deptMapper.deleteById(secretDept);
        assertTrue(integer > 0);
    }

    @Test
    public void listTest() {
        List<Map<String, Object>> list = this.deptDao.list("总公司");
        assertTrue(list.size() > 0);
    }
}
