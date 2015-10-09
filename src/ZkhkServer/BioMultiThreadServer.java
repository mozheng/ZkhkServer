package ZkhkServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class BioMultiThreadServer
{
	public static int port = 9000;//���յĶ˿�д����
	private ServerSocket serverSocket;
	private ExecutorService executorService;// �̳߳�
	private final int POOL_SIZE = 10;// ����CPU�̳߳ش�С
	public static int timeout = 20000;



	public BioMultiThreadServer() 
	{
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Runtime��availableProcessor()�������ص�ǰϵͳ��CPU��Ŀ.
		executorService = Executors.newFixedThreadPool(Runtime.getRuntime()
				.availableProcessors() * POOL_SIZE);
		System.out.println("socket server start");
	}

	public void service()
	{

		while (true)
		{
			System.out.println("startthread");
			Socket socket = null;
			try
			{
					socket = serverSocket.accept();
					executorService.execute(new socketHandler(socket));
				
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
