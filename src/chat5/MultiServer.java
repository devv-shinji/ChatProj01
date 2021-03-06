package chat5;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiServer {
	
	static ServerSocket serverSocket = null;
	static Socket socket = null;
	
	//기본생성자-추후 쓰일 예정
	public MultiServer() {
		//실행부 없음
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
		ms.init();
	}
	
	class MultiServerT extends Thread {
		
		Socket socket;
		PrintWriter out = null;
		BufferedReader in = null;
				
		public MultiServerT(Socket socket) {
			this.socket = socket;
			try {
				out = new PrintWriter(this.socket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			}
			catch (Exception e) {
				System.out.println("예외:"+ e);
			}
		}
		@Override
		public void run() {
			String name = "";
			String s = "";
			
			try {
				//클라이언트의 이름을 읽어온 후 콘솔에 출력하고 Echo한다.
				if(in != null) {
					name = in.readLine();					
					System.out.println(name +" 접속");
					out.println("> "+ name +"님이 접속했습니다.");
				}
				
				//클라이언트의 메세지를 읽어서 콘솔에 출력하고 Echo해준다.
				while(in != null) {
					s = in.readLine();
					if(s == null) break;
					
					System.out.println(name +" >>"+ s);
					sendAllMsg(name, s);
				}
			}
			catch (Exception e) {
				System.out.println("예외:" + e);
			}
			finally {
				//종료되는 클라이언트의 스레드명을 출력한다.
				System.out.println(Thread.currentThread().getName()+ " 종료");
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
		
		//서버가 클라이언트에게 메세지를 Echo해주는 메소드
		public void sendAllMsg (String name, String msg) {
			try {
				out.println("> "+ name +" ==> "+ msg);
			}
			catch(Exception e) {
				System.out.println("예외:"+ e);
			}
		}
		
	}
		
}
