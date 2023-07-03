package com.diana.api.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.diana.api.common.ErrorCode;
import com.diana.api.exception.BusinessException;
import com.diana.api.mapper.UserMapper;
import com.diana.apicommon.model.entity.User;
import com.diana.apicommon.service.InnerUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class InnerUserServiceImpl implements InnerUserService {

    @Resource
    private UserMapper userMapper;

    /**
     * 数据库中查是否已分配给用户秘钥
     * @param accessKey 秘钥
     * @return 返回查询到的用户，为null 表示 没有
     */
    @Override
    public User getInvokeUser(String accessKey) {
        if (StringUtils.isAnyBlank(accessKey)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("accessKey", accessKey);
        return userMapper.selectOne(queryWrapper);
    }
}
