package ZkhkServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

class socketHandler implements Runnable
{
/*成员变量
 * */
	private CurrentTime ct = new CurrentTime();
	private Socket socket;
	/*构造函数
	 * */
	public socketHandler(Socket socket)
	{
		this.socket = socket;
	}
	/*重要函数
	 * */
	public void run()
	{

		BufferedReader br = null;
		PrintWriter pw = null;
		boolean res = true;
		try
		{
			int count = 0;
			int port = BioMultiThreadServer.port;
			System.out.println("新连接： " + socket.getInetAddress() + "  端口:"
					+ socket.getPort() + "  连接时间：" + getDate());
			br = getReader(socket);
			// 用于发送返回信息,可以不需要装饰这么多io流使用缓冲流时发送数据要注意调用.flush()方法
			pw = getWriter(socket);
			socket.setSoTimeout(BioMultiThreadServer.timeout);
			while (res && count < 10)
			{
				byte[] b = new byte[43];
				for (int i = 0; i < b.length; i++)
				{
					b[i] = (byte) br.read();
				}
				String receive_message = new String(b);
				String start = receive_message.substring(0, 2);

				if (start.equals("5A"))
				{
					String SN_original = receive_message.substring(11)
							.substring(0, 9); // 机器码

					String SN = SN_original.substring(7).substring(0, 2)
							+ SN_original.substring(0, 7);

					int high = Integer.parseInt(receive_message.substring(20)
							.substring(0, 3));
					int low = Integer.parseInt(receive_message.substring(23)
							.substring(0, 3));
					int pulse = Integer.parseInt(receive_message.substring(26)
							.substring(0, 3));
					String time = "20"
							+ receive_message.substring(29).substring(0, 2)
							+ "-"
							+ receive_message.substring(31).substring(0, 2)
							+ "-"
							+ receive_message.substring(33).substring(0, 2)
							+ " "
							+ receive_message.substring(35).substring(0, 2)
							+ ":"
							+ receive_message.substring(37).substring(0, 2)
							+ ":00";// 时间

					System.out.println("收到字符串：" + receive_message + " 设备号："
							+ SN + "  测量时间: " + time);

					ct.getCurrentTime();
					String msg = "+IP792A206723281F" + getYearH().toUpperCase()
							+ getMonthH().toUpperCase()
							+ getDayH().toUpperCase()
							+ getHourH().toUpperCase()
							+ getMinuteH().toUpperCase()
							+ getsum().toUpperCase() + "OK";
					pw.println(msg);
					addDataToSqlite(SN_original, SN, high, low, pulse, time);

				}
				pw.flush();
				count++;
			}

		} catch (Exception e)
		{
			res = false;
		} finally
		{
			try
			{
				if (socket != null)
					socket.close();
			} catch (Exception e)
			{

				e.printStackTrace();
			}
			try
			{
				if (br != null)
					br.close();
				if (pw != null)
				{
					pw.close();
					pw.flush();
				}
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}	
		Thread.yield();
	}

	private void addDataToSqlite(String SN_original, String SN, int high,
			int low, int pulse, String time)
	{
		if (SN_original.substring(0, 1).equals("H")||SN_original.substring(0, 1).equals("I")|| SN_original.substring(0, 1).equals("J")|| SN_original.substring(0, 1).equals("K"))
		{
			try
			{
				String sql;
				System.out.println(12);
				Class.forName("org.sqlite.JDBC");
				Connection connection = DriverManager.getConnection("jdbc:sqlite://c:/jkserver.sqlite");
				Statement statement = connection.createStatement();
				sql = "insert into dev values('"+SN+ "',"+high+","+low+","+ pulse+", '"+time+ "' ,'"+getDate()+"')";
				System.out.println(sql);
				statement.executeUpdate(sql);
				connection.close();
			} catch (Exception e)
			{
				e.printStackTrace();
			} 
		}
	}

/*
 * 类bean方法
 * */	
	private String getDate()
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		return df.format(new Date());// new Date()为获取当前系统时间
	}


	private PrintWriter getWriter(Socket socket) throws IOException
	{

		OutputStream socketOut = socket.getOutputStream();
		return new PrintWriter(new BufferedWriter(new OutputStreamWriter(
				socketOut)), true);

	}

	private BufferedReader getReader(Socket socket) throws IOException
	{
		InputStream socketIn = socket.getInputStream();
		return new BufferedReader(new InputStreamReader(socketIn));
	}

	
	public String getYearH()
	{
		if (Integer.toHexString(ct.getYear() & 0xFF).length() == 1)
		{
			return "0" + Integer.toHexString(ct.getYear() & 0xFF);
		} else
		{
			return Integer.toHexString(ct.getYear() & 0xFF);
		}
	}

	public String getMonthH()
	{
		if (Integer.toHexString(ct.getMonth() & 0xFF).length() == 1)
		{
			return "0" + Integer.toHexString(ct.getMonth() & 0xFF);
		} else
		{
			return Integer.toHexString(ct.getMonth() & 0xFF);
		}
	}

	public String getDayH()
	{
		if (Integer.toHexString(ct.getDay() & 0xFF).length() == 1)
		{
			return "0" + Integer.toHexString(ct.getDay() & 0xFF);
		} else
		{
			return Integer.toHexString(ct.getDay() & 0xFF);
		}
	}

	public String getHourH()
	{
		if (Integer.toHexString(ct.getHour() & 0xFF).length() == 1)
		{
			return "0" + Integer.toHexString(ct.getHour() & 0xFF);
		} else
		{
			return Integer.toHexString(ct.getHour() & 0xFF);
		}
	}

	public String getMinuteH()
	{
		if (Integer.toHexString(ct.getMinute() & 0xFF).length() == 1)
		{
			return "0" + Integer.toHexString(ct.getMinute() & 0xFF);
		} else
		{
			return Integer.toHexString(ct.getMinute() & 0xFF);
		}
	}

	public String getsum()
	{
		if (Integer.toHexString(
				(ct.getYear() ^ ct.getMonth() ^ ct.getDay() ^ ct.getHour() ^ ct
						.getMinute()) & 0xFF).length() == 1)
		{
			return "0"
					+ Integer
							.toHexString((ct.getYear() ^ ct.getMonth()
									^ ct.getDay() ^ ct.getHour() ^ ct
									.getMinute()) & 0xFF);
		} else
		{
			return Integer.toHexString((ct.getYear() ^ ct.getMonth()
					^ ct.getDay() ^ ct.getHour() ^ ct.getMinute()) & 0xFF);
		}
	}

}
