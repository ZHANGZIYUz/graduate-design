package com.example.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.model.domain.SessionOccurrence;
import com.example.backend.service.SessionOccurrenceService;
import com.example.backend.mapper.SessionOccurrenceMapper;
import org.springframework.stereotype.Service;

/**
* @author szs
* @description 针对表【session_occurrence】的数据库操作Service实现
* @createDate 2025-04-05 19:28:43
*/
@Service
public class SessionOccurrenceServiceImpl extends ServiceImpl<SessionOccurrenceMapper, SessionOccurrence>
    implements SessionOccurrenceService{

}




