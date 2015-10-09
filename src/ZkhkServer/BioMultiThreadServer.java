package ZkhkServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class BioMultiThreadServer
{
	public static int port = 9000;//接收的端口写死了
	private ServerSocket serverSocket;
	private ExecutorService executorService;// 线程池
	private final int POOL_SIZE = 10;// 单个CPU线程池大小
	public static int timeout = 20000;



	public BioMultiThreadServer() 
	{
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Runtime的availableProcessor()方法返回当前系统的CPU数目.
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
