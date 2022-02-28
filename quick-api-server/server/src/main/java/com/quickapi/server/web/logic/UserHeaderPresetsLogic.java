package com.quickapi.server.web.logic;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.quickapi.server.common.constant.CONSTANT_DEFINE;
import com.quickapi.server.common.utils.StringUtils;
import com.quickapi.server.common.utils.UUIDUtil;
import com.quickapi.server.exception.BusinessException;
import com.quickapi.server.web.dao.UserHeaderPresetsDao;
import com.quickapi.server.web.dao.entity.UserHeaderPresets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserHeaderPresetsLogic {
    @Autowired
    private UserHeaderPresetsDao userHeaderPresetsDao;

    public UserHeaderPresets addUserHeaderPresets(String presetId, String userName, String name, String value) {
        UserHeaderPresets userHeaderPresets = new UserHeaderPresets();
        userHeaderPresets.setUserName(userName);
        userHeaderPresets.setName(name);
        userHeaderPresets.setValue(value);
        userHeaderPresets.setDeleteFlag(CONSTANT_DEFINE.NOT_DELETE);

        if (StringUtils.isEmpty(presetId)) { // 插入
            userHeaderPresets.setPresetId(UUIDUtil.getUUID());
            while (userHeaderPresetsDao.selectById(userHeaderPresets.getPresetId()) != null) {
                userHeaderPresets.setPresetId(UUIDUtil.getUUID());
            }

            userHeaderPresetsDao.insert(userHeaderPresets);
        } else {
            // 更新
            userHeaderPresets.setPresetId(presetId);
            userHeaderPresetsDao.updateById(userHeaderPresets);
        }

        return userHeaderPresets;
    }

    public List<UserHeaderPresets> getUserHeaderPresets(String userName) {
        if (StringUtils.isEmpty(userName)) {
            throw new BusinessException("getUserHeaderPresets(), 参数不完整");
        }

        LambdaQueryWrapper<UserHeaderPresets> queryWrapper = new LambdaQueryWrapper<UserHeaderPresets>()
                .eq(UserHeaderPresets::getUserName, userName)
                .eq(UserHeaderPresets::getDeleteFlag, CONSTANT_DEFINE.NOT_DELETE)
                .orderByDesc(UserHeaderPresets::getCreateTime);

        return userHeaderPresetsDao.selectList(queryWrapper);
    }

    public void deleteUserHeaderPresets(String presetId) {
        if (StringUtils.isNotEmpty(presetId)) {
            UserHeaderPresets userHeaderPresets = new UserHeaderPresets();
            userHeaderPresets.setPresetId(presetId);
            userHeaderPresets.setDeleteFlag(CONSTANT_DEFINE.IS_DELETE);

            userHeaderPresetsDao.updateById(userHeaderPresets);
        }
    }
}
