package com.diana.api.service;

import com.diana.api.model.entity.UserInterfaceInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 凉冰
* @description 针对表【user_interface_info(用户调用接口关系表)】的数据库操作Service
* @createDate 2023-04-03 14:28:40
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    /**
     * 校验
     * @param userInterfaceInfo
     * @param b
     */
    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean b);


    /**
     * 调用接口统计
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(long interfaceInfoId, long userId);

}
