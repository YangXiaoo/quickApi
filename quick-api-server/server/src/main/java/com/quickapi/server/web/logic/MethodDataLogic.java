package com.quickapi.server.web.logic;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.quickapi.server.common.constant.CONSTANT_DEFINE;
import com.quickapi.server.common.tools.DateTool;
import com.quickapi.server.common.utils.UUIDUtil;
import com.quickapi.server.exception.BusinessException;
import com.quickapi.server.web.dao.MethodModelDao;
import com.quickapi.server.web.dao.entity.MethodModel;
import com.quickapi.server.web.dao.entity.MethodModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class MethodDataLogic {
    @Autowired(required = false)
    private MethodModelDao methodModelDao;

    /**
     * 保存一条接口信息
     * <p>
     *     存在则更新，不存在则插入
     * </p>
     * @param methodModel 单个接口信息
     * @return void
     * @author yangxiao
     * @date 2020/12/29 21:09
     */
    public void saveMethodData(MethodModel methodModel) {
        if (methodModel != null) {
            List<MethodModel> methodModelList = this.findByMethodName(methodModel);
            if (!CollectionUtils.isEmpty(methodModelList)) {
                for (MethodModel cur : methodModelList) {
                    cur.setUpdateTime(DateTool.getCurrentDate());

                    UpdateWrapper<MethodModel> updateWrapper = new UpdateWrapper<>();
                    updateWrapper.eq("PROJECT_NAME", cur.getProjectName());
                    updateWrapper.eq("URL", cur.getUrl());
                    updateWrapper.eq("DELETE_FLAG", CONSTANT_DEFINE.NOT_DELETE);
                    methodModelDao.update(cur, updateWrapper);
                }
            } else {
                methodModel.setDataApiId(UUIDUtil.getUUID());
                while (!CollectionUtils.isEmpty(selectById(methodModel))) {
                    methodModel.setDataApiId(UUIDUtil.getUUID());
                }
                methodModel.setCreateTime(DateTool.getCurrentDate());
                methodModelDao.insert(methodModel);
            }
        }
    }

    /**
     * 根据项目名和方法URL查找未删除的方法
     * @param methodModel MethodModel
     * @return java.util.List<com.quickapi.server.web.dao.entity.MethodModel>
     * @author yangxiao
     * @date 2021/1/8 20:48
     */
    public List<MethodModel> findByMethodName(MethodModel methodModel) {
        List<MethodModel> ret = null;
        if (methodModel != null) {
            QueryWrapper<MethodModel> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("PROJECT_NAME", methodModel.getProjectName());
            queryWrapper.eq("URL", methodModel.getUrl());
            queryWrapper.eq("DELETE_FLAG", CONSTANT_DEFINE.NOT_DELETE);

            ret = methodModelDao.selectList(queryWrapper);
        }

        return ret;
    }
    /**
     * 根据ID查询
     * @param methodModel MethodModel
     * @return java.util.List<com.quickapi.server.web.dao.entity.MethodModel>
     * @author yangxiao
     * @date 2021/1/6 22:47
     */
    public List<MethodModel> selectById(MethodModel methodModel) {
        List<MethodModel> ret = null;
        if (methodModel != null) {
            QueryWrapper<MethodModel> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("DATA_API_ID", methodModel.getDataApiId());
            ret = methodModelDao.selectList(queryWrapper);
        }

        return ret;
    }
    /**
     * 根据项目名称查找项目所有接口
     * @param projectName 项目名称
     * @return java.util.List<com.quickapi.server.web.dao.entity.MethodModel>
     * @author yangxiao
     * @date 2020/12/29 21:21
     */
    public List<MethodModel> findApiDataByProjectName(String projectName) {
        if (StringUtils.isBlank(projectName)) {
            throw new BusinessException("findByProjectName()参数不完整");
        }
        QueryWrapper<MethodModel> queryWrapper = new QueryWrapper();
        queryWrapper.eq("PROJECT_NAME", projectName);
        queryWrapper.eq("DELETE_FLAG", CONSTANT_DEFINE.NOT_DELETE);

        return methodModelDao.selectList(queryWrapper);
    }

    /**
     * 删除一组接口方法信息（假删）
     * @param methodModelList 接口方法信息
     * @return void
     * @author yangxiao
     * @date 2021/1/4 21:34
     */
    public void deleteMethodDataList(List<MethodModel> methodModelList) {
        if (!CollectionUtils.isEmpty(methodModelList)) {
            for (MethodModel methodModel : methodModelList) {
                UpdateWrapper<MethodModel> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("PROJECT_NAME", methodModel.getProjectName());
                updateWrapper.eq("URL", methodModel.getUrl());
                methodModel.setDeleteFlag(CONSTANT_DEFINE.IS_DELETE);
                methodModelDao.update(methodModel, updateWrapper);
            }
        }
    }

    /**
     * 更新接口方法信息
     * @param url 路径
     * @param projectName 项目名称
     * @param name 菜单名
     * @param methodGroup 所属菜单组
     * @return void
     * @author yangxiao
     * @date 2021/1/8 22:22
     */
    public void updateMethodData(String url, String projectName,
                                 String name, String methodGroup) {
        if (StringUtils.isBlank(url) || StringUtils.isBlank(name)
                || StringUtils.isBlank(projectName) || StringUtils.isBlank(methodGroup)) {
            throw new BusinessException("updateMethodData(), 参数不完整");
        }

        MethodModel methodModel;                                            // 定义待更新的接口方法

        MethodModel tmp = new MethodModel();
        tmp.setProjectName(projectName);
        tmp.setUrl(url);
        List<MethodModel> methodModelList = this.findByMethodName(tmp);
        if (CollectionUtils.isEmpty(methodModelList)) {
            throw new BusinessException("没有找到相关接口方法");
        } else {
            if (methodModelList.size() > 1) {
                throw new BusinessException("找到至少两个方法");
            }
            methodModel = methodModelList.get(0);
            methodModel.setName(name);
            methodModel.setMethodGroup(methodGroup);
        }

        UpdateWrapper<MethodModel> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("PROJECT_NAME", projectName);
        updateWrapper.eq("URL", url);
        methodModelDao.update(methodModel, updateWrapper);
    }
}
