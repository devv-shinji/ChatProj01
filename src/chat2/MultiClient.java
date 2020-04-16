package chat2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class MultiClient {

	public static void main(String[] args) {
		
		System.out.print("이름을 입력하세요:");
		Scanner scanner = new Scanner(System.in);
		String s_name = scanner.nextLine();
		
		PrintWriter out = null;
		BufferedReader in = null;
		
		try {
			//localhost대신 127.0.0.1로 접속해도 무방하다.
			//별도의 매개변수가 없으면 접속IP는 localhost로 고정됨
			String ServerIP = "localhost";
			//클라이언트 실행 시 매개변수가 있는 경우 IP로 설정함
			if(args.length > 0) {
				ServerIP = args[0];
			}
			//IP주소와 포트를 기반으로 소켓객체를 생성하여 서버에 전달함
			Socket socket = new Socket(ServerIP, 9999);
			//서버와 연결되면 콘솔에 메세지 출력
			System.out.println("서버와 연결되었습니다...");
			
			/*
			InputStreamTrsfrt / OutputStreamReader는
			바이트스트림과 문자스트림의 상호변환을 제공하는 입출력스트림이다.
			바이트를 읽어서 지정된 문자인코딩에 따라 문자로 변환하는 데 사용된다.
			 */
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			//접속자의 "대화명"을 서버측으로 최초 전송한다.
			out.println(s_name);
			
			/*
			소켓이 close되기 전이라면 클라이언트는 지속적으로 서버측으로 메세지를 보낼 수 있다.
			 */
			while(out != null) {
				try {
					//서버가 echo해준 내용을 라인 단위로 읽어와서 콘솔출력
					if(in!=null) {
						System.out.println("Receive: "+ in.readLine());
					}
					
					//클라이언트는 내용을 입력 후 서버로 전송한다.
					String s2 = scanner.nextLine();
					if(s2.equals("q")||s2.equals("Q")) {
						//입력값이 Q(q)이면 while 루프 탈출
						break;
					}
					else {
						//만약 q가 아니면 서버로 입력내용 전송
						out.println(s2);
					}
				}
				catch (Exception e) {
					System.out.println("예외:"+ e);
				}
			}

			//클라이언트가 q를 입력하면 스트림과 소켓을 종료한다.
			in.close();
			out.close();
			socket.close();
		}
		catch (Exception e) {
			System.out.println("예외발생[MultiClient]"+ e);
		}
	}
}
