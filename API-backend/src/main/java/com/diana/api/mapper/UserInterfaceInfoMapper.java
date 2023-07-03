package com.diana.api.mapper;

import com.diana.api.model.entity.UserInterfaceInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author 凉冰
* @description 针对表【user_interface_info(用户调用接口关系表)】的数据库操作Mapper
* @createDate 2023-04-03 14:28:40
* @Entity com.diana.api.model.entity.UserInterfaceInfo
*/
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {

    /**
     * 获取 topN 的调用接口
     * @param limit N
     * @return 用户接口调用关系list集合
     */
    List<UserInterfaceInfo> listTopInvokeInterfaceInfo(int limit);


}




