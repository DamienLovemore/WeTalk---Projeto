package comunicacao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import recursos.ApplicationInfo;

/**
 * Classe responsável por representar um cliente, e a sua conecção ao servidor socket.
 * @author ???
 *
 */
public class ConeccaoCliente
{
	private Socket cliente; //A conecção atual, que será conectada ao servidor.
	private String enderecoIPCliente;
	private volatile DadosTransferencia dadosParaEnvio; //volatile serve para atualizar os dados dentro de Threads em loop de execução. Mesmo que isso seja por outros classes que não sejam elas.
	private Thread envioDados;
	private Thread recebimentoDados;
	private ObjectOutputStream fluxoSaidaDados;
	private ObjectInputStream fluxoEntradaDados;
	private volatile boolean continuarRealizandoTransferencia;
	
	/**
	 * Cria uma conecção (socket) para o client, e inicializa todas as variáveis e métodos necessários para o envio e recebimento de dados do cliente.
	 * @param enderecoIP O endereço IP desse cliente, que será conectado ao nosso servidor Socket. (Socket Server)
	 */
	public ConeccaoCliente(String enderecoIP)
	{
		try
		{
			this.enderecoIPCliente = enderecoIP;
			
			DadosTransferencia dadoVazio = new DadosTransferencia(null,"Vazio",null,(byte)1); //Define uma classe vazio de transferência
			
			this.dadosParaEnvio = dadoVazio; //Sinaliza qua a classe acabou de ser criada, e não tem nada para poder enviar.
			
			//InetAddress.getLocalHost().getHostAddress(); Pega o IP da máquina atual.
			this.cliente = new Socket(enderecoIPCliente,12345); //Cria uma nova conecção (socket) para o endereço IP especificado, e o conecta a porta aonde está o nosso servidor para intermediar o envio de requisições de mensagens.
							
			fluxoEntradaDados = null; //Variável que recebe os dados transmitidos
			
			fluxoSaidaDados = new ObjectOutputStream(cliente.getOutputStream()); //Variável que envia os dados a serem transmitidos.
									
			this.continuarRealizandoTransferencia = true; //Sinaliza quando os métodos de envio e recebimento de dados devem parar
									
			//Cria o método que inicializa a Thread que cuidará do envio de mensagens.
			enviarMensagemServidor();
			
			receberMensagensServidor();//Cria o método que inicializa a Thread que cuidará do recebimento de mensagens.
		}
		
		catch(IOException e)
		{
			System.out.println("Não foi possível se conectar ao Cliente!");
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Responsável por realizar o envio de dados e mensagens.
	 */
	private void enviarMensagemServidor()
	{
		envioDados =
		new Thread()
		{
			public void run()
			{
				while(continuarRealizandoTransferencia) //Faz com que ele fica rodando de fundo indefinitamente ouvindo por envio de mensagens
				{
					try
					{
						if (dadosParaEnvio.getMensagemTexto().equals("Vazio") == false) //Para que ele possa saber diferencia quando enviar as mensagens. (E não começar a enviar assim que a classe é criada)
						{
							//Realiza o envio dos dados
							fluxoSaidaDados.writeUnshared(dadosParaEnvio);
							fluxoSaidaDados.flush();
							System.out.println(String.format("Mensagem enviada com sucesso! (Dados enviados: %s)",dadosParaEnvio)); //Mostra quais dados estão sendo transmitidos
							
							dadosParaEnvio = new DadosTransferencia(null,"Vazio",null,(byte)1); //Define que os dados já foram enviados, se não ele vai ficar enviando pra sempre. (E aqui queremos que envie cada mensagem somente uma vez)
						}
					}
					
					catch(IOException e)
					{
						System.out.println("Falha ao realizar o envio de dados!");
						e.printStackTrace();
					}
				}
					
				System.out.println(String.format("Parei de executar! (Thread de Envio de mensagens de Cliente %s)",ConeccaoCliente.this));
			}
		};
		
		envioDados.start();
	}
	
	/**
	 * Responsável por realizar o recebimento de dados e mensagens. (Download)
	 */
	private void receberMensagensServidor()
	{
		recebimentoDados =
		new Thread()
		{
			public void run()
			{
				while(continuarRealizandoTransferencia) //Faz com que ele fica rodando de fundo indefinitamente ouvindo por envio de mensagens
				{
					try
					{
						if (fluxoEntradaDados == null) //Certifica de que ele apenas está criando um ObjectInputStream para a vida da classe do Cliente.
							fluxoEntradaDados = new ObjectInputStream(cliente.getInputStream());

						DadosTransferencia dadosReceber = (DadosTransferencia) fluxoEntradaDados.readObject(); //Fica esperando até ele receber um objeto que foi passado pela conecção. (Enquanto isso não acontecer ele fica travado nessa parte do código)
						System.out.println("Mensagem sendo recebida: "+dadosReceber.getMensagemTexto()); //Mostra a mensagem que ele recebeu
						
						if(dadosReceber.getTipoMensagemTransferir() == 2) //Tem dois tipos de mensagens 1 que é somente mensagens de texto. E 2 que é mensagens de envio de arquivo.
						{
							System.out.println("Arquivo sendo recebido: "+dadosReceber.getArquivoTransferido()); //Mostra qual arquivo foi que ele está tentando receber
						}
												
						if((dadosReceber.getTipoMensagemTransferir() == 2) && (dadosReceber.getArquivoTransferido()!=null)) //Vê se essa é uma mensagem de arquivos, somente se for ai ele verifica se tem arquivo para receber. Quando as duas condições forem verdadeiras inicia o download. (Transferência de arquivos)
						{
							InputStream entradaArquivo = new FileInputStream(dadosReceber.getArquivoTransferido()); //Pega o arquivo do diretório original
							OutputStream saidaArquivo = new FileOutputStream(new File(ApplicationInfo.applicationDownloadsFolder+"\\"+dadosReceber.getArquivoTransferido().getName())); //Cria o arquivo no diretório destino, com a extensão especificada. (Inicialmente vazio, com zero bytes de tamanho)
							
							//Escreve todos os bytes na posição correta no novo arquivo em branco que foi criado no diretório destino, já com a extensão correta do arquivo.
							byte[] bytesParaEscrever = new byte[1024 * 50]; //Transferência pela rede, ou pelo disco se dá por bytes. É transferido um byte por vez.
							int tamanho; //Sinaliza até qual byte da sequência total de bytes é para ele "escrever".
							
							while((tamanho = entradaArquivo.read(bytesParaEscrever)) > 0) //Verifica se ainda tem dados para escrever, e se tiver pega a posição dos bytes para escrever
							{
								saidaArquivo.write(bytesParaEscrever,0, tamanho); //Escreve no arquivo os bytes que foram carregados atualmente. (De 0 até o tamanho; Não lê tudo duma vez, vai lendo os bytes aos poucos até completar a transferência)
							}
							System.out.println("Transferência (download) feito com sucesso!");
						}
						
						System.out.println("Mensagem recebida com sucesso!");
						fluxoSaidaDados.reset();
					}
					
					catch(IOException e)
					{
						System.out.println("Falha ao realizar o recebimento de arquivos!");
						e.printStackTrace();
					}
					
					catch(ClassNotFoundException e)
					{
						System.out.println("Por algum motivo não foi encontrada a classe de transferência da dados na conecção do remetente!");
						e.printStackTrace();
					}
				}
					
				System.out.println(String.format("Parei de executar! (Thread de recebimento de mensagens de Cliente %s)",ConeccaoCliente.this));
			}
		};
		
		recebimentoDados.start();
	}
	
	/**
	 * Define os dados a serem enviados por essa Cliente.(Quando há dados para serem enviados)
	 * @param dadosEnvio As informações a serem enviadas.
	 */
	public void setDadosParaEnvio(DadosTransferencia dadosEnvio)
	{
		this.dadosParaEnvio = dadosEnvio;
	}
	
	/**
	 * Para pegar os dados que estão sendo trefegados pelo Cliente, caso seja necessário
	 * @return Os dados que estão sendo trafegados (transmitidos). (Os dados são representados pela classe DadosTransferência)
	 * @see DadosTransferencia
	 */
	public DadosTransferencia getDadosParaEnvio()
	{
		DadosTransferencia retorno;
		
		retorno = this.dadosParaEnvio;
		
		return retorno;
	}
	
	/**
	 * Pega o endereço IP desse Cliente.(Para ter referência de quem está enviando, e não apenas para ver para quem ele vai enviar)
	 * @return O endereço IP desse Cliente.
	 */
	public String getEnderecoIPCliente()
	{
		String retorno = enderecoIPCliente;
		
		return retorno;
	}
	
	/**
	 * Responsável por encerrar Clientes não mais utilizados.
	 */
	public void encerrarConeccao()
	{
		continuarRealizandoTransferencia = false;
		recebimentoDados.stop();
	}
}
