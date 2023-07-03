package com.diana.api.service.impl.inner;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.diana.api.common.ErrorCode;
import com.diana.api.exception.BusinessException;
import com.diana.api.model.entity.UserInterfaceInfo;
import com.diana.api.service.UserInterfaceInfoService;
import com.diana.apicommon.service.InnerUserInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    /**
     * 接口调用次数 + 1
     * @param interfaceInfoId 接口id
     * @param userId 调用用户id
     * @return 调用 UserInterfaceInfoServiceImpl 中实现的方法
     */
    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        return userInterfaceInfoService.invokeCount(interfaceInfoId, userId);
    }

    /**
     * 根据 用户id  和 接口id  查询剩余次数
     * @return
     */
    @Override
    public int getCountByUserIdAndInterfaceInfoId(Long userId, Long interfaceinfoId) {
        LambdaQueryWrapper<UserInterfaceInfo> query = new LambdaQueryWrapper<>();
        query.eq(UserInterfaceInfo::getUserId, userId).eq(UserInterfaceInfo::getInterfaceInfoId, interfaceinfoId);
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoService.getOne(query);
        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数错误");
        }
        // 返回剩余次数
        return userInterfaceInfo.getLeftNum();
    }
}
