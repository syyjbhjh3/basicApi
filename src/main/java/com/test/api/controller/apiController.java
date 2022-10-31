package com.test.api.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.api.dto.UserResponseDto;
import com.test.api.entity.User;
import com.test.api.mapping.Message;
import com.test.api.mapping.Message.StatusEnum;
import com.test.api.mapping.UserMapping;
import com.test.api.repo.UserJpaRepo;
import com.test.api.util.AES256;
import com.test.api.util.JwtUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

@Api(tags = {"Api"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api")
public class apiController {
	
	@Autowired
	private UserJpaRepo repo;

	@ApiOperation(value = "회원가입", notes = "회원가입")
	@RequestMapping(value="/signup", method=RequestMethod.POST, produces="application/json; charset=UTF8")
	public Message signup(@ApiParam(value = "회원정보", required = true) @RequestBody Map<String, String> param) throws Exception {
		Message message = new Message();
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		AES256 aes256 = new AES256();
		
		Map userInfo = new HashMap<String, String>();
		
		if(userInfo.containsKey(param.get("name"))) {
			String regNo = getRegNo(param.get("regNo")); 
			if(userInfo.get(param.get("name")).equals(regNo)) {
				List<User> loginInfo = repo.findByUserId(param.get("userId"));
				
				if(loginInfo.isEmpty()) {
					String pwd = encoder.encode(param.get("password"));
			        
			        regNo = aes256.encrypt(regNo);  
			        
					User user = User.builder()
			                .userId(param.get("userId"))
			                .password(pwd) //단방향암호화저장
			                .name(param.get("name"))
			                .regNo(regNo) //양방향암호화
			                .build();		
					repo.save(user);
					 
					message.setStatus(StatusEnum.OK);
			        message.setMessage("회원가입");
				}else {
					message.setStatus(StatusEnum.OK);
			        message.setMessage("이미 가입된 회원입니다.");
				}
			}else {
				message.setStatus(StatusEnum.OK);
		        message.setMessage("가입할 수 없는 정보입니다.");
			}
		}else {
			message.setStatus(StatusEnum.OK);
	        message.setMessage("가입할 수 없는 정보입니다.");
		}
        return message;
	}

	@ApiOperation(value = "로그인", notes = "로그인")
	@RequestMapping(value="/login", method=RequestMethod.POST, produces="application/json; charset=UTF8")
	public Message login(@ApiParam(value = "로그인정보", required = true) @RequestBody Map<String, String> param) throws Exception {
		Message message = new Message();
		JwtUtils jwt = new JwtUtils();
		
		List<User> loginInfo = repo.findByUserId(param.get("userId"));
		
		if(!loginInfo.isEmpty()) {
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			
			if(encoder.matches(param.get("password"), loginInfo.get(0).getPassword())) {
				message.setStatus(StatusEnum.OK);
		        message.setMessage("로그인");
		        
		        Long userNo = loginInfo.get(0).getUserNo();
		        String userId = loginInfo.get(0).getUserId();
		        
		        String token = jwt.createJwt(userNo, userId);
		        
		        Map data = new HashMap<String, String>();
		        data.put("type", "BEARER");
		        data.put("token", token);
		        
		        message.setData(data);
			}else {
				message.setStatus(StatusEnum.OK);
		        message.setMessage("비밀번호를 확인해주세요.");
			}
		}else {
			message.setStatus(StatusEnum.OK);
	        message.setMessage("로그인 정보가 존재하지 않습니다.");
		}
		return message;
	}

	@ApiOperation(value = "내 정보 보기", notes = "내 정보 보기")
	@RequestMapping(value="/me", method=RequestMethod.GET, produces="application/json; charset=UTF8")
	public Message me(@ApiParam(value = "인증토큰", required = true) HttpServletRequest request) throws Exception {
		Message message = new Message();
		
		Map<String, Object> tokenInfo = tokenRead(request);
		
		if(tokenInfo != null) {
			AES256 aes256 = new AES256();
	        
			message.setStatus(StatusEnum.OK);
	        message.setMessage("내정보");
	        
	        Map<String, Object> userinfo = (Map<String, Object>) tokenInfo.get("info");
	        Long userNo = (((Integer) userinfo.get("userNo")).longValue());
	        String userId = (String) userinfo.get("userId");
	        
	        UserMapping loginInfo = repo.findByUserNoAndUserId(userNo, userId);
	        String regNo = aes256.decrypt(loginInfo.getRegNo());
	        UserResponseDto userInfo = new UserResponseDto(loginInfo.getUserNo(), loginInfo.getUserId(), loginInfo.getName(), regNo);
	        
	        message.setData(userInfo);
		}else {
			message.setStatus(StatusEnum.OK);
	        message.setMessage("token 검증 실패");
		}
		return message;
	}

	@ApiOperation(value = "스크랩", notes = "스크랩")
	@RequestMapping(value="/scrap", method=RequestMethod.POST, produces="application/json; charset=UTF8")
	public Message scrap(@ApiParam(value = "인증토큰", required = true) HttpServletRequest request) throws Exception {
		Message message = new Message();
		Map<String, Object> tokenInfo = tokenRead(request);

		if(tokenInfo != null) {
			message.setStatus(StatusEnum.OK);
	        message.setMessage("동기스트랩");
	        
	        Map<String, Object> userinfo = (Map<String, Object>) tokenInfo.get("info");
	        Long userNo = (((Integer) userinfo.get("userNo")).longValue());
	        String userId = (String) userinfo.get("userId");
	        
	        UserMapping loginInfo = repo.findByUserNoAndUserId(userNo, userId);
	        
	        Map paramMap = new HashMap<String, String>();
	        Map resultMap = new HashMap<String, String>();
	        
	        if(loginInfo != null) {
	        	AES256 aes256 = new AES256();
	        	
	        	paramMap.put("name", loginInfo.getName());
	        	paramMap.put("regNo", aes256.decrypt(loginInfo.getRegNo()));
	        	
	        	ObjectMapper mapper = new ObjectMapper();

	        	try {
	        		String json = mapper.writeValueAsString(paramMap);
	        		resultMap = callApi("", json);
	        	}catch(Exception e) {
	        		e.printStackTrace();
	        	}
	        	message.setData(resultMap);
	        }else {
	        	message.setStatus(StatusEnum.OK);
		        message.setMessage("회원정보 없음");
	        }
		}else {
			message.setStatus(StatusEnum.OK);
	        message.setMessage("token 검증 실패");
		}
		return message;
	}

	public String getRegNo(String regNo) {
		if (regNo == null)
			return "";
		if (regNo.length() <= 0 || (regNo.length() != 13 && regNo.length() != 14))
			return regNo;
		String strRet = "";
		if (regNo.length() == 13) {
			strRet = regNo.substring(0, 6) + "-" + regNo.substring(6);
		} else if (regNo.length() == 14) {
			strRet = regNo;
		}
		return strRet;
	}
	
	public Map tokenRead(HttpServletRequest request) throws Exception {
		Map<String, Object> tokenInfo = new HashMap<String, Object>();
		JwtUtils jwt = new JwtUtils();
		
		String token = request.getHeader("Authorization");
		
		if(!"".equals(token)) {
			if(Pattern.matches("^Bearer .*", token)) {
				token = token.replaceAll("^Bearer( )*", "");
				tokenInfo = jwt.verifyJWT(token);
			}
		}
		return tokenInfo;
	}
	
	 public Map callApi(String sendUrl, String jsonValue) throws IllegalStateException {
		 Map data = new HashMap<>();
		 
		 try {
             URL url = new URL(sendUrl);
             HttpURLConnection conn = (HttpURLConnection)url.openConnection();
             conn.setDoOutput(true);
             conn.setRequestMethod("POST");
             conn.setRequestProperty("Content-Type", "application/json");
             conn.setRequestProperty("Accept", "application/json");
             conn.setConnectTimeout(20000);
             conn.setReadTimeout(20000);
            
             OutputStream os = conn.getOutputStream();
             os.write(jsonValue.getBytes("UTF-8"));
             os.flush();
			 			    
			 // 데이터  읽어오기
			 BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			 StringBuilder sb = new StringBuilder();
			 String line = "";
			 while((line = br.readLine()) != null) {
			 	sb.append(line);
			 }
			 conn.disconnect();

			 // JSON Parsing
			 JSONObject jsonObj = (JSONObject) new JSONParser().parse(sb.toString());

			 data = (Map) jsonObj.get("data");
		 }catch (Exception e) {
			e.printStackTrace();
		}
        return data;
    }
}