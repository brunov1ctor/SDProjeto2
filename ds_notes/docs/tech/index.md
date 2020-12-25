# Tecnologias

O objetivo deste capítulo é visitar algumas técnicas e tecnologias recentes e interessantes na área de sistemas distribuídos.


## Como sincronizar duas máquinas?

* Abordagem 1
   * Copie os arquivos da fonte para o destino
* Abordagem 2
   * Produza um hash do arquivo
   * Troque o hash com a outra máquina
   * Se hashes iguais, pronto.
   * Se hashes diferentes, copie o arquivo para a outra máquina.
* Abordagem 3 - Merkle Tree
   * Divida o arquivo em blocos de mesmo tamanho
   * Faça um hash de cada bloco
   * Se mais de um hash gerado, 
	* Concatene hashes duas a duas; cada concatenação resulta em um novo bloco.
        * Repita o processo; os hashes resultantes correspondem a uma árvore  
        ![By [Azaghal](https://commons.wikimedia.org/w/index.php?curid=18157888)](./images/merkle_tree.png)
   * Troque hashes da raiz.
   * Se hashes iguais, pronto.
   * Se hashes diferentes troque hashes das raizes das subárvores e execute recursivamente.

!!!question ""
     Se a única mudança no arquivo foi a adição de um byte no começo do arquivo?
    
     !!!todo "figura"
         Descrever deslocamento de blocos com imagem.


[Modern Algorithms and Data Structures: Merkle Trees](http://www.slideshare.net/quipo/modern-algorithms-and-data-structures-1-bloom-filters-merkle-trees)

A conclusão é que blocos com tamanho pré-definido são problemáticos e que precisamos de blocos definidos pelo conteúdo. Por exemplo, em um texto, uma possibilidade é definir um bloco como um período ou um parágrafo; se uma palavra é inserida no texto, , somente o bloco em que foi inserida é modificado e somente o hash do mesmo terá novo valor.

O problema desta abordagem é que não é genérico: qual o correspondente em uma imagem ou um áudio ou em um arquivo `tgz`?

!!!todo "Rabin Fingerprint"
    [Rolling Hash](https://en.wikipedia.org/wiki/Rolling_hash)





## Blockchain

Interações multipartido são complicadas de se rastrear e auditar. Por exemplo, em uma cadeia de fornecimento (*supply chain*), temos, em vários níveis, **Clientes**, **Vendedores** e **Fornecedores**, que estabelecem **contratos** de bens e serviços.
A um consumidor seria interessante saber quem produziu o cacho de bananas que está comprando, se a produção é livre de trabalho escravo, se o transporte for feito dentro de parâmetros corretos de temperatura, se não violou tratados de rotas marítimas para proteger baleias, e assim por diante. Essencialmente, quer saber como o bem "bananas" foi transferido de mão em mão até chegar à feira livre.

Há diversos tipos de **bens**, alguns tangíveis (e.g., **casa**) e outros intangíveis (e.g., **patente**),  alguns associados a um nome (e.g., **promissória**) e outros ao portador (e.g., **dinheiro "vivo"**).
A transferência destes bens acontece via um registro (e.g., **casa**, **ação**) ou diretamente entre os envolvidos na transação (**dinheiro**).
Registros são feitos um **"livro razão"**.

* 1000 reais pagos ao funcionário F
* Carro X vendido ao João por 50k
* Cada entidade mantém o seu, privadamente.
* Ineficiente -- retrabalho e lento
* Caro -- Retrabalho
* Vulnerável -- hack, erros, e modificações maliciosas

\begin{frame}{Blockchain}
Ledger distribuído e centralizado
	\begin{itemize}
		\item Replicado usando P2P
		\item Só envolvidos tem acesso ao registro
		\item Consenso -- acordo na transação
		\item Proveniência -- todo o histórico de um asset é mantido na blockchain.
		\item Imutabilidade -- entradas não podem ser alteradas
		\item Finalidade -- entradas não podem ser refutadas
	\end{itemize}
\end{frame}

\begin{frame}{Bitcoin}
Primeira aplicação da blockchain
\begin{itemize}
	\item Bitcoin
	\begin{itemize}
		\item Moeda é o asset
		\item Anonimidade
		\item Proof of work
	\end{itemize}
	\item Negócios	
	\begin{itemize}
		\item Qualquer coisa é asset
		\item Identificação das partes
		\item Selective endorsement
	\end{itemize}
\end{itemize}
\end{frame}

\begin{frame}{Smart Contracts}
	Os termos do negócio são mantidos na blockchain: "Se na data X a entidade E não tiver transferido D dinheiros para a entidade F, então transfira o asset A de E para F."
	
	Verificável, assinável e \emph{executável}.	
\end{frame}


\frame{}


https://www.paperdigest.org/2020/06/recent-papers-on-blockchain-bitcoin/

\begin{frame}{Bitcoin}
\includegraphics[width=\textwidth]{images/bitcoin_jun_2018}
\end{frame}

\begin{frame}{Bitcoin}
\includegraphics[width=\textwidth]{images/bitcoin_dec_2018}











## A Small Piece of Big Data

!!!quote "[Big-Data](https://en.wikipedia.org/wiki/Big_data)"
    Big data is a term for data sets that are so large or complex that traditional data processing application software is inadequate to deal with them.

Ciclo convencional:

* Coleta
* Armazenamento
* Análise
* Consulta
* Compartilhamento
* Visualização
* Atualização
...


Áreas

Grandes massas de dados:

* Propaganda
* Astronomia
* Ciência
* e-governos
* meteorologia
* genomics

Dados
Internet das coisas
sensoriamento remoto
suas fotos
logs de software
RFID
redes de sensores
...

Quão grande é ``big'' o suficiente? 
Depende dos dados, ferramentas, e capacidade de manipulá-los. 
Uma vez dado um passo, o alvo passa a ser o próximo passo. 
Isso quer dizer que vai de alguns TB até Petabytes, dependendo do problema.


!!!quote "Gartner, 2012"
     Big data is high volume, high velocity, and/or high variety information assets that require new forms of processing to enable enhanced decision making, insight discovery and process optimization.

     * Volume: incapacidade de armazenar todos os dados; apenas observe e guarde conclusões
     * Velocidade: dados passando em ``tempo real''
     * Variedade: imagens, vídeos, áudio, temperatura,...
     * Machine learning para automação de extração de informação, por exemplo, detecção de padrões, sem se preocupar com o porquê dos mesmos.


Como lidar?

* Bancos de dados colunares
* Stream DBs
* MapReduce
* ...

Google FS

* Google, 2003
* File System
* Dados recuperados da Internet usados em consultas
* Milhões de arquivos de múltiplos GB
* Chunks de 64MB (``blocos do disco'')
* Operações comuns são appends ou reads
* Servidores/discos/memórias estão sempre falhando
* Centenas de clientes concorrentes no mesmo arquivo

\includegraphics[width=.6\textwidth]{images/gfs3}

\begin{frame}{Google FS}
\includegraphics[width=.7\textwidth]{images/gfs2}

\begin{itemize}
\item Clusters de nós ``comuns''
\item Master node: metadata
\item Chunk servers: data
\item Permite usar um cluster como um único HD elástico na rede.
\end{itemize}

\href{https://www.cs.rutgers.edu/~pxk/417/lectures/l-dfs.html}{Fonte}
\end{frame}

\begin{frame}{Google FS}
\includegraphics[width=.7\textwidth]{images/gfs5}

\begin{itemize}
	\item Apps recebem \emph{leases} de acesso direto aos dados
	\item Atomic commitment garante consistência entre réplicas
\end{itemize}

\href{http://google-file-system.wikispaces.asu.edu/}{Fonte}
\end{frame}

\begin{frame}[fragile,allowframebreaks]{Google FS: Consistência }
\includegraphics[width=\textwidth]{images/gfs6}

\framebreak
\begin{enumerate}
\item Application sends the file name and data to the GFS client.
\item GFS Client send the file name and chunk index to master
\item Master sends the identity of the primary and other secondary replicas to the client.
\item Client caches this information. Client contacts master again only when primary is unreachable or it sends a reply saying it does not holds the lease anymore.
\item Considering the network topology the client sends the data to all the replicas.This improves performance. GFS separates data flow from the control flow. Replicas store the data in their LRU buffers till it is used.
\item After all replicas receiving of the data, client sends write request to the primary. Primary decides the mutation order. It applies this order to its local copy.
\item Primary sends the write request to all the secondary replicas. They perform write according to serial order decided by the primary.
\item After completing the operation all secondary acknowledge primary.
\item Primary replies the client about completion of the operation. In case of the errors that is when some of the secondary fail to write client request is supposed to be fail.This leaves modified chunk inconsistent. \item Client handles this by retrying the failed mutation. 
\end{enumerate}

\href{http://google-file-system.wikispaces.asu.edu/}{Fonte}
\end{frame}

\begin{frame}{Map Reduce}
\begin{itemize}
	\item Google, 2004
	\item Processamento distribuído
	\item Processa arquivos no Google FS
\end{itemize}
\includegraphics[width=.6\textwidth]{images/gfs4}
\end{frame}

\frame{\alert{leases?}}

\begin{frame}{Chubby}
	\begin{itemize}
		\item Google, 2006
	\end{itemize}
	\includegraphics[width=.6\textwidth]{images/chubby1}
\end{frame}





\begin{frame}{Hadoop}
\begin{itemize}
	\item HDFS: Hadoop Distributed File System
	\item Map Reduce
	\item Yahoo!
	\item Open source em 2011, 1.0.0
	\item 2012, 2.0.0,
	\item 2017, 3.0.0
	\item nov 2018, 2.9.2
\end{itemize}
\end{frame}


\begin{frame}{Hadoop Ecosystem}
\begin{itemize}
	\item Hive: data warehouse
	\item Spark: 
	\item Kafka
	\item Yarn
	\item Pig: linguagem para especificação de data flow.
	\item HBase: banco de dados estruturado
	\item Sqoop
	\item Flume
	\item Oozie
	\item Avro: serialização
	\item Mahout: machine learning
\end{itemize}
\end{frame}



\begin{frame}{HDFS}
\begin{itemize}
	\item Distribuído
	\item Escalável
	\item Cost effective
	\item Tolerante a falhas
	\item Alta vazão
\end{itemize}
\end{frame}


\begin{frame}{Arquitetura}
\begin{itemize}
	\item Rack e rack failure
	\item Top of rack switch
	\item Core switch
	\item Name Node: nomes das pastas e arquivos
	\item Data Node: conteúdo dos arquivos
	\item Cliente
\end{itemize}
\end{frame}

\begin{frame}{Arquitetura}
\begin{itemize}
	\item Crie arquivo: cliente -> name node
	\item Escreva um block (e.g., 128MB): cliente
	\item Aloque block: cliente -> name node
	\item Salve os dados: cliente -> data node
	\item Heartbeat block report: data node -> name node
	\item Dados são replicados (RF configurado por arquivo): Data node -> data node
\end{itemize}
\end{frame}



\begin{frame}{Name node}
Dados em memory e edit log.

\begin{itemize}
	\item Name node é um SPOF? 
	\item Quorum Journal Manager replica edit log.
	\item Standby Name Node
	\item Zookeeper usado para decidir quem é o líder
	\item Secondary Name Node replica checkpoint da imagem em memória.
\end{itemize}
\end{frame}

\subsection{MapReduce}
\begin{frame}{MapReduce}
	\begin{itemize}
		\item Programação funcional
		\item Map: (map length (() (a) (a b c)) = (0 1 3))
		\item Fold/Reduce: (reduce + (1 2 3)) = 6
	\end{itemize}
\end{frame}


\begin{frame}{MapReduce}
	\begin{itemize}
		\item Não há dependência entre os dados
		\item Dados divididos em \emph{shards}
		\item Execução paralela e distribuída
		\item Trabalhador recebe um shard
		\item Mestre agrega valores
		\item Milhares de processos
		\item Petabytes de dados
	\end{itemize}
\end{frame}

\begin{frame}{MapReduce}
	\begin{itemize}
		\item Shards são arquivos do GFS/HDFS/EC2
		\item Função mapeada a cada shard
		\item Resultado é lista de chaves e valores
		\item Agregação acontece por chaves
		\item Resultado são arquivos no GFS/HDFS/EC2
	\end{itemize}
\end{frame}

\begin{frame}{MapReduce}
	\includegraphics[width=.8\textwidth]{images/mapreduce1}
\end{frame}

\begin{frame}{MapReduce}
	\includegraphics[width=.8\textwidth]{images/mapreduce2}
\end{frame}


\subsection{Laboratório}

\begin{frame}[fragile]{Exemplo}
\begin{lstlisting}[language=java]
import ...

public class WordCount 
{
 public static class TokenizerMapper 
 extends Mapper<Object, Text, Text, IntWritable>
 {
  private final static IntWritable one = new IntWritable(1);
  private Text word = new Text();

  public void map(Object key, Text value, Context context) 
   throws IOException, InterruptedException 
  {
   StringTokenizer itr = new StringTokenizer(value.toString());
   while (itr.hasMoreTokens()) 
   {
    word.set(itr.nextToken());
    context.write(word, one);
   }
  } 
 }
...
\end{lstlisting}
\end{frame}

\begin{frame}[fragile]{Exemplo}
\begin{lstlisting}[language=java]
...
 public static class IntSumReducer 
  extends Reducer<Text,IntWritable,Text,IntWritable> 
 { 
  private IntWritable result = new IntWritable();
  public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException 
  {
   int sum = 0;
   for (IntWritable val : values) 
    sum += val.get();
   result.set(sum);
   context.write(key, result);
  }
 }
	
 public static void main(String[] args) throws Exception 
 {
  ...
 }
}
	
\end{lstlisting}
\href{https://hadoop.apache.org/docs/current/hadoop-mapreduce-client/hadoop-mapreduce-client-core/MapReduceTutorial.html#Example:_WordCount_v1.0}{Fonte}
\end{frame}

\frame{\url{https://youtu.be/DJPwV2ge9m0?list=PLkz1SCf5iB4dw3jbRo0SYCk2urRESUA3v}}




\section{Estudo de caso: Kafka}

\subsection{Introdução}



\begin{frame}
\includegraphics[width=\textwidth]{images/kafka0}
\end{frame}

\begin{frame}{Apache Kafka}

``Kafka is a distributed streaming platform.''

\begin{itemize}
	\item LinkedIn
	\item OpenSource em 2011
	\item Projeto Apache em ????
\end{itemize}
\end{frame}


\begin{frame}{O quê?}
\includegraphics[width=.6\textwidth]{images/kafka1}
\end{frame}

\begin{frame}{Usos}
\includegraphics[width=\textwidth]{images/kafka2}

\pause record ~= message

\pause
\begin{block}{Enterprise Messaging System}
	Producers x Message Broker x Consumers
\end{block}
\end{frame}

\begin{frame}{Componentes}
\includegraphics[width=\textwidth]{images/kafka3}

\begin{itemize}
	\item Produtores: enviam dados/mensagens/records (array de bytes)
	\item Consumidores: recebem dados
	\item Cluster/Broker: distribuído e tolerantes a falhas.
	\item Conectores: integração simplificada com outras aplicações 
	\item Stream processors: spark ou outros frameworks; transformam dados
\end{itemize}
\end{frame}


\begin{frame}{Apache Kafka}
\begin{itemize}
	\item Brokers
	\item Cluster de brokers
	\item Distribuído
	\item Tolerante a falhas
	\item Desacoplamento espacial
	\item Desacoplamento temporal
	\item Tópicos, não endereços
\end{itemize}
\end{frame}

\begin{frame}{Tópicos}
\begin{itemize}
	\item Nome de uma stream de dados: ordem de serviço, exame de sangue, MSFT
	\item Quantidade pode ser imensa.
\end{itemize}
\end{frame}

\begin{frame}{Partição}
\begin{itemize}
	\item Subdivisões de tópicos
	\item Número de partições é definido por usuário
	\item Cada partição está associada a um único servidor
\end{itemize}
\end{frame}

\begin{frame}{Offset}
\begin{itemize}
	\item Índice de uma mensagem em uma partição
	\item Índices atribuídos na ordem de chegada
	\item Offsets são locais às partições
	\item Mensagens são unicamente identificadas por (tópico, partição, índice)
\end{itemize}

\includegraphics[width=.6\textwidth]{images/kafka4}

\end{frame}


\begin{frame}{Consumer group}
\begin{itemize}
	\item Carga pode ser muito grande para um consumidor
	\item Compartilham o processamento de um tópico
	\item Cada mensagem é processada por um membro do grupo
	\item A mesma mensagem pode ser processada por múltiplos grupos
	\item Número de consumidores $\leq$ partições no tópico
	\item Máximo de dois consumidores por partição (mantem pos. de cada um)
\end{itemize}

\includegraphics[width=.6\textwidth]{images/kafka5}
\end{frame}

\subsection{Quickstart}

\begin{frame}{Baixar e Executar}
Siga o tutorial em \url{http://kafka.apache.org/quickstart}, até o passo 5.

\begin{itemize}
	\item Baixe e descompacte
	\item Rode o zookeeper (Terminal 1)
	\item Rode o Kafka (Terminal 2)
	\item Crie um tópico (Terminal 3)\\
		Mais de uma partição em um servidor
	\item \alert{Conecte-se ao Zookeeper e dê uma olhada. O que está vendo?}
	\item Liste os tópicos criados
	\item Envie algumas mensagens
	\item Inicie um consumidor (Terminal 4)
\end{itemize}
\end{frame}



\subsection{Tolerância a Falhas}
\begin{frame}{O quê?}
Manter dados/serviços disponíveis a despeito de falhas.
\end{frame}

\begin{frame}{Replicação}
No Kafka, o \alert{Replication Factor} determina quantas cópias de cada tópico (todas as partições no tópico).
\end{frame}

\begin{frame}{Líder e Seguidor}
\begin{itemize}
	\item Produtor conversa com líder. Líder grava localmente e envia ack ao produtor.
	\item Consumidor conversa com líder. Líder envia dados ao consumidor.
	\item Líder replica dados para seguidores.
\end{itemize}
\end{frame}

\begin{frame}{Replicar}
Passo 6  ensina a criar um sistema com múltiplos brokers.

\begin{itemize}
	\item Identificador
	\item Porta (mesmo servidor)
	\item \alert{Log directory}
\end{itemize}
\end{frame}

\begin{frame}{Replicar}
\begin{itemize}
	\item Crie um novo tópico, com RF = 3 e duas partições
	\item \lstinline|bin/kafka-topics.sh --list --zookeeper localhost:2181 --describe --topic <topico>|
	\item Lista de réplicas
	\item Lista de réplicas sincronizadas: \emph{list of \alert{i}n \alert{s}ync \alert{r}eplicas}
\end{itemize}
\end{frame}


\begin{frame}{Zookeeper}
\begin{itemize}
	\item Permite que nós do cluster se descubram
	\item Elege líder
\end{itemize}
\end{frame}

\begin{frame}{Armazenamento}
\begin{itemize}
	\item Dado deve ser removido depois de um tempo de ``retenção''
	\item Pode definir retenção por tamanho (por partição, não tópico)
\end{itemize}
\end{frame}


\subsection{Produtor}

\begin{frame}{Produtor}
\begin{itemize}
	\item Produtor envia mensagens para os brokers
	\item Producer API
	\item \href{https://github.com/LearningJournal/ApacheKafkaTutorials}{Learning Journal}
\end{itemize}
\end{frame}

\begin{frame}[fragile]{SimpleProducer.java}
\begin{lstlisting}[language=Java]
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import java.util.Properties;

public class SimpleProducer {
 public static void main(String[] args) {
  String topicName = "SimpleProducerTopic";
  String key = "Chave";
  String value = "Valor";
  Properties props = new Properties();
  props.put("bootstrap.servers", "localhost:9092, localhost:9093");
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

  Producer<String, String> producer = new KafkaProducer<String, String>(props);

  ProducerRecord<String, String> record = new ProducerRecord<String, String>(topicName, key, value);

  producer.send(record);
  producer.close();

  System.out.println("SimpleProducer Completed.");
 }
}

\end{lstlisting}
\end{frame}

\begin{frame}{Workflow}
\includegraphics[width=.8\textwidth]{images/kafka6}

\begin{itemize}
	\item Particionador default
	\begin{itemize}
		\item Partition
		\item Hash da ``chave''
		\item Round robin
	\end{itemize}
	\item Retry automático
\end{itemize}
\end{frame}


\begin{frame}{Fire and Forget}
Envia a mensagem e não se importa com o resultado.
\end{frame}

\begin{frame}[fragile]{Synchronous Call}
Envia a mensagem e espera para saber se foi entregue ou não.

\begin{lstlisting}[language=Java]
try{
 RecordMetadata metadata = producer.send(record).get();
 System.out.println("Message is sent to Partition no " + metadata.partition() + " and offset " + metadata.offset());
 System.out.println("SynchronousProducer Completed with success.");
}catch (Exception e) {
 e.printStackTrace();
 System.out.println("SynchronousProducer failed with an exception");
}finally{
 producer.close();
}
\end{lstlisting}
\begin{itemize}
	\item Future
\end{itemize}
\end{frame}

\begin{frame}[fragile]{Callback}
Envia a mensagem e é invocado depois de receber um ACK

\begin{lstlisting}[language=Java]
producer.send(record, new MyProducerCallback());

...

class MyProducerCallback implements Callback{
 @Override
 public  void onCompletion(RecordMetadata recordMetadata, Exception e) {
  if (e != null)
   System.out.println("AsynchronousProducer failed with an exception");
  else
   System.out.println("AsynchronousProducer call Success:");
 }
}
\end{lstlisting}
\begin{itemize}
	\item max.in.flight.requests.per.connection
\end{itemize}
\end{frame}


\begin{frame}{Default Partitioner}
\includegraphics[width=.8\textwidth]{images/kafka6}

\begin{itemize}
	\item Partition
	\item Hash da ``chave'' \% \#partition
	\item Round robin
\end{itemize}

\href{https://github.com/LearningJournal/ApacheKafkaTutorials/blob/master/ProducerExamples/SensorPartitioner.java}{Exemplo de Custom Partitioner}
\end{frame}

\subsection{Consumidor}

\begin{frame}{Consumer Groups}
\begin{itemize}
	\item Múltiplos consumidores processam dados em paralelo
	\item Grupo de consumidores de tópicos
	\item Grupo pertence à mesma aplicação
	\includegraphics[width=.6\textwidth]{images/kafka7}
	\item Duplicate reads? Consumidores não compartilham partições
	\item Group coordinator (broker eleito): lista de consumidores
	\item Group líder: rebalanceamento
\end{itemize}
\end{frame}

\begin{frame}[fragile, allowframebreaks]{Consumer}
\begin{lstlisting}[language=Java]
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

public class SimpleConsumer {
 public static void main(String[] args) throws IOException {
  String topicName = "SimpleProducerTopic";
  String groupName = "SupplierTopicGroup";

  Properties props = new Properties();
  props.put("bootstrap.servers", "localhost:9092,localhost:9093");
  props.put("group.id", groupName);
  props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
  props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

  KafkaConsumer<String, String> consumer = null;

  try {
   consumer = new KafkaConsumer<String, String>(props);
   consumer.subscribe(Arrays.asList(topicName));

   while (true) {
    ConsumerRecords<String,String> records = consumer.poll(100);

    for (ConsumerRecord<String, String> record: records)
     System.out.println("Key = " + record.key() + " Value = " + record.value());
   }
  } catch (Exception ex) {
   ex.printStackTrace();
  } finally {
   consumer.close();
  }
 }
}
\end{lstlisting}

\begin{itemize}
	\item Se não definir grupo, será novo grupo, e lerá todas as mensagens disponíveis
\end{itemize}
\end{frame}


\begin{frame}{Poll}
\begin{itemize}
	\item poll também envia hearbeat
	\item executar a cada 3s, no mínimo	
	\item Current offset: a cada poll, broker incrementa current offset
	\item Commited offset: o consumidor informa quais índices foram processados
	\begin{itemize}
		\item Auto Commit
		\begin{itemize}
			\item enable.auto.commit
			\item auto.commit.interval.ms
			\item Pode causar reprocessamento de mensagens
		\end{itemize}
		\item Manual Commit
		\begin{itemize}
			\item CommitSync
			\item CommitAsync
		\end{itemize}
	\end{itemize}
\end{itemize}
\end{frame}


\subsection{Arquitetura}
%\begin{frame}{Líder}

%\end{frame}

%mensagens são ack depois de copiadas para todas as réplicas
%replicas lentas são removidas se lentas ou falhas
%at least once, at most once, exactly one (nao suportado)
%rolling upgrade
%tls security
%rest
%CRUD


\subsection{Data Streams}
\subsection{Connectors}

%\begin{frame}{Data streams}
%\begin{itemize}
%	\item 
%\end{itemize}
%\end{frame}











###
Serviços de Coordenação

* Zookeeper
* Atomix
* OpenReplica



\subsection{Falhas Bizantinas}

Uma história de três exércitos -- Versão 2}
Exércitos estão às portas de Bizâncio, aka Constantinopla, aka Istambul.

Todos os exércitos tem que atacar em conjunto ou se retirar em conjunto.

Cada exército é comandado por um General. Alguns destes preferem atacar, enquanto outros preferem se retirar.

Alguns generais podem ter sido comprados, e mandar mensagens discrepantes para os outros, ou simplesmente não mandar mensagens.

Fonte: \href{http://research.microsoft.com/en-us/um/people/lamport/pubs/byz.pdf}{Lamport, L.; Shostak, R.; Pease, M. (1982). "The Byzantine Generals Problem" (PDF). ACM Transactions on Programming Languages and Systems. 4 (3): 382–401. doi:10.1145/357172.357176.}


Generais e Tenentes}
Problema pode ser mudado para:

	* Comandante envia ordem.
	* Todos os tenentes leais executam ordem recebida.
	* Comandante pode ser traidor.




Generais e Tenentes}
Suponha 3 exércitos. \\
Comandante (traidor) diz "Ataque!" Tenente A e "Retirada!" tenente B.\\
Ou \\
Comandante diz "Ataque!" a ambos. Tenente A segue a ordem mas B se retira.

 E se os tenentes trocarem informações?

 Como diferenciar casos em que Comandante ou Tenente é traidor?





Generais e Tenentes}
Só há solução se mais de $\frac{2}{3}$ dos Generais/Tenentes são leais.


%http://www.drdobbs.com/cpp/the-byzantine-generals-problem/206904396?pgno=5

Comunicação}

	* Toda mensagem enviada é entregue corretamente.
	* A ausência de mensagem pode ser detectada (mensagem Null é entregue no lugar) (Sistema síncrono)




4/0}
General manda ordens.

Ausência de ordem = Retirada

Tenente repassa ordens

Maioria de comandos é comando a ser seguido



4/0}
General manda ordens.

Ausência de ordem = Retirada

Tenente repassa ordens

Maioria de comandos é comando a ser seguido



Comunicação}

	* Toda mensagem enviada é entregue corretamente.
	* Toda mensagem é assinada.
	* A ausência de mensagem pode ser detectada (mensagem Null é entregue no lugar) (Sistema síncrono)


 É possível detectar inconsistências e processos bizantinos.



%http://cs.brown.edu/courses/cs138/s16/lectures/19consen-notes.pdf
\section{Outros tópicos}

%TODO \subsection{Detectores de Falhas}




\subsection{Reconfiguração}

Reconfiguração da Aplicação}
Na segunda entrega do projeto, você distribuiu a carga do seu banco de dados entre vários nós. Caso um nó falhe, parte dos seus dados será perdida.

Para corrigir esta deficiência, na terceira entrega, cada nó será replicado em três vias e, assim, caso um nó falhe, outros dois continuarão a manter o dado.



Reconfiguração da Aplicação}
Ainda assim, há problemas. E se mais de um, de um mesmo conjunto de réplicas, falhar? 

 Embora seja pequena a probabilidade de dois nós de um mesmo grupo falharem em instantes próximos, dado tempo suficiente, qualquer evento com probabilidade diferente de 0 acontecerá.

 Precisamos de uma forma de trocar nós da aplicação que falharam por novos nós. 

Este é problema denominado Pertinência de Grupo ou \emph{Group Membership}



Group Membership}
Para não correr o risco, retire o processo falhos do grupo e coloque outro no lugar!

I.e., mude a visão que o sistema de quem é o grupo.




Visões}
\includegraphics[width=.7\textwidth]{images/vc}

Fonte: \href{https://www.cs.rutgers.edu/~pxk/417/notes/virtual_synchrony.html}{Paul Krzyzanowski}

$G$ é o grupo de processos participando do sistema, é a Visão do Sistema.


Inicialmente, $G$ consiste de apenas o processo $p$, como o processo que cria o cluster no Atomix. Na sequência, outros processo vão se unindo ao grupo através de View Changes. Uma vez que $p$ e $q$ estão no grupo, inicia-se a comunicação entre eles. Quando $r, s$ e $t$ aparecem, também entram no grupo por meio de uma nova visão.

Finalmente, quando ambos $p$ e $q$ falham, os outros processo os excluem da visão, e continuam funcionando normalmente.


Impossibilidade de Detecção de Falhas}
Em um sistema distribuído assíncrono, é impossível distinguir com toda certeza um processo falho (parou de funcionar) de um que está lento.

 Como decidir se mudar ou não de visão?


Ou aceita a imprecisão e muda quando suspeitar de uma falha, ou corre o risco de ficar esperando \emph{ad eternum} e não mudar, mesmo quando uma falha aconteceu.

Uma ``solução''!}
Quando suspeitar de falha, reporte suspeita a outros processos, que também passarão a suspeitar.

Tome decisão baseado na suspeita, isto é, troque de visão quando houver suspeita.

Pague o preço de uma suspeita errada, isto é, quando um processo for removido da visão indevidamente, adicione-o novamente.



Sincronismos Virtual}
Gerenciamento de Grupo/Group Membership e Comunicação em Grupo

	* Processos se unem ao grupo
	* Processos saem do grupo
	* Processos enviam mensagens para o grupo
	* Diferentes ordenações
		
			* Atomic Multicast
		



