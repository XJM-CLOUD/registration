package com.xjm.hospital.registration.security;

import cn.hutool.core.util.ObjectUtil;
import com.xjm.hospital.registration.consts.StatusConsts;
import com.xjm.hospital.registration.entity.UserEntity;
import com.xjm.hospital.registration.repository.UserEntityRepository;
import com.xjm.hospital.registration.resp.ErrorCodeEnum;
import com.xjm.hospital.registration.security.vo.MyUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private UserEntityRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userEntity = repository.findByUserName(username);
        UserEntity entity = userEntity.orElseThrow(() -> new UsernameNotFoundException(ErrorCodeEnum.USER_NOT_FIND.getMessage()));
        if(ObjectUtil.equal(entity.getEnableStatus(), StatusConsts.DISABLED_STATUS)){
            throw new UsernameNotFoundException(ErrorCodeEnum.USER_NOT_FIND.getMessage());
        }
        return new MyUserDetails(entity.getUserId(), entity.getUserName(), entity.getUserPwd(), entity.getRoles(), null, null);
    }
}
