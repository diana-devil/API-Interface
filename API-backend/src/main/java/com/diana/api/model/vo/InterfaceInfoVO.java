package com.diana.api.model.vo;

import com.diana.apicommon.model.entity.InterfaceInfo;
import lombok.Data;


/**
 * @ClassName InterfaceInfoVO
 * @Date 2023/3/29 22:39
 * @Author diane
 * @Description 接口视图
 * @Version 1.0
 */
@Data
public class InterfaceInfoVO extends InterfaceInfo {

    /**
     * 主键
     */
    private Integer totalNum;

}
