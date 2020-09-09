package kr.goodee.controller;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import kr.goodee.exception.LoginException;
import kr.goodee.logic.Item;
import kr.goodee.logic.Sale;
import kr.goodee.logic.SaleItem;
import kr.goodee.logic.ShopService;
import kr.goodee.logic.User;
import kr.goodee.util.CipherUtil;
//http://localhost:8080/shop3/user/mypage.shop
@Controller
@RequestMapping("user")
public class UserController {
	@Autowired
	private ShopService service;
	
	@GetMapping("*")
	public String form(Model model) {
		model.addAttribute(new User());
		return null;
	}
	@PostMapping("userEntry")
	public ModelAndView userEntry(@Valid User user,
			 BindingResult bresult) {
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) {
			bresult.reject("error.input.user");
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		try {
			//1. 비밀번호 : 해쉬알고리즘으로 값을 생성 db에 저장
			//2. 이메일 :  암호화 하여 db에 저장
			//           키는 userid의 해쉬값의 앞16자리로 설정.
			user.setPassword(CipherUtil.makehash(user.getPassword()));
			String userid = CipherUtil.makehash(user.getUserid());
			String email = CipherUtil.encrypt
					   (user.getEmail(),userid.substring(0,16));
			user.setEmail(email);
			service.userInsert(user);
			mav.setViewName("redirect:login.shop");
		} catch(Exception e) {
			e.printStackTrace();
			bresult.reject("error.duplicate.user");
			mav.getModel().putAll(bresult.getModel());
		}
		return mav;
	}
	@PostMapping("login")
	public ModelAndView login(@Valid User user, BindingResult bresult,
			HttpSession session) {
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) {
			bresult.reject("error.input.user");
			return mav;
		}
		//1. db의 정보의 id,password 비교
		//2. 일치 : session loginUser 정보 저장
		//3. 불일치 : 비밀번호 확인 내용 출력
		//4. db에 해당  id정보가 없는 경우 내용 출력
		try {
		   User dbUser = service.getUser(user.getUserid());
		   //user.getPassword() : 입력된 비밀번호
		   String password = CipherUtil.makehash(user.getPassword()); 
		   if(password.equals(dbUser.getPassword())) {
			  String userid = CipherUtil.makehash(user.getUserid());
			  String email = CipherUtil.decrypt (dbUser.getEmail(),
					    		 userid.substring(0,16));
			  dbUser.setEmail(email);
 			  session.setAttribute("loginUser", dbUser); 			  
			  mav.setViewName("redirect:main.shop");
		   } else {
			  bresult.reject("error.login.password");
		   }
		} catch (Exception e) {
			e.printStackTrace();
			bresult.reject("error.login.id");			
		}
		return mav;
	}
	@RequestMapping("logout")
	public String loginChecklogout(HttpSession session) {
		session.invalidate();
		return "redirect:login.shop";
	}
	@RequestMapping("main") 
	//login 되어야 실행 가능. 메서드이름을 loginxxx로 지정
	public String loginCheckmain(HttpSession session) {
		return null;
	}
	/*
	 * AOP 설정하기
	 * 1. UserController의 check로 시작하는 메서드에 매개변수가
	 *    id, session 인 경우
	 *    -로그인 안된경우 : 로그인하세요. => login.shop 페이지 이동
	 *    -admin이 아니면서, 다른 아이디 정보 조회시 . 본인정보만 조회가능 
	 *                   => main.shop 페이지 이동
	 */
	@RequestMapping("mypage")
	public ModelAndView checkmypage(String id, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		User user = service.getUser(id); 
		//sale 테이블에서 saleid,userid,saledate 컬럼값만 저장된 Sale 객체의
		//  List로 리턴
		List<Sale> salelist = service.salelist(id);
		for(Sale sa : salelist) {
			List<SaleItem> saleitemlist = 
					      service.saleItemList(sa.getSaleid());
			for(SaleItem si : saleitemlist) {
				Item item = service.getItem
						       (Integer.parseInt(si.getItemid()));
				si.setItem(item);
			}
			sa.setItemList(saleitemlist);
		}
		//email 복호화 
		try {
			//키값
			String userid = CipherUtil.makehash(user.getUserid());
			String email = CipherUtil.decrypt
					(user.getEmail(),userid.substring(0,16));
			user.setEmail(email);
		} catch (Exception e) {
			e.printStackTrace();
		}
		mav.addObject("user",user);
		mav.addObject("salelist", salelist);
		return mav;
	}
	@GetMapping(value= {"update","delete"})
	public ModelAndView checkview(String id, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		User user = service.getUser(id);
		try {
			String userid = CipherUtil.makehash(user.getUserid());
			String email = CipherUtil.decrypt
					(user.getEmail(),userid.substring(0,16));
			user.setEmail(email);
		} catch (Exception e) {
			e.printStackTrace();
		}
		mav.addObject("user",user);
		return mav;
	}
	/*
	 * 1. 유효성검증
	 * 2. 비밀번호 검증 : 불일치
	 *    유효성 출력으로 error.login.password 코드로 실행
	 * 3. 비밀번호 일치
	 *    update 실행.
	 *    로그인정보 수정. 단 admin이 다른사람의 정보 수정시는 로그인 정보 수정 안됨
	 * 4. 수정완료 => mypage.shop으로 페이지 이동.      
	 */
	
	@PostMapping("update")
	public ModelAndView checkupdate(@Valid User user, 
			BindingResult bresult, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		//유효성 검증
		if(bresult.hasErrors()) {
			bresult.reject("error.input.user");
			return mav;
		}
		//비밀번호 검증
		User loginUser = (User)session.getAttribute("loginUser");
		//로그인한 정보의 비밀번호와 입력된 비밀번호 검증
		String password = null;
		try {
			password = CipherUtil.makehash(user.getPassword());
		} catch (Exception e1) {
			e1.printStackTrace();
		} 
		if(!password.equals(loginUser.getPassword())) {
			bresult.reject("error.login.password");
			return mav;
		}
		//비밀번호 일치 : 수정 가능 
		try {
			String userid = CipherUtil.makehash(user.getUserid());
			String email = CipherUtil.encrypt
					   (user.getEmail(),userid.substring(0,16));
			user.setEmail(email);
			service.userUpdate(user);
			mav.setViewName
			   ("redirect:mypage.shop?id="+user.getUserid());
			//로그인 정보 수정
			if(loginUser.getUserid().equals(user.getUserid())) {
			    user.setEmail
			    (CipherUtil.decrypt(email,userid.substring(0,16)));
				user.setPassword
				(CipherUtil.makehash(user.getPassword()));
				session.setAttribute("loginUser", user);
			}
		} catch (Exception e) {
			e.printStackTrace();
			bresult.reject("error.user.update");
		}
		return mav;
	}	
	/*
	 * 회원 탈퇴
	 *   1. 비밀번호 검증 불일치 : "비밀번호 오류 메세지 출력. delete.shop 이동
	 *   2. 비밀번호 검증 일치
	 *      회원db에서 delete 하기
	 *      본인인경우 : logout 하고. login.shop 페이지 요청
	 *      관리자인경우 : main.shop으로 페이지 이동 
	 */	
	@PostMapping("delete")
	public ModelAndView checkdelete
	         (String userid,String password,HttpSession session) {
		ModelAndView mav = new ModelAndView();
		User loginUser = (User)session.getAttribute("loginUser");
		if(userid.equals("admin")) {
			throw new LoginException
			("관리자 탈퇴는 불가능합니다.","main.shop");
		}
		//관리자 로그인 : 관리자비밀번호 검증
		//사용자 로그인 : 본인비밀번호 검증
		try {
			password = CipherUtil.makehash(password);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(!loginUser.getPassword().equals(password)) {
			throw new LoginException
			("회원탈퇴시 비밀번호가 틀립니다.","delete.shop?id="+userid);
		}
		try {
			service.userDelete(userid);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LoginException("회원 탈퇴시 오류가 발생했습니다. 전산부 연락 요망",
					"delete.shop?id="+userid);
		}
		//탈퇴 이후
		if(loginUser.getUserid().equals("admin")) {
			mav.setViewName("redirect:/user/main.shop");
		} else { //일반사용자가 탈퇴시
			session.invalidate(); //로그아웃
			throw new LoginException
				(userid +"회원님. 탈퇴 되었습니다.","login.shop");
		}
		return mav;
	}	
}