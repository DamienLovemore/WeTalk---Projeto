package comunicacao;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

/**
 * Classe respons�vel por ser o nosso servidor, e obter conec��es.<br>
 * Juntamente com a troca de informa��es, como envio de mensagens de texto e arquivos.
 * @author ???
 *
 */
public class ServidorPrincipal 
{
	private ServerSocket socketServidorCentral; //O servidor principal que receber� todas as conec��es e troca de arquivos e informa��es
	private List<Socket> socketsConectados; //Essa lista ela armazena todos os sockets (conec��es) que est�o conectados ao nosso servidor atualmente.
	private ObjectInputStream fluxoEntradaDados;
		
	/**
	 * Cria um servidor web socket que obt�m conec��es e realiza a troca de informa��es
	 */
	public ServidorPrincipal()
	{
		//Inicialica a troca que vai ficar rodando de fundo para ouvir as solicita��es do servidor
		ThreadRodarServ ouvirRequests = new ThreadRodarServ();
		ouvirRequests.start(); //Coloca pra executar a Thread, e executa tamb�m o seu m�todo run().
	}
	
	/**
	 * L� as mensagens enviadas por um Cliente (socket) conectado.
	 * @param fluxoEntradaDados O fluxo de dados de entrada, que est� sendo passado pelo cliente (socket) conectado.
	 */
	private void lerMensagemClienteConectado(final ObjectInputStream fluxoEntradaDados)
	{
		//Cria uma Thread nova para ler cada mensagem recebida
		new Thread()
		{
			
			@SuppressWarnings("unused")
			public void run()
			{
				String mensagemEntrada = ""; //A mensagem recebida da conec��o. (Socket)
				//Envia os dados para o distinat�rio
				ObjectOutputStream fluxoSaidaDados = null;				
				
				try
				{
					while(true)
					{
						Socket coneccaoAlvo = null; //A conec��o (socket) alvo que receber� os dados a serem passados
						
						//Obt�m os dados do remetente.
						DadosTransferencia dadoEnviar = (DadosTransferencia) fluxoEntradaDados.readObject(); //Obt�m os dados a serem enviados
						
						//Percorre cada conec��o (socket) at� achar a conec��o que corresponde ao destinat�rio desejado
						for (Socket socketConectado : socketsConectados)
						{
							String ipConeccao = socketConectado.getLocalAddress().getHostAddress();
							
							if(ipConeccao.equals(dadoEnviar.getEnderecoIPConeccao())) //Olha at� achar a conecc��o alvo que tenha o IP correto, e por �ltimo o encerra.
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
					System.out.println("Por algum motivo n�o foi encontrada a classe de transfer�ncia da dados na conec��o do remetente!");
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
	 * Thread respons�vel por ficar ouvindo de fundo as conec��es e o recebimento de mensagens.
	 * @author ???
	 *
	 */
	class ThreadRodarServ extends Thread
	{
		public void run()
		{
			System.out.println("Thread para aceitar conec��es e realizar envio de mensagens e troca de informa��es iniciada!"+super.toString());
			
			try
			{
				socketServidorCentral = new  ServerSocket(12345); //Um servidor socket precisa definir o n�mero da porta para receber conex�es dos clientes. Esse n�mero pode variar entre 0 e 65535. N�meros de porta de 1024 abaixo s�o reservadas para uso do sistema, e devem ser evitadas.
				socketsConectados = new ArrayList<Socket>(); //Inicializa a lista para que possa receber dados
				
				
				do //Fica ouvindo de fundo por novas conec��es e mensagens enquanto o programa estiver rodando.
				{
					System.out.println("Buscando novas conec��es");
									
					//Aceita conec��o atual, e prepara para receber informa��es
					Socket socketConeccaoAtual = socketServidorCentral.accept(); //Pega o socket que est� tentando se conectar atualmente e aceita a sua conec��o.(Esse m�todo bloqueia o c�digo da Thread at� que uma conec��o seja feita)
							
					System.out.println("Conec��o realizada! (Conec��o: "+socketConeccaoAtual+")");
					
					fluxoEntradaDados = new ObjectInputStream(socketConeccaoAtual.getInputStream()); //Pega o fluxo de entrada de dados de uma conex�o (socket) e se prepara para ler ela.
					
					//Imprime o endere�o IP do Cliente que foi conectado atualmente.
					System.out.println("Cliente "+socketConeccaoAtual.getLocalAddress().getHostAddress()); //Imprime no console o endere�o IP da conec��o
					
					//Adiciona a lista de conec��es.
					socketsConectados.add(socketConeccaoAtual); //Adiciona essa nova conex�o a lista de cone��es do servidor.
					
					//L� as mensagens enviadas pelo Cliente
					lerMensagemClienteConectado(fluxoEntradaDados);
				} while(true);
			}
			
			catch (Exception e)
			{
				JOptionPane.showMessageDialog(null,"N�o foi poss�vel se conectar ao servidor para receber as mensagens!","N�o foi poss�vel se conecctar ao servidor",JOptionPane.ERROR_MESSAGE); //Imprime mensagem de falha a conec��o do servidor
			}
		}
	}
}


