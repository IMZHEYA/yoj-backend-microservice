package com.yupi.yojbackendserviceclient.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.yojbackendcommon.common.ErrorCode;
import com.yupi.yojbackendcommon.exception.BusinessException;
import com.yupi.yojbackendmodel.model.dto.user.UserQueryRequest;
import com.yupi.yojbackendmodel.model.entity.User;
import com.yupi.yojbackendmodel.model.enums.UserRoleEnum;
import com.yupi.yojbackendmodel.model.vo.LoginUserVO;
import com.yupi.yojbackendmodel.model.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.yupi.yojbackendcommon.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户服务
 *
 * @author <a href="https://github.com/liyupi">段元哲</a>
 * @from <a href="https://yupi.icu">dyz</a>
 */
@FeignClient(name = "yoj-backend-user-service",path = "/api/user/inner")
public interface UserFeignClient {

    /**
     * userService.getById(userId)
     * userService.getUserVO(user)
     * userService.listByIds(userIdSet)
     * userService.isAdmin(loginUser)
     * userService.getLoginUser(request)
     */
    @GetMapping("/get/id")
    User getById(@RequestParam("userId") long userId);


    @GetMapping("/get/ids")
    List<User> listByIds(@RequestParam("idList")Collection<Long> idList);


    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    default User getLoginUser(HttpServletRequest request){
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }

    /**
     * 是否为管理员
     *
     * @param user
     * @return
     */
   default boolean isAdmin(User user){
       return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
   }

    /**
     * 获取脱敏的用户信息
     *
     * @param user
     * @return
     */
    default  UserVO getUserVO(User user){
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }





}
