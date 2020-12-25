import org.apache.ratis.proto.RaftProtos;
import org.apache.ratis.protocol.Message;
import org.apache.ratis.statemachine.TransactionContext;
import org.apache.ratis.statemachine.impl.BaseStateMachine;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.lang.*;

class Tupla{
	String v,vers,ts,d;
	
	public Tupla(String v,String vers,String ts,String d){
	this.v = v;	
	this.vers = vers;	
	this.ts = ts;	
	this.d = d;		
	}
	
	public String getv(){
		return v;
	}
	
	public String getvers(){
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
        final String[] opKeyValue = entry.getStateMachineLogEntry().getLogData().toString(Charset.defaultCharset()).split(":");
		
		
		if(opKeyValue[0].equals("set")){ // se a operação for set 
		
			if(! key2values.containsKey(opKeyValue[1])){ //e a chave não existir
				System.out.printf(opKeyValue[5]);
				Tupla t = new Tupla(opKeyValue[2], opKeyValue[3], opKeyValue[4], opKeyValue[5]);
				
				key2values.put(opKeyValue[1], t);
				final String result = "SUCCESS :" + opKeyValue[2]+ ", " +opKeyValue[3]+ ", " +opKeyValue[4]+ ", " +opKeyValue[5];
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
		
		
		
		
		
		
		if(opKeyValue[0].equals("delv")){ // se a operação for delete 
		
		}
		
		if(opKeyValue[0].equals("del")){ // se a operação for delete com versao 
		
		}
		
		if(opKeyValue[0].equals("testandset")){ // se a operação for testandsetset 
		
		}
		
		
		
		
		final CompletableFuture<Message> f = CompletableFuture.completedFuture(Message.valueOf(opKeyValue[0] + "Serviço não implementado"));
		return f;
    }
	
    @Override
    public CompletableFuture<Message> query(Message request) {        ///get
        final String[] opKey = request.getContent().toString(Charset.defaultCharset()).split(":");
		
		if(key2values.get(opKey[1]) != null){ //se a chave existir
		
			
			final String result = "SUCCESS :" + key2values.get(opKey[1]);		
			LOG.debug("{}: {} = {}", opKey[0], opKey[1], result);
			return CompletableFuture.completedFuture(Message.valueOf(result));
			
		}else{//se a chave não existir
			final String result = "ERROR :" + key2values.get(opKey[1]);		
			LOG.debug("{}: {} = {}", opKey[0], opKey[1], result);
			return CompletableFuture.completedFuture(Message.valueOf(result));
		}
    }

}
