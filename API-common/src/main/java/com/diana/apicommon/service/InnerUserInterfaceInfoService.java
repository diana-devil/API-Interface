package com.diana.apicommon.service;

/**
 *
 */
public interface InnerUserInterfaceInfoService {

    /**
     * 调用接口统计
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(long interfaceInfoId, long userId);


    /**
     * 根据 用户id  和 接口id  查询剩余次数
     * @return
     */
    public int getCountByUserIdAndInterfaceInfoId(Long userId, Long interfaceinfoId);

}
