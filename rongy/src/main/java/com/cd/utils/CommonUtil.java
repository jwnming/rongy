package com.cd.utils;

import com.cd.common.GoodsSaleTabsEnum;
import com.cd.enums.ErrorCodeEnum;
import com.cd.exception.BusiException;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CommonUtil {

    /**
     * 借助 UUID 生成唯一的序号，去除'-'
     * @return
     */
    public static String uniqueNo() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String toJson(Enum<?>[] enumValues) throws IllegalAccessException, InvocationTargetException {
        StringBuffer buffer=new StringBuffer("[");
        boolean obj1st=true;
        for (Object obj : enumValues) {
            if(obj1st){
                obj1st=false;
            }else{
                buffer.append(",");
            }
            buffer.append("{");

            Method[] methods = obj.getClass().getMethods();
            boolean method1st=true;
            for (int i = 0; i < methods.length; i++) {

                Method method = methods[i];
                //获取枚举值的get方法
                if (method.getName().startsWith("get") && method.getParameterTypes().length == 0 && !method.getName().contains("Class")) {
                    //处理逗号
                    if(method1st){
                        method1st=false;
                    }else{
                        buffer.append(",");
                    }
                    //将get方法的get去掉,并且首字母小写
                    String name = method.getName().replace("get","");
                    buffer.append("\"" + name.substring(0, 1).toLowerCase() + name.substring(1) + "\":\"");
                    buffer.append(method.invoke(obj)+"\"");
                }
            }
            buffer.append("}");
        }
        buffer.append("]");
        return buffer.toString();
    }

    public static void isBlank(String des, String key){
        if(StringUtils.isEmpty(key)){
            throw new BusiException(ErrorCodeEnum.MARKETGOODSDONE2003.getCode(), des + ErrorCodeEnum.MARKETGOODSDONE2003.getMsg());
        }
    }

    public static void isPrice(String str) {
        Pattern pattern = Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$");
        Matcher match=pattern.matcher(str);
        if(!match.matches()) {
            throw new BusiException(ErrorCodeEnum.MARKETGOODSDONE2004);
        }
    }


}
