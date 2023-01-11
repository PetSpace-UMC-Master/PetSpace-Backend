package com.petspace.dev.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.petspace.dev.config.BaseException;
import com.petspace.dev.domain.HostPermission;
import com.petspace.dev.domain.OauthProvider;
import com.petspace.dev.domain.Status;
import com.petspace.dev.domain.User;
import com.petspace.dev.dto.LoginResponseDto;
import com.petspace.dev.dto.ResponseDto;
import com.petspace.dev.dto.SessionUserDto;
import com.petspace.dev.repository.UserRepository;
import com.petspace.dev.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

import static com.petspace.dev.config.BaseResponseStatus.POST_USERS_EXISTS_EMAIL;
import static com.petspace.dev.config.BaseResponseStatus.POST_USERS_WRONG_INPUT;

@Service
@Component
@Transactional(readOnly = true)
public class  UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${spring.security.oauth2.client.registration.kakao.clientId}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.kakao.clientSecret}")
    private String clientSecret;
    @Value("${spring.security.oauth2.client.registration.kakao.authorizationGrantType}")
    private String GrantType;
    @Value("${spring.security.oauth2.client.registration.kakao.redirectUri}")
    private String redirectUri;
    @Value("${spring.security.oauth2.client.provider.kakao.tokenUri}")
    private String tokenUri;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional
    public ResponseDto signup(SessionUserDto userDto) throws BaseException {
        String password = passwordEncoder.encode(userDto.getPassword());
        // 비밀번호 암호화
        userDto.setPassword(password);

        String username = userDto.getUsername();
        String nickname = userDto.getNickname();
        String birth = userDto.getBirth();
        String email = userDto.getEmail();
        String imgUrl = userDto.getImgUrl();
        OauthProvider oauthProvider = OauthProvider.NONE;
        HostPermission hostPermission = HostPermission.GUEST;
        Status status = Status.ACTIVE;

        // 이메일 중복 확인

        Optional<User> findByEmails = userRepository.findByEmail(userDto.getEmail());
        if (!findByEmails.isEmpty()) {
            throw new BaseException(POST_USERS_EXISTS_EMAIL);
        }

        User user = new User(username, nickname, birth, email, password, imgUrl, oauthProvider, status, hostPermission);


        userRepository.save(user);

        return new ResponseDto("success", "회원가입에 성공하였습니다", "");

    }

    public String getKaKaoAccessToken(String code) {

        String access_Token = "";
        String refresh_Token = "";
        String reqURL = tokenUri;

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //POST 요청을 위해 기본값이 false인 setDoOutput을 true로
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            //POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=" + GrantType);
            sb.append("&client_id=" + clientId);
            sb.append("&client_secret=" + clientSecret);
            sb.append("&redirect_uri=" + redirectUri);
            sb.append("&code=" + code);
            System.out.println("code : " + code);
            bw.write(sb.toString());
            bw.flush();

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }

            //Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            access_Token = element.getAsJsonObject().get("access_token").getAsString();
            refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();

            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return access_Token;
    }

    @Transactional
    public Object createKakaoUser(String token) {
        HttpServletResponse response;
        String reqURL = "https://kapi.kakao.com/v2/user/me";

        //access_token을 이용하여 사용자 정보 조회
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + token); //전송할 header 작성, access_token전송

            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }

            //Gson 라이브러리로 JSON파싱
            JsonElement element = JsonParser.parseString(result);

            Long id = element.getAsJsonObject().get("id").getAsLong();
            boolean hasEmail = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("has_email").getAsBoolean();
            boolean hasBirth = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("has_birthday").getAsBoolean();

            String email = "";
            String birth = "";
            String nickname = "";
            String imgUrl = "";
            String kakaoId = "";
            String password = "";

            if (hasEmail) {
                email = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("email").getAsString();
            }

            if (hasBirth) {
                birth = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("birthday").getAsString();
            }

            nickname = element.getAsJsonObject().get("properties").getAsJsonObject().get("nickname").getAsString();
            imgUrl = element.getAsJsonObject().get("properties").getAsJsonObject().get("profile_image").getAsString();
            kakaoId = element.getAsJsonObject().get("id").getAsString();

            OauthProvider oauthProvider = OauthProvider.KAKAO;
            Status status = Status.ACTIVE;
            HostPermission hostPermission = HostPermission.GUEST;


            Optional<User> findByEmails = userRepository.findByEmail(email);
            if (!findByEmails.isEmpty()) {
                User user = new User(kakaoId, nickname, birth, email, password, imgUrl, oauthProvider, status, hostPermission);
                user.setImgUrl(imgUrl);
                String jwtToken = jwtTokenProvider.createToken(user.getEmail());
                return jwtToken;
            } else {

                User user = new User(kakaoId, nickname, birth, email, password, imgUrl, oauthProvider, status, hostPermission);
                userRepository.save(user);
                br.close();
                String jwtToken = jwtTokenProvider.createToken(user.getEmail());
                return jwtToken;
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
        return null;
    }

    private void kakaoUsersAuthorizationInput(User user, HttpServletResponse response) {
        // response header에 token 추가
        String token = jwtTokenProvider.createToken(user.getEmail());
        response.addHeader("Authorization", "BEARER" + " " + token);
    }

    //로그인 로직
    public Object login(String email, String password) throws BaseException {

        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return new BaseException(POST_USERS_EXISTS_EMAIL);
        }
        
        // 패스워드 암호화
        if (!passwordEncoder.matches(password, user.get().getPassword())) {
            return new BaseException(POST_USERS_WRONG_INPUT);
        }

        return user;
    }


}
