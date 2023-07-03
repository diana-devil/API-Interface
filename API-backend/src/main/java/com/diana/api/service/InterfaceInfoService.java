package com.diana.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.diana.apicommon.model.entity.InterfaceInfo;


/**
* @author 凉冰
* @description 针对表【interface_info(接口信息表)】的数据库操作Service
* @createDate 2023-03-29 21:46:07
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    /**
     * 校验
     * @param interfaceInfo
     * @param b
     */
    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean b);

}