Visão de Grupo}

	* Visão: conjunto de processos no sistema.
	* Multicast feito para processos na visão.
	* Visão é consistente entre os processos.
	* Entrada e saída de processos muda a visão.



Eventos}

	* Mensagem
	* Mudança de Visão
	* Checkpoint



Visões}
\includegraphics[width=.7\textwidth]{images/vc}

Fonte: \href{https://www.cs.rutgers.edu/~pxk/417/notes/virtual_synchrony.html}{Paul Krzyzanowski}


Sincronismo Virtual}
Deve satisfazer

	* Se uma mensagem é enviada em uma visão, ela só pode ser entregue naquela visão.
	* Se uma mensagem é entregue a um processo correto em uma visão, então é entregue a todos os processos corretos naquela visão.
	* Se um processo não recebe a mensagem, ele não estará na próxima visão.
	* Ao entrar em uma visão, o processo recebe o estado dos outros processos e seu estado se torna equivalente ao de um processo que recebeu todas as mensagens já entregues.


A troca de Visão é uma barreira.


ISIS Toolkit}
	Sistema de Sincronismo Virtual tolerante a falhas desenvolvido por Ken Birman, Cornell University (\url{http://www.cs.cornell.edu/Info/Projects/Isis/})\\
	ISIS: An Environment for Constructing Fault-Tolerant Distributed Systems. Kenneth Birman, D. Skeen, A. El Abbadi, W. C. Dietrich and T. Raeuchle. May 1983.
	

	* 100.000's/s
	* Em uso até 2009
	* NY Stock Exchange
	* Swiss Exchange
	* US Navy
	* Precursos de sistemas como Zookeeker
	* Totem, ISIS, Horus, Transis (Partições), \alert{Spread}, \alert{Ensamble}, \alert{JGroups}, Appia, QuickSilver, vSynch (née ISIS 2)



Difusão Totalmente Ordenada}

	* Corretude: Se um processo $p$ envia uma mensagem $m$ para processos no grupo $G$, então se $p$ não falha, todos os processos corretos em $G$ recebem a mensagem.
	
	* Acordo: Se um processo correto em $G$ recebe uma mensagem $m$, então todo processo correto em $G$ recebe $m$
	
	* Ordenação: Se um processo recebe mensagem $m$ e depois $n$, então qualquer processo que receba a mensagem $n$ deve primeiro receber $m$
	
	* Validade: Somente mensagens difundidas são entregues.


E se mandarmos mensagens do tipo ``A partir da entrega desta mensagem, o grupo de processos é $G$.''


Sincronismo Virtual}
Deve satisfazer

	* Se uma mensagem é enviada em uma visão, ela só pode ser entregue naquela visão.\\
	Mensagens de troca de visão podem incrementar um contador\\
	Mensagens normais carregam o valor atual do contador\\
	Mensagem descartada se valor na mensagem é maior contador no destinatário
	
	* Se uma mensagem é entregue a um processo correto em uma visão, então é entregue a todos os processos corretos naquela visão.\\
	Pela difusão, se a mensagem de troca for entregue para um processo, será entregue para todos os corretos, na mesma ordem
	Se mensagem comum for entregue antes para algum, será entregue ante para todos.
	
	* Se um processo não recebe a mensagem, ele não estará na próxima visão.\\
	Se um processo não recebe uma mensagem comum que foi entregue pelos outros, então ele não troca de visão.

	* Ao entrar em uma visão, o processo recebe o estado dos outros processos e seu estado se torna equivalente ao de um processo que recebeu todas as mensagens já entregues.\\
	Caso contrário, não haveria porquê trocar os processos



