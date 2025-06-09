package com.example.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.model.domain.TimetableEntry;
import com.example.backend.service.TimetableEntryService;
import com.example.backend.mapper.TimetableEntryMapper;
import org.springframework.stereotype.Service;

/**
* @author szs
* @description 针对表【timetable_entry】的数据库操作Service实现
* @createDate 2025-04-06 16:41:55
*/
@Service
public class TimetableEntryServiceImpl extends ServiceImpl<TimetableEntryMapper, TimetableEntry>
    implements TimetableEntryService{

}




