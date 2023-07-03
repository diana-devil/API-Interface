package com.diana.api.model.dto.interfaceinfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 接口调用请求
 *
 * @author diana
 * 
 */
@Data
public class InterfaceInfoInvokeRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 用户请求参数
     */
    private String userRequestParams;



}