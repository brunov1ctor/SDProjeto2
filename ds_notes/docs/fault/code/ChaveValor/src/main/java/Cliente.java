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
import java.sql.Timestamp;

public class Cliente
{

    public static void main(String[] args) throws IOException
    {
        String raftGroupId = "raft_group____um"; // 16 caracteres.
		
		String nome,ip;
		int porta;
		char novo;
		
		Scanner ler = new Scanner(System.in);
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
		
		do{
		System.out.printf("Escolha um serviço:\n");
		System.out.printf("1-set:\n");
		System.out.printf("2-get:\n");
		System.out.printf("3-delete:\n");
		System.out.printf("4-test and set:\n");
		
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		
		int op;
		String k,v,d,vers;
		
		
		op = ler.nextInt();
        switch (op){
            case 1:
							
				System.out.printf("Informe a chave:\n");
				k = ler.next();
				System.out.printf("Informe o valor:\n");
				v = ler.next();
				vers="1";
				System.out.printf("Informe o dado:\n");
				d = ler.next();
                getValue = client.send(Message.valueOf("set:" + k + ":" + v + ":" + vers + ":" + ts + ":" + d));
                response = getValue.getMessage().getContent().toString(Charset.defaultCharset());
                System.out.println(response);
                break;
				
            case 2:
			
				System.out.printf("Informe a chave:\n");
				k = ler.next();
                getValue = client.sendReadOnly(Message.valueOf("get:" + k));
                response = getValue.getMessage().getContent().toString(Charset.defaultCharset());
                System.out.println(response);
                break;
				
			case 3:
			
				char a;
				
				System.out.printf("Deseja informar a chave (s/n):\n");
				a = (char)System.in.read();
				
				if (a=='s'){
				System.out.printf("Informe a chave:\n");
				k = ler.next();
				System.out.printf("Informe a versão:\n");
				vers = ler.next();
				
				getValue = client.send(Message.valueOf("delv:" + k + ":" + vers));
                response = getValue.getMessage().getContent().toString(Charset.defaultCharset());
                System.out.println(response);
				}
				else{
				System.out.printf("Informe a chave:\n");
				k = ler.next();
				
				getValue = client.send(Message.valueOf("del:" + k));
                response = getValue.getMessage().getContent().toString(Charset.defaultCharset());
                System.out.println("Resposta:" + response);
				}
				break;
				
			case 4:
				
				System.out.printf("Informe a chave:\n");
				k = ler.next();
				System.out.printf("Informe o valor:\n");
				v = ler.next();
				System.out.printf("Informe a versão:\n");
				vers = ler.next();
				
				getValue = client.send(Message.valueOf("testandset:" + k + ":" + v + ":" + vers));
                response = getValue.getMessage().getContent().toString(Charset.defaultCharset());
                System.out.println(response);
				break;
				
            default:
                System.out.println("comando inválido");
        }
		
		System.out.printf("Realizar nova operação (s/n):\n");
		novo = (char)System.in.read();
		}while(novo=='s');
			
        client.close();
    }
}