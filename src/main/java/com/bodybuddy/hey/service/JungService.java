package com.bodybuddy.hey.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.bodybuddy.hey.bean.Member;
import com.bodybuddy.hey.bean.OpCategory;
import com.bodybuddy.hey.bean.Question;
import com.bodybuddy.hey.bean.YesOrNo;
import com.bodybuddy.hey.dao.KirimDao;
import com.bodybuddy.hey.dao.MemberDao;

import com.google.gson.Gson;

@Service
public class JungService {
	@Autowired
	private MemberDao mDao;
	@Autowired
	private HttpSession session;

	Member m;
	String view = null;
	ModelAndView mav;
	Question q;
	

	public ModelAndView getProfileList(String m_id) {
		System.out.println("정 서비스");
		mav = new ModelAndView();
		String view = null;
		m = new Member();
		System.out.println("696969696969696969   " + m_id);
		m = mDao.getProfileList(m_id);

		view = "manage/profileModifyT";
		mav.setViewName(view);

		mav.addObject("m", m);

		return mav;
	}

	private String makeHTMLProfileList(String bname, String addr) {
		StringBuilder sb = new StringBuilder();
		sb.append("         <table id=\"recent-purchases-listing\" class=\"table\">\n" + "           <thead>\n"
				+ "            <tr>\n" + "             <th>업체이름</th>\n" + "             <th>업체주소</th>\n"
				+ "             <th>요청</th>\n" + "             <th>요청취소</th>\n" + "            </tr>\n"
				+ "           </thead>\n" + "           <tbody>\n" + "             <tr>\n" + "              <td>"
				+ bname + "</td>\n" + "              <td>" + addr + "</td>\n"
				+ "              <td><button onclick='acceptrequest()'>요청</button></td>\n"
				+ "              <td><button onclick='cancel()'>취소</button></td>\n" + "             </tr>\n"
				+ "           </tbody>\n" + "         </table>");
		return sb.toString();
	}

	public String getTfindC(String name) {
		System.out.println("드루와!!");
		mav = new ModelAndView();
		String view = null;
		m = new Member();
		m.setC_bname(name);

		m = mDao.getTfindC(m);
		System.out.println("다오다녀옴");
		String addr = m.getM_addr();
		String bname = m.getC_bname();
		System.out.println("상태보여라");
		System.out.println("상태 : " + m.getYn_state());
		String html = null;
		System.out.println("DB 다녀왔어요!");

		System.out.println("company search select success");
		html = makeHTMLProfileList(bname, addr);

		System.out.println("company search select error");

		return html;

	}

	public ModelAndView questionList(String id) {
		System.out.println("정 서비스");
		mav = new ModelAndView();
		String view = null;
		m = new Member();
		List<Member> mList = new ArrayList<Member>();

		m = mDao.getProfileList(id);
		System.out.println("이거다");
		if (null != m) {
			System.out.println("profile list select success");
			view = "manage/profileModifyT";
			mav.setViewName(view);
			mav.addObject("m", m);
		} else {
			System.out.println("profile list select error");
			view = "redirect:profileModifyT.jsp";
			mav.setViewName(view);
		}
		return mav;
	}

	public ModelAndView getQuestionList(String id) {
		mav = new ModelAndView();

		List<Question> qList = null;
		System.out.println("다오 들어가요!");

		qList = mDao.getQuestionList(id);
		System.out.println("다오 다녀왔어요");

		if (0 != qList.size()) {
			System.out.println("qList size = " + qList.size());
			view = "manage/question/questionList";
			mav.setViewName(view);
			mav.addObject("qList", qList);
			//session.setAttribute("qLists", qList);
			//qList.get(0).
		} else {
			System.out.println("없어");
			view = "manage/question/questionList";
			mav.setViewName(view);
		}

		return mav;

	}

	// 취소취소취소취소취소취소취소취소취소취소취소취소취소취소취소
	public String cancel(Member mb) {
		String cc = null;
		String id = mb.getM_id();
		if (mDao.cancel(id)) {
			System.out.println("취소완료");
			cc = "취소";
		}

		else {
			System.out.println("취소실패");
		}

		return cc;
	}

	// 요청요청요청요청요청요청

	public String acceptrequest(Member mb, String name) {
		System.out.println("드루와!!");
		mav = new ModelAndView();
		String view = null;

		m = new Member();

		m = mDao.findC_id(name);
		m.setM_id(mb.getM_id());
		m.setC_bname(name);

		System.out.println("회사 아이디c_id : " + m.getC_id());
		System.out.println("로그인한 본인 t_id:" + m.getM_id());
		System.out.println("회사이름 name:" + m.getC_bname());

		int count = mDao.comfirm(m);
		System.out.println("count : " + count);

		/* m.getC_id()!= 디비에있는 업체아이디; */
		try {
			if (count == 1)
				mDao.acceptrequestupdate(m);
			if (count == 0)
				mDao.acceptrequestInsert(m);

		} catch (Exception e) {

		}

		System.out.println("나오자");

		mav.setViewName("manage/profileModifyT");

		return "zzz";
	}

