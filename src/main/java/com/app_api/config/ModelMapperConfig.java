package com.app_api.config;

import com.app_api.dto.response.UserResponse;
import com.app_api.resource.entity.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // User, UserResponse
        modelMapper.addMappings(
                new PropertyMap<User, UserResponse>() {
                    @Override
                    protected void configure() {
                        map().setRole(source.getRole().getValue());
                    }
                });

        return modelMapper;
    }

}
