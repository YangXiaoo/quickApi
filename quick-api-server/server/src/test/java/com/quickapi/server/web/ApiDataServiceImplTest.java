package com.quickapi.server.web;

import com.quickapi.server.common.BaseTest;
import com.quickapi.server.web.service.impl.MethodDataServiceImpl;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApiDataServiceImplTest extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(ApiDataServiceImplTest.class);
    @Autowired
    private MethodDataServiceImpl apiDataService;

    @Test
    public void testInsert() {
        //MethodModel apiDataEntity = new MethodModel();
        //apiDataEntity.setProjectName("test");
        //apiDataEntity.setDelete(CONSTANT_DEFINE.NOT_DELETE);
        //apiDataEntity.setAuthor("yangxiao");
        //apiDataEntity.setClassName("apiclass");
        //apiDataEntity.setContentType("none");
        //apiDataEntity.setCreateTime(DateTool.getCurrentDate());
        //apiDataEntity.setDataApiId(UUIDUtil.getUUID());
        //apiDataEntity.setDescription("desc");
        //apiDataEntity.setDownload(CONSTANT_DEFINE.NOT_DOWNLOAD);
        //apiDataEntity.setGroup("菜单1");
        //apiDataEntity.setMethodName("query");
        //apiDataEntity.setName("查询");
        //apiDataEntity.setUrl("http://baidu.com");
        //
        //Map<String, Object> map = new HashMap<>();
        //map.put("data", apiDataEntity);
        //JsonModel jsonModel = apiDataService.saveApiData(map);
        //logger.info(jsonModel.toString());
    }
}
