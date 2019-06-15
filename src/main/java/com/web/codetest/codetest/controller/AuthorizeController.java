package com.web.codetest.codetest.controller;

import com.web.codetest.codetest.dto.AccessTokenDTO;
import com.web.codetest.codetest.dto.GithubUser;
import com.web.codetest.codetest.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client_id}")
    private String client_id;

    @Value("${github.client_secret}")
    private String client_secret;

    @Value("${github.redirect_uri}")
    private String redirect_uri;


    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name="state") String state){

        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(client_id);
        accessTokenDTO.setClient_secret(client_secret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirect_uri);
        accessTokenDTO.setState(state);


        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = this.githubProvider.gethubUser(accessToken);
        if(githubUser != null){

            System.out.println(githubUser);

        }
        return "index";
    }
}
