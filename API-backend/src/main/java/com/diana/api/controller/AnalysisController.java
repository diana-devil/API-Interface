package com.diana.api.controller;

import com.diana.api.annotation.AuthCheck;
import com.diana.api.common.BaseResponse;
import com.diana.api.common.ResultUtils;
import com.diana.api.mapper.UserInterfaceInfoMapper;
import com.diana.api.model.entity.UserInterfaceInfo;
import com.diana.api.model.vo.InterfaceInfoVO;
import com.diana.api.service.InterfaceInfoService;
import com.diana.apicommon.model.entity.InterfaceInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static com.diana.api.constant.UserConstant.ADMIN_ROLE;

/**
 * @ClassName AnalysisController
 * @Date 2023/4/11 15:14
 * @Author diane
 * @Description 统计接口调用次数
 *      先查 user_interface_info 表，按照调用次数的总和排好序，调用高的排在前面
 *      在查 interface_info 表， 根据上面查出来的 接口id，获取对应的接口信息，并且封装到VO中返回
 * @Version 1.0
 */
@RestController
@RequestMapping("/analysis")
@Slf4j
public class AnalysisController {

    @Resource
    UserInterfaceInfoMapper mapper;

    @Resource
    InterfaceInfoService service;

    @GetMapping("/top/interface/invoke")
    @AuthCheck(mustRole = ADMIN_ROLE)
    public BaseResponse<List<InterfaceInfoVO>> listTopInvokeInterfaceInfo() {
        int limit = 3;
        // 聚合接口调用次数
        List<UserInterfaceInfo> userInterfaceInfos = mapper.listTopInvokeInterfaceInfo(limit);
        List<InterfaceInfoVO> interfaceInfoVOList = userInterfaceInfos.stream().map(s -> {
            // 根据 接口id 查询接口信息
            InterfaceInfo interfaceInfo = service.getById(s.getInterfaceInfoId());
            // 新建 vo对象，并copy 接口信息
            InterfaceInfoVO interfaceInfoVO = new InterfaceInfoVO();
            BeanUtils.copyProperties(interfaceInfo, interfaceInfoVO);
            interfaceInfoVO.setTotalNum(s.getTotalNum());
            return interfaceInfoVO;
        }).collect(Collectors.toList());
        return ResultUtils.success(interfaceInfoVOList);
    }

}
