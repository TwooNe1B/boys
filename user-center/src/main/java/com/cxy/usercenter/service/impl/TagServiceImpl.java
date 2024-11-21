package com.cxy.usercenter.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxy.usercenter.domain.Tag;
import com.cxy.usercenter.mapper.TagMapper;
import com.cxy.usercenter.service.TagService;
import org.springframework.stereotype.Service;

/**
* @author glh
* @description 针对表【tag】的数据库操作Service实现
* @createDate 2024-11-21 13:46:22
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService{

}




