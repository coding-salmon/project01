package _0221구현현황;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import org.json.simple.JSONObject;

public class OneChat implements Runnable{ //Runnable 인터페이스 구현
	
	private BufferedReader stdIn; // 표준 입력 스트림
	private PrintWriter out; // 출력 스트림
	
	// 생성자
	public OneChat(BufferedReader stdIn, PrintWriter out) {
		this.stdIn = stdIn;
		this.out = out;
	}

	@Override
	public void run(){
		try {
			//서버로 채팅방 입장을 요청하는 메시지 전송
		JSONObject json = new JSONObject();
		json.put("type", "enterChatRoom"); //메시지 타입 설정
		out.println(json.toJSONString()); //서버에 메시지 전송
		
		//서버로부터의 응답수신
		String response = stdIn.readLine();
		System.out.println(response); //서버로부터 응답 출력
		
		
		//메시지를 클라이언트에게 전송하는 메서드
		sendMessage("사이렌오더 채팅방에 오신걸 환영합니다.");//예시 메시지 전송
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
    
        // 메시지를 클라이언트에게 전송하는 메서드
        public void sendMessage(String content) {
            JSONObject jsonMessage = new JSONObject(); // JSON 객체 생성
            jsonMessage.put("type", "broadcast"); // 메시지 유형 설정
            jsonMessage.put("content", content); // 메시지 내용 설정
            out.println(jsonMessage.toJSONString()); // JSON 형식의 메시지 전송
        }
    }
