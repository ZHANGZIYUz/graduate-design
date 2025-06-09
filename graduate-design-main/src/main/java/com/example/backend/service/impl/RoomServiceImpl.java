package com.example.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.model.domain.Room;
import com.example.backend.service.RoomService;
import com.example.backend.mapper.RoomMapper;
import org.springframework.stereotype.Service;

/**
* @author szs
* @description 针对表【room】的数据库操作Service实现
* @createDate 2025-04-06 16:38:45
*/
@Service
public class RoomServiceImpl extends ServiceImpl<RoomMapper, Room>
    implements RoomService{

}




