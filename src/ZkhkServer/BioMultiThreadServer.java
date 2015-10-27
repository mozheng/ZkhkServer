package ZkhkServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BioMultiThreadServer implements Runnable
{
	public static int port = 9000;//���յĶ˿�д����
	private ServerSocket serverSocket;
	private ExecutorService executorService;// �̳߳�
	private final int POOL_SIZE = 10;// ����CPU�̳߳ش�С
	public static int timeout = 20000;

	public BioMultiThreadServer() throws Exception
	{
			serverSocket = new ServerSocket(port);
		// Runtime��availableProcessor()�������ص�ǰϵͳ��CPU��Ŀ.
		executorService = Executors.newFixedThreadPool(Runtime.getRuntime()
				.availableProcessors() * POOL_SIZE);
		System.out.println("socket server start");
	}

	public void run()
	{
		while (true)
		{
			System.out.println("startthread");
			Socket socket = null;
				try {
					socket = serverSocket.accept();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				executorService.execute(new socketHandler(socket));
		}
	}
}
