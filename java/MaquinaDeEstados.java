import org.apache.ratis.proto.RaftProtos;
import org.apache.ratis.protocol.Message;
import org.apache.ratis.statemachine.TransactionContext;
import org.apache.ratis.statemachine.impl.BaseStateMachine;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.lang.*;
import java.text.SimpleDateFormat;  
import java.util.Date; 

class Tupla{
	String ts,d;
	int vers;
	
	public Tupla(int vers,String d,String ts){	
	this.vers = vers;	
	this.d = d;	
	this.ts = ts;		
	}
	
	public int getvers(){
		return vers;
	}
	
	public String getts(){
		return ts;
	}
	
	public String getd(){
		return d;
	}
}

public class MaquinaDeEstados extends BaseStateMachine
{
	

    private final Map<String, Object> key2values = new ConcurrentHashMap<>();
	
	 @Override
    public CompletableFuture<Message> applyTransaction(TransactionContext trx) {   ///set del delv testandset   
        final RaftProtos.LogEntryProto entry = trx.getLogEntry();
        final String[] opKeyValue = entry.getStateMachineLogEntry().getLogData().toString(Charset.defaultCharset()).split(";");
		
		if(opKeyValue[0].equals("set")){ // se a operação for set 
		
			if(! key2values.containsKey(opKeyValue[1])){ //e a chave não existir
				
				Tupla t = new Tupla(Integer.parseInt(opKeyValue[2]), opKeyValue[3], opKeyValue[4]);
				
				key2values.put(opKeyValue[1], t);
				final String result = "SUCCESS :" + null;
				final CompletableFuture<Message> f = CompletableFuture.completedFuture(Message.valueOf(result));
				final RaftProtos.RaftPeerRole role = trx.getServerRole();
				LOG.info("{}:{} {} {}={}", role, getId(), opKeyValue[0], opKeyValue[1], opKeyValue[4]);

				if (LOG.isTraceEnabled()) {
					LOG.trace("{}: key/values={}", getId(), key2values);
				}
				return f;
				
			}else{//a chave existe
				Tupla t = (Tupla)key2values.get(opKeyValue[1]);
				final String result = "ERROR :" + "(" + t.vers + ", " + t.ts + ", " + t.d +  ")";
				final CompletableFuture<Message> f = CompletableFuture.completedFuture(Message.valueOf(result));
				final RaftProtos.RaftPeerRole role = trx.getServerRole();
				LOG.info("{}:{} {} {}={}", role, getId(), opKeyValue[0], opKeyValue[1], opKeyValue[4]);

				if (LOG.isTraceEnabled()) {
					LOG.trace("{}: key/values={}", getId(), key2values);
				}
				return f;			
				
			}
		}
		
		if(opKeyValue[0].equals("delv")){ // se a operação for delete com versao
		
			if(key2values.containsKey(opKeyValue[1]) ){ //e a chave for encontrada
				Tupla t  = (Tupla)key2values.get(opKeyValue[1]); //pego minha tupla no mapa
				
				if(t.getvers()==Integer.parseInt(opKeyValue[2])){//se a versão for igual
					key2values.remove(opKeyValue[1]);
					final String result = "SUCCESS :" + " (" + String.valueOf(t.getvers())+", "+ t.getts()+", "+ t.getd() + ")";
					final CompletableFuture<Message> f = CompletableFuture.completedFuture(Message.valueOf(result));
					final RaftProtos.RaftPeerRole role = trx.getServerRole();
					LOG.info("{}:{} {} {}={}", role, getId(), opKeyValue[0], opKeyValue[1], opKeyValue[2]);

					if (LOG.isTraceEnabled()) {
						LOG.trace("{}: key/values={}", getId(), key2values);
					}
					return f;
				
				}else{//a versao é diferente
					final String result = "ERROR_WV :" +", "+ String.valueOf(t.getvers())+", "+ t.getts()+", "+ t.getd();
					final CompletableFuture<Message> f = CompletableFuture.completedFuture(Message.valueOf(result));
					final RaftProtos.RaftPeerRole role = trx.getServerRole();
					LOG.info("{}:{} {} {}={}", role, getId(), opKeyValue[0], opKeyValue[1], opKeyValue[2]);

					if (LOG.isTraceEnabled()) {
						LOG.trace("{}: key/values={}", getId(), key2values);
					}
					return f;					
				}				
				
			}else{//a chave não foi encontrada
					final String result = "ERROR_NE :" + null;
					final CompletableFuture<Message> f = CompletableFuture.completedFuture(Message.valueOf(result));
					final RaftProtos.RaftPeerRole role = trx.getServerRole();
					LOG.info("{}:{} {} {}={}", role, getId(), opKeyValue[0], opKeyValue[1], opKeyValue[2]);

					if (LOG.isTraceEnabled()) {
						LOG.trace("{}: key/values={}", getId(), key2values);
					}
					return f;				
			}
		}
		
		if(opKeyValue[0].equals("del")){ // se a operação for delete
	
			if(key2values.containsKey(opKeyValue[1])){ //e a chave for encontrada
				Tupla t  = (Tupla)key2values.get(opKeyValue[1]); //pego minha tupla no mapa
				
				key2values.remove(opKeyValue[1]);
				final String result = "SUCCESS :" + " (" + String.valueOf(t.getvers())+", "+ t.getts()+", "+ t.getd()+")";
				final CompletableFuture<Message> f = CompletableFuture.completedFuture(Message.valueOf(result));
				final RaftProtos.RaftPeerRole role = trx.getServerRole();
				LOG.info("{}:{} {} {}={}", role, getId(), opKeyValue[0], opKeyValue[1], result);
				if (LOG.isTraceEnabled()) {
					LOG.trace("{}: key/values={}", getId(), key2values);
				}
				System.out.printf("f");
				return f;
				
			}else{//a chave não foi encontrada
				
				final String result = "ERROR :" + null;
				final CompletableFuture<Message> f = CompletableFuture.completedFuture(Message.valueOf(result));
				final RaftProtos.RaftPeerRole role = trx.getServerRole();
				LOG.info("{}:{} {} {}={}", role, getId(), opKeyValue[0], opKeyValue[1], result);

				if (LOG.isTraceEnabled()) {
					LOG.trace("{}: key/values={}", getId(), key2values);
				}
				return f;
				
			}
		}
		
		if(opKeyValue[0].equals("testandset")){ // se a operação for testandsetset 
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			String ts  = dateFormat.format(new Date());//atualizo timestamp
					
			if(key2values.containsKey(opKeyValue[1]) ){ //e a chave for encontrada
				Tupla t  = (Tupla)key2values.get(opKeyValue[1]); //pego minha tupla no mapa
				
				if(t.getvers()==Integer.parseInt(opKeyValue[2])){//se a versão for igual
								
					
					Tupla t_novo = new Tupla(Integer.parseInt(opKeyValue[3]),opKeyValue[4], opKeyValue[5]);//atualizo a versão
										
					key2values.replace(opKeyValue[1], t_novo); //coloco a nova tupla no mapa com a mesma chave
					final String result = "SUCCESS : " + "(" + t.getvers()+ ", " +t.getd()+ ", " +t.getts() + ")";  //retorno a mensagem com dados atualizados
					final CompletableFuture<Message> f = CompletableFuture.completedFuture(Message.valueOf(result));
					final RaftProtos.RaftPeerRole role = trx.getServerRole();
					LOG.info("{}:{} {} {}={}", role, getId(), opKeyValue[0], opKeyValue[1], opKeyValue[2]);

					if (LOG.isTraceEnabled()) {
						LOG.trace("{}: key/values={}", getId(), key2values);
					}
					return f;
				
				}else{//a versao é diferente
					final String result = "ERROR_WV :" + "(" + t.getvers()+ ", " +t.getd()+ ", " +t.getts() + ")";
					final CompletableFuture<Message> f = CompletableFuture.completedFuture(Message.valueOf(result));
					final RaftProtos.RaftPeerRole role = trx.getServerRole();
					LOG.info("{}:{} {} {}={}", role, getId(), opKeyValue[0], opKeyValue[1], opKeyValue[2]);

					if (LOG.isTraceEnabled()) {
						LOG.trace("{}: key/values={}", getId(), key2values);
					}
					return f;					
				}				
				
			}else{//a chave não foi encontrada
					final String result = "ERROR_NE :" + null;
					final CompletableFuture<Message> f = CompletableFuture.completedFuture(Message.valueOf(result));
					final RaftProtos.RaftPeerRole role = trx.getServerRole();
					LOG.info("{}:{} {} {}={}", role, getId(), opKeyValue[0], opKeyValue[1], opKeyValue[2]);

					if (LOG.isTraceEnabled()) {
						LOG.trace("{}: key/values={}", getId(), key2values);
					}
					return f;				
			}
		
		}
				
		
		final CompletableFuture<Message> f = CompletableFuture.completedFuture(Message.valueOf(opKeyValue[0] + "Serviço não implementado"));
		return f;
    }
	
	@Override
    public CompletableFuture<Message> query(Message request) {
		final String[] opKey = request.getContent().toString(Charset.defaultCharset()).split(";");
		Tupla t  = (Tupla)key2values.get(opKey[1]);

						
		if(key2values.get(opKey[1]) != null){ //se a chave existir			
			final String result = "SUCCESS : " + String.valueOf(t.getvers())+", "+ t.getd()+", "+ t.getts();		
			LOG.debug("{}: {} = {}", opKey[0], opKey[1], result);
			return CompletableFuture.completedFuture(Message.valueOf(result));
					
		}else{//se a chave não existir
			final String result = "ERROR :" + null;					
			LOG.debug("{}: {} = {}", opKey[0], opKey[1], result);
			return CompletableFuture.completedFuture(Message.valueOf(result));
		}
	}
}
