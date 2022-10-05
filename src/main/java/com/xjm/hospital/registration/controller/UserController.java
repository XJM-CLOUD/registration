package com.xjm.hospital.registration.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.xjm.hospital.registration.entity.UserEntity;
import com.xjm.hospital.registration.query.UserQuery;
import com.xjm.hospital.registration.repository.UserEntityRepository;
import com.xjm.hospital.registration.resp.ErrorCodeEnum;
import com.xjm.hospital.registration.resp.ResponseResult;
import com.xjm.hospital.registration.util.JwtTokenUtils;
import com.xjm.hospital.registration.util.UserUtil;
import com.xjm.hospital.registration.vo.PageRespVO;
import com.xjm.hospital.registration.vo.UserInfoRespVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("user")
public class UserController {

    @Resource
    private UserEntityRepository userEntityRepository;

    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("list")
    public ResponseResult<PageRespVO> list(UserQuery userQuery) {
        Specification spc = (root, query, cb) -> {
            List<Predicate> andPredicateList = Lists.newArrayList();
            if (ObjectUtil.isNotEmpty(userQuery.getUserName())) {
                andPredicateList.add(cb.like(root.get("userName"),
                        StrUtil.concat(true, "%", userQuery.getUserName(), "%")));
            }
            if (ObjectUtil.isNotEmpty(userQuery.getPhone())) {
                andPredicateList.add(cb.like(root.get("phone"),
                        StrUtil.concat(true, "%", userQuery.getPhone(), "%")));
            }
            Predicate[] andPredicates = andPredicateList.toArray(new Predicate[andPredicateList.size()]);
            return cb.and(cb.and(andPredicates));
        };

        // JPA分页默认从0开始
        Pageable pageable = PageRequest.of(userQuery.getPage() - 1, userQuery.getLimit());
        Page page = userEntityRepository.findAll(spc, pageable);
        PageRespVO pageResp = new PageRespVO(page.getContent(), page.getTotalElements());
        return ResponseResult.success(pageResp);
    }

    @GetMapping("getInfo")
    public ResponseResult<UserInfoRespVO> getInfo(HttpServletRequest request) {
        String authToken = UserUtil.getToken(request);
        String username = JwtTokenUtils.parseToken(authToken);
        Optional<UserEntity> userOptional = userEntityRepository.findByUserName(username);
        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            List<String> roles = StrUtil.split(user.getRoles(), StrUtil.C_COMMA);
            return ResponseResult.success(new UserInfoRespVO(user.getRealName(), user.getAvatar(), roles));
        }
        return ResponseResult.failed(ErrorCodeEnum.USER_NOT_FIND);
    }

    @GetMapping("detail")
    public ResponseResult<UserEntity> detail(Long userId) {
        Optional<UserEntity> byId = userEntityRepository.findById(userId);
        if (byId.isPresent()) {
            return ResponseResult.success(byId.get());
        }
        return ResponseResult.failed(ErrorCodeEnum.USER_NOT_FIND);
    }

    @PostMapping("save")
    @Transactional
    public ResponseResult<UserDetails> save(@RequestBody UserEntity user) {
        Optional<UserEntity> userEntityOptional = userEntityRepository.findById(user.getUserId());
        if (userEntityOptional.isPresent()) {
            // 修改
            UserEntity userEntity = userEntityOptional.get();
            if (ObjectUtil.isNotEmpty(user.getEnableStatus())) {
                userEntity.setEnableStatus(user.getEnableStatus());
            }
            if (ObjectUtil.isNotEmpty(user.getRoles())) {
                userEntity.setRoles(user.getRoles());
            }
            if (ObjectUtil.isNotEmpty(user.getUserPwd())) {
                userEntity.setUserPwd(passwordEncoder.encode(user.getUserPwd()));
            }
        } else {
            // 新增
            user.setCreateTime(LocalDateTime.now());
            user.setUserPwd(passwordEncoder.encode(user.getUserPwd()));
            userEntityRepository.save(user);
        }
        return ResponseResult.success();
    }
}
