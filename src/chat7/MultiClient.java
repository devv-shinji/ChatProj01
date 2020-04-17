package chat7;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class MultiClient {

	public static void main(String[] args) {
		
		String s_name = null;		
		boolean flag = true;
		
		System.out.print("이름을 입력하세요:");
		Scanner scanner = new Scanner(System.in);
		s_name = scanner.nextLine();
		
		try {
			String ServerIP = "localhost";
			if(args.length > 0) {
				ServerIP = args[0];
			}
			//IP주소와 포트를 기반으로 소켓객체를 생성하여 서버에 전달함
			Socket socket = new Socket(ServerIP, 9999);
			
			//서버와 연결되면 콘솔에 메세지 출력
			System.out.println("서버와 연결되었습니다...");
			System.out.println("==============================");
			System.out.println("메세지를 입력하세요.");
			System.out.println("- 귓속말: /to 대화명 대화내용");
			System.out.println("- 귓속말 고정/해제 : /to 대화명");
			System.out.println("- 전체접속자 보기 : /list");
			System.out.println("==============================\n");
			
			
			//서버에서 보내는 메세지를 읽어와 클라이언트에 출력하기 위한 Receiver쓰레드 생성
			Thread receiver = new Receiver(socket);
			receiver.start();
			
			//클라이언트의 메세지를 서버로 전송해주는 쓰레드 생성
			Thread sender = new Sender(socket, s_name);
			sender.start();
			
		}
		catch(Exception e) {
			System.out.println("예외발생[MultiClient]"+ e);
		}
		
	}
}
