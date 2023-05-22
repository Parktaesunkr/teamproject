package test;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);

		Board board = new Board();

		board.setScanner(scanner);

		board.start();

		scanner.close();

	}
}

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

	// 시작 및 기능 구현된 함수 호출하여 작동되도록 하는 함수
	void start() {
		showHelp();

		while (true) {
			System.out.printf("게시판) ");
			String command = scanner.nextLine().trim();

			if (command.equals("help")) {
				doCommandHelp();
			} else if (command.equals("list")) {
				doCommandList();
			} else if (command.equals("add")) {
				doCommandAdd();
			} else if (command.equals("detail")) {
				System.out.printf("상세보기 할 게시물의 번호 : ");
				int idToDetail = scanner.nextInt();
				scanner.nextLine();
				doCommandDetail(idToDetail);
			} else if (command.equals("modify")) {
				System.out.printf("수정 할 게시물의 번호 : ");
				int idToModify = scanner.nextInt();
				scanner.nextLine();
				doCommandModify(idToModify);
			} else if (command.equals("delete")) {
				System.out.printf("삭제 할 게시물의 번호 : ");
				int idToDelete = scanner.nextInt();
				scanner.nextLine();
				doCommandDelete(idToDelete);
			} else if (command.equals("recd")) {
				System.out.printf("추천 할 게시물의 번호 : ");
				int idToRecd = scanner.nextInt();
				scanner.nextLine();
				doCommandRecd(idToRecd);
			} else if (command.equals("signup")) {
				doCommandSignup();
			} else if (command.equals("login")) {
				doCommandLogin();
			} else if (command.equals("logout")) {
				doCommandLogout();
			} else if (command.equals("exit")) {
				doCommandExit();
				break;
			} else {
				System.out.println("잘못된 명령어 입니다.");
			}

		}
	}

	void doCommandHelp() {
		showHelp();
	}

	void doCommandList() {
		System.out.println("== 게시물 리스트 ==");
		System.out.printf("번호  |           날짜                     |    작성자        | 조회수 | 추천수 | 제목\n");
		if (articlesLastIndex >= 0) {
			for (int i = 0; i <= articlesLastIndex; i++) {
				System.out.printf("%d   | %s     | %s         |  %d  |  %d  | %s\n", articles[i].id, articles[i].resDate,
						articles[i].writer, articles[i].views_count, articles[i].recd_count, articles[i].title);
			}
		} else {
			System.out.println("현재 게시물이 존재하지 않습니다.");
		}
	}

	void doCommandAdd() {
		System.out.println("== 게시물 추가 ==");

		// 2짜리 배열의 경우 0, 1번에 값 저장, 마지막 배열인 1과 배열 길이 2 - 1 이 같은 경우 배열 확장.
		if (articlesLastIndex == articles.length - 1) {
			getArticlesIndexExpand();
		}

		Article article = new Article();

		Article lastArticle = null;

		if (articlesLastIndex >= 0) {
			lastArticle = articles[articlesLastIndex];
		}

		int newId;

		if (lastArticle == null) {
			newId = 1;
		} else {
			newId = lastArticle.id + 1;
		}

		if (loginedMember != null) {
			article.id = newId;
			article.resDate = getNowDateStr();
			System.out.printf("신규 게시물 제목 : ");
			article.title = scanner.nextLine();
			System.out.printf("신규 게시물 내용 : ");
			article.body = scanner.nextLine();
			article.writer = loginedMember.nickName; // 작성자 : 로그인된 맴버의 닉네임 설정되도록.

			articlesLastIndex = lastArticle();

			articles[articlesLastIndex] = article;

			System.out.printf("%d번 글이 생성되었습니다.\n", article.id);
		} else {
			System.out.println("로그인 한 회원만 게시글 작성이 가능합니다.");
		}

	}

	void doCommandDetail(int id) {
		System.out.println("== 게시물 상세 ==");

		if (loginedMember != null) {
			if (getArticleById(id) != null) {
				System.out.printf("번호 : %d\n", getArticleById(id).id);
				System.out.printf("날짜 : %s\n", getArticleById(id).resDate);
				System.out.printf("제목 : %s\n", getArticleById(id).title);
				System.out.printf("내용 : %s\n", getArticleById(id).body);
				getArticleById(id).views_count++;

			} else {
				System.out.println("상세보기 할 게시물이 존재하지 않습니다.");
			}
		} else {
			System.out.println("로그인 한 회원만 게시글 상세보기가 가능합니다.");
		}
	}

	void doCommandModify(int id) {
		System.out.println("== 게시물 수정 ==");
		if (loginedMember != null) {
			if (getArticleById(id) != null) {
				if (loginCode == 1 || loginedMember.nickName.equals(getArticleById(id).writer)) {
					System.out.printf("게시물 수정을 진행하시겠습니까? ( Y / N ) ");
					String temp = scanner.next();
					scanner.nextLine();

					if (temp.equals("Y") || temp.equals("y")) {
						System.out.printf("수정 할 게시물 제목 : ");
						getArticleById(id).title = scanner.nextLine();
						System.out.printf("수정 할 게시물 내용 : ");
						getArticleById(id).body = scanner.nextLine();
						System.out.println("게시물 수정이 완료되었습니다.");
					} else if (temp.equals("N") || temp.equals("n")) {
						System.out.println("게시물 수정이 취소되었습니다.");
					} else {
						System.out.println("잘못된 명령어 입니다.");
					}

				} else {
					System.out.println("작성자만 수정 가능합니다.");
				}
			} else {
				System.out.println("수정 할 게시물이 존재하지 않습니다.");
			}
		} else {
			System.out.println("로그인 한 회원만 게시물 수정이 가능합니다.");
		}

	}

	void doCommandDelete(int id) {
		System.out.println("== 게시물 삭제 ==");
		if (loginedMember != null) {
			if (getArticleById(id) != null) {
				if (loginCode == 1 || loginedMember.nickName.equals(getArticleById(id).writer)) {
					int temp = getArticleById(id).id - 1;
					for (int i = temp; i <= articlesLastIndex; i++) {
						articles[i] = articles[i + 1];
					}
					articlesLastIndex--;
					System.out.println("게시물 삭제가 완료되었습니다.");
				} else {
					System.out.println("작성자만 삭제 가능합니다.");
				}
			} else {
				System.out.println("게시물이 존재하지 않습니다.");
			}
		} else {
			System.out.println("로그인 한 회원만 게시물 삭제가 가능합니다.");
		}

	}

	void doCommandRecd(int id) {
		System.out.println("== 게시물 추천 ==");
		if (loginedMember != null) {
			if (getArticleById(id) != null) {
				getArticleById(id).recd_count++;
			} else {
				System.out.println("게시물이 존재하지 않습니다.");
			}
		} else {
			System.out.println("로그인 한 회원만 게시물 추천이 가능합니다.");
		}

	}

	void doCommandSignup() {
		System.out.println("== 회원 가입 ==");

		// 2짜리 배열의 경우 0, 1번에 값 저장, 마지막 배열인 1과 배열 길이 2 - 1 이 같은 경우 배열 확장.
		if (membersLastIndex == members.length - 1) {
			getMembersIndexExpand();
		}

		Member member = new Member();

		Member lastMember = null;

		if (membersLastIndex >= 0) {
			lastMember = members[membersLastIndex];
		}

		int newId;

		if (lastMember == null) {
			newId = 1;
		} else {
			newId = lastMember.id + 1;
		}

		member.id = newId;

		System.out.printf("신규 아이디 : ");
		member.loginId = scanner.next().trim();
		scanner.nextLine();

		if (member.loginId.equals("admin")) {
			member.loginPw = "admin";
			member.nickName = "관리자";
			loginCode = 1;
			System.out.println("관리자가 생성되었습니다.");
			membersLastIndex = lastMember();
			members[membersLastIndex] = member;
		} else {
			if (getLoginIdCheck(member.loginId)) {
				System.out.println("중복된 아이디가 존재합니다.");
			} else {
				System.out.printf("비밀번호 : ");
				member.loginPw = scanner.next().trim();
				scanner.nextLine();

				System.out.printf("닉네임 : ");
				member.nickName = scanner.next().trim();
				scanner.nextLine();

				if (getLoginNickNameCheck(member.nickName)) {
					System.out.println("중복된 닉네임이 존재합니다.");
				} else {
					System.out.println("회원가입이 완료되었습니다.");
					System.out.printf("%s님 환영합니다.\n", member.loginId);

					membersLastIndex = lastMember();
					members[membersLastIndex] = member;
					loginCode = 0;
				}
			}
		}
	}

	void doCommandLogin() {
		System.out.println("== 로그 인 ==");
		if (loginedMember == null) {
			System.out.printf("로그인 아이디 입력 : ");
			String tempId = scanner.next();
			scanner.nextLine();

			if (getLoginIdFact(tempId)) {
				System.out.printf("로그인 비밀번호 입력 : ");
				String tempPw = scanner.next();
				scanner.nextLine();
				if (getLoginPwFact(tempPw)) {
					System.out.printf("%s님 환영합니다. 로그인 되었습니다.\n", tempId);
					loginedMember = getMemberById(tempId);
				} else {
					System.out.println("비밀번호가 일치하지 않습니다.");
				}
			} else {
				System.out.println("일치하는 아이디가 존재하지 않습니다.");
			}
		} else {
			System.out.println("현재 다른 회원이 로그인 중 입니다.");
		}

	}

	void doCommandLogout() {
		System.out.println("== 로그 아웃 ==");
		if (loginedMember != null) {
			System.out.println("로그아웃 하시겠습니까? ( Y / N )");
			String temp = scanner.next();
			scanner.nextLine();
			if (temp.equals("Y") || temp.equals("y")) {
				System.out.println("로그아웃이 완료되었습니다.");
				loginedMember = null;
			} else if (temp.equals("N") || temp.equals("n")) {
				System.out.println("로그아웃이 취소되었습니다.");
			}
		} else {
			System.out.println("현재 로그인 중이지 않습니다.");
		}
	}

	void doCommandExit() {
		System.out.println("== 게시판 종료 ==");
	}

	// 명령어 리스트
	void showHelp() {
		System.out.println("== 명령어 리스트 ==");
		System.out.println("help : 명령어 리스트");
		System.out.println("list : 게시물 리스팅");
		System.out.println("add : 게시물 추가");
		System.out.println("detail : 게시물 상세보기");
		System.out.println("exit : 게시판 종료");
		System.out.println("== 추가 명령어 리스트 ==");
		System.out.println("modify : 게시물 수정");
		System.out.println("delete : 게시물 삭제");
		System.out.println("recd : 게시물 추천");
		System.out.println("signup : 회원 가입");
		System.out.println("login : 로그 인");
		System.out.println("logout : 로그 아웃");
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
