import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) throws IOException {
        int port = 8088;

        ServerSocket server = new ServerSocket(port);
        while (true){
            Socket socket = server.accept();

            new Thread(new Task(socket)).start();

        }
    }

    /**
     * socket请求处理
     */
    public static class Task implements Runnable{
        private Socket socket;

        public Task(Socket socket){
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                handleSocket();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        /**
         * 客户端socket通信
         */
        private void handleSocket() throws IOException {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(),"GBK"));
            StringBuilder sb = new StringBuilder();
            String temp;
            int index;
            while ((temp=br.readLine())!=null){
                System.out.println(temp);
                if ((index = temp.indexOf("eof")) != -1){
                    sb.append(temp.substring(0,index));
                    break;
                }
                sb.append(temp);
            }
            System.out.println("client: "+sb);

            Writer writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
            writer.write("你好，客户端，我是服务器");
            writer.write("over\n");
            writer.flush();
            writer.close();
            br.close();
            socket.close();
        }
    }

}
