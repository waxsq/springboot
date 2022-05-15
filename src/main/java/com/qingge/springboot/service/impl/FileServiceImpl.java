package com.qingge.springboot.service.impl;

import com.qingge.springboot.entity.Files;
import com.qingge.springboot.mapper.FileMapper;
import com.qingge.springboot.service.IFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 青哥哥
 * @since 2022-04-13
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, Files> implements IFileService {

}
