package com.quickapi.server.web.logic;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.quickapi.server.common.constant.CONSTANT_DEFINE;
import com.quickapi.server.common.tools.DateTool;
import com.quickapi.server.common.utils.UUIDUtil;
import com.quickapi.server.exception.BusinessException;
import com.quickapi.server.web.dao.ApiDocDao;
import com.quickapi.server.web.dao.MethodModelDao;
import com.quickapi.server.web.dao.UserMethodDao;
import com.quickapi.server.web.dao.entity.ProjectApIExample;
import com.quickapi.server.web.dao.entity.ProjectApiMethod;
import com.quickapi.server.web.dao.entity.UserApiMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MethodDataLogic {
    @Autowired(required = false)
    private MethodModelDao methodModelDao;
    @Autowired
    private ApiDocDao apiDocDao;
    @Autowired
    private UserMethodDao userMethodDao;

    /**
     * 保存一条接口信息
     * <p>
     *     存在则更新，不存在则插入
     * </p>
     * @param projectApiMethod 单个接口信息
     * @return void
     * @author yangxiao
     * @date 2020/12/29 21:09
     */
    public void saveMethodData(ProjectApiMethod projectApiMethod) {
        if (projectApiMethod != null) {
            List<ProjectApiMethod> projectApiMethodList = this.findMethodByMethodName(projectApiMethod);
            if (!CollectionUtils.isEmpty(projectApiMethodList)) {
                for (ProjectApiMethod cur : projectApiMethodList) {
                    cur.setUpdateTime(DateTool.getCurrentDate());

                    UpdateWrapper<ProjectApiMethod> updateWrapper = new UpdateWrapper<>();
                    updateWrapper.eq("PROJECT_NAME", cur.getProjectName());
                    updateWrapper.eq("URL", cur.getUrl());
                    updateWrapper.eq("DELETE_FLAG", CONSTANT_DEFINE.NOT_DELETE);
                    methodModelDao.update(cur, updateWrapper);
                }
            } else {
                projectApiMethod.setDataApiId(UUIDUtil.getUUID());
                while (!CollectionUtils.isEmpty(selectMethodById(projectApiMethod))) {
                    projectApiMethod.setDataApiId(UUIDUtil.getUUID());
                }
                projectApiMethod.setCreateTime(DateTool.getCurrentDate());
                methodModelDao.insert(projectApiMethod);
            }
        }
    }

    /**
     * 根据项目名和方法URL查找未删除的方法
     * @param projectApiMethod ProjectApiMethod
     * @return java.util.List<com.quickapi.server.web.dao.entity.ProjectApiMethod>
     * @author yangxiao
     * @date 2021/1/8 20:48
     */
    public List<ProjectApiMethod> findMethodByMethodName(ProjectApiMethod projectApiMethod) {
        List<ProjectApiMethod> ret = null;
        if (projectApiMethod != null) {
            QueryWrapper<ProjectApiMethod> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("PROJECT_NAME", projectApiMethod.getProjectName());
            queryWrapper.eq("URL", projectApiMethod.getUrl());
            queryWrapper.eq("DELETE_FLAG", CONSTANT_DEFINE.NOT_DELETE);

            ret = methodModelDao.selectList(queryWrapper);
        }

        return ret;
    }
    /**
     * 根据ID查询
     * @param projectApiMethod ProjectApiMethod
     * @return java.util.List<com.quickapi.server.web.dao.entity.ProjectApiMethod>
     * @author yangxiao
     * @date 2021/1/6 22:47
     */
    public List<ProjectApiMethod> selectMethodById(ProjectApiMethod projectApiMethod) {
        List<ProjectApiMethod> ret = null;
        if (projectApiMethod != null) {
            QueryWrapper<ProjectApiMethod> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("DATA_API_ID", projectApiMethod.getDataApiId());
            ret = methodModelDao.selectList(queryWrapper);
        }

        return ret;
    }

    /**
     * 根据项目名称查找项目所有接口
     * @param projectName 项目名称
     * @return java.util.List<com.quickapi.server.web.dao.entity.ProjectApiMethod>
     * @author yangxiao
     * @date 2020/12/29 21:21
     */
    public List<ProjectApiMethod> findApiDataByProjectName(String projectName) {
        if (StringUtils.isBlank(projectName)) {
            throw new BusinessException("findByProjectName()参数不完整");
        }
        QueryWrapper<ProjectApiMethod> queryWrapper = new QueryWrapper();
        queryWrapper.eq("PROJECT_NAME", projectName);
        queryWrapper.eq("DELETE_FLAG", CONSTANT_DEFINE.NOT_DELETE);

        return methodModelDao.selectList(queryWrapper);
    }

    /**
     * 删除一组接口方法信息（假删）
     * @param projectApiMethodList 接口方法信息
     * @return void
     * @author yangxiao
     * @date 2021/1/4 21:34
     */
    public void deleteMethodDataList(List<ProjectApiMethod> projectApiMethodList) {
        if (!CollectionUtils.isEmpty(projectApiMethodList)) {
            for (ProjectApiMethod projectApiMethod : projectApiMethodList) {
                UpdateWrapper<ProjectApiMethod> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("PROJECT_NAME", projectApiMethod.getProjectName());
                updateWrapper.eq("URL", projectApiMethod.getUrl());
                projectApiMethod.setDeleteFlag(CONSTANT_DEFINE.IS_DELETE);
                methodModelDao.update(projectApiMethod, updateWrapper);
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

        ProjectApiMethod projectApiMethod;                                            // 定义待更新的接口方法

        ProjectApiMethod tmp = new ProjectApiMethod();
        tmp.setProjectName(projectName);
        tmp.setUrl(url);
        List<ProjectApiMethod> projectApiMethodList = this.findMethodByMethodName(tmp);
        if (CollectionUtils.isEmpty(projectApiMethodList)) {
            throw new BusinessException("没有找到相关接口方法");
        } else {
            if (projectApiMethodList.size() > 1) {
                throw new BusinessException("找到至少两个方法");
            }
            projectApiMethod = projectApiMethodList.get(0);
            projectApiMethod.setName(name);
            projectApiMethod.setMethodGroup(methodGroup);
        }

        UpdateWrapper<ProjectApiMethod> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("PROJECT_NAME", projectName);
        updateWrapper.eq("URL", url);
        methodModelDao.update(projectApiMethod, updateWrapper);
    }

    /**
     * 查找用户所有接口方法
     * @param userName 用户姓名
     * @return java.util.List<com.quickapi.server.web.dao.entity.UserApiMethod>
     * @author yangxiao
     * @date 2021/1/14 21:14
     */
    public List<UserApiMethod> getUserMethodDataList(String userName) {
        if (StringUtils.isBlank(userName)) {
            throw new BusinessException("getUserMethodDataList()参数不完整");
        }
        List<UserApiMethod> ret = null;
        QueryWrapper<UserApiMethod> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("USER_NAME", userName);
        queryWrapper.eq("DELETE_FLAG", CONSTANT_DEFINE.NOT_DELETE);

        ret = userMethodDao.selectList(queryWrapper);

        return ret;
    }

    /**
     * 更新方法信息
     * @param url 路由
     * @param userName 用户名
     * @param name 方法名
     * @param methodGroup 方法所属组
     * @return void
     * @author yangxiao
     * @date 2021/1/14 21:28
     */
    public void updateUserMethodData(String url, String userName, String name, String methodGroup) {
        if (StringUtils.isBlank(url) || StringUtils.isBlank(userName)
                || StringUtils.isBlank(name) || StringUtils.isBlank(methodGroup)) {
            throw new BusinessException("updateUserMethodData()参数不完整");
        }

        UserApiMethod methodModel;

        UserApiMethod tmp = new UserApiMethod();
        tmp.setUserName(userName);
        tmp.setUrl(url);
        List<UserApiMethod> methodModelList = this.findUserMethodByMethodURL(tmp);
        if (CollectionUtils.isEmpty(methodModelList)) {
            throw new BusinessException("没有找到相关接口方法");
        } else {
            if (methodModelList.size() > 1) {
                throw new BusinessException("找到至少两个方法");
            }
            methodModel = methodModelList.get(0);
            methodModel.setMethodName(name);
            methodModel.setMethodGroup(methodGroup);
        }

        UpdateWrapper<UserApiMethod> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("USER_NAME", userName);
        updateWrapper.eq("URL", url);
        userMethodDao.update(methodModel, updateWrapper);
    }

    /**
     * 根据方url查找用户方法数据
     * @param methodModel 查询条件
     * @return java.util.List<com.quickapi.server.web.dao.entity.UserApiMethod>
     * @author yangxiao
     * @date 2021/1/14 21:23
     */
    public List<UserApiMethod> findUserMethodByMethodURL(UserApiMethod methodModel) {
        List<UserApiMethod> ret = null;
        if (methodModel != null) {
            QueryWrapper<UserApiMethod> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("USER_NAME", methodModel.getUserName());
            queryWrapper.eq("URL", methodModel.getUrl());
            queryWrapper.eq("DELETE_FLAG", CONSTANT_DEFINE.NOT_DELETE);

            ret = userMethodDao.selectList(queryWrapper);
        }

        return ret;
    }

    /**
     * 保存用户接口方法信息
     * <p>
     *     存在更新，不存在则插入
     * </p>
     * @param url 路由
     * @param userName 用户名
     * @param name 方法名
     * @param methodGroup 方法所属组
     * @return void
     * @author yangxiao
     * @date 2021/1/14 21:33
     */
    public void saveUserMethodData(String url, String userName, String name, String methodGroup, String reqType) {
        if (StringUtils.isBlank(url) || StringUtils.isBlank(userName)
                || StringUtils.isBlank(name) || StringUtils.isBlank(methodGroup)) {
            throw new BusinessException("saveUserMethodData()参数不完整");
        }
        UserApiMethod methodModel;

        // 先查询是否已经存在
        UserApiMethod tmp = new UserApiMethod();
        tmp.setUserName(userName);
        tmp.setUrl(url);
        List<UserApiMethod> methodModelList = this.findUserMethodByMethodURL(tmp);
        if (CollectionUtils.isEmpty(methodModelList)) {
            tmp.setMethodName(name);
            tmp.setMethodGroup(methodGroup);
            tmp.setDeleteFlag(CONSTANT_DEFINE.NOT_DELETE);
            tmp.setRequestType(reqType);
            tmp.setUserMethodId(UUIDUtil.getUUID());
            while (!CollectionUtils.isEmpty(selectUserMethodById(tmp))) {
                tmp.setUserMethodId(UUIDUtil.getUUID());
            }

            userMethodDao.insert(tmp);
        } else {
            if (methodModelList.size() > 1) {
                throw new BusinessException("找到至少两个方法");
            }
            methodModel = methodModelList.get(0);
            methodModel.setMethodName(name);
            methodModel.setMethodGroup(methodGroup);
            methodModel.setRequestType(reqType);
            UpdateWrapper<UserApiMethod> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("USER_NAME", userName);
            updateWrapper.eq("URL", url);
            userMethodDao.update(methodModel, updateWrapper);
        }
    }

    /**
     * 根据主键ID查找用户接口方法信息
     * @param methodModel UserApiMethod
     * @return java.util.List<com.quickapi.server.web.dao.entity.UserApiMethod>
     * @author yangxiao
     * @date 2021/1/14 21:39
     */
    public List<UserApiMethod> selectUserMethodById(UserApiMethod methodModel) {
        List<UserApiMethod> ret = null;
        if (methodModel != null) {
            QueryWrapper<UserApiMethod> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("USER_METHOD_ID", methodModel.getUserMethodId());
            ret = userMethodDao.selectList(queryWrapper);
        }

        return ret;
    }

    /**
     * 删除用户接口方法
     * @param url url
     * @param userName 用户名
     * @return void
     * @author yangxiao
     * @date 2021/1/14 21:46
     */
    public void deleteUserMethodData(String url, String userName) {
        if (StringUtils.isBlank(url) || StringUtils.isBlank(userName)) {
            throw new BusinessException("deleteUserMethodData()参数不完整");
        }

        UserApiMethod methodModel;

        UserApiMethod tmp = new UserApiMethod();
        tmp.setUserName(userName);
        tmp.setUrl(url);
        List<UserApiMethod> methodModelList = this.findUserMethodByMethodURL(tmp);
        if (CollectionUtils.isEmpty(methodModelList)) {
            throw new BusinessException("没有找到相关接口方法");
        } else {
            if (methodModelList.size() > 1) {
                throw new BusinessException("找到至少两个方法");
            }
            methodModel = methodModelList.get(0);
            methodModel.setDeleteFlag(CONSTANT_DEFINE.IS_DELETE);
        }

        UpdateWrapper<UserApiMethod> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("USER_METHOD_ID", methodModel.getUserMethodId());
        userMethodDao.update(methodModel, updateWrapper);
        // TODO 删除对应的接口文档
    }

    /**
     * 获得项目已完成接口的详细信息
     * @param projectName 项目名
     * @return java.util.Map<java.lang.String,java.util.List<com.quickapi.server.web.dao.entity.ProjectApIExample>>
     * @author yangxiao
     * @date 2021/1/25 20:07
     */
    public Map<String, List<ProjectApIExample>> getProjectFinishedMethodDataMap(String projectName) {
        if (StringUtils.isBlank(projectName)) {
            throw new BusinessException("getProjectFinishedMethodDataMap()参数不完整");
        }
        Map<String, List<ProjectApIExample>> apiDocMap = new HashMap<>();
        QueryWrapper<ProjectApIExample> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("PROJECT_NAME", projectName);
        queryWrapper.eq("DELETE_FLAG", CONSTANT_DEFINE.NOT_DELETE);
        List<ProjectApIExample> projectApIExampleList = apiDocDao.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(projectApIExampleList))  {
            for (ProjectApIExample projectApIExample : projectApIExampleList) {
                if (apiDocMap.containsKey(projectApIExample.getMethodUrl())) {
                    apiDocMap.get(projectApIExample.getMethodUrl()).add(projectApIExample);
                } else {
                    List<ProjectApIExample> tmp = new ArrayList<>();
                    tmp.add(projectApIExample);
                    apiDocMap.put(projectApIExample.getMethodUrl(), new ArrayList<>(tmp));
                }
            }
        }

        return apiDocMap;
    }

    /**
     * 获得项目已完成接口数量
     * @param projectName 项目名
     * @return double
     * @author yangxiao
     * @date 2021/1/25 20:07
     */
    public double getProjectFinishedMethodDataCount(String projectName) {
        if (StringUtils.isBlank(projectName)) {
            throw new BusinessException("getProjectFinishedMethodDataCount()参数不完整");
        }

        return getProjectFinishedMethodDataMap(projectName).size();
    }
}
