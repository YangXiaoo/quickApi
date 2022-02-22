package com.quickapi.server.common.utils;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.BeanUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quickapi.server.exception.BusinessException;
import org.apache.commons.collections4.MapUtils;

import java.util.List;
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

    public static <T> T toBean(Object object, Class<T> clazz) {
        if (object == null) {
            throw new BusinessException("MapToBean.toBean, object is null");
        }
        String jsonString = JSONObject.toJSONString(object);
       return (T) JSONObject.parseObject(jsonString, clazz);
    }

    public static <T> List<T> toList(Object object, Class<T> clazz) {
        String jsonString = JSONObject.toJSONString(object);
        return JSONObject.parseArray(jsonString, clazz);
    }

    public static <T> Page<T> toPage(Object object, Class<T> clazz) {
        Map<String, Object> map = (Map<String, Object>)object;
        Page<T> page = new Page<>();
        page.setTotal(MapUtils.getLong(map, "total", page.getTotal()));
        page.setSize(MapUtils.getLong(map, "size", page.getSize()));
        page.setCurrent(MapUtils.getLong(map, "current", page.getCurrent()));
        page.setRecords(toList(MapUtils.getLong(map, "records"), clazz));

       return page;
    }

    public static <T> Page<T> getPage(Map<String, Object> map, String key, Class<T> clazz) {
        Map<String, Object> data = (Map<String, Object>)MapUtils.getObject(map, key);
        if (MapUtils.isEmpty(data)) {
            return null;
        }

        Page<T> page = new Page<>();
        page.setSize(MapUtils.getLong(map, "size", page.getSize()));
        page.setCurrent(MapUtils.getLong(map, "current", page.getCurrent()));

        return page;
    }

    public static <T> T getObject(Map<String, Object> map, String key, Class<T> clazz) {
        Object object = MapUtils.getObject(map, key);
        String jsonString = JSONObject.toJSONString(object);
        return JSONObject.parseObject(jsonString, clazz);
    }
}