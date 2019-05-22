package com.stylefeng.guns.common.util;

import com.stylefeng.guns.common.constant.tips.ErrorTip;
import com.stylefeng.guns.common.constant.tips.SuccessTip;
import com.stylefeng.guns.common.constant.tips.Tip;
import com.stylefeng.guns.modular.system.transfer.QueryPwdManageDto;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtil {
    public static Tip ValidError(QueryPwdManageDto queryPwdManage) {
        StringBuilder sb = new StringBuilder();
        boolean status = true;
        try {
            Class clazz = queryPwdManage.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                NotNull valid = field.getAnnotation(NotNull.class);
                if (valid != null) {
                    Object value = field.get(queryPwdManage);
                    if (value == null) {
                        sb.append(valid.message() + "\r\n");
                        status = status && false;
                    }
                }
                PatternReg eValid = field.getAnnotation(PatternReg.class);
                if (eValid != null) {
                    String regEx = eValid.regexp();
                    // 编译正则表达式
                    Pattern pattern = Pattern.compile(regEx);
                    // 忽略大小写的写法
                    // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
                    String value = String.valueOf(field.get(queryPwdManage));
                    Matcher matcher = pattern.matcher(value);
                    // 字符串是否与正则表达式相匹配
                    boolean rs = matcher.matches();
                    if (!rs) {
                        sb.append(eValid.message() + "\r\n");
                        status = status && false;
                    }
                }
            }
        } catch (Exception ex) {
            ErrorTip tip = new ErrorTip(0, "验证异常");
            return tip;
        }
        SuccessTip tip = new SuccessTip();
        tip.setCode(status ? 200 : 0);
        tip.setMessage(sb.toString());
        return tip;
    }
}
