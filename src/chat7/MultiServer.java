package chat7;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class MultiServer {
	
	static ServerSocket serverSocket = null;
	static Socket socket = null;
	//클라이언트의 정보 저장을 위한 Map컬렉션 정의
	Map<String, PrintWriter> clientMap;
	
	public Connection con;
	public PreparedStatement psmt;
	public static DBconnect dbconnect;
		
	//생성자
	public MultiServer() {
		//클라이언트의 이름과 출력스트림을 저장할 HashMap생성
		clientMap = new HashMap<String, PrintWriter>();
		//HashMap동기화 설정.아래의 메소드 호출로 간단히 스레드가 사용자정보에 동시에 접근하는 것을 차단한다.
		Collections.synchronizedMap(clientMap);
	}

	//서버의 초기화를 담당할 메소드
	public void init() {
		
		try {
			//9999포트를 열고 클라이언트의 접속을 대기
			serverSocket = new ServerSocket(9999);
			System.out.println("서버가 시작되었습니다.");
			
			/////....접속대기중...
			
			/*
			1명의 클라이언트가 접속할 때마다 접속을 허용(accept())해주고 
			동시에 MultiServerT 스레드를 생성한다.
			해당 스레드는 1명의 클라이언트가 전송하는 메세지를 읽어서 Echo해주는 역할을 담당한다.
			 */
			while(true) {
				socket = serverSocket.accept();			
				/*
				클라이언트의 메세지를 모든 클라이언트에게 전달하기 위한 스레드 생성 및 start.
				 */
				Thread mst = new MultiServerT(socket);
				mst.start();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				serverSocket.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	//메인메소드: Server객체를 생성한 후 초기화한다.
	public static void main(String[] args) {
		MultiServer ms = new MultiServer();
		dbconnect = new DBconnect();
		ms.init();
	}
	
	//접속된 모든 클라이언트에게 메세지를 전달하는 역할의 메소드
	public void sendAllMsg (String name, String msg) {
		
		//Map에 저장된 객체(클라이언트)의 키값(이름)을 먼저 얻어온다.
		Iterator<String> it = clientMap.keySet().iterator();
		//저장된 객체의 갯수만큼 반복한다.
		while(it.hasNext()) {
			try {
				//각 클라이언트의 PrintWriter객체를 얻어온다.
				PrintWriter it_out = (PrintWriter) clientMap.get(it.next());
				
				//클라이언트에게 메세지를 전달한다.
				/*
				매개변수 name이 있는 경우에는 이름+메세지
				없는 경우에는 메세지만 클라이언트로 전달한다.
				 */
				if(name.equals("")) {
					it_out.println(URLEncoder.encode(msg, "UTF-8"));
				}
				else {
					it_out.println("["+ URLEncoder.encode(name, "UTF-8") +"]:"+ URLEncoder.encode(msg, "UTF-8"));
				}
			}
			catch (UnsupportedEncodingException e1) {}
			catch(Exception e) {
				System.out.println("예외:"+ e);
			}
		}
	}////sendAllMsg
	
	public void sendSelfMsg (String name, String msg) {
		
		Iterator<String> it = clientMap.keySet().iterator();
		
		//저장된 객체의 갯수만큼 반복한다.
		while(it.hasNext()) {
			try {
				//각 클라이언트의 PrintWriter객체를 얻어온다.
				String ClientName = it.next(); //키값
				PrintWriter it_out = (PrintWriter) clientMap.get(ClientName); //모든접속자 밸류값
				PrintWriter self_out = null; //해당접속자 밸류값
				
				if(name.equals(ClientName)) {
					
					Iterator<String> it2 = clientMap.keySet().iterator();
					while(it2.hasNext()) {
						it_out.println(it2.next());
					}
				}
			}
			catch(Exception e) {
				System.out.println("예외:"+ e);
			}
		}
		
	}////sendSelfMsg
	
	
	public void sendSecretMsg (String name, String msg) {
		//Map에 저장된 객체(클라이언트)의 키값(이름)을 먼저 얻어온다.
		Iterator<String> it = clientMap.keySet().iterator();
		String toName = null;
		String toMessage = null;
		
		if(msg.startsWith("/to")) {
			
			String[] msgArr = msg.split(" ");
			toName = msgArr[1];
						
			//명령어와 이름만 입력되었을 때. (귓속말 고정)
			if(msgArr.length == 2){
				Scanner scan = new Scanner(System.in);
				String secretFix = scan.nextLine();
				toMessage = secretFix;
			}
			else {
				for (int e=3; e<msgArr.length; e++) {
					toMessage = msgArr[2];
					toMessage += " " + msgArr[e];
				}
			}
		}
		//저장된 객체의 갯수만큼 반복한다.
		while(it.hasNext()) {
			try {
				//각 클라이언트의 PrintWriter객체를 얻어온다.
				String ClientName = it.next();
				PrintWriter it_out = (PrintWriter) clientMap.get(ClientName);
				
				if(toName.equals(ClientName)) {
					it_out.println(URLEncoder.encode(toMessage, "UTF-8"));
				}
			}
			catch (UnsupportedEncodingException e1) {}
			catch(Exception e) {
				System.out.println("예외:"+ e);
			}
		}
	}////sendSecretMsg
	
			
	//내부 클래스
	class MultiServerT extends Thread {
		
		//멤버변수
		Socket socket;
		PrintWriter out = null;
		BufferedReader in = null;
		//생성자: Socket을 기반으로 입출력 스트림을 생성한다.
		public MultiServerT(Socket socket) {
			this.socket = socket;
			try {
				out = new PrintWriter(this.socket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), "UTF-8"));
			}
			catch (Exception e) {
				System.out.println("예외:"+ e);
			}
		}
		
		@Override
		public void run() {
			//클라이언트로부터 전송된 이름(대화명)을 저장할 변수
			String name = "";
			//메세지 저장용 변수
			String s = "";
			
			try {
				//클라이언트의 이름을 읽어와서 저장
				name = in.readLine();
				name = URLDecoder.decode(name, "UTF-8");
				
				//접속한 클라이언트에게 새로운 사용자의 입장을 알림.
				//신규 접속자는 아직 HashMap에 저장이 되지 않았으므로 아래 출력메세지는 기존 접속자에게만 전송된다.
				//접속자를 제외한 나머지 클라이언트만 입장메세지를 받는다.
				sendAllMsg("", name +"님이 입장하셨습니다.");
				
				//현재 접속한(신규) 클라이언트를 HashMap에 저장한다.
				clientMap.put(name, out);
				
				//HashMap에 저장된 객체의 수로 접속자 수를 파악할 수 있다.
				System.out.println(name +" 접속");
				System.out.println("현재 접속자 수는"+ clientMap.size()+ "명 입니다.");
				
				
				//입력한 메세지는 모든 클라이언트에게 Echo된다.
				while(in != null) {
					s = in.readLine();
					s = URLDecoder.decode(s, "UTF-8");
					
					if(s == null) break;
					
					if(s.equalsIgnoreCase("/list")) {
						sendSelfMsg(name, s);
					}
					else if(s.startsWith("/to")) {
						sendSecretMsg(name, s);
					}
					else {
						System.out.println(name  +" >>"+ s);
						sendAllMsg(name, s);
						dbconnect.execute(name, s);							
					}
				}
				
			}
			catch (Exception e) {
				System.out.println("예외:" + e);
			}
			finally {
				/*
				클라이언트가 접속을 종료하면 예외가 발생하게 되어 finally로 넘어오게 된다.
				이 때 "대화명"을 통해 remove()시켜준다.
				 */
				clientMap.remove(name);
				sendAllMsg("", name +"님이 퇴장하셨습니다.");
				//퇴장하는 클라이언트의 스레드명을 보여준다.
				System.out.println(name +" ["+ Thread.currentThread().getName()+"] 퇴장");
				System.out.println("현재 접속자 수는"+ clientMap.size()+"명 입니다.");
				
				try {
					in.close();
					out.close();
					socket.close();
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	}////end of thread
	
}		