	@Transactional
	public ModelAndView adinsert(MultipartHttpServletRequest multi) {
		
		mav = new ModelAndView();
		Question adadd = new Question();
		
		String[] op_trainer=null;
		
		String op_clock1=null;
		String op_clock2=null;
		String ad_category=null;
		String ad_title=null;
		String ad_content=null;
		String[] op_contentValues=null;
		String[] op_content_1=null;
		String[] day=null;
		String[] day1=null;
		String op_period=null;
		String[] op_period_n=null;
		String op_period_1s=null;
		String[] op_period_1=null;
		String op_period_2s=null;
		String[] op_period_2=null;
		String op_times=null;
		String[] op_times_1=null;
		String[] op_clock_1=null;
		String[] op_clock_2=null;
		String op_price=null;
		String[] op_price_1=null;
		String op_personnel=null;
		String[] op_personnel_1=null;
		
		
		List<String> ad_nameTId = null;
		String[] op_trainerValues=null;
		String[] dayValues=null;
		String[] op_periodValues=null;
		String[] op_period_1sValues=null;
		String[] op_period_2sValues=null;
		String[] op_timesValues=null;
		String[] op_clock1Values=null;
		String[] op_clock2Values=null;
		String[] op_priceValues=null;
		String[] op_personnelValues=null;
		
		
		
		/*
		 * Map<String, String> fMap = new HashMap<String, String>();
		 * 
		 * String root = multi.getSession().getServletContext().getRealPath("/");
		 * System.out.println("root=" + root); String path = root + "resources/upload/";
		 * // 2.폴더 생성을 꼭 할것... File dir = new File(path); if (!dir.isDirectory()) { //
		 * upload폴더 없다면 dir.mkdirs(); // upload폴더 생성 } List<MultipartFile> files =
		 * multi.getFiles("ap_image");
		 */
		Member mb = (Member) session.getAttribute("mb");
		adadd.setAd_name(mb.getM_id());//세션아이디
		
		int checkNum = Integer.valueOf(multi.getParameter("checkNum")).intValue();
		
		ad_category = multi.getParameter("ad_category");// adadd.getAd_category();
		if(ad_category!=null && ad_category!="") {
			System.out.println("ad_category : " + ad_category);
			if(ad_category.equals("normal")) adadd.setAd_category("일반");
			if(ad_category.equals("pt")) adadd.setAd_category("피트니스");
			if(ad_category.equals("homeTraining")) adadd.setAd_category("홈트레이닝");
			if(ad_category.equals("pilates")) adadd.setAd_category("필라테스");
			if(ad_category.equals("yoga")) adadd.setAd_category("요가");
		}
		
		ad_title = multi.getParameter("ad_title");// adadd.getAd_title();
		if(ad_title!=null && ad_title!="") {
			System.out.println("ad_title : " + ad_title);
			adadd.setAd_title(ad_title);
		}
		
		ad_content = multi.getParameter("ad_content");// adadd.getAd_content();
		if(ad_content!=null && ad_content!="") {
			System.out.println("ad_content : " + ad_content);
			adadd.setAd_content(ad_content);
		}

		
		op_trainerValues = multi.getParameterValues("op_trainer");
		ad_nameTId = new ArrayList<>();
		for(int i=0;i<op_trainerValues.length; i++) {
			if(op_trainerValues[i]!="") {
				String[] ad_nameT = op_trainerValues[i].split(",");
				System.out.println("ad_nameT[0] : " + ad_nameT[0]);
				System.out.println("ad_nameT[1] : " + ad_nameT[1]);
				ad_nameTId.add(ad_nameT[1]);//ad_name3
				String ad_nameTName = ad_nameT[0];//ad_name
				System.out.println(i+"트레이너아이디 출력 " + ad_nameTId.get(i));//ad_name3
				System.out.println(i+"트레이너이름  : " + ad_nameTName);//ad_name
			}
		}
		
		op_contentValues= multi.getParameterValues("op_content");// adadd.getOp_content();
		op_timesValues = multi.getParameterValues("op_times");
		op_clock1Values = multi.getParameterValues("op_clock1");
		op_clock2Values = multi.getParameterValues("op_clock2");
		op_priceValues = multi.getParameterValues("op_price");
		op_personnelValues = multi.getParameterValues("op_personnel");
		
		// day
		dayValues = multi.getParameterValues("day");
		String b="";
		for(int i=0;i<dayValues.length;i++) {
			if(dayValues[i]!="") {
				b+=dayValues[i];
			}
		}
		System.out.println("b="+b);
		day1 = b.split("@"); // @ 기준으로 나누기	
		
		//일반 일 수
		op_periodValues = multi.getParameterValues("op_period");
		if(op_periodValues.length!=0 && op_periodValues!=null) {
			for(int i=0;i<op_periodValues.length; i++) {
				System.out.println("op_period출력 : " + op_periodValues[i]);
			}
		}
		
		// period
		// 달력
		op_period_1sValues = multi.getParameterValues("op_period1");// adadd.getOp_period1().split(",");
		// op_period_1[0] = mm/dd/yyyy
		if(op_period_1sValues.length!=0 && op_period_1sValues!=null) {
			for(int i=0; i<op_period_1sValues.length;i++) {
				System.out.println("op_period_1출력i "+op_period_1sValues[i]);
			}
			
			/*if(op_period_1s!="" && op_period_1s.contains(",") || op_period_1s!=null && op_period_1s.contains(",")) {
				op_period_1 = op_period_1s.split(",");
				for (int i = 0; i < op_period_1.length; i++) {
					op_period_1[i] = op_period_1[i].replace("/", "");
					String x = op_period_1[i].substring(4);
					String y = op_period_1[i].substring(0, 4);
					op_period_1[i] = x + y;
				}
			}*/
		}
		// String op_period2 = adadd.getOp_period1().replace("/", ""); // 06072019
		/*
		 * String x2 = op_period2.substring(4); String y2 = op_period2.substring(0, 4);
		 * String z2 = x2 + y2;
		 */
		
		op_period_2sValues = multi.getParameterValues("op_period2");// adadd.getOp_period1().split(",");
		// op_period_1[0] = mm/dd/yyyy
		if(op_period_2sValues.length!=0 && op_period_2sValues!=null) {
			System.out.println("op_period_2출력 "+op_period_2sValues);
			for(int i=0; i<op_period_2sValues.length;i++) {
				System.out.println("op_period_2출력i "+op_period_2sValues[i]);
			}
			/*if(op_period_2s!="" && op_period_2s.contains(",") || op_period_2s!=null && op_period_2s.contains(",")) {
				op_period_2 = op_period_2s.split(",");
				for (int i = 0; i < op_period_2.length; i++) {
					op_period_2[i] = op_period_2[i].replace("/", "");
					String x = op_period_2[i].substring(4);
					String y = op_period_2[i].substring(0, 4);
					op_period_2[i] = x + y;
				}
			}*/
		}
		
		adadd.setAd_status("모집중");
		
////////////////////////////////////////////////사전 대작업 완료
		// 대작업 시작

		/*
		 * String[] all = new String[op_content.length()]; for (int i = 0; i <
		 * op_content.length() - 1; i++) { all[i] = op_trainer_1[i]
		 * +","+op_price_1[i]+","+ op_period_1[i] +"~"+ op_period_2[i]
		 * +","+op_times_1[i]+","+ day1[i] +"," +op_content_1[i]+","+op_clock_1[i] + "~"
		 * + op_clock_2[i]+","+op_personnel_1[i]; } System.out.println("all[0] : " +
		 * all[0]);
		 * System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++"
		 * ); System.out.println("all[1] : " + all[1]);
		 * System.out.println("------------------------------------------------------");
		 */
		
		// advertise insert
		Map<String, String> opMap = new HashMap<String, String>();
		String xxx;
		if (mDao.adinsert(adadd)) {
			System.out.println("광고입력성공");
			xxx = adadd.getXxx();
			
//////////////////////////////////////////AP INSERT///////////////////////
			String root = multi.getSession().getServletContext().getRealPath("/");
			System.out.println("root=" + root);
			String path = root + "resources/upload/";
			// 2.폴더 생성을 꼭 할것...
			File dir = new File(path);
			if (!dir.isDirectory()) { // upload폴더 없다면
				dir.mkdirs(); // upload폴더 생성
			}
			//String[] ap_image = multi.getParameterValues("ap_image");
			Map<String, String> map = new HashMap<>();
			List<MultipartFile> files = multi.getFiles("ap_image");
			String oriFileName = null;
			String sysFileName = null;
			System.out.println("::::::"+files.size());
			try {
				for(int i=0; i<files.size();i++) {
					oriFileName = files.get(i).getOriginalFilename();
					String index = String.valueOf(i);
					sysFileName = index+System.currentTimeMillis() + "."
						+ oriFileName.substring(oriFileName.lastIndexOf(".") + 1);
					System.out.println("oriFileName=" + oriFileName);
					System.out.println("sysFileName=" + sysFileName);
					map.put("ap_adcode", xxx);
					map.put("ap_image", sysFileName);
					System.out.println("ap_adcode=" + map.get("ap_adcode"));
					System.out.println("ap_image=" + map.get("ap_image"));
					
					if (sysFileName != null) {
						files.get(i).transferTo(new File(path + sysFileName));
						if(mDao.adPhotoInsert(map)) {
							System.out.println(i+"번째 광고사진 업로드 및 등록 성공");
						}
					}
				}
			} catch (Exception e) {
				System.out.println("AdPhoto Insert fail");
			}
////////////////////////////////////////////////AP INSERT 끝//////////////////
			System.out.println("광고입력성공2");
			System.out.println("xxx : " + adadd.getXxx());
			for (int i = 0; i < op_contentValues.length; i++) {
				opMap.put("op_adcode", xxx);
				opMap.put("op_content", op_contentValues[i]);
				opMap.put("op_price", op_priceValues[i]);
				
				if(ad_nameTId.size()!=0)
				opMap.put("op_trainer", ad_nameTId.get(i));
				
				if(op_periodValues[i]!="")
				opMap.put("op_period", op_periodValues[i]);
				
				if(op_period_1sValues[i]!="" && op_period_2sValues[i]!="")
				opMap.put("op_period", op_period_1sValues[i] + "~" + op_period_2sValues[i]);//일반일경우 제외
				
				if(op_clock1Values[i]!="" && op_clock2Values[i]!="")
				opMap.put("op_clock", op_clock1Values[i] + "~" + op_clock2Values[i]);

				if(op_timesValues[i]!="")
				opMap.put("op_times", op_timesValues[i]);
				
				if(day1!=null && day1.length!=0 && day1[i]!="")
				opMap.put("op_day", day1[i]);
				
				if(op_personnelValues[i]!="")
				opMap.put("op_personnel", op_personnelValues[i]);
				
				System.out.println(opMap.get("op_trainer"));
				System.out.println(opMap.get("op_period"));
				System.out.println(opMap.get("op_adcode"));
				System.out.println(opMap.get("op_adcode"));
				if (mDao.opinsert(opMap)) {
					System.out.println(i+"옵션입력성공");
				}
			}
		} else {
			System.out.println("광고등록실패");
		}
		mav.setViewName("/advertisemanage");
		return mav;
	}

