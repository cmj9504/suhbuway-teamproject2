package project.suhbuway.controller;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.JsonNode;

import project.suhbuway.dto.User;
import project.suhbuway.security.KakaoAccessToken;
import project.suhbuway.service.user.UserService;

/**
 * 유저 
 */
@Controller
public class UserController {
	//@Autowired
	private KakaoAccessToken kakao = new KakaoAccessToken();
	
	@Autowired
	private UserService service;

	/**
	 *  로그인폼
	 */
	@RequestMapping("/user/login")
	public void login() {}

	/**
	 *  로그인 실패: FailHandler의 tiles 적용위한 controller
	 */
	@RequestMapping("/errorLogin")
	public ModelAndView test(HttpServletRequest request) {
		String errorMessage =(String)request.getAttribute("errorMessage");
		return new ModelAndView("user/login" , "errorMessage" ,errorMessage );
	}

	/**
	 *  회원가입폼
	 */
	@RequestMapping("/user/register")
	public void registerForm() {}

	/**
	 * 아이디 중복확인 Ajax
	 */
	@RequestMapping("/idcheckAjax")
	@ResponseBody
	public String idCheckAjax(HttpServletRequest request) {
		//System.out.println("컨트롤러 id체크 : " + request.getParameter("userId") );
		return service.idcheck(request.getParameter("userId"));
	}
	/**
	 * 쇼셜 아이디 로그인 방지 Ajax
	 */
	@RequestMapping("/socialCheckAjax")
	@ResponseBody
	public String socialCheckAjax( String userId ) {
		User dbUser = service.selectUserById(userId);
		if( dbUser.getSocialType() != null ) { // 쇼셜가입이면
			return "fail";
		}
		return "ok";
	}
	
	/**
	 *  회원가입하기
	 */
	@RequestMapping("/userRegister")
	public String register( User user ) {
		
		// 프로필 파일 경로 설정
		
		// 이름
		//System.out.println("회원가입 file 이름: " + user.getFile().getName() );
		//System.out.println("회원가입 file 이름: " + user.getFile().getSize() );
		
		// 해당 컬럼에 추가해주기 setPro~~(get )
		
		// 수정된 user를 회원가입에 넣기
		service.joinUser(user);
		return "index";  
	}

	/**
	 * kakao 로그인
	 */
	@RequestMapping(value = "/kakaoLogin", produces = "application/json")
	public String kakaoLogin(@RequestParam("code") String code, Model model, HttpSession session) {
		
		// 로그인 후 결과값 
		JsonNode node = kakao.getKakaoAccessToken(code);  

		// 토큰 값 문자열 변환
		String accessToken = node.get("access_token").toString(); 
		String refreshToken = node.get("refresh_token").toString().replace("\"", ""); // "" 있어서 제거
		//System.out.println("refreshToken: " + refreshToken );
		
		// 사용자 정보 가져오기: 닉네임
		Map<String, Object> userInfo = kakao.getUserInfo(accessToken);
		
		// 기존 id랑 비교
		String returnUrl="";
		String kakaoId= (String)userInfo.get("id");
		User dbUser = service.selectUserById(kakaoId);
		
		// 기존회원이면서 refreshToken이 다르면 값 변경
		if( dbUser!=null ) { 
			if( dbUser.getSocialToken()==null || !dbUser.getSocialToken().equals(refreshToken) ) {
				dbUser.setSocialToken(refreshToken);
				service.updateBySocialToken( kakaoId , refreshToken ); 
			}
			// 세션 담아줌
			session.setAttribute("kakaoToken", accessToken);
			session.setAttribute( "userId" , kakaoId );
			returnUrl="index";
			
		// 회원가입 페이지로 이동	
		} else {
			model.addAttribute("userId", kakaoId );
			model.addAttribute("userName", (String)userInfo.get("nickname") );
			model.addAttribute("socialType", "kakao" );
			model.addAttribute("socialToken", refreshToken );
			returnUrl="user/register";
		}
		return returnUrl;
	}
	/**
	 * kakao로그아웃
	 */
	@RequestMapping(value = "/kakaoLogout", produces = "application/json")
    public String Logout(HttpSession session) {
		
		// 카카오 로그아웃 - 사용자 토큰을 만료는 시키는 역할
        JsonNode node = kakao.Logout(session.getAttribute("kakaoToken").toString());
        System.out.println("로그인 후 반환되는 아이디 : " + node.get("id"));
        
        // 세션 지움
        session.removeAttribute("kakaoToken");
        session.removeAttribute("userId");
        session.invalidate();//전체세션 지움
        return "redirect:/";
    }
	
	/**
	 *  마이페이지폼
	 */
	@RequestMapping("/user/myPage")
	public void myPage() {}

}
