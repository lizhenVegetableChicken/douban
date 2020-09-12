package fm.douban.app.control;

import fm.douban.model.*;
import fm.douban.param.UserQueryParam;
import fm.douban.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UserControl {

    private static final Logger LOG = LoggerFactory.getLogger(UserControl.class);

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login(Model model) {
        return "user/login";
    }

    @PostMapping("/authenticate")
    //@ResponseBody
    public String login(@RequestParam String name, @RequestParam String password, HttpServletRequest request,
                     HttpServletResponse response) throws IOException {

        // 根据登录名查询用户
        User loginUser = getUserByName(name);

        // 找不到此登录用户
        if (loginUser == null) {
            return "user/result/loginerror";
        }

        if (loginUser.getPassword().equals(password)) {
            UserLoginInfo userLoginInfo = new UserLoginInfo();
            userLoginInfo.setUserId("123456789abcd");
            userLoginInfo.setUserName(name);
            // 取得 HttpSession 对象
            HttpSession session = request.getSession();
            // 写入登录信息
            session.setAttribute("userLoginInfo", userLoginInfo);
            return "user/result/loginsuccess";
        } else {
            return "user/result/loginerror";
        }
    }

    @GetMapping("/sign")
    public String signPage(Model modle) {
        return "user/sign";
    }

    @PostMapping("/register")
    //@ResponseBody
    public String registerAction(@RequestParam String loginName, @RequestParam String password, @RequestParam String mobile,
                           HttpServletRequest request, HttpServletResponse response) {
        LOG.info("user name: "+loginName+" password: "+password+" mobile: "+mobile);
        User registeredUser = getUserByName(loginName);

        if (registeredUser != null) {
            return "user/result/signerror";
        } else {
            User newUser = new User();
            newUser.setLoginName(loginName);
            newUser.setPassword(password);
            newUser.setMobile(mobile);
            userService.add(newUser);
            LOG.info("user name: "+newUser.getLoginName()+" password: "+newUser.getPassword()+" mobile: "+newUser.getMobile());
        }

        registeredUser = getUserByName(loginName);

        if (registeredUser == null || registeredUser.getId() == null){
            return "user/result/signerror";
        }

        return "user/result/signsuccess";

    }

    private User getUserByName(String name) {
        UserQueryParam param = new UserQueryParam();
        param.setLoginName(name);
        Page<User> userPage = userService.list(param);
        List<User> users = userPage.getContent();
        if(users.size()>0) {
            return users.get(0);
        }
        return null;
    }

    @GetMapping("/findPassword")
    public String findPwd(Model model) {
        return "user/result/findpwd";
    }

    @GetMapping("/findPassword/result")
    @ResponseBody
    public Map pwdResult(@RequestParam("userName") String userName) {
        Map result = new HashMap<>();
        UserQueryParam param = new UserQueryParam();
        param.setLoginName(userName);
        Page<User> userPage = userService.list(param);
        List<User> users = userPage.getContent();
        if (users.size() == 0) {
            result.put("result","null");
        } else {
            result.put("result", userPage.getContent().get(0).getPassword());
        }
        return result;
    }
}