	public ModelAndView profileComplete(Member mb, String t_career) {

		mav = new ModelAndView();
		System.out.println("t_career:" + t_career);
		mb.setT_career(t_career);
		mDao.profileComplete(mb);
		String view = "manage/trainer/trainerMain";
		mav.setViewName(view);
		return mav;
	}

	public ModelAndView advertisewriterfrm(Member mb) {
		mav = new ModelAndView();
		System.out.println("광고입력폼");

		String trainerlist = makeHtmltrainerlist(mb);
		System.out.println(trainerlist);

		mav.addObject("trainerlist", trainerlist);
		String view = "manage/advertisewritefrm";
		mav.setViewName(view);
		System.out.println("서비스 끝");
		return mav;
	}

	public String makeHtmltrainerlist(Member mb) {
		StringBuilder sb = new StringBuilder();
		String id = mb.getM_id();
		System.out.println("id:" + id);
		// 다오가즈아
		m = mDao.kindkind(id);
		String kind = m.getM_kind();
		System.out.println("KIND : " + kind);
		System.out.println("Name : " + m.getM_name());
		if (kind.equals("t")) {

			System.out.println("트레이너네요");
			sb.append("<select name=\"op_trainer\"><option value=" + m.getM_name() + "," + m.getM_id() + ">"
					+ m.getM_name() + "</option></select>");
			System.out.println("저장되었다");
			return sb.toString();

		} else if (kind.equals("c")) {// 업체
			List<YesOrNo> yn = mDao.trinerlist(id);
			sb.append("<select name=\"op_trainer\">\r\n" + "    <option value=\"\">트레이너 선택</option>\r\n");
			for (int i = 0; i < yn.size(); i++) {
				sb.append("<option value=\"트레이너\">" + yn.get(i).getYn_trainer() + "</option>\r\n");
			}
			sb.append("</select>");

		}
		return sb.toString();
	}

	public ModelAndView getAdvertisemanage(String id) {
		mav = new ModelAndView();
		String view = null;
		m = new Member();
		
		List<Question> adList = null;
		
		
		adList = mDao.getAdvertiselist(id);
		System.out.println("이거다");
		if (0 != adList.size()) {
			System.out.println("advertise list select success");
			view = "manage/advertisemanage";
			mav.setViewName(view);
			mav.addObject("adList", adList);
		} else {
			System.out.println("advertise list select error");
			view = "manage/advertisemanage";
			mav.setViewName(view);
		}
		return mav;
	}

}