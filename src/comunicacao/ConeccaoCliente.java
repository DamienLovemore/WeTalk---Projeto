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
 * Classe respons�vel por representar um cliente, e a sua conec��o ao servidor socket.
 * @author ???
 *
 */
public class ConeccaoCliente
{
	private Socket cliente; //A conec��o atual, que ser� conectada ao servidor.
	private String enderecoIPCliente;
	private volatile DadosTransferencia dadosParaEnvio; //volatile serve para atualizar os dados dentro de Threads em loop de execu��o. Mesmo que isso seja por outros classes que n�o sejam elas.
	private Thread envioDados;
	private Thread recebimentoDados;
	private ObjectOutputStream fluxoSaidaDados;
	private ObjectInputStream fluxoEntradaDados;
	private volatile boolean continuarRealizandoTransferencia;
	
	/**
	 * Cria uma conec��o (socket) para o client, e inicializa todas as vari�veis e m�todos necess�rios para o envio e recebimento de dados do cliente.
	 * @param enderecoIP O endere�o IP desse cliente, que ser� conectado ao nosso servidor Socket. (Socket Server)
	 */
	public ConeccaoCliente(String enderecoIP)
	{
		try
		{
			this.enderecoIPCliente = enderecoIP;
			
			DadosTransferencia dadoVazio = new DadosTransferencia(null,"Vazio",null,(byte)1); //Define uma classe vazio de transfer�ncia
			
			this.dadosParaEnvio = dadoVazio; //Sinaliza qua a classe acabou de ser criada, e n�o tem nada para poder enviar.
			
			//InetAddress.getLocalHost().getHostAddress(); Pega o IP da m�quina atual.
			this.cliente = new Socket(enderecoIPCliente,12345); //Cria uma nova conec��o (socket) para o endere�o IP especificado, e o conecta a porta aonde est� o nosso servidor para intermediar o envio de requisi��es de mensagens.
							
			fluxoEntradaDados = null; //Vari�vel que recebe os dados transmitidos
			
			fluxoSaidaDados = new ObjectOutputStream(cliente.getOutputStream()); //Vari�vel que envia os dados a serem transmitidos.
									
			this.continuarRealizandoTransferencia = true; //Sinaliza quando os m�todos de envio e recebimento de dados devem parar
									
			//Cria o m�todo que inicializa a Thread que cuidar� do envio de mensagens.
			enviarMensagemServidor();
			
			receberMensagensServidor();//Cria o m�todo que inicializa a Thread que cuidar� do recebimento de mensagens.
		}
		
		catch(IOException e)
		{
			System.out.println("N�o foi poss�vel se conectar ao Cliente!");
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Respons�vel por realizar o envio de dados e mensagens.
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
						if (dadosParaEnvio.getMensagemTexto().equals("Vazio") == false) //Para que ele possa saber diferencia quando enviar as mensagens. (E n�o come�ar a enviar assim que a classe � criada)
						{
							//Realiza o envio dos dados
							fluxoSaidaDados.writeUnshared(dadosParaEnvio);
							fluxoSaidaDados.flush();
							System.out.println(String.format("Mensagem enviada com sucesso! (Dados enviados: %s)",dadosParaEnvio)); //Mostra quais dados est�o sendo transmitidos
							
							dadosParaEnvio = new DadosTransferencia(null,"Vazio",null,(byte)1); //Define que os dados j� foram enviados, se n�o ele vai ficar enviando pra sempre. (E aqui queremos que envie cada mensagem somente uma vez)
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
	 * Respons�vel por realizar o recebimento de dados e mensagens. (Download)
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
						if (fluxoEntradaDados == null) //Certifica de que ele apenas est� criando um ObjectInputStream para a vida da classe do Cliente.
							fluxoEntradaDados = new ObjectInputStream(cliente.getInputStream());

						DadosTransferencia dadosReceber = (DadosTransferencia) fluxoEntradaDados.readObject(); //Fica esperando at� ele receber um objeto que foi passado pela conec��o. (Enquanto isso n�o acontecer ele fica travado nessa parte do c�digo)
						System.out.println("Mensagem sendo recebida: "+dadosReceber.getMensagemTexto()); //Mostra a mensagem que ele recebeu
						
						if(dadosReceber.getTipoMensagemTransferir() == 2) //Tem dois tipos de mensagens 1 que � somente mensagens de texto. E 2 que � mensagens de envio de arquivo.
						{
							System.out.println("Arquivo sendo recebido: "+dadosReceber.getArquivoTransferido()); //Mostra qual arquivo foi que ele est� tentando receber
						}
												
						if((dadosReceber.getTipoMensagemTransferir() == 2) && (dadosReceber.getArquivoTransferido()!=null)) //V� se essa � uma mensagem de arquivos, somente se for ai ele verifica se tem arquivo para receber. Quando as duas condi��es forem verdadeiras inicia o download. (Transfer�ncia de arquivos)
						{
							InputStream entradaArquivo = new FileInputStream(dadosReceber.getArquivoTransferido()); //Pega o arquivo do diret�rio original
							OutputStream saidaArquivo = new FileOutputStream(new File(ApplicationInfo.applicationDownloadsFolder+"\\"+dadosReceber.getArquivoTransferido().getName())); //Cria o arquivo no diret�rio destino, com a extens�o especificada. (Inicialmente vazio, com zero bytes de tamanho)
							
							//Escreve todos os bytes na posi��o correta no novo arquivo em branco que foi criado no diret�rio destino, j� com a extens�o correta do arquivo.
							byte[] bytesParaEscrever = new byte[1024 * 50]; //Transfer�ncia pela rede, ou pelo disco se d� por bytes. � transferido um byte por vez.
							int tamanho; //Sinaliza at� qual byte da sequ�ncia total de bytes � para ele "escrever".
							
							while((tamanho = entradaArquivo.read(bytesParaEscrever)) > 0) //Verifica se ainda tem dados para escrever, e se tiver pega a posi��o dos bytes para escrever
							{
								saidaArquivo.write(bytesParaEscrever,0, tamanho); //Escreve no arquivo os bytes que foram carregados atualmente. (De 0 at� o tamanho; N�o l� tudo duma vez, vai lendo os bytes aos poucos at� completar a transfer�ncia)
							}
							System.out.println("Transfer�ncia (download) feito com sucesso!");
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
						System.out.println("Por algum motivo n�o foi encontrada a classe de transfer�ncia da dados na conec��o do remetente!");
						e.printStackTrace();
					}
				}
					
				System.out.println(String.format("Parei de executar! (Thread de recebimento de mensagens de Cliente %s)",ConeccaoCliente.this));
			}
		};
		
		recebimentoDados.start();
	}
	
	/**
	 * Define os dados a serem enviados por essa Cliente.(Quando h� dados para serem enviados)
	 * @param dadosEnvio As informa��es a serem enviadas.
	 */
	public void setDadosParaEnvio(DadosTransferencia dadosEnvio)
	{
		this.dadosParaEnvio = dadosEnvio;
	}
	
	/**
	 * Para pegar os dados que est�o sendo trefegados pelo Cliente, caso seja necess�rio
	 * @return Os dados que est�o sendo trafegados (transmitidos). (Os dados s�o representados pela classe DadosTransfer�ncia)
	 * @see DadosTransferencia
	 */
	public DadosTransferencia getDadosParaEnvio()
	{
		DadosTransferencia retorno;
		
		retorno = this.dadosParaEnvio;
		
		return retorno;
	}
	
	/**
	 * Pega o endere�o IP desse Cliente.(Para ter refer�ncia de quem est� enviando, e n�o apenas para ver para quem ele vai enviar)
	 * @return O endere�o IP desse Cliente.
	 */
	public String getEnderecoIPCliente()
	{
		String retorno = enderecoIPCliente;
		
		return retorno;
	}
	
	/**
	 * Respons�vel por encerrar Clientes n�o mais utilizados.
	 */
	public void encerrarConeccao()
	{
		continuarRealizandoTransferencia = false;
		recebimentoDados.stop();
	}
}
