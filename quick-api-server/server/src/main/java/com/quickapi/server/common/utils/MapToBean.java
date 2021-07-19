package com.quickapi.server.common.utils;


import com.baomidou.mybatisplus.core.toolkit.BeanUtils;

import java.util.Map;

public class MapToBean {

    /**
     *
     * @Title: mapToObject
     * @Description: TODO(map转换为bean)
     * @return T    返回类型
     * @param map
     * @param beanClass
     * @return
     * @throws Exception
     */
    public static <T> T mapToObject(Map<String, Object> map, Class<T> beanClass) throws Exception {
        if (map == null) {
            return null;
        }

        return  BeanUtils.mapToBean(map, beanClass);
    }
}