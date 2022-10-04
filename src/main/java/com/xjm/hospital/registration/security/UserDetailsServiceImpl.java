package com.xjm.hospital.registration.security;

import com.xjm.hospital.registration.entity.UserEntity;
import com.xjm.hospital.registration.repository.UserEntityRepository;
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
        UserEntity entity = userEntity.orElseThrow(() -> new UsernameNotFoundException("用户名不存在"));

        return new MyUserDetails(entity.getUserName(), entity.getUserPwd(), entity.getRoles());
    }
}
