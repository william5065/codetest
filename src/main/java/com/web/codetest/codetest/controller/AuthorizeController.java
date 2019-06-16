package com.web.codetest.codetest.controller;

import com.web.codetest.codetest.dto.AccessTokenDTO;
import com.web.codetest.codetest.dto.GithubUser;
import com.web.codetest.codetest.mapper.UserMapper;
import com.web.codetest.codetest.model.User;
import com.web.codetest.codetest.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {


    @Autowired
    private GithubProvider githubProvider;

    @Autowired
    private UserMapper userMapper;

    @Value("${github.client_id}")
    private String client_id;

    @Value("${github.client_secret}")
    private String client_secret;

    @Value("${github.redirect_uri}")
    private String redirect_uri;


    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name="state") String state,
                           HttpServletRequest request,
                           HttpServletResponse response){

        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(client_id);
        accessTokenDTO.setClient_secret(client_secret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirect_uri);
        accessTokenDTO.setState(state);


        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = this.githubProvider.gethubUser(accessToken);
        if(githubUser != null){
            User user = new User();
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setToken(UUID.randomUUID().toString());
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
            //手动添加cookie

            Cookie cookie = new Cookie("accessToken",user.getToken());
            response.addCookie(cookie);

            //可以直接用下面一句设置session，就可以一把cookie也写进去
//            request.getSession().setAttribute("user",githubUser);

            System.out.println(githubUser);

        }
        return "redirect:/";
    }
    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        String token = (String) request.getSession().getAttribute("accessToken");
        userMapper.deleteToken(token);

//        request.getSession()
//
//
//        .removeAttribute("accessToken");


        return "redirect:/";

    }
}
