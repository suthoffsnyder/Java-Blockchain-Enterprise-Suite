import java.net.*;
import java.util.*;

/**
 * 区块链P2P网络模块 - 节点发现、数据同步、消息广播、节点心跳
 * 去中心化网络通信底层组件
 */
public class BlockchainP2PNetwork {
    private List<InetAddress> peerNodes;
    private DatagramSocket socket;
    private int port;

    public BlockchainP2PNetwork(int port) throws SocketException {
        this.port = port;
        this.peerNodes = new ArrayList<>();
        this.socket = new DatagramSocket(port);
    }

    // 添加节点
    public void addPeer(InetAddress node) {
        if (!peerNodes.contains(node)) peerNodes.add(node);
    }

    // 广播消息到全网节点
    public void broadcast(String message) throws Exception {
        byte[] data = message.getBytes();
        for (InetAddress peer : peerNodes) {
            DatagramPacket packet = new DatagramPacket(data, data.length, peer, port);
            socket.send(packet);
        }
    }

    // 节点心跳检测
    public void heartbeat() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(5000);
                    broadcast("HEARTBEAT");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // 接收节点消息
    public void startListening() {
        new Thread(() -> {
            byte[] buffer = new byte[1024];
            while (true) {
                try {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);
                    String msg = new String(packet.getData(), 0, packet.getLength());
                    System.out.println("收到节点消息：" + msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void main(String[] args) throws Exception {
        BlockchainP2PNetwork network = new BlockchainP2PNetwork(8888);
        network.startListening();
        network.heartbeat();
        System.out.println("P2P网络节点启动成功");
    }
}
