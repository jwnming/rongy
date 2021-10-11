package com.cd.service.impl;

import com.cd.dao.ConfigMapper;
import com.cd.entity.Config;
import com.cd.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfigServiceImpl implements ConfigService {

    @Autowired
    private ConfigMapper configMapper;

    @Override
    public List<Config> list() {
        return configMapper.list();
    }
}
