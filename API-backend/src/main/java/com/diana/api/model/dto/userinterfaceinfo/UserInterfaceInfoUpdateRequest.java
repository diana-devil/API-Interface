package com.diana.api.model.dto.userinterfaceinfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 更新请求
 * 仅管理员
 *
 * @author diana
 * 
 */
@Data
public class UserInterfaceInfoUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;


    /**
     * 总调用次数
     */
    private Integer totalNum;

    /**
     * 剩余调用次数
     */
    private Integer leftNum;

    /**
     * 0-正常， 1-禁用
     */
    private Integer status;


}