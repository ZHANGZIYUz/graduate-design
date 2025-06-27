package com.example.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.model.domain.Module;
import com.example.backend.service.ModuleService;
import com.example.backend.mapper.ModuleMapper;
import org.springframework.stereotype.Service;

/**
* @author szs
* @description 针对表【module】的数据库操作Service实现
* @createDate 2025-04-05 19:28:40
*/
@Service
public class ModuleServiceImpl extends ServiceImpl<ModuleMapper, Module>
    implements ModuleService{

}




