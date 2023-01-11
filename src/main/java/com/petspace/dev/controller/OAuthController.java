package com.petspace.dev.controller;

import com.petspace.dev.service.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/oauth")
public class OAuthController{

    @Autowired
    OAuthService ks;

    @GetMapping("/logIn")
    public String logInPage(){
        return "logInView";
    }

    @GetMapping("/kakao")
    public String getCI(@RequestParam String code, Model model) throws IOException{
        System.out.println("code = " + code);
        String access_token = ks.getToken(code);
        Map<String, Object> userInfo = ks.getUserInfo(access_token);
        model.addAttribute("code", code);
        model.addAttribute("access_token", access_token);
        model.addAttribute("userInfo", userInfo);

        //ci는 비즈니스 전환후 검수신청 -> 허락받아야 수집 가능
        return "index";
    }

}




//@Controller
//@RequestMapping("/oauth")
//public class OAuthController {
//
//    public final String REST_API_KEY = "55fa1de73c35cdd19f21553bd0e42ab9";
//    private final String REDIRECT_URI = "localhost:8080/oauth";
//    // https://frogand.tistory.com/91
//    private RestTemplate restTemp = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
//
//    // TODO FE) 임시 로그인 창
//    @GetMapping()
//    public String getLoginView(){
//        return "logInView"; // 카카오 로그인 버튼 기능이 달려있는 Activity 이다.
//    }
//
//    /**
//     * 인가 코드 받아오기 // TODO 추후에 FE 에서 localhost:8080/oauth/logIn
//     */
//    @GetMapping("/logIn")
//    public String oauthlogIn()
//    {
//        // code 받아오기. parameter 값을 넘겨준다.
//        String result = restTemp.getForObject("https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=${REST_API_KEY}&redirect_uri=${REDIRECT_URI}"
//        , String.class, REST_API_KEY, REDIRECT_URI);
//
//        return REDIRECT_URI;
//
//        //System.out.println("@@@ this is result @@@\n"+result);
//
////        @GetMapping("https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=${REST_API_KEY}&redirect_uri=${REDIRECT_URI}")
////        public getAuthCode(@RequestParam(name = REST_API_KEY, value = REST_API_KEY)
////        @RequestParam(name = REDIRECT_URI, value = REDIRECT_URI) String param_redirect)
//
//        // return result;
//    }
//
//    @GetMapping("/redirect")
//    @ResponseBody
//    public String printRedirectResult(){
//        return response
//    }
//
//
//    /**
//     * 토큰 받아오기
//     */
////    @GetMapping("/token")
////    public String getToken(@RequestBody){
////        return "logInView";
////    }
//
//
//
//}
