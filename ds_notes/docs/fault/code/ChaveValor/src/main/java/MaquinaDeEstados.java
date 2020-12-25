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
	String v,ts,d;
	int vers;
	
	public Tupla(String v,int vers,String d,String ts){
	this.v = v;	
	this.vers = vers;	
	this.d = d;	
	this.ts = ts;		
	}
	
	public String getv(){
		return v;
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
				
				Tupla t = new Tupla(opKeyValue[2], Integer.parseInt(opKeyValue[3]), opKeyValue[4], opKeyValue[5]);
				
				key2values.put(opKeyValue[1], t);
				final String result = "SUCCESS :" + opKeyValue[2]+ ", " +Integer.parseInt(opKeyValue[3])+ ", " +opKeyValue[4]+ ", " +opKeyValue[5];
				final CompletableFuture<Message> f = CompletableFuture.completedFuture(Message.valueOf(result));
				final RaftProtos.RaftPeerRole role = trx.getServerRole();
				LOG.info("{}:{} {} {}={}", role, getId(), opKeyValue[0], opKeyValue[1], opKeyValue[2]);

				if (LOG.isTraceEnabled()) {
					LOG.trace("{}: key/values={}", getId(), key2values);
				}
				return f;
				
			}else{//a chave existe
				final String result = "ERROR :" + opKeyValue[2]+ ", " +opKeyValue[3]+ ", " +opKeyValue[4]+ ", " +opKeyValue[5];
				final CompletableFuture<Message> f = CompletableFuture.completedFuture(Message.valueOf(result));
				final RaftProtos.RaftPeerRole role = trx.getServerRole();
				LOG.info("{}:{} {} {}={}", role, getId(), opKeyValue[0], opKeyValue[1], opKeyValue[2]);

				if (LOG.isTraceEnabled()) {
					LOG.trace("{}: key/values={}", getId(), key2values);
				}
				return f;			
				
			}
		}
		
		if(opKeyValue[0].equals("delv")){ // se a operação for delete com versao
		
			if(key2values.containsKey(opKeyValue[1]) ){ //e a chave for encontrada
				Tupla t  = (Tupla)key2values.get(opKeyValue[1]); //pego minha tupla no mapa
				
				if(t.getvers()==Integer.parseInt(opKeyValue[3])){//se a versão for igual
					key2values.remove(opKeyValue[1]);
					final String result = "SUCCESS :" + opKeyValue[2]+ ", " +opKeyValue[3]+ ", " +opKeyValue[4]+ ", " +opKeyValue[5];
					final CompletableFuture<Message> f = CompletableFuture.completedFuture(Message.valueOf(result));
					final RaftProtos.RaftPeerRole role = trx.getServerRole();
					LOG.info("{}:{} {} {}={}", role, getId(), opKeyValue[0], opKeyValue[1], opKeyValue[2]);

					if (LOG.isTraceEnabled()) {
						LOG.trace("{}: key/values={}", getId(), key2values);
					}
					return f;
				
				}else{//a versao é diferente
					final String result = "ERROR_WV :" + opKeyValue[2]+ ", " +opKeyValue[3]+ ", " +opKeyValue[4]+ ", " +opKeyValue[5];
					final CompletableFuture<Message> f = CompletableFuture.completedFuture(Message.valueOf(result));
					final RaftProtos.RaftPeerRole role = trx.getServerRole();
					LOG.info("{}:{} {} {}={}", role, getId(), opKeyValue[0], opKeyValue[1], opKeyValue[2]);

					if (LOG.isTraceEnabled()) {
						LOG.trace("{}: key/values={}", getId(), key2values);
					}
					return f;					
				}				
				
			}else{//a chave não foi encontrada
					final String result = "ERROR_NE :" + opKeyValue[2]+ ", " +opKeyValue[3]+ ", " +opKeyValue[4]+ ", " +opKeyValue[5];
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
				
				key2values.remove(opKeyValue[1]);
				final String result = "SUCCESS :" + opKeyValue[2]+ ", " +opKeyValue[3]+ ", " +opKeyValue[4]+ ", " +opKeyValue[5];
				final CompletableFuture<Message> f = CompletableFuture.completedFuture(Message.valueOf(result));
				final RaftProtos.RaftPeerRole role = trx.getServerRole();
				LOG.info("{}:{} {} {}={}", role, getId(), opKeyValue[0], opKeyValue[1], opKeyValue[2]);

				if (LOG.isTraceEnabled()) {
					LOG.trace("{}: key/values={}", getId(), key2values);
				}
				return f;
				
			}else{//a chave não foi encontrada
				
				final String result = "ERROR :" + opKeyValue[2]+ ", " +opKeyValue[3]+ ", " +opKeyValue[4]+ ", " +opKeyValue[5];
				final CompletableFuture<Message> f = CompletableFuture.completedFuture(Message.valueOf(result));
				final RaftProtos.RaftPeerRole role = trx.getServerRole();
				LOG.info("{}:{} {} {}={}", role, getId(), opKeyValue[0], opKeyValue[1], opKeyValue[2]);

				if (LOG.isTraceEnabled()) {
					LOG.trace("{}: key/values={}", getId(), key2values);
				}
				return f;
				
			}
		}
		
		if(opKeyValue[0].equals("testandset")){ // se a operação for testandsetset 
			
			if(key2values.containsKey(opKeyValue[1]) ){ //e a chave for encontrada
				Tupla t  = (Tupla)key2values.get(opKeyValue[1]); //pego minha tupla no mapa
				
				if(t.getvers()==Integer.parseInt(opKeyValue[3])){//se a versão for igual
				
					SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
					String ts  = dateFormat.format(new Date());//atualizo timestamp				
					
					Tupla t_novo = new Tupla(opKeyValue[2], Integer.parseInt(opKeyValue[3])+1, opKeyValue[4], ts);//atualizo a versão
										
					key2values.replace(opKeyValue[1], t_novo); //coloco a nova tupla no mapa com a mesma chave
					final String result = "SUCCESS :" + t_novo.getv()+ ", " +t_novo.getvers()+ ", " +t_novo.getd()+ ", " +t_novo.getts();  //retorno a mensagem com dados atualizados
					final CompletableFuture<Message> f = CompletableFuture.completedFuture(Message.valueOf(result));
					final RaftProtos.RaftPeerRole role = trx.getServerRole();
					LOG.info("{}:{} {} {}={}", role, getId(), opKeyValue[0], opKeyValue[1], opKeyValue[2]);

					if (LOG.isTraceEnabled()) {
						LOG.trace("{}: key/values={}", getId(), key2values);
					}
					return f;
				
				}else{//a versao é diferente
					final String result = "ERROR_WV :" + opKeyValue[2]+ ", " +Integer.parseInt(opKeyValue[3])+ ", " +opKeyValue[4]+ ", " +opKeyValue[5];
					final CompletableFuture<Message> f = CompletableFuture.completedFuture(Message.valueOf(result));
					final RaftProtos.RaftPeerRole role = trx.getServerRole();
					LOG.info("{}:{} {} {}={}", role, getId(), opKeyValue[0], opKeyValue[1], opKeyValue[2]);

					if (LOG.isTraceEnabled()) {
						LOG.trace("{}: key/values={}", getId(), key2values);
					}
					return f;					
				}				
				
			}else{//a chave não foi encontrada
					final String result = "ERROR_NE :" + opKeyValue[2]+ ", " +Integer.parseInt(opKeyValue[3])+ ", " +opKeyValue[4]+ ", " +opKeyValue[5];
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
    public CompletableFuture<Message> query(Message request) {        ///get
        final String[] opKey = request.getContent().toString(Charset.defaultCharset()).split(";");
		Tupla t  = (Tupla)key2values.get(opKey[1]);
		
		if(key2values.get(opKey[1]) != null){ //se a chave existir			
			final String result = "SUCCESS :" + t.getv()+", "+ String.valueOf(t.getvers())+", "+ t.getts()+", "+ t.getd();		
			LOG.debug("{}: {} = {}", opKey[0], opKey[1], result);
			return CompletableFuture.completedFuture(Message.valueOf(result));
			
		}else{//se a chave não existir
			final String result = "ERROR :" + t.getv()+", "+ String.valueOf(t.getvers())+", "+ t.getts()+", "+ t.getd();		
			LOG.debug("{}: {} = {}", opKey[0], opKey[1], result);
			return CompletableFuture.completedFuture(Message.valueOf(result));
		}
    }
	
}
