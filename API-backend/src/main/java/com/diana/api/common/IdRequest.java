package com.diana.api.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 包装id
 * 将id包装成对象，方便前端传递json数据
 *
 * @author diana
 * 
 */
@Data
public class IdRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}