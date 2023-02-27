package views;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.google.gson.Gson;

import dto.request.RequsetDto;

import java.awt.CardLayout;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ClientApplication extends JFrame {

	private static final long serialVersionUID = -4753767777928836759L;

	private Gson gson;
	private Socket socket;
	
	private JPanel mainPanel;
	private CardLayout mainCard;
	
	private JTextField usernameField;
	
	private JTextField sendMessageField;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientApplication frame = new ClientApplication(); // 싱글톤으로 수정
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ClientApplication() {
		
		/* ===============<< init >>============== */
		gson = new Gson();
		try {
			socket = new Socket("127.0.0.1", 9090);
			ClientReceive clientReceive = new ClientReceive(socket);
			clientReceive.start();
			
		} catch (UnknownHostException e1) { // IOException 하위에 포함되어있음
			e1.printStackTrace();
		} catch (ConnectException e1) { 
			JOptionPane.showMessageDialog(this, "서버에 접속할 수 없습니다.", "접속오류", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		/* ===============<< frame set >>============== */

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(600, 150, 480, 800); // x축,y축 창이 뜨는 좌표(600,150)

		/* ===============<< panel >>============== */

		mainPanel = new JPanel();
		JPanel loginPanel = new JPanel();
		JPanel roomListPanel = new JPanel();
		JPanel roomPanel = new JPanel();

		/* ===============<< layout >>============== */
		
		mainCard = new CardLayout();
		
		mainPanel.setLayout(mainCard);
		loginPanel.setLayout(null);
		roomListPanel.setLayout(null);
		roomPanel.setLayout(null);

		/* ===============<< panel set >>============== */

		setContentPane(mainPanel);
		mainPanel.add(loginPanel, "loginPanel");
		mainPanel.add(roomListPanel, "roomListPanel");
		mainPanel.add(roomPanel, "roomPanel");

		
		/* ===============<< login panel >>============== */
		
		JButton enterButton = new JButton("접속하기");
		
		usernameField = new JTextField();
		usernameField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					enterButton.doClick();  //엔터버튼 == 클릭으로 처리 
				}
			}
		});
		
		usernameField.setBounds(75, 410, 300, 40);
		loginPanel.add(usernameField);
		usernameField.setColumns(10);

		enterButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				RequsetDto<String> usernameCheckReqDto = 
						new RequsetDto<String>("usernameCheck", usernameField.getText());
				sendRequest(usernameCheckReqDto);
			}
		});
		enterButton.setBounds(75, 480, 300, 40);
		loginPanel.add(enterButton);

		/* ===============<< roomList panel >>============== */

		JScrollPane roomListScroll = new JScrollPane();
		roomListScroll.setBounds(118, 0, 336, 761);
		roomListPanel.add(roomListScroll);

		JList roomList = new JList();
		roomListScroll.setViewportView(roomList);

		JButton createRoomButton = new JButton("방생성");
		createRoomButton.setBounds(9, 60, 97, 97);
		roomListPanel.add(createRoomButton);

		/*===============<< room panel >>==============*/

		JScrollPane joinUserLIstScroll = new JScrollPane();
		joinUserLIstScroll.setBounds(0, 0, 350, 100);
		roomPanel.add(joinUserLIstScroll);

		JList joinUserList = new JList();
		joinUserLIstScroll.setViewportView(joinUserList);

		JButton roomExitButton = new JButton("나가기");
		roomExitButton.setBounds(352, 0, 102, 100);
		roomPanel.add(roomExitButton);

		JScrollPane chattingContentScroll = new JScrollPane();
		chattingContentScroll.setBounds(0, 110, 454, 561);
		roomPanel.add(chattingContentScroll);

		JTextArea chattingContent = new JTextArea();
		chattingContentScroll.setViewportView(chattingContent);

		sendMessageField = new JTextField();
		sendMessageField.setBounds(0, 681, 372, 60);
		roomPanel.add(sendMessageField);
		sendMessageField.setColumns(10);

		JButton sendButton = new JButton("\"전송\"");
		sendButton.setBounds(373, 681, 81, 60);
		roomPanel.add(sendButton);

	}
	private void sendRequest(RequsetDto<?> requestDto) {
		String reqJson = gson.toJson(requestDto);
		OutputStream outputStream = null;
		PrintWriter printWriter = null;
		try {
			outputStream = socket.getOutputStream();
			PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
			printWriter.println(reqJson);
		}
		
		
		
	}
}
