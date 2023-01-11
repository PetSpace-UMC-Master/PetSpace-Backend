package com.petspace.dev.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.petspace.dev.domain.User;
import com.petspace.dev.repository.UserRepository;
import com.petspace.dev.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    //private final UserRepositoryImpl userRepositoryImpl;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 회원가입
     */
    @Transactional
    public Long singIn(User user) {
        //중복 이메일 검사
        checkEmailDuplicate(user.getEmail());
        User member = userRepository.save(user);

        return user.getId();
    }


    /**
     * 중복 이메일 검사
     */
    private void validateDuplicateUser(User user) {
        Optional<User> findUsers = userRepository.findByEmail(user.getEmail());
        if(!findUsers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        }
    }

    /**
     * 중복 이메일 검사 jpa
     */
    public void checkEmailDuplicate(String email) {
        if(userRepository.existsByEmail(email)) {
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        }
    }

    /**
     * 기본 로그인 -> password 암호화 과정x
     */
    private boolean login(User user) {
        Optional<User> findUsers = userRepository.findByEmail(user.getEmail());
        User findUser = findUsers.get();

        if(findUser == null) {
            return false;
        }

        if(!findUser.getPassword().equals(user.getPassword())) {
            return false;
        }
        return true;
    }

    /**
     * jwt 이용한 로그인 -> password 암호화 과정o
     */
    @Transactional
    public String loginJwt(Map<String, String> user) {
        System.out.println(user.get("email"));
        User loginUser = userRepository.findByEmail(user.get("email"))
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));

        if (!passwordEncoder.matches(user.get("password"), loginUser.getPassword())) {
            throw new IllegalStateException("잘못된 비밀번호입니다.");
        }
        List<String> roles = new ArrayList<>();
        System.out.println(loginUser.getRole());
        roles.add(loginUser.getRole().name());
        return jwtTokenProvider.createToken(loginUser.getUsername(), roles);
    }

    /**
     * 카카오 소셜 로그인 access_token 받기
     */
    public String getKakaoAccessToken(String code) {
        String accessToken = "";
        String refreshToken = "";
        String reqUrl = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // POST 요청을 위해 기본값이 false인 setDoOutput을 true로
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            // POST 요청을 필요로 요구하는 파라미터 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=eabf0b1f969f5a1f60d80dd04b4d28c8"); // TODO REST_API_KEY 입력
            sb.append("&redirect_uri=http://localhost:9070/kakao"); // TODO 인가코드 받은 redirect_uri 입력
            sb.append("&code=" + code);
            bw.write(sb.toString());
            bw.flush();

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode: " + responseCode);
            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            //Gson 라이브러리에 포함된 클래스로 JSON 파싱 객체 생성
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            accessToken = element.getAsJsonObject().get("accessToken").getAsString();
            refreshToken = element.getAsJsonObject().get("refreshToken").getAsString();

            System.out.println("accessToken : " + accessToken);
            System.out.println("refreshToken : " + refreshToken);

            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return accessToken;
    }
}
