package com.diana.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.diana.api.common.ErrorCode;
import com.diana.api.exception.BusinessException;
import com.diana.api.exception.ThrowUtils;
import com.diana.api.mapper.UserInterfaceInfoMapper;
import com.diana.api.model.entity.UserInterfaceInfo;
import com.diana.api.service.UserInterfaceInfoService;
import org.springframework.stereotype.Service;

/**
* @author 凉冰
* @description 针对表【user_interface_info(用户调用接口关系表)】的数据库操作Service实现
* @createDate 2023-04-03 14:28:40
*/
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
    implements UserInterfaceInfoService{

    @Override
    public void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add) {
        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Long id = userInterfaceInfo.getId();
        Long userId = userInterfaceInfo.getUserId();
        Long interfaceInfoId = userInterfaceInfo.getInterfaceInfoId();
        Integer totalNum = userInterfaceInfo.getTotalNum();
        Integer leftNum = userInterfaceInfo.getLeftNum();
        Integer status = userInterfaceInfo.getStatus();

        // 创建时，参数不能为空
        if (add) {
            ThrowUtils.throwIf((userId == null || interfaceInfoId == null), ErrorCode.PARAMS_ERROR, "用户或接口不存在");
        }
        // 有参数则校验
        if (totalNum < 1) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "总调用次数过少");
        }
        if (leftNum < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "剩余调用次数不应该是负数");
        }

    }


    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        ThrowUtils.throwIf(interfaceInfoId < 0 || userId < 0, ErrorCode.PARAMS_ERROR);
        // 更新调用次数 --- 使用写sql的方式
        UpdateWrapper<UserInterfaceInfo> query = new UpdateWrapper<>();
        query.eq("interfaceInfoId", interfaceInfoId).eq("userId", userId);
        query.gt("leftNum", 0);
        query.setSql("leftNum = leftNum - 1, totalNum = totalNum + 1");
        return update(query);
    }
}




