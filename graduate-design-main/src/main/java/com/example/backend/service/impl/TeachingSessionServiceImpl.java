package com.example.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.model.domain.TeachingSession;
import com.example.backend.service.TeachingSessionService;
import com.example.backend.mapper.TeachingSessionMapper;
import org.springframework.stereotype.Service;

/**
* @author szs
* @description 针对表【teaching_session】的数据库操作Service实现
* @createDate 2025-04-05 22:01:49
*/
@Service
public class TeachingSessionServiceImpl extends ServiceImpl<TeachingSessionMapper, TeachingSession>
    implements TeachingSessionService{

}




