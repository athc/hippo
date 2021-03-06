package io.hippo.third.controller;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.extension.api.R;
import io.hippo.third.config.oauth2.SignInHelper;
import io.hippo.third.exception.ElementExistException;
import io.hippo.third.params.UserParams;
import io.hippo.third.service.LoginAndRegisterServiceImpl;
import io.swagger.annotations.Api;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author dellll
 * http://localhost:8013/oauth/token?grant_type=refresh_token&refresh_token=3680e51e-fbf4-417a-85d9-6a8205c14c0a&client_id=user&client_secret=123456
 * http://localhost:8013/oauth/token?client_id=user&client_secret=123456&scope=read&grant_type=client_credentials
 * http://localhost:8013/oauth/token?username=zhangsan&password=123456&grant_type=password&scope=read&client_id=user&client_secret=1234567
 */
@RestController
@RequestMapping("/security")
@Api(description = "注册与登录", tags = "注册与登录")
public class LoginController {

  private final LoginAndRegisterServiceImpl loginAndRegisterService;

  private final SignInHelper signInHelper;


  @Autowired public LoginController(LoginAndRegisterServiceImpl loginAndRegisterService, SignInHelper signInHelper) {
    this.loginAndRegisterService = loginAndRegisterService;
    this.signInHelper = signInHelper;
  }


  @PostMapping(value = "/login/in")
  public R loginIn(@RequestBody UserParams userParams, @ApiIgnore HttpServletRequest request) throws HttpRequestMethodNotSupportedException {
    return R.ok(loginAndRegisterService.login(request, userParams));
  }

  @PostMapping(value = "/two/step/login/in")
  public R twoStepLoginIn() {
    return R.ok(signInHelper.signInTwoStep());
  }

  @PostMapping(value = "/login/out")
  public R loginOut() {
    return R.ok(signInHelper.logout());
  }

  @PostMapping(value = "/register")
  public R register(@RequestBody UserParams userParams) {
    Assert.notNull(userParams.getAccount(), "账号不能为空");
    Assert.notNull(userParams.getPassword(), "密码不能为空");
    try {
      return R.ok(loginAndRegisterService.register(userParams));
    } catch (ElementExistException e) {
      e.printStackTrace();
      return R.failed(e.getMessage());
    }
  }

}
