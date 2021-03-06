package bridgechat.backend;

import bridgechat.backend.chat.Chat;
import bridgechat.backend.tracker.BroadcastMessage;
import bridgechat.backend.tracker.OnlineUser;
import bridgechat.backend.tracker.Token;
import bridgechat.backend.tracker.exception.SameUserException;
import bridgechat.dao.MessageDAO;
import bridgechat.dao.OnlineUserDAO;
import bridgechat.dao.UserDAO;
import com.google.gson.GsonBuilder;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.BindException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Node extends Thread {

    private static int PORT = 50123;
    private static final Logger LOGGER = Logger.getLogger(Node.class.getName());
    private static String trackerAddr;
    private static String token;
    
    private static final Gson GSON = new GsonBuilder().create();
    private static boolean interrupted;

    public static void setTrackerAddr(String ta) {
        trackerAddr = "https://" + ta + ":5000";
    }
    
    @Override
    public void run() {
//        setup_ssl();
//        System.out.println(trackerAddr);
//        try {
//            register_user(UserDAO.getInstance().getUsername());
////            broadcast_ip();
//            OnlineUserDAO.getInstance().setUsers(getOnlines());
//        } catch (java.net.ConnectException e) {
//            LOGGER.severe("Can't reach the tracker");
//            interrupt();
//        } catch (IOException ex) {
//            Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
//            interrupt();
//        }
        
        while(!interrupted) {
            try {
                chat_server();   
            } catch (BindException e) {
                LOGGER.log(Level.WARNING, "Port {0} alredy in use", PORT);
            } catch (IOException ex) {
                Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                PORT++;
            }
        }
        
        MessageDAO.getInstace().closeChats();
    }
    
    public static void setup_ssl() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                @Override
                public void checkClientTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
                }
                @Override
                public void checkServerTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
                }
            }
        };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (KeyManagementException | NoSuchAlgorithmException e) {

        }

        HttpsURLConnection.setDefaultHostnameVerifier ((hostname, session) -> true);
    }
    
    public static void register_user(String user) throws IOException, SameUserException {
        URL url = new URL(String.format(
                "%s/user",
                trackerAddr
        ));
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("X-Auth-Login", user);
        
        if(con.getResponseCode() == 401)
            throw new SameUserException();
        
        BufferedReader br =  new BufferedReader(
                new InputStreamReader(con.getInputStream())
        );
  
        String s = br.lines().collect(Collectors.joining());
        
        token = GSON.fromJson(s, Token.class).getToken();
        LOGGER.info("Registerd");
    }
    
    private static void broadcast_ip() throws IOException {
        broadcast_ip(1);
    }
    
    public static void broadcast_ip(int op) throws IOException {
        String ip;
        try(final DatagramSocket socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ip = socket.getLocalAddress().getHostAddress();
        }
        
        URL url = new URL(String.format(
                "%s/broadcast",
                trackerAddr
        ));
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("content-type", "application/json");
        con.setRequestProperty("X-Auth-Token", token);
        con.setDoOutput(true); 
        con.setDoInput(true); 
        
        
        BroadcastMessage msg = new BroadcastMessage(ip, PORT, op);
        try (DataOutputStream output = new DataOutputStream(con.getOutputStream())) {
            output.writeBytes(GSON.toJson(msg));
        }
        
        System.out.println(con.getResponseCode());
        LOGGER.info("Broadcasted");
    }
    
    public static OnlineUser[] getOnlines() throws IOException {
        URL url = new URL(String.format(
                "%s/online",
                trackerAddr
        ));
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("X-Auth-Token", token);
        
        BufferedReader br =  new BufferedReader(
                new InputStreamReader(con.getInputStream())
        );
  
        String s = br.lines().collect(Collectors.joining());
        return GSON.fromJson(s, OnlineUser[].class);
    }
    
    private static void chat_server() throws IOException {
        ServerSocket ss;
        ss = new ServerSocket(PORT);
        LOGGER.log(Level.INFO, "Starting node at port {0}", PORT);
        broadcast_ip();
        
        while(!interrupted) {
            Chat cs;
            cs = new Chat(ss.accept());
            cs.start();
        }
    }

    public static int getPORT() {
        return PORT;
    }

    @Override
    public boolean isInterrupted() {
        return interrupted;
    }

    @Override
    public void interrupt() {
        interrupted = true;
    }
}