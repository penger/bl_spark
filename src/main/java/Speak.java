import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Speak  {
    boolean flag = false;
    Socket client;
    PrintWriter pw;
    ServerSocket server;
    static int port = 9999;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public Speak() {
        try {
            server = new ServerSocket(port);
            this.serve();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void serve() throws IOException {
        flag = true;
        while (flag) {
            try {
                client = server.accept();
                System.out.println("["+sdf.format(new Date())+"] "+client.getInetAddress() + "已建立连接！");
                // 输入流
                InputStream is = client.getInputStream();
                BufferedReader bri = new BufferedReader(new InputStreamReader(is));
                // 输出流
                OutputStream os = client.getOutputStream();
                // 参数true表示每写一行，PrintWriter缓存就自动溢出，把数据写到目的地
                pw = new PrintWriter(os, true);
                pw.write("gonpeng");
                System.out.println("write to socket");
                pw.flush();
                is.close();
                bri.close();
                os.close();
                pw.close();
                Thread.sleep(100);
            } catch (Exception e) {
                System.out.println("["+sdf.format(new Date())+"] connection exit!");
                System.out.println();
            } finally {

            }
        }

    }

    public static void main(String args[]) {
        if(args.length==1) {
            try{
                port = Integer.parseInt(args[0]);
            }catch(Exception e) {
                String help="(1)no arguments,listen the default port:9999\n\"java nc\"\n\n(2)or listen customer port:8888\n\"java nc 8888\"";
                System.out.println(help);
                return;
            }
        }
        new Speak();
    }
}
