import org.apache.ratis.client.RaftClient;
import org.apache.ratis.conf.Parameters;
import org.apache.ratis.conf.RaftProperties;
import org.apache.ratis.grpc.GrpcFactory;
import org.apache.ratis.protocol.*;
import org.apache.ratis.thirdparty.com.google.protobuf.ByteString;

import java.util.Scanner;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.text.SimpleDateFormat;  
import java.util.Date; 

public class teste
{

    public static void main(String[] args) throws IOException
    {
        String raftGroupId = "raft_group____um"; // 16 caracteres.
		
		String nome,ip;
		int porta;
		char novo;
		
		Scanner ler = new Scanner(System.in);
        Map<String,InetSocketAddress> id2addr = new HashMap<>();

        
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String ts  = dateFormat.format(new Date());//atualizo timestamp	
		
        id2addr.put("p1", new InetSocketAddress("127.0.0.1", 3000));
        id2addr.put("p2", new InetSocketAddress("127.0.0.1", 3500));
        id2addr.put("p3", new InetSocketAddress("127.0.0.1", 4000));
		
        List<RaftPeer> addresses = id2addr.entrySet()
                .stream()
                .map(e -> new RaftPeer(RaftPeerId.valueOf(e.getKey()), e.getValue()))
                .collect(Collectors.toList());

        final RaftGroup raftGroup = RaftGroup.valueOf(RaftGroupId.valueOf(ByteString.copyFromUtf8(raftGroupId)), addresses);
        RaftProperties raftProperties = new RaftProperties();

        RaftClient client = RaftClient.newBuilder()
                                      .setProperties(raftProperties)
                                      .setRaftGroup(raftGroup)
                                      .setClientRpc(new GrpcFactory(new Parameters())
                                      .newRaftClientRpc(ClientId.randomId(), raftProperties))
                                      .build();

        RaftClientReply getValue;
        String response;
		
        String k1,k2,d;
        int v,vers;

        k1 = "15000";
        k2 = "20000";
        d  = "Opa";
        v = 1;
        vers = 4;

        
        int i;
        for (i = 25; i<1025 ;i++){
            getValue = client.send(Message.valueOf("set;" + Integer.toString(i) + ";" + v + ";" + ts + ";" + d));
            response = getValue.getMessage().getContent().toString(Charset.defaultCharset());
            System.out.println(response);
        }
        
        System.out.println("Teste Estresse Concluido");
        System.out.printf("\n");

        System.out.printf("Teste SET:");
		getValue = client.send(Message.valueOf("set;" + k1 + ";" + v + ";" + ts + ";" + d));
		response = getValue.getMessage().getContent().toString(Charset.defaultCharset());
        System.out.println(response);
        
        System.out.printf("Teste SET ERROR:");
		getValue = client.send(Message.valueOf("set;" + k1 + ";" + v + ";" + ts + ";" + d));
		response = getValue.getMessage().getContent().toString(Charset.defaultCharset());
        System.out.println(response);
        
        System.out.printf("\n");

        System.out.printf("Teste GET:");
		getValue = client.sendReadOnly(Message.valueOf("get;" + k1));
		response = getValue.getMessage().getContent().toString(Charset.defaultCharset());
        System.out.println(response);
        

        System.out.printf("Teste GET ERROR:");
		getValue = client.sendReadOnly(Message.valueOf("get;" + k2));
		response = getValue.getMessage().getContent().toString(Charset.defaultCharset());
        System.out.println(response);
        
        System.out.printf("\n");

        System.out.printf("Teste DEL:");
		getValue = client.send(Message.valueOf("del;" + k1));
		response = getValue.getMessage().getContent().toString(Charset.defaultCharset());
        System.out.println(response);
        

        System.out.printf("Teste DEL ERROR:");
		getValue = client.send(Message.valueOf("del;" + k1));
		response = getValue.getMessage().getContent().toString(Charset.defaultCharset());
        System.out.println(response);
        

        System.out.printf("\n");
        System.out.println("Necessário inserção de novos dados para testes de DELV\n");
        System.out.printf("Teste SET:");
		getValue = client.send(Message.valueOf("set;" + k1 + ";" + v + ";" + ts + ";" + d));
		response = getValue.getMessage().getContent().toString(Charset.defaultCharset());
        System.out.println(response);
       
        System.out.printf("Teste SET:");
		getValue = client.send(Message.valueOf("set;" + k2 + ";" + v + ";" + ts + ";" + d));
		response = getValue.getMessage().getContent().toString(Charset.defaultCharset());
        System.out.println(response);

        System.out.printf("\n");

        System.out.printf("Teste DELV:");
		getValue = client.send(Message.valueOf("delv;" + k1 + ";" + v));
		response = getValue.getMessage().getContent().toString(Charset.defaultCharset());
        System.out.println(response);
        

        System.out.printf("Teste DELV ERROR_NE:");
		getValue = client.send(Message.valueOf("delv;" + k1 + ";" + v));;
		response = getValue.getMessage().getContent().toString(Charset.defaultCharset());
        System.out.println(response);

        
        System.out.printf("Teste DELV ERROR_WV:");
		getValue = client.send(Message.valueOf("delv;" + k2 + ";" + vers));
		response = getValue.getMessage().getContent().toString(Charset.defaultCharset());
        System.out.println(response);

        
        System.out.printf("\n");

        System.out.printf("Teste TEST AND SET ERROR_NE:");
		getValue = client.send(Message.valueOf("testandset;" + k1 + ";" + v + ";" + vers + ";" + ts + ";" + d));
		response = getValue.getMessage().getContent().toString(Charset.defaultCharset());
        System.out.println(response);

        
        System.out.printf("Teste TEST AND SET ERROR_WV:");
		getValue = client.send(Message.valueOf("testandset;" + k2 + ";" + vers + ";" + v + ";" + ts + ";" + d));
		response = getValue.getMessage().getContent().toString(Charset.defaultCharset());
        System.out.println(response);

        
        System.out.printf("Teste TEST AND SET:");
		getValue = client.send(Message.valueOf("testandset;" + k2 + ";" + v + ";" + vers + ";" + ts + ";" + d));
		response = getValue.getMessage().getContent().toString(Charset.defaultCharset());
        System.out.println(response);

        client.close();
    }
}