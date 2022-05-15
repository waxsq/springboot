package com.qingge.springboot.service;

import com.qingge.springboot.entity.Dict;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 青哥哥
 * @since 2022-04-08
 */
public interface IDictService extends IService<Dict> {

    List<Dict> getAllIcon();
}
