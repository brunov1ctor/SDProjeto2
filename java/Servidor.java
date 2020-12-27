import org.apache.ratis.conf.RaftProperties;
import org.apache.ratis.grpc.GrpcConfigKeys;
import org.apache.ratis.protocol.RaftGroup;
import org.apache.ratis.protocol.RaftGroupId;
import org.apache.ratis.protocol.RaftPeer;
import org.apache.ratis.protocol.RaftPeerId;
import org.apache.ratis.server.RaftServer;
import org.apache.ratis.server.RaftServerConfigKeys;
import org.apache.ratis.thirdparty.com.google.protobuf.ByteString;
import org.apache.ratis.util.LifeCycle;

import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Servidor
{

    //Parametros: myId
    public static void main(String[] args) throws IOException, InterruptedException
    {
        String raftGroupId = "raft_group____um"; // 16 caracteres.

		String nome,ip;
		int porta;
		char novo;
		
		Scanner ler = new Scanner(System.in);
		//Setup for node all nodes.
        Map<String,InetSocketAddress> id2addr = new HashMap<>();
		
		do{
		System.out.printf("Informe o nome do servidor para participar do grupo raft:\n");
		nome = ler.next();
		System.out.printf("Informe o ip do servidor:\n");
		ip = ler.next();
		System.out.printf("Informe a porta do servidor:\n");
		porta = ler.nextInt();
		  
		id2addr.put(nome, new InetSocketAddress(ip, porta));
		
		System.out.printf("Adicionar novo membro servidor (s/n):\n");
		novo = (char)System.in.read();
		
		}while(novo=='s');
        

        List<RaftPeer> addresses = id2addr.entrySet()
                                          .stream()
                                          .map(e -> new RaftPeer(RaftPeerId.valueOf(e.getKey()), e.getValue()))
                                          .collect(Collectors.toList());
										  
		
		String self;
		System.out.printf("Definir este servidor como:\n");
		self = ler.next();
		
        //Setup for this node.
        RaftPeerId myId = RaftPeerId.valueOf(self);

        if (addresses.stream().noneMatch(p -> p.getId().equals(myId)))
        {
            System.out.println("Identificador " + self + " é inválido.");
            System.exit(1);
        }

        RaftProperties properties = new RaftProperties();
        properties.setInt(GrpcConfigKeys.OutputStream.RETRY_TIMES_KEY, Integer.MAX_VALUE);
        GrpcConfigKeys.Server.setPort(properties, id2addr.get(self).getPort());
        RaftServerConfigKeys.setStorageDir(properties, Collections.singletonList(new File("/tmp/" + myId)));


        //Join the group of processes.
        final RaftGroup raftGroup = RaftGroup.valueOf(RaftGroupId.valueOf(ByteString.copyFromUtf8(raftGroupId)), addresses);
        RaftServer raftServer = RaftServer.newBuilder()
                .setServerId(myId)
                .setStateMachine(new MaquinaDeEstados()).setProperties(properties)
                .setGroup(raftGroup)
                .build();
        raftServer.start();

        while(raftServer.getLifeCycleState() != LifeCycle.State.CLOSED) {
            TimeUnit.SECONDS.sleep(1);
        }
    }
}