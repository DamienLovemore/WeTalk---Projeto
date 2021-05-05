package comunicacao;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

/**
 * Classe responsável por ser o nosso servidor, e obter conecções.<br>
 * Juntamente com a troca de informações, como envio de mensagens de texto e arquivos.
 * @author ???
 *
 */
public class ServidorPrincipal 
{
	private ServerSocket socketServidorCentral; //O servidor principal que receberá todas as conecções e troca de arquivos e informações
	private List<Socket> socketsConectados; //Essa lista ela armazena todos os sockets (conecções) que estão conectados ao nosso servidor atualmente.
	private ObjectInputStream fluxoEntradaDados;
		
	/**
	 * Cria um servidor web socket que obtém conecções e realiza a troca de informações
	 */
	public ServidorPrincipal()
	{
		//Inicialica a troca que vai ficar rodando de fundo para ouvir as solicitações do servidor
		ThreadRodarServ ouvirRequests = new ThreadRodarServ();
		ouvirRequests.start(); //Coloca pra executar a Thread, e executa também o seu método run().
	}
	
	/**
	 * Lê as mensagens enviadas por um Cliente (socket) conectado.
	 * @param fluxoEntradaDados O fluxo de dados de entrada, que está sendo passado pelo cliente (socket) conectado.
	 */
	private void lerMensagemClienteConectado(final ObjectInputStream fluxoEntradaDados)
	{
		//Cria uma Thread nova para ler cada mensagem recebida
		new Thread()
		{
			
			@SuppressWarnings("unused")
			public void run()
			{
				String mensagemEntrada = ""; //A mensagem recebida da conecção. (Socket)
				//Envia os dados para o distinatário
				ObjectOutputStream fluxoSaidaDados = null;				
				
				try
				{
					while(true)
					{
						Socket coneccaoAlvo = null; //A conecção (socket) alvo que receberá os dados a serem passados
						
						//Obtém os dados do remetente.
						DadosTransferencia dadoEnviar = (DadosTransferencia) fluxoEntradaDados.readObject(); //Obtém os dados a serem enviados
						
						//Percorre cada conecção (socket) até achar a conecção que corresponde ao destinatário desejado
						for (Socket socketConectado : socketsConectados)
						{
							String ipConeccao = socketConectado.getLocalAddress().getHostAddress();
							
							if(ipConeccao.equals(dadoEnviar.getEnderecoIPConeccao())) //Olha até achar a coneccção alvo que tenha o IP correto, e por último o encerra.
							{
								coneccaoAlvo = socketConectado;
								break;
							}
						}
						
						if (fluxoSaidaDados == null)
							fluxoSaidaDados = new ObjectOutputStream(coneccaoAlvo.getOutputStream());
						
						fluxoSaidaDados.writeObject(dadoEnviar);
					}
				}
				
				catch(ClassNotFoundException e)
				{
					System.out.println("Por algum motivo não foi encontrada a classe de transferência da dados na conecção do remetente!");
					e.printStackTrace();
				}
				
				catch(Exception e)
				{
					this.stop();
				}
			}
		}.start();
	}
	
	/**
	 * Thread responsável por ficar ouvindo de fundo as conecções e o recebimento de mensagens.
	 * @author ???
	 *
	 */
	class ThreadRodarServ extends Thread
	{
		public void run()
		{
			System.out.println("Thread para aceitar conecções e realizar envio de mensagens e troca de informações iniciada!"+super.toString());
			
			try
			{
				socketServidorCentral = new  ServerSocket(12345); //Um servidor socket precisa definir o número da porta para receber conexões dos clientes. Esse número pode variar entre 0 e 65535. Números de porta de 1024 abaixo são reservadas para uso do sistema, e devem ser evitadas.
				socketsConectados = new ArrayList<Socket>(); //Inicializa a lista para que possa receber dados
				
				
				do //Fica ouvindo de fundo por novas conecções e mensagens enquanto o programa estiver rodando.
				{
					System.out.println("Buscando novas conecções");
									
					//Aceita conecção atual, e prepara para receber informações
					Socket socketConeccaoAtual = socketServidorCentral.accept(); //Pega o socket que está tentando se conectar atualmente e aceita a sua conecção.(Esse método bloqueia o código da Thread até que uma conecção seja feita)
							
					System.out.println("Conecção realizada! (Conecção: "+socketConeccaoAtual+")");
					
					fluxoEntradaDados = new ObjectInputStream(socketConeccaoAtual.getInputStream()); //Pega o fluxo de entrada de dados de uma conexão (socket) e se prepara para ler ela.
					
					//Imprime o endereço IP do Cliente que foi conectado atualmente.
					System.out.println("Cliente "+socketConeccaoAtual.getLocalAddress().getHostAddress()); //Imprime no console o endereço IP da conecção
					
					//Adiciona a lista de conecções.
					socketsConectados.add(socketConeccaoAtual); //Adiciona essa nova conexão a lista de coneções do servidor.
					
					//Lê as mensagens enviadas pelo Cliente
					lerMensagemClienteConectado(fluxoEntradaDados);
				} while(true);
			}
			
			catch (Exception e)
			{
				JOptionPane.showMessageDialog(null,"Não foi possível se conectar ao servidor para receber as mensagens!","Não foi possível se conecctar ao servidor",JOptionPane.ERROR_MESSAGE); //Imprime mensagem de falha a conecção do servidor
			}
		}
	}
}


