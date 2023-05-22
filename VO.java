package test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

public class VO {
	class Board {

	Scanner scanner;

	// 스캐너 기능 받아오는 함수
	void setScanner(Scanner scanner) {
		this.scanner = scanner;
	}

	Article[] articles;

	int articlesLastIndex;

	Member[] members;

	int membersLastIndex;

	// 로그인 유무확인, 로그인 중일경우 로그인된 객체를 갖고있다.
	Member loginedMember = null;
	
	// 관리자 유무 체크, 관리자(admin)의 경우 1, 일반은 0 
	int loginCode = 0; 

	Board() {
		articles = new Article[100];
		articlesLastIndex = -1;
		members = new Member[100];
		membersLastIndex = -1;
	}

	int lastArticle() {
		return articlesLastIndex + 1;
	}

	int lastMember() {
		return membersLastIndex + 1;
	}
	// 배열 늘리기(게시글)
		void getArticlesIndexExpand() {
			Article[] article = new Article[articlesLastIndex * 2];

			for (int i = 0; i <= article.length; i++) {
				article[i] = articles[i];
			}

			articles = article;
		}

		// 배열 늘리기(맴버)
		void getMembersIndexExpand() {
			Member[] member = new Member[membersLastIndex * 2];

			for (int i = 0; i <= member.length; i++) {
				member[i] = members[i];
			}

			members = member;
		}

		// 아이디 일치하는게 있는지 체크(로그인 시)
		boolean getLoginIdFact(String loginId) {
			for (int i = 0; i <= membersLastIndex; i++) {
				if (members[i].loginId.equals(loginId)) {
					return true;
				}
			}
			return false;
		}

		// 비밀번호 일치하는게 있는지 체크(로그인 시)
		boolean getLoginPwFact(String loginPw) {
			for (int i = 0; i <= membersLastIndex; i++) {
				if (members[i].loginPw.equals(loginPw)) {
					return true;
				}
			}
			return false;
		}

		// 중복 아이디 체크(회원가입 시)
		boolean getLoginIdCheck(String loginId) {
			for (int i = 0; i <= membersLastIndex; i++) {
				if (members[i].loginId.equals(loginId)) {
					return true;
				}
			}
			return false;
		}

		// 중복 닉네임 체크(회원가입 시)
		boolean getLoginNickNameCheck(String nickName) {
			for (int i = 0; i <= membersLastIndex; i++) {
				if (members[i].nickName.equals(nickName)) {
					return true;
				}
			}
			return false;
		}

		// 로그인 된 객체 전달(로그인 유무 확인 위해)
		Member getMemberById(String loginId) {
			for (int i = 0; i <= membersLastIndex; i++) {
				if (members[i].loginId.equals(loginId)) {
					return members[i];
				}
			}
			return null;
		}

		Article getArticleById(int id) {
			for (int i = 0; i <= articlesLastIndex; i++) {
				if (articles[i].id == id) {
					return articles[i];
				}
			}
			return null;
		}

		Article getLastArticle() {
			if (articlesLastIndex >= 0) {
				return articles[articlesLastIndex];
			}
			return null;
		}

		// 현재 날짜/시간 반환
		String getNowDateStr() {
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat Date = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
			String dateStr = Date.format(cal.getTime());
			return dateStr;
		}

		
		class Article {
			int id;
			int views_count;
			int recd_count;
			String resDate;
			String title;
			String body;
			String writer;
		}

		class Member {
			int id;
			String loginId;
			String loginPw;
			String nickName;
		}

	}
	}
