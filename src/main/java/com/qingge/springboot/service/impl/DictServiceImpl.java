package com.qingge.springboot.service.impl;

import com.qingge.springboot.entity.Dict;
import com.qingge.springboot.mapper.DictMapper;
import com.qingge.springboot.service.IDictService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 青哥哥
 * @since 2022-04-08
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements IDictService {

    @Resource
    private DictMapper dictMapper;
    @Override
    public List<Dict> getAllIcon() {
        return dictMapper.selectList(null);
    }
}
