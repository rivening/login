package mvc.vga.mem;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;






@Controller
public class MemCont {
	@Autowired
	@Qualifier("mvc.vga.mem.MemProc")
	private MemProcInter memProc = null;
	
	public MemCont() {
		System.out.println("---> MemCont created.");
	}	
	
	  /**
	   * �޽���
	   * @param memberno
	   * @return
	   */
	  @RequestMapping(value="/mem/msg.do", method=RequestMethod.GET)
	  public ModelAndView delete_msg(String url){
	    ModelAndView mav = new ModelAndView();
	    
	    mav.setViewName("/mem/" + url); // forward
	    
	    return mav; 
	  }
	  
	  // http://localhost:9090/team4/mem/create.do
	  /**
	   * ��� ��
	   * @return
	   */
	  @RequestMapping(value="/mem/create.do", method=RequestMethod.GET )
	  public ModelAndView create() {
	    ModelAndView mav = new ModelAndView();
	    mav.setViewName("/mem/create"); 
	    
	    return mav;
	  }
	  
	  // http://localhost:9090/team4/mem/checkID.do?id=user1
	  /**
	   * ID �ߺ� üũ, JSON ���
	   * @return
	   */
	  @ResponseBody
	  @RequestMapping(value="/mem/checkID.do", method=RequestMethod.GET ,
	                              produces = "text/plain;charset=UTF-8" )
	  public String checkID(String mem_id) {
	    int cnt = this.memProc.checkID(mem_id);
	    
	    JSONObject json = new JSONObject();
	    json.put("cnt", cnt);
	    
	    return json.toString(); 
	  }
	  
	  /**
	   * ��� ó��
	   * @param memberVO
	   * @return
	   */
	  @RequestMapping(value="/mem/create.do", method=RequestMethod.POST)
	  public ModelAndView create(MemVO memVO){
	    ModelAndView mav = new ModelAndView();
	   	    
	    int cnt= memProc.create(memVO);
	    mav.addObject("cnt", cnt); // redirect parameter ����
	    mav.addObject("url", "create_msg"); // create_msg.jsp, redirect parameter ����
	    
	    mav.setViewName("redirect:/mem/msg.do");
	    
	    return mav;
	  }
	  

	  
	  /**
	   * �α��� ��
	   * @param memno
	   * @return
	   */
	  @RequestMapping(value="/mem/login.do", method=RequestMethod.GET)
	  public ModelAndView login(HttpServletRequest request){
	    ModelAndView mav = new ModelAndView();
	    
	    Cookie[] cookies = request.getCookies();
	    Cookie cookie = null;

	    String ck_id = ""; // id ����
	    String ck_id_save = ""; // id ���� ���θ� üũ
	    String ck_passwd = ""; // passwd ����
	    String ck_passwd_save = ""; // passwd ���� ���θ� üũ

	    if (cookies != null) {
	      for (int i=0; i < cookies.length; i++){
	        cookie = cookies[i]; // ��Ű ��ü ����
	        
	        if (cookie.getName().equals("ck_id")){
	          ck_id = cookie.getValue(); 
	        }else if(cookie.getName().equals("ck_id_save")){
	          ck_id_save = cookie.getValue();  // Y, N
	        }else if (cookie.getName().equals("ck_passwd")){
	          ck_passwd = cookie.getValue();         // 1234
	        }else if(cookie.getName().equals("ck_passwd_save")){
	          ck_passwd_save = cookie.getValue();  // Y, N
	        }
	      }
	    }
	    
	    mav.addObject("ck_id", ck_id); 
	    mav.addObject("ck_id_save", ck_id_save);
	    mav.addObject("ck_passwd", ck_passwd);
	    mav.addObject("ck_passwd_save", ck_passwd_save);
	    
	    mav.setViewName("/mem/login");
	    
	    return mav;
	  }
	  

	  /**
	   * �α��� ó��
	   * @param mem_id
	   * @param mem_pw
	   * @return
	   */
	  @RequestMapping(value="/mem/login.do", method=RequestMethod.POST)
	  public ModelAndView login_proc(HttpServletRequest request,
              HttpServletResponse response,
              HttpSession session,
              String mem_id, String mem_pw,
              @RequestParam(value="id_save", defaultValue="") String id_save,
              @RequestParam(value="passwd_save", defaultValue="") String passwd_save){
	    
		ModelAndView mav = new ModelAndView();
	    
	    HashMap<Object, Object> map = new HashMap<Object, Object>();
	    map.put("mem_id", mem_id);
	    map.put("mem_pw", mem_pw);
	    
	    int cnt = this.memProc.login(map);
	    

    	
	    if (cnt == 1) { // ���� �н����尡 ��ġ�ϴ� ���
	    	MemVO memVO = this.memProc.readById(mem_id);
	    	mav.addObject("memVO", memVO);
	    	
	    	session.setAttribute("memno", memVO.getMemno());
	        session.setAttribute("mem_id", mem_id);
	        session.setAttribute("memVO", memVO);
	        
	     // -------------------------------------------------------------------
	        // id ���� ��� ����
	        // -------------------------------------------------------------------
	        if (id_save.equals("Y")) { // id�� ������ ���
	          Cookie ck_id = new Cookie("ck_id", mem_id);
	          ck_id.setMaxAge(60 * 60 * 72 * 10); // 30 day, �ʴ���
	          response.addCookie(ck_id);
	        } else { // N, id�� �������� �ʴ� ���
	          Cookie ck_id = new Cookie("ck_id", "");
	          ck_id.setMaxAge(0);
	          response.addCookie(ck_id);
	        }
	        // id�� �������� �����ϴ�  CheckBox üũ ����
	        Cookie ck_id_save = new Cookie("ck_id_save", id_save);
	        ck_id_save.setMaxAge(60 * 60 * 72 * 10); // 30 day
	        response.addCookie(ck_id_save);
	        // -------------------------------------------------------------------

	        // -------------------------------------------------------------------
	        // Password ���� ��� ����
	        // -------------------------------------------------------------------
	        if (passwd_save.equals("Y")) { // �н����� ������ ���
	          Cookie ck_passwd = new Cookie("ck_passwd", mem_pw);
	          ck_passwd.setMaxAge(60 * 60 * 72 * 10); // 30 day
	          response.addCookie(ck_passwd);
	        } else { // N, �н����带 �������� ���� ���
	          Cookie ck_passwd = new Cookie("ck_passwd", "");
	          ck_passwd.setMaxAge(0);
	          response.addCookie(ck_passwd);
	        }
	        // passwd�� �������� �����ϴ�  CheckBox üũ ����
	        Cookie ck_passwd_save = new Cookie("ck_passwd_save", passwd_save);
	        ck_passwd_save.setMaxAge(60 * 60 * 72 * 10); // 30 day
	        response.addCookie(ck_passwd_save);
	        // -------------------------------------------------------------------
		    mav.setViewName("/mem/mypage");    
	    } else {
	    	mav.setViewName("/mem/login_fail");
	    }

	    return mav;
	  }

	  /**
	   * �α׾ƿ� ó��
	   * @param session
	   * @return
	   */
	  @RequestMapping(value="/mem/logout.do", 
	                             method=RequestMethod.GET)
	  public ModelAndView logout(HttpSession session){
	    ModelAndView mav = new ModelAndView();
	    session.invalidate(); // ��� session ���� ����
	    
	    mav.setViewName("redirect:/mem/logout_msg.jsp");
	    
	    return mav;
	  }
	  

}