State Transfer}
\includegraphics[width=.7\textwidth]{images/state_transfer}


\href{http://www.gsd.inesc-id.pt/~ler/docencia/tfd0405/bib/BSRNA.pdf}{Building Secure and Reliable Network Applications}



Difusão Atômica $\equiv$ Sincronismo Virtual?}
Seria uma boa aproximação, mas que poderia ser relaxada. 

Em certas aplicações, FIFO ou Causal seriam suficientes dentro da visão, desde que a mensagem de mudança da visão seja totalmente ordenada com as comuns.



Particionamento}
E se dois subconjuntos mutuamente exclusivos se formarem e criarem visões independentes?

 \emph{Primary Partition Model} -- Somente a partição primária pode mudar de visão.

 Lembram-se que no Raft somente uma partição com uma maioria de processo pode decidir? É exatamente a mesma situação, pois os processos estão chegando a um Consenso sobre quem é a nova visão.



Extended Virtual Synchrony}
\emph{Primary Partition Model} -- Não é adequado a uma rede geograficamente distribuída (Internet scale).

 Lembram-se que no Raft somente uma partição com uma maioria de processo pode decidir? É exatamente a mesma situação, pois os processos estão chegando a um Consenso sobre quem é a nova visão.


É possível que no trabalho dois, alguns de vocês tenham tentado gerar locks do sistema para manipular objetos distribuídos no sistema. Esse locks são perigosos por quê processos pode travar/quebrar/falhar e nunca liberarem os locks. O uso de um algoritmo VS poderia ser usado para resolver o problema.\right 




[Swim](https://asafdav2.github.io/2017/swim-protocol/)




http://courses.cs.vt.edu/cs5204/fall05-gback/lectures/Lecture8.pdf





### Estudo de caso: Kafka
???todo
