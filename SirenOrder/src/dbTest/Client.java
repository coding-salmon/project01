package dbTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Scanner; // 추가

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Client {

	// 서버 주소와 포트 번호 설정
	private static final String serverAddress = "localhost";
	private static final int serverPort = 9999;
	Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) throws ParseException {
		try (Socket socket = new Socket(serverAddress, serverPort);
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {
			System.out.println("사이렌오더 서버에 연결되었습니다.");

			// 사용자 입력을 통해 로그인, 회원가입, 종료 중 하나를 선택할 수 있는 메뉴 반복
			while (true) {
				displayMenu();
				String userInput = stdIn.readLine();
				switch (userInput) {
				case "1":
					login(stdIn, out, in); // 로그인 처리
					break;
				case "2":
					signup(stdIn, out, in); // 회원가입 처리
					break;
				case "3":
					System.out.println("사이렌오더 서버 연결을 종료합니다."); // 프로그램 종료
					return;
				default:
					System.out.println("잘못된 입력입니다. 다시 선택해주세요."); // 잘못된 입력 처리
				}
			}
		} catch (IOException e) {
			System.out.println("서버 연결에 실패했습니다: " + e.getMessage()); // 서버 연결 실패 처리
		}
	}

	// 사용자에게 메뉴 옵션을 표시하는 메서드
	private static void displayMenu() {
		System.out.println("1. [로그인]");
		System.out.println("2. [회원가입]");
		System.out.println("3. [종료]");
		System.out.println("메뉴를 선택하세요. >> ");
	}

	// 로그인 기능을 처리하는 메서드
	private static void login(BufferedReader stdIn, PrintWriter out, BufferedReader in)
			throws IOException, ParseException {
		JSONObject json = new JSONObject();
		json.put("type", "login");
		json.put("userid", promptForInput(stdIn, "아이디를 입력하세요>>"));
		json.put("password", promptForInput(stdIn, "패스워드를 입력하세요>>"));

		out.println(json.toString()); // 서버의 로그인 요청 전송

		handleServerResponse(stdIn, out, in); // 로그인 응답 처리

	}

	// 회원가입 기능을 처리하는 메서드
	private static void signup(BufferedReader stdIn, PrintWriter out, BufferedReader in)
			throws IOException, ParseException {
		JSONObject json = new JSONObject();
		json.put("type", "signup");
		json.put("userid", promptForInput(stdIn, "사용할 아이디를 입력하세요"));
		json.put("password", promptForInput(stdIn, "사용할 패스워드를 입력하세요"));

		out.println(json.toString());
		handleServerResponse(stdIn, out, in); // 서버로부터의 응답 처리
	}

	// 사용자로부터 입력을 요청하는 메서드
	private static String promptForInput(BufferedReader stdIn, String prompt) throws IOException {
		System.out.println(prompt);
		return stdIn.readLine();
	}

	private static void handleServerResponse(BufferedReader stdIn,PrintWriter out, BufferedReader in) throws IOException, ParseException {
		JSONParser parser = new JSONParser();
		try {
			String responseString = in.readLine();
			if (responseString != null) {
				JSONObject response = (JSONObject) parser.parse(responseString);
				System.out.println("서버 응답" + response.toJSONString());

				// 로그인 응답 처리
				if (response.containsKey("로그인상태")) {
				  String loginStatus =(String) response.get("로그인상태");
				       if(loginStatus.equals("성공")) {
				            handleLoginSuccess(stdIn, out, in);
				        } else {
				            System.out.println("로그인에 실패했습니다. 다시 시도해주세요.");
				        }
				        }else {
				        	System.out.println("알 수 없는 응답 타입입니다.");
				}
			} else {
				System.out.println("서버로부터 응답을 받지 못했습니다.");
			}
		} catch (ParseException e) {
			System.out.println("서버로부터 응답을 파싱하는 중 오류가 발생했습니다: " + e.getMessage());
		}
		
	}

	private static void handleLoginSuccess(BufferedReader stdIn, PrintWriter out, BufferedReader in) throws IOException, ParseException {
		boolean keepRunning = true;
		while (keepRunning) {

			displayPostLoginMenu(); // 로그인 성공 후 메뉴 표시

			String userInput = stdIn.readLine(); // 사용자 입력 받기
			switch (userInput) {
			case "1":
				orderCoffee(stdIn, out, in); // 커피 주문 처리
				break;
			case "2":
				pointCharge(stdIn, out); // 채팅방 이동 처리
				break;
			case "3":
				oneChat(stdIn, out); // 채팅방 이동 처리
				break;
			case "4":
				System.out.println("서비스를 종료합니다.");
				keepRunning = false; // 반복문 종료
				break;
			default:
				System.out.println("잘못된 입력입니다. 다시 선택해주세요.");
				break;
			}
		}
	}

	private static void displayPostLoginMenu() {
		System.out.println("\n로그인중입니다.");
		System.out.println("1. 커피 주문");
		System.out.println("2. 포인트 충전");
		System.out.println("3. 채팅방 이동");
		System.out.println("4. 종료");
		System.out.println("메뉴를 선택하세요. >> ");

	}
	
	private static void orderCoffee(BufferedReader stdIn, PrintWriter out, BufferedReader in) throws IOException, ParseException {
		 CoffeeOrder coffeeOrder = new CoffeeOrder(); // CoffeeOrder 객체 생성
		 coffeeOrder.orderCoffee(stdIn, out); // 커피 주문 메서드 호출
	}
	
	//추가 : payment 메서드 수정해야 함
	/*private static void payment(int price) throws IOException {

	    int payChoice = 0;
	    do {
	        String pay;
	        System.out.println("결제 수단을 선택해주세요:");
	        System.out.println("1. 스타벅스 카드");
	        System.out.println("2. 신용카드");
	        System.out.println("3. 쿠폰");
	        payChoice = scanner.nextInt();
	        switch (payChoice) {
	            case 1:
	                pay = "스타벅스 카드";
	                System.out.println(pay+ " 결제가 완료되었습니다.");
	                newpoint -= coffeePrice; // 커피 가격만큼 포인트 차감
	                System.out.println("잔액 : " + newpoint);
	                break;
	            case 2:
	                pay = "신용카드";
	                System.out.println(pay+" 결제가 완료되었습니다.");
	                break;
	            case 3:
	                pay = "쿠폰";
	                System.out.println(pay+ " 결제가 완료되었습니다.");
	                break;
	            default:
	                System.out.println("잘못된 선택입니다.");
	                scanner.nextLine();
	                break;
	        }
	    } while (payChoice != 1 && payChoice != 2 && payChoice != 3);
	}*/
	
	// 추가 : pointCharge 메서드 작성
	private static void pointCharge(BufferedReader stdIn, PrintWriter out) {
		PointCharge pointChange = new PointCharge();
        
        System.out.println("충전할 금액을 입력하세요. >>");
        int charge;
        
		try {
			charge = Integer.parseInt(stdIn.readLine());
			int newPoint = pointChange.chargePoint(charge);
	        if (newPoint != -1) {
	            System.out.println("충전이 완료되었습니다.(잔액: " + newPoint + ")");
	        } else {
	            System.out.println("충전에 실패했습니다.");
	        }
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void oneChat(BufferedReader stdIn, PrintWriter out) {
	
	}

}