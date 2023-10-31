# 💡Spring에서 로그인 기능을 구현한 프로젝트 (내가 만든 쿠키~)

## Login
      <select id="login" parameterType="HashMap" resultType="int">
		SELECT COUNT(memno) as cnt
		FROM mem
		WHERE mem_pw=#{mem_pw} AND mem_id=#{mem_id}
      </select>
   
## Cookie

          // id 관련 쿠키 저장
	        // -------------------------------------------------------------------
	        if (id_save.equals("Y")) { // id를 저장할 경우
	          Cookie ck_id = new Cookie("ck_id", mem_id);
	          ck_id.setMaxAge(60 * 60 * 72 * 10); // 30 day, 초단위
	          response.addCookie(ck_id);
	        } else { // N, id를 저장하지 않는 경우
	          Cookie ck_id = new Cookie("ck_id", "");
	          ck_id.setMaxAge(0);
	          response.addCookie(ck_id);
	        }
	        // id를 저장할지 선택하는  CheckBox 체크 여부
	        Cookie ck_id_save = new Cookie("ck_id_save", id_save);
	        ck_id_save.setMaxAge(60 * 60 * 72 * 10); // 30 day
	        response.addCookie(ck_id_save);
	        // -------------------------------------------------------------------

	        // -------------------------------------------------------------------
	        // Password 관련 쿠키 저장
	        // -------------------------------------------------------------------
	        if (passwd_save.equals("Y")) { // 패스워드 저장할 경우
	          Cookie ck_passwd = new Cookie("ck_passwd", mem_pw);
	          ck_passwd.setMaxAge(60 * 60 * 72 * 10); // 30 day
	          response.addCookie(ck_passwd);
	        } else { // N, 패스워드를 저장하지 않을 경우
	          Cookie ck_passwd = new Cookie("ck_passwd", "");
	          ck_passwd.setMaxAge(0);
	          response.addCookie(ck_passwd);
	        }
	        // passwd를 저장할지 선택하는  CheckBox 체크 여부
	        Cookie ck_passwd_save = new Cookie("ck_passwd_save", passwd_save);
	        ck_passwd_save.setMaxAge(60 * 60 * 72 * 10); // 30 day
	        response.addCookie(ck_passwd_save);

