public class Client {

    String res;
    Socket socket = null;            //Server와 통신하기 위한 Socket
    InetAddress ia = null;

    public String clientTest(){

        try {
            ia = InetAddress.getByName("172.30.58.165");    //서버로 접속
            socket = new Socket(ia,9999);

            System.out.println(socket.toString());

            res = "succeed";
        }catch(IOException e) {
            e.printStackTrace();
            res = "fail";
        }


        return res;
    }
}
