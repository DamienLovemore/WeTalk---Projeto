package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import comunicacao.ConeccaoCliente;
import comunicacao.DadosTransferencia;
import comunicacao.ServidorPrincipal;
import ferramentasDesign.DesignFluido;
import ferramentasDesign.BalaoMensagens;

import recursos.ApplicationInfo;
import recursos.AudioFileFilter;
import recursos.DocumentFileFilter;
import recursos.ImageFileFilter;
import recursos.ImageSelection;
import recursos.VideoFileFilter;

@SuppressWarnings("serial")
public class GUI extends JFrame
{
	private JMenuBar barraMenus;
	private JMenu arquivo;
	private JMenuItem arquivoSair;
	private JMenu enviar;
	private JMenuItem enviarDocumento;
	private JMenuItem enviarImagem;
	private JMenuItem enviarAudio;
	private JMenuItem enviarVideo;
	private JMenuItem enviarOutros;
	private JMenu config;
	private JMenuItem configDefinirNomUsuario;
	private JMenuItem configDefinirAlvoMensagem;
	protected static String alvoMensagem;
	private JMenu ajuda;
	private JMenuItem ajudaSobre;
	private static TelaSobre telaSobre = null;
		
	private int[] resolucaoTelaInicial, resolucaoTelaAtual;
	private int tamanhoFontesMenu;
	
	private ImageIcon iconeLogo;
	private Container conteudoJanela;
	
	private JPanel painelSuperior;
	private JLabel lblTituloServ;
	private JPanel botoesEdicaoMensagem;
	private JButton botaoCopiarMensagem;
	private ImageIcon iconeCopiarRedimensionado;
	private JButton botaoBaixarMensagem;
	private ImageIcon iconeBaixarRedimensionado;
	private JButton botaoRemoverMensagem;
	private ImageIcon iconeRemoverRedimensionado;
	private boolean botoesEdicaoExibindo=false;
	
	@SuppressWarnings("unused")
	private ServidorPrincipal servidorTrocaMensagens;
	private JList<BalaoMensagens> lstListaMensagem;
	private List<BalaoMensagens> mensagensAplicacao;
	List<ConeccaoCliente> listaConeccoes;
	
	private JPanel painelInferior;
	private JPanel painelMensagem;
	private JTextArea  txtMensagem;
	private JScrollPane rolamentoTextoUsu;
	private popupMenuClipboard popupComponenteTexto;
	private JButton btnAnexo;
	private popupMenuAnexoEnvio popupComponenteAnexo;
	private JButton btnEnviar;
	
	/**
	 * Construtor responsável por criar a interface gráfica da janela principal, junto com todos os seus componentes e funcionalides necessárias.<p>
	 * Utiliza todos os recursos empacotados em outros package.
	 */
	public GUI()
	{
		//Define o título da janela.
		super(ApplicationInfo.applicationName);
		
		alvoMensagem = null;
		
		//Usado para calcular a reoslução inicial que a tela deve apresentar, e o tamanho mínimo dessa janela para que o layout funcione adequadamente.
		resolucaoTelaInicial = DesignFluido.calcularResolucaoCorrespondente(47.80f,66.67f); //650X512 -> 1360X768. Ou, 47.80%X66.67% da tela. 
		resolucaoTelaAtual = resolucaoTelaInicial.clone(); //Inicialmente a resolução da tela atual recebe o valor de resolução da tela inicial.
		tamanhoFontesMenu = 0; //Variável utilizada para calcular e definir o tamanho das fontes do menu desse programa.
		
		this.addComponentListener
		(
			new ComponentAdapter()
			{
				public void componentResized(ComponentEvent eventoRedimensionar)
				{
					//Obtém a resolução atual
					resolucaoTelaAtual[0] = GUI.this.getSize().width; //Obtém o comprimento atual da tela.
					resolucaoTelaAtual[1] = GUI.this.getSize().height; //Obtém a altura atual da tela.
					
					//Define as fontes do menu
					Font fontesMenu = new Font("Dialog",Font.PLAIN,12); //Define a fonte padrão a ser utilizada nos componentes do Menu.
					tamanhoFontesMenu = Math.round((2.73f/100)*resolucaoTelaAtual[1]);
					
					arquivo.setFont(new Font(fontesMenu.getFontName(),fontesMenu.getStyle(),tamanhoFontesMenu)); //Define a fonte a ser utilizada no componente como sendo essa que a gente criou.
					arquivoSair.setFont(new Font(fontesMenu.getFontName(),fontesMenu.getStyle(),tamanhoFontesMenu));
					
					enviar.setFont(new Font(fontesMenu.getFontName(),fontesMenu.getStyle(),tamanhoFontesMenu));
					enviarDocumento.setFont(new Font(fontesMenu.getFontName(),fontesMenu.getStyle(),tamanhoFontesMenu));
					enviarImagem.setFont(new Font(fontesMenu.getFontName(),fontesMenu.getStyle(),tamanhoFontesMenu));
					enviarAudio.setFont(new Font(fontesMenu.getFontName(),fontesMenu.getStyle(),tamanhoFontesMenu));
					enviarVideo.setFont(new Font(fontesMenu.getFontName(),fontesMenu.getStyle(),tamanhoFontesMenu));
					enviarOutros.setFont(new Font(fontesMenu.getFontName(),fontesMenu.getStyle(),tamanhoFontesMenu));
					
					config.setFont(new Font(fontesMenu.getFontName(),fontesMenu.getStyle(),tamanhoFontesMenu));
					configDefinirNomUsuario.setFont(new Font(fontesMenu.getFontName(),fontesMenu.getStyle(),tamanhoFontesMenu));
					configDefinirAlvoMensagem.setFont(new Font(fontesMenu.getFontName(),fontesMenu.getStyle(),tamanhoFontesMenu));
					
					ajuda.setFont(new Font(fontesMenu.getFontName(),fontesMenu.getStyle(),tamanhoFontesMenu));
					ajudaSobre.setFont(new Font(fontesMenu.getFontName(),fontesMenu.getStyle(),tamanhoFontesMenu));
					
					popupComponenteTexto.setFontesItensMenu(new Font(fontesMenu.getFontName(),fontesMenu.getStyle(),tamanhoFontesMenu)); //Usado para definir as fontes dos componentes desse JPopupMenu de uma só vez
					popupComponenteAnexo.setFontesItensMenu(new Font(fontesMenu.getFontName(),fontesMenu.getStyle(),tamanhoFontesMenu));
					
					//Define as fontes dos componentes da GUI
					Font fonteComponente = new Font("Bell MT",Font.BOLD,26);
					int tamanhoFontesComponente = Math.round((5.078f/100)*resolucaoTelaAtual[1]);
					lblTituloServ.setFont(new Font(fonteComponente.getFontName(),fonteComponente.getStyle(),tamanhoFontesComponente));
					
					fonteComponente = new Font("Rockwell",Font.PLAIN,24);
					tamanhoFontesComponente = Math.round((4.69f/100)*resolucaoTelaAtual[1]);
					
					btnEnviar.setFont(new Font(fonteComponente.getFontName(),fonteComponente.getStyle(),tamanhoFontesComponente));
					
					fonteComponente = new Font("Arial",Font.PLAIN,20);
					tamanhoFontesComponente = Math.round((4f/100)*resolucaoTelaAtual[1]);
					txtMensagem.setFont(new Font(fonteComponente.getFontName(),fonteComponente.getStyle(),tamanhoFontesComponente));
					
					//Ajusta o tamanho das mensagens, suas fontes e imagens caso tenham
					for(BalaoMensagens mensagem:mensagensAplicacao)
					{
						mensagem.setResolucaoAtualTelaPrincipal(resolucaoTelaAtual.clone()); //Avisa para cada container de mensagem que o tamanho da tela mudou e portanto deve se redimensionar de acordo.
					}
					
					//Converte a lista de mensagens para um vetor.
					BalaoMensagens[] vetorMensagensAplicacao = new BalaoMensagens[mensagensAplicacao.size()]; 
					vetorMensagensAplicacao = mensagensAplicacao.toArray(vetorMensagensAplicacao); 
					
					lstListaMensagem.setListData(vetorMensagensAplicacao); //Necessário para forçar o redimensionamento dos itens dentro da JList.
					
					//Define o tamanho do painel Inferior
					int[] resolucaoEspecificada = new int[2];//650X102 de 650X512.
					resolucaoEspecificada[0] = Math.round((100f/100)*resolucaoTelaAtual[0]);
					resolucaoEspecificada[1] = Math.round((20f/100)*resolucaoTelaAtual[1]);
					painelInferior.setPreferredSize(new Dimension(resolucaoEspecificada[0],resolucaoEspecificada[1]));
					
					//Define o tamanho das imagens
					try
					{
						int[] tamanhoImagens = new int[2];//68X61 cada imagem de 650X512.
						
						tamanhoImagens[0] = Math.round((10.46f/100)*resolucaoTelaAtual[0]);
						tamanhoImagens[1] = Math.round((11.91f/100)*resolucaoTelaAtual[1]);
																		
						Image iconeAnexo = new ImageIcon(GUI.this.getClass().getResource("/gui/imagens/Attachment-WeTalk.png")).getImage();
						Image iconeAnexoRedimensionado = iconeAnexo.getScaledInstance(tamanhoImagens[0],tamanhoImagens[1],Image.SCALE_SMOOTH);
						btnAnexo.setIcon(new ImageIcon(iconeAnexoRedimensionado));
						
						//Carrega as imagens dos botões do painel superior
						tamanhoImagens[0] = Math.round((17.23f/100)*resolucaoTelaAtual[0]); //112 de 650
						tamanhoImagens[1] = Math.round((8f/100)*resolucaoTelaAtual[1]); //29 de 512
						
						Image iconeCopiar = new ImageIcon(GUI.this.getClass().getResource("/gui/imagens/CopyMessage-WeTalk.png")).getImage();
						Image iconeCopiarRedimensionado = iconeCopiar.getScaledInstance(tamanhoImagens[0],tamanhoImagens[1],Image.SCALE_SMOOTH);
						GUI.this.iconeCopiarRedimensionado = new ImageIcon(iconeCopiarRedimensionado);
						
						Image iconeEditar = new ImageIcon(GUI.this.getClass().getResource("/gui/imagens/DownloadMessage-WeTalk.png")).getImage();
						Image iconeEditarRedimensionado = iconeEditar.getScaledInstance(tamanhoImagens[0],tamanhoImagens[1],Image.SCALE_SMOOTH);
						GUI.this.iconeBaixarRedimensionado = new ImageIcon(iconeEditarRedimensionado);
						
						Image iconeRemover = new ImageIcon(GUI.this.getClass().getResource("/gui/imagens/RemoveMessage-WeTalk.png")).getImage();
						Image iconeRemoverRedimensionado = iconeRemover.getScaledInstance(tamanhoImagens[0],tamanhoImagens[1],Image.SCALE_SMOOTH);
						GUI.this.iconeRemoverRedimensionado = new ImageIcon(iconeRemoverRedimensionado);
						
						if (botoesEdicaoExibindo) //Ao redimensionar se é para exibir os icones dos botões então ele coloca eles no botão.
						{
							botaoCopiarMensagem.setIcon(GUI.this.iconeCopiarRedimensionado);
							botaoBaixarMensagem.setIcon(GUI.this.iconeBaixarRedimensionado);
							botaoRemoverMensagem.setIcon(GUI.this.iconeRemoverRedimensionado);
						}
						
						else
						{
							botaoCopiarMensagem.setIcon(null);
							botaoBaixarMensagem.setIcon(null);
							botaoRemoverMensagem.setIcon(null);
						}
						
					}
					
					catch(NullPointerException erroCarregarImagens)
					{
						btnAnexo.setBackground(Color.black);
					}
				}
			}
		);
		
		try //Ele só define a logo da janela, caso ele tenha encontrado a imagem. Para evitar de quebrar a aplicação caso não tenha encontrado aquela imagem.
		{
			iconeLogo = new ImageIcon(this.getClass().getResource("/gui/imagens/Logo-WeTalk.png")); //Carrega a logo dentro dos recursos disponíveis no programa.
			this.setIconImage(iconeLogo.getImage()); //Define a logo da janela
		}
		
		catch(NullPointerException eventoFalhaCarregaLogo)
		{
			//Caso não tenha achado a logo para o programa ele então não faz nada em relação a definir logos. (Fica a logo padrão do Java no lugar)
		}
				 		
		//Barra de menus da janela.
		barraMenus = new JMenuBar();
		barraMenus.setBackground(new Color(11,77,146)); //San Jose
		barraMenus.setBorder(BorderFactory.createMatteBorder(0,0,2,0,Color.black));
				
		//Define um dos menus da nossa barra de menus
		arquivo = new JMenu("Arquivo");
		arquivo.setMnemonic(KeyEvent.VK_F1); //Define que a janela tiver foco ALT+F1 irá abrir esse menu.
		arquivo.setForeground(new Color(142,183,255)); //Colorado
				
		arquivoSair = new JMenuItem("Sair");
		arquivoSair.setMnemonic(KeyEvent.VK_S); //Define que quando o menu Arquivo estiver aberto, a tecla S aciona o evento desse item de menu.
		arquivoSair.addActionListener
		(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent eventoSair) //O que deve ocorrer quando esse item de menu for clicado ou ativado por atalho.
					{
						System.exit(0);
					}
				}
		);
		arquivo.add(arquivoSair); //Adiciona esse item  de menu a um menu para que ele possa ser exibido e utilizado.
		
		barraMenus.add(arquivo); //Adiciona a barra de menus dessa janela esse menu criado, e dentro dele os seus itens de menu.
		
		enviar = new JMenu("Enviar");
		enviar.setMnemonic(KeyEvent.VK_F2);	
		enviar.setForeground(new Color(142,183,255)); //Colorado
				
		enviarDocumento = new JMenuItem("Documento");
		enviarDocumento.setMnemonic(KeyEvent.VK_D);
		enviarDocumento.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent eventoEnviarDocumento)
				{
					if ((alvoMensagem == null) || (alvoMensagem.length()==0)) //Não envia nada, se não tiver um destinatário definido para envio.
					{
						JOptionPane.showMessageDialog(conteudoJanela,"Defina o destinatário da mensagem através do menu configurações!","Alvo da mensagem não definido",JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					File documentoEscolhido;
					double tamanhoArquivo; //Usado para calcular e limitar o tamanho do envio de arquivos
					
					documentoEscolhido = getDocumentoSistemaUsuario();
					
					try
					{
						tamanhoArquivo = documentoEscolhido.length() / 1e9; //File.length() retorna o tamanho em Bytes, e em seguida converte o valor para GB.
					}
					
					catch(NullPointerException e)
					{
						tamanhoArquivo = 0;
					}
					
					if (tamanhoArquivo > 2.00)
					{
						JOptionPane.showMessageDialog(conteudoJanela,"O limite de envio de arquivos é de no máximo 2GB!","Tamanho de arquivos ultrapassado",JOptionPane.ERROR_MESSAGE);
						documentoEscolhido = null; //Invalida a operação de envio
					}
					
					if (documentoEscolhido!=null)
					{
						//"Envia" a mensagem visualmente no aplicativo
						BalaoMensagens novaMensagemImagem;
						
						novaMensagemImagem = new BalaoMensagens(resolucaoTelaAtual[0],resolucaoTelaAtual[1],documentoEscolhido,null,(byte) 2);
						
						mensagensAplicacao.add(novaMensagemImagem); //Adiciona a nova mensagem criada a lista de mensagens
						
						//Converte a lista de mensagens para um vetor.
						BalaoMensagens[] vetorMensagensAplicacao = new BalaoMensagens[mensagensAplicacao.size()]; 
						vetorMensagensAplicacao = mensagensAplicacao.toArray(vetorMensagensAplicacao); 
						
						lstListaMensagem.setListData(vetorMensagensAplicacao); //Atualiza a lista de conteúdos daquele JList.
						
						//Realiza a transferência via Sockets.
						DadosTransferencia informacoesPassar = new DadosTransferencia(listaConeccoes.get(1).getEnderecoIPCliente(),"Envio de Documento",documentoEscolhido,(byte)2);
						listaConeccoes.get(0).setDadosParaEnvio(informacoesPassar);
					}
				}
			}
		);
		enviar.add(enviarDocumento);
		
		enviarImagem = new JMenuItem("Imagem");
		enviarImagem.setMnemonic(KeyEvent.VK_I);
		enviarImagem.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent eventoEnviarDocumento)
				{
					if ((alvoMensagem == null) || (alvoMensagem.length()==0))
					{
						JOptionPane.showMessageDialog(conteudoJanela,"Defina o destinatário da mensagem através do menu configurações!","Alvo da mensagem não definido",JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					File imagemEscolhida;
					double tamanhoArquivo; 
					
					imagemEscolhida = getImagemSistemaUsuario();
					
					try
					{
						tamanhoArquivo = imagemEscolhida.length() / 1e9; //File.length() retorna o tamanho em Bytes, e em seguida converte o valor para GB.
					}
					
					catch(NullPointerException e)
					{
						tamanhoArquivo = 0;
					}
					
					if (tamanhoArquivo > 2.00)
					{
						JOptionPane.showMessageDialog(conteudoJanela,"O limite de envio de arquivos é de no máximo 2GB!","Tamanho de arquivos ultrapassado",JOptionPane.ERROR_MESSAGE);
						imagemEscolhida = null;
					}
										
					if (imagemEscolhida!=null)
					{
						BalaoMensagens novaMensagemImagem;
						
						novaMensagemImagem = new BalaoMensagens(resolucaoTelaAtual[0],resolucaoTelaAtual[1],imagemEscolhida,null,(byte) 3);
						
						mensagensAplicacao.add(novaMensagemImagem); //Adiciona a nova mensagem criada a lista de mensagens
						
						//Converte a lista de mensagens para um vetor.
						BalaoMensagens[] vetorMensagensAplicacao = new BalaoMensagens[mensagensAplicacao.size()]; 
						vetorMensagensAplicacao = mensagensAplicacao.toArray(vetorMensagensAplicacao); 
						
						lstListaMensagem.setListData(vetorMensagensAplicacao); //Atualiza a lista de conteúdos daquele JList.
						
						DadosTransferencia informacoesPassar = new DadosTransferencia(listaConeccoes.get(1).getEnderecoIPCliente(),"Envio de Imagem",imagemEscolhida,(byte)2);
						listaConeccoes.get(0).setDadosParaEnvio(informacoesPassar);
					}
				}
			}
		);
		enviar.add(enviarImagem);
		
		enviarAudio = new JMenuItem("Audio");
		enviarAudio.setMnemonic(KeyEvent.VK_A);
		enviarAudio.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent eventoEnviarDocumento)
				{
					if ((alvoMensagem == null) || (alvoMensagem.length()==0))
					{
						JOptionPane.showMessageDialog(conteudoJanela,"Defina o destinatário da mensagem através do menu configurações!","Alvo da mensagem não definido",JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					File audioEscolhido;
					double tamanhoArquivo; 
					
					audioEscolhido = getAudioSistemaUsuario();
					
					try
					{
						tamanhoArquivo = audioEscolhido.length() / 1e9; //File.length() retorna o tamanho em Bytes, e em seguida converte o valor para GB.
					}
					
					catch(NullPointerException e)
					{
						tamanhoArquivo = 0;
					}
					
					if (tamanhoArquivo > 2.00)
					{
						JOptionPane.showMessageDialog(conteudoJanela,"O limite de envio de arquivos é de no máximo 2GB!","Tamanho de arquivos ultrapassado",JOptionPane.ERROR_MESSAGE);
						audioEscolhido = null;
					}
					
					if (audioEscolhido!=null)
					{
						BalaoMensagens novaMensagemImagem;
						
						novaMensagemImagem = new BalaoMensagens(resolucaoTelaAtual[0],resolucaoTelaAtual[1],audioEscolhido,null,(byte) 4);
						
						mensagensAplicacao.add(novaMensagemImagem); //Adiciona a nova mensagem criada a lista de mensagens
						
						//Converte a lista de mensagens para um vetor.
						BalaoMensagens[] vetorMensagensAplicacao = new BalaoMensagens[mensagensAplicacao.size()]; 
						vetorMensagensAplicacao = mensagensAplicacao.toArray(vetorMensagensAplicacao); 
						
						lstListaMensagem.setListData(vetorMensagensAplicacao); //Atualiza a lista de conteúdos daquele JList.
						
						DadosTransferencia informacoesPassar = new DadosTransferencia(listaConeccoes.get(1).getEnderecoIPCliente(),"Envio de Audio",audioEscolhido,(byte)2);
						listaConeccoes.get(0).setDadosParaEnvio(informacoesPassar);
					}
				}
			}
		);
		enviar.add(enviarAudio);
		
		enviarVideo = new JMenuItem("Vídeo");
		enviarVideo.setMnemonic(KeyEvent.VK_V);
		enviarVideo.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent eventoEnviarDocumento)
				{
					if ((alvoMensagem == null) || (alvoMensagem.length()==0))
					{
						JOptionPane.showMessageDialog(conteudoJanela,"Defina o destinatário da mensagem através do menu configurações!","Alvo da mensagem não definido",JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					File videoEscolhido;
					double tamanhoArquivo; 
					
					videoEscolhido = getVideoSistemaUsuario();
					
					try
					{
						tamanhoArquivo = videoEscolhido.length() / 1e9; //File.length() retorna o tamanho em Bytes, e em seguida converte o valor para GB.
					}
					
					catch(NullPointerException e)
					{
						tamanhoArquivo = 0;
					}
					
					if (tamanhoArquivo > 2.00)
					{
						JOptionPane.showMessageDialog(conteudoJanela,"O limite de envio de arquivos é de no máximo 2GB!","Tamanho de arquivos ultrapassado",JOptionPane.ERROR_MESSAGE);
						videoEscolhido = null;
					}
					
					if (videoEscolhido!=null)
					{
						BalaoMensagens novaMensagemImagem;
						
						novaMensagemImagem = new BalaoMensagens(resolucaoTelaAtual[0],resolucaoTelaAtual[1],videoEscolhido,null,(byte) 5);
						
						mensagensAplicacao.add(novaMensagemImagem); //Adiciona a nova mensagem criada a lista de mensagens
						
						//Converte a lista de mensagens para um vetor.
						BalaoMensagens[] vetorMensagensAplicacao = new BalaoMensagens[mensagensAplicacao.size()]; 
						vetorMensagensAplicacao = mensagensAplicacao.toArray(vetorMensagensAplicacao); 
						
						lstListaMensagem.setListData(vetorMensagensAplicacao); //Atualiza a lista de conteúdos daquele JList.
						
						DadosTransferencia informacoesPassar = new DadosTransferencia(listaConeccoes.get(1).getEnderecoIPCliente(),"Envio de Video",videoEscolhido,(byte)2);
						listaConeccoes.get(0).setDadosParaEnvio(informacoesPassar);
					}
				}
			}
		);
		enviar.add(enviarVideo);
		
		enviarOutros = new JMenuItem("Outros");
		enviarOutros.setMnemonic(KeyEvent.VK_O);
		enviarOutros.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent eventoEnviarDocumento)
				{
					if ((alvoMensagem == null) || (alvoMensagem.length()==0))
					{
						JOptionPane.showMessageDialog(conteudoJanela,"Defina o destinatário da mensagem através do menu configurações!","Alvo da mensagem não definido",JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					File arquivoEscolhido;
					double tamanhoArquivo; 
					
					arquivoEscolhido = getOutroArquivoSistemaUsuario();
					
					try
					{
						tamanhoArquivo = arquivoEscolhido.length() / 1e9; //File.length() retorna o tamanho em Bytes, e em seguida converte o valor para GB.
					}
					
					catch(NullPointerException e)
					{
						tamanhoArquivo = 0;
					}
					
					if (tamanhoArquivo > 2.00)
					{
						JOptionPane.showMessageDialog(conteudoJanela,"O limite de envio de arquivos é de no máximo 2GB!","Tamanho de arquivos ultrapassado",JOptionPane.ERROR_MESSAGE);
						arquivoEscolhido = null;
					}
					
					if (arquivoEscolhido!=null)
					{
						BalaoMensagens novaMensagemImagem;
						
						novaMensagemImagem = new BalaoMensagens(resolucaoTelaAtual[0],resolucaoTelaAtual[1],arquivoEscolhido,null,(byte) 6);
						
						mensagensAplicacao.add(novaMensagemImagem); //Adiciona a nova mensagem criada a lista de mensagens
						
						//Converte a lista de mensagens para um vetor.
						BalaoMensagens[] vetorMensagensAplicacao = new BalaoMensagens[mensagensAplicacao.size()]; 
						vetorMensagensAplicacao = mensagensAplicacao.toArray(vetorMensagensAplicacao); 
						
						lstListaMensagem.setListData(vetorMensagensAplicacao); //Atualiza a lista de conteúdos daquele JList.
						
						DadosTransferencia informacoesPassar = new DadosTransferencia(listaConeccoes.get(1).getEnderecoIPCliente(),"Envio de um arquivo qualquer (Outros)",arquivoEscolhido,(byte)2);
						listaConeccoes.get(0).setDadosParaEnvio(informacoesPassar);
					}
				}
			}
		);
		enviar.add(enviarOutros);
		
		barraMenus.add(enviar);
		
		config = new JMenu("Configurações");
		config.setMnemonic(KeyEvent.VK_F3);
		config.setForeground(new Color(142,183,255)); //Colorado
		
		configDefinirNomUsuario = new JMenuItem("Nome de usuário");
		configDefinirNomUsuario.setMnemonic(KeyEvent.VK_N);
		configDefinirNomUsuario.addActionListener
		(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent eventoDefinirNomUsuario)
					{
						String escolhaNomeUsuario=""; //Armazena o novo nome digitado pelo usuário
						
						//Abre uma nova janela pra receber entrada de texto do usuario, e a armazena em uma variável
						escolhaNomeUsuario =JOptionPane.showInputDialog(conteudoJanela,"Digite o seu novo nome de usuário","Escolha o seu nome",JOptionPane.INFORMATION_MESSAGE);
						
						if((escolhaNomeUsuario == null) || (escolhaNomeUsuario.length()<5)) //Se a quantidade de caracteres digitado por ele for menor que 10, ele mostra a mensagem abaixo.
						{
							JOptionPane.showMessageDialog(conteudoJanela,"Quantidade insuficiente de caracteres foi digitado!","Nome inválido",JOptionPane.ERROR_MESSAGE);
						}
						
						else
						{
							ApplicationInfo.applicationUserName = escolhaNomeUsuario; //Caso contrário define o novo nome de usuário para aparecer nas mensagens.
						}
					}
				}
		);
		config.add(configDefinirNomUsuario);
		
		configDefinirAlvoMensagem = new JMenuItem("Alvo da Mensagem");
		configDefinirAlvoMensagem.setMnemonic(KeyEvent.VK_A);
		configDefinirAlvoMensagem.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent eventoDefinirAlvo)
				{
					String retornoUsuario;
					
					retornoUsuario = JOptionPane.showInputDialog(conteudoJanela,"Exemplos de valores válidos de IP (IPv4) são: 127.0.0.3, 172.16.254.1 e 76.240.249.145","Digite o IP do destinatário da mensagem",JOptionPane.WARNING_MESSAGE);
					
					if ((retornoUsuario == null) || (retornoUsuario.length()==0))
					{
						JOptionPane.showMessageDialog(popupComponenteAnexo,"Não foi encontrado um valor!","Valor não informado",JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					String[] validacaoEntrada = retornoUsuario.split("\\.");
					boolean valorValido = true;
					
					if (validacaoEntrada.length != 4)
						valorValido = false;
					else if ((validacaoEntrada[0].length() < 1) | (validacaoEntrada[0].length() > 3))
						valorValido = false;
					else if ((validacaoEntrada[1].length() < 1) | (validacaoEntrada[1].length() > 3))
						valorValido = false;
					else if ((validacaoEntrada[2].length() < 1) | (validacaoEntrada[2].length() > 3))
						valorValido = false;
					else if ((validacaoEntrada[3].length() < 1) | (validacaoEntrada[3].length() > 3))
						valorValido = false;
					
					try
					{
						String valorNumerico;
						valorNumerico = validacaoEntrada[0]+validacaoEntrada[1]+validacaoEntrada[2]+validacaoEntrada[3];
						Integer.parseInt(valorNumerico);
					}
					
					catch(NumberFormatException | ArrayIndexOutOfBoundsException e)
					{
						valorValido = false;
					}
					
					if (valorValido == false)
					{
						JOptionPane.showMessageDialog(conteudoJanela,"Formato inválido para endereço de IP foi digitado!","Formato inválido encontrado",JOptionPane.ERROR_MESSAGE);
					}
					
					else
					{
						alvoMensagem = retornoUsuario;
						
						for (int cont = 1;cont<=listaConeccoes.size()-1;cont+=1)
						{
							try 
							{
								if (alvoMensagem.equals(listaConeccoes.get(cont).getEnderecoIPCliente()))
								{
									JOptionPane.showMessageDialog(conteudoJanela,"Endereço IP informado já existente!","Conecção já feita",JOptionPane.WARNING_MESSAGE);
									return;
								}
							}
							
							catch(IndexOutOfBoundsException e)
							{
								break;
							}
						}
						
						if (listaConeccoes.size() > 1)
						{
							for (int cont = 1;cont<=listaConeccoes.size()-1;cont+=1)
							{
								try
								{
									ConeccaoCliente clienteRemovido = listaConeccoes.get(cont);
									clienteRemovido.encerrarConeccao();
																		
									listaConeccoes.remove(cont);
									System.out.println(listaConeccoes.get(1));
								}
								
								catch(IndexOutOfBoundsException e)
								{
									break;
								}
							}
						}
						
						ConeccaoCliente coneccaoDesteCliente = new ConeccaoCliente(alvoMensagem);
						listaConeccoes.add(coneccaoDesteCliente);
					}
				}
			}
		);
		config.add(configDefinirAlvoMensagem);
		
		barraMenus.add(config);
		
		ajuda = new JMenu("Ajuda");
		ajuda.setMnemonic(KeyEvent.VK_F4);
		ajuda.setForeground(new Color(142,183,255)); //Colorado
		
		ajudaSobre = new JMenuItem("Sobre");
		ajudaSobre.setMnemonic(KeyEvent.VK_S);
		ajudaSobre.addActionListener
		(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent eventoJanelaSobre)
					{
						if(telaSobre == null)
						{
							telaSobre = new TelaSobre(resolucaoTelaAtual[0],resolucaoTelaAtual[1],GUI.this);
						}
						
						telaSobre.setVisible(true);
					}
				}
		);
		ajuda.add(ajudaSobre);
		
		barraMenus.add(ajuda);
		
		conteudoJanela = this.getContentPane();
		conteudoJanela.setLayout(new BorderLayout());
		conteudoJanela.setBackground(new Color(2,81,142)); //Los Angeles (secondary)
		
		painelSuperior = new JPanel();
		painelSuperior.setLayout(new GridLayout(2,1)); //Define um gerenciador de layout que tenha 2 linhas e uma coluna.
		painelSuperior.setBorder(BorderFactory.createMatteBorder(0,0,2,0,Color.black));
		painelSuperior.setBackground(new Color(2,81,142)); //Los Angeles (secondary)
		
		lblTituloServ = new JLabel("Canal: Servidor Principal");
		lblTituloServ.setForeground(new Color(142,183,255));//Colorado
		lblTituloServ.setHorizontalAlignment(SwingConstants.CENTER);
		lblTituloServ.setBorder(BorderFactory.createMatteBorder(0,0,2,0,Color.black));
		painelSuperior.add(lblTituloServ);
		
		botoesEdicaoMensagem = new JPanel();
		botoesEdicaoMensagem.setLayout(new GridLayout(1,3));
		botoesEdicaoMensagem.setOpaque(false);
				
		botaoCopiarMensagem = new JButton();
		botaoCopiarMensagem.setHorizontalAlignment(SwingConstants.CENTER);
		botaoCopiarMensagem.addMouseListener
		(
				new MouseAdapter() 
				{
					public void mousePressed(MouseEvent e) //Quando o mouse é pressionado
					{
						//Usado para fazer com o que botão se torne visível e que a sua cor de fundo seja a especificada.
						botaoCopiarMensagem.setBackground(new Color(24,25,76)); //Chicago
						botaoCopiarMensagem.setContentAreaFilled(true);
					}
					
					public void mouseReleased(MouseEvent e) //Quando o mouse é solto
					{
						//Usado para tornar o botão transparente denovo
						botaoCopiarMensagem.setBackground(null);
						botaoCopiarMensagem.setContentAreaFilled(false);
					}
				}
		);
		botaoCopiarMensagem.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent eventoCopiarMensagem)
				{
					if(botoesEdicaoExibindo) //Só ouve a eventos se os botões estão exibindo.
					{
						BalaoMensagens mensagemMenorIndice;
						
						mensagemMenorIndice = lstListaMensagem.getSelectedValue(); //Caso tenha mais de um item selecionado esse método retorna o item com o menor indíce. (Não tem como copiar mais de uma mensagem ao mesmo tempo)
						
						if (mensagemMenorIndice.getTipoMensagem() == 1) //Caso seja uma mensagem de texto.
						{
							StringSelection itemParaCopiar;
							Clipboard areaTransferencia;
							
							areaTransferencia = Toolkit.getDefaultToolkit().getSystemClipboard(); //Obtém a área de transferência do sistema.
							
							itemParaCopiar = new StringSelection(mensagemMenorIndice.getTextoMensagem()); //Obtém o texto contido nessa mensagem e o prepara para ser transferido para a área de transferência.
							
							areaTransferencia.setContents(itemParaCopiar,null); //Envia para a área de transferência o conteúdo da mensagem selecionado.(Caso tenha algo lá, ele a sobreescreve)
						}
						
						else if (mensagemMenorIndice.getTipoMensagem() == 3) //Caso seja uma mensagem de envio de imagem.
						{
							ImageSelection itemParaCopiar;
							Clipboard areaTransferencia;
							
							areaTransferencia = Toolkit.getDefaultToolkit().getSystemClipboard();
							
							File arquivoMensagem = mensagemMenorIndice.getArquivoMensagem(); //Obtém o arquivo contido dentro dessa mensagem.
							Image imagemParaTransferir;
							
							try
							{
								imagemParaTransferir = ImageIO.read(arquivoMensagem); //Como os arquivos são aramazenados em formato genérico, ele deve converter primeiro para o formato genérico de imagem que o Java reconhece.(java.awt.image)
								
								//Converte a imagem para um formato de imagem que a área de transferência entende. (Alguns formatos de imagem ele não aceitaria normalmente)
								
								
								itemParaCopiar = new ImageSelection(imagemParaTransferir); //Prepara para ser transferido para a área de transferência a imagem especificada.
								
								areaTransferencia.setContents(itemParaCopiar, null);
							}
							
							catch(IOException e) //Caso o arquivo por algum motivo não possa ser lido (Arquivo corrompido), ele não faz nada.
							{
								
							}
						}
						
						else //Caso seja uma mensagem de outros tipos deve retornar um erro. Pois só é possível copiar textos e imagens.
						{
							JOptionPane.showMessageDialog(conteudoJanela,"Só é possível copiar imagens e textos para a área de transferência!","Formato inválido",JOptionPane.WARNING_MESSAGE); //Mostra uma mensagem de erro indicando que este tipo de mensagem não é permitido a cópia.
						}
					}
				}
			}
		);
		botaoCopiarMensagem.setContentAreaFilled(false);
		botaoCopiarMensagem.setBorder(BorderFactory.createMatteBorder(0,0,0,2,Color.black));
		botoesEdicaoMensagem.add(botaoCopiarMensagem);
		
		botaoBaixarMensagem = new JButton();
		botaoBaixarMensagem.setHorizontalAlignment(SwingConstants.CENTER);
		botaoBaixarMensagem.addMouseListener
		(
				new MouseAdapter() 
				{
					//Efeitinhos visual de clique
					public void mousePressed(MouseEvent e) //Quando o mouse é pressionado
					{
						//Usado para fazer com o que botão se torne visível e que a sua cor de fundo seja a especificada.
						botaoBaixarMensagem.setBackground(new Color(24,25,76)); //Chicago
						botaoBaixarMensagem.setContentAreaFilled(true);
					}
					
					public void mouseReleased(MouseEvent e) //Quando o mouse é solto
					{
						//Usado para tornar o botão transparente denovo
						botaoBaixarMensagem.setBackground(null);
						botaoBaixarMensagem.setContentAreaFilled(false);
					}
				}
		);
		botaoBaixarMensagem.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent eventoCopiarMensagem)
				{
					if(botoesEdicaoExibindo)
					{
						BalaoMensagens mensagemSelecionada;
						
						mensagemSelecionada = lstListaMensagem.getSelectedValue(); //Obtém o item selecionado co menor indíce, ou o item único caso seja selecionado apenas um item.
						
						if(mensagemSelecionada.getTipoMensagem() > 1) //Verifica se a mensagem selecionada tem algum arquivo para ser salvo.
						{
							File arquivoSalvar = mensagemSelecionada.getArquivoMensagem(); //Obtém o arquivo da mensagem a ser salvo.
							
							JFileChooser salvadorArquivos = new JFileChooser();
							salvadorArquivos.setFileSelectionMode(JFileChooser.FILES_ONLY); //Previni o usuári de tentar salvar um diretório.
														
							int sucesso_operacao = salvadorArquivos.showSaveDialog(conteudoJanela);
							
							if (sucesso_operacao == JFileChooser.APPROVE_OPTION) //Se o usuário escolheu, salvar. E não cancelor, ou ocorreu um erro durante o processo.
							{
								FileOutputStream escreverArquivo;
								
								try
								{
									byte[] bytesParaEscrever = new byte[1024 * 50]; //Cria uma cadeia de bytes com tamanho correto para armazenar cada byte do arquivo.
									int tamanho;
									String extensaoArquivoSalvar;
									File localizacaoDiretorioSalvar;
									
									//Lendo o arquivo
									FileInputStream lerArquivo = new FileInputStream(arquivoSalvar); //Abre o processador de arquivos e prepara ele para ler o arquivo que foi passado para ele.
									
									int posicaoExtensao = (arquivoSalvar.getName().indexOf(".")) + 1; //Aponta para o começo do nome da extensão.
									extensaoArquivoSalvar = arquivoSalvar.getName().substring(posicaoExtensao); //Pega todo o nome da extensão
									localizacaoDiretorioSalvar = new File(salvadorArquivos.getSelectedFile().getPath() + "." + extensaoArquivoSalvar); //Define o diretório para salvar o arquivo, juntamente com a extensão (formato) correta para o arquivo.
																		
									escreverArquivo = new FileOutputStream(localizacaoDiretorioSalvar); //Pega o nome do arquivo, e o diretório escolhido pelo usuário pra salvar o arquivo.
									
									while((tamanho = lerArquivo.read(bytesParaEscrever)) > 0)
									{
										escreverArquivo.write(bytesParaEscrever,0, tamanho);
									}
									
									lerArquivo.close();
									escreverArquivo.close();
								}
								catch (IOException e)
								{
									JOptionPane.showMessageDialog(conteudoJanela,"Ouve um erro inesperado ao tentar salvar o arquivo!","Falha ao tentar salvar o arquivo",JOptionPane.ERROR_MESSAGE);
								}
							}
						}
						
						else
						{
							JOptionPane.showMessageDialog(conteudoJanela,"Só é possível fazer download de mensagens que contenham arquivos!","Arquivo não encontrado",JOptionPane.WARNING_MESSAGE);
						}
					}
				}
			}
		);
		botaoBaixarMensagem.setContentAreaFilled(false);
		botaoBaixarMensagem.setBorder(BorderFactory.createMatteBorder(0,2,0,2,Color.black));
		botoesEdicaoMensagem.add(botaoBaixarMensagem);
		
		botaoRemoverMensagem = new JButton();
		botaoRemoverMensagem.setHorizontalAlignment(SwingConstants.CENTER);
		botaoRemoverMensagem.addMouseListener
		(
				new MouseAdapter() 
				{
					public void mousePressed(MouseEvent e) //Quando o mouse é pressionado
					{
						//Usado para fazer com o que botão se torne visível e que a sua cor de fundo seja a especificada.
						botaoRemoverMensagem.setBackground(new Color(24,25,76)); //Chicago
						botaoRemoverMensagem.setContentAreaFilled(true);
					}
					
					public void mouseReleased(MouseEvent e) //Quando o mouse é solto
					{
						//Usado para tornar o botão transparente denovo
						botaoRemoverMensagem.setBackground(null);
						botaoRemoverMensagem.setContentAreaFilled(false);
					}
				}
		);
		botaoRemoverMensagem.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent eventoCopiarMensagem)
				{
					if(botoesEdicaoExibindo)
					{
						List<BalaoMensagens> mensagensSelecionadas;
						
						mensagensSelecionadas = lstListaMensagem.getSelectedValuesList(); //Obtém um vetor com todos os itens que estão selecionados atualmente na JList.
												
						for(BalaoMensagens mensagemAtual : mensagensSelecionadas) //Percorre todos os elementos da lista.
						{
							mensagensAplicacao.remove(mensagemAtual); //Remove da lista da itens o item que ele encontrou atualmente.
						}
						
						//Converte a lista de mensagens para um vetor.
						BalaoMensagens[] vetorMensagensAplicacao = new BalaoMensagens[mensagensAplicacao.size()]; 
						vetorMensagensAplicacao = mensagensAplicacao.toArray(vetorMensagensAplicacao); 
						
						lstListaMensagem.setListData(vetorMensagensAplicacao); //Atualiza a lista de conteúdos daquele JList.
					}
				}
			}
		);
		botaoRemoverMensagem.setContentAreaFilled(false);
		botaoRemoverMensagem.setBorder(BorderFactory.createMatteBorder(0,2,0,0,Color.black));
		botoesEdicaoMensagem.add(botaoRemoverMensagem);
				
		painelSuperior.add(botoesEdicaoMensagem);
		
		conteudoJanela.add(painelSuperior,BorderLayout.NORTH);
		
		mensagensAplicacao = new ArrayList<BalaoMensagens>(); //Cria a List que guardará todas as mensagens.
		BalaoMensagens[] vetorMensagensAplicacao = new BalaoMensagens[mensagensAplicacao.size()]; //Cria um vetor que tem o mesmo tamanho da ArrayList.
		vetorMensagensAplicacao = mensagensAplicacao.toArray(vetorMensagensAplicacao); //Como JList só aceita vetores, e necessitamos de usar uma ArrayList pois não podemos usar um vetor de tamanho fixo por questão de eficiência, a cada atribuição devemos converter a ArrayList para um Vetor.
		
		lstListaMensagem = new JList<BalaoMensagens>(vetorMensagensAplicacao); //Cria um JList que podem ser adicionados vários JPanels dentro dela.
		lstListaMensagem.setVisibleRowCount(3); //A quantidade de linhas mínima a ser exibido, sem precisar rolar para baixo.
		lstListaMensagem.setSelectionModel
		(
				new DefaultListSelectionModel()
				{
					public void setSelectionInterval(int index0, int index1) //Em listas de seleção única, o index1 representa o index do item atual, e o index0 representa o index do item anterior.
					{
						if  (lstListaMensagem.isSelectedIndex(index0)) //Se o item que ele selecionou atualmente é o mesmo item que ele selecinou anteriormente.
						{
							lstListaMensagem.removeSelectionInterval(index0, index1); //Remove a seleção
						}
						
						else //Caso ele tenha selecionado um item diferente do item anterior, então valida a seleção e seleciona o item
						{
							lstListaMensagem.addSelectionInterval(index0, index1); //Seleciona o intervalo (itens) especificado.
						}
					}
				}
		);
		lstListaMensagem.addListSelectionListener
		(
			new ListSelectionListener()
			{
				public void valueChanged(ListSelectionEvent eventoSelecionarMensagem) //Evento disparado quando um item da lista é clicado. (Selecionado)
				{
					BalaoMensagens itemSelecionado = lstListaMensagem.getSelectedValue(); //Obtém o item seleciondo atualmente, e retorna null se não tiver nada selecionado.
															
					if (itemSelecionado == null) //Se não tem nada selecionado.
					{
						botoesEdicaoExibindo = false; //Define que nenhum botão deve ser exibido, pois nada foi selecionado. E assim quando a tela for redimensionada ele aplica a renderização adequada.
												
						botaoCopiarMensagem.setIcon(null);
						botaoBaixarMensagem.setIcon(null);
						botaoRemoverMensagem.setIcon(null);
					}
					
					else //Se algo foi selecinado
					{
						if (botoesEdicaoExibindo == false) //Se os botões já estiverem exibindo ele não carrega as imagens denovo. Só muda se antes não estivesse exibindo
						{
							botoesEdicaoExibindo = true; //Define que todos os botões devem serem exibidos, pois nada foi selecionado. E assim quando a tela for redimensionada ele aplica a renderização adequada.
							
							botaoCopiarMensagem.setIcon(iconeCopiarRedimensionado);
							botaoBaixarMensagem.setIcon(iconeBaixarRedimensionado);
							botaoRemoverMensagem.setIcon(iconeRemoverRedimensionado);
						}
					}
				}
			}
		);
		lstListaMensagem.setCellRenderer(new PanelRenderer());
		lstListaMensagem.setFont(new Font("Arial",Font.PLAIN,28));
		lstListaMensagem.setForeground(Color.black);
		lstListaMensagem.setBackground(new Color(41,64,119)); //Dallas
		//lstListaMensagem.setOpaque(false); //Define a JList como invísivel para poder aparecer o fundo.
		//lstListaMensagem.setBackground(new Color(0,0,0,0)); //Coloca o fundo da JList como uma cor invísivel, pois mesmo com a opção acima quando é adicionado elementos ele fica visível denovo.
		
		JScrollPane rolamentoListaMensagem = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); //Define barra de rolagem verticais que aparecerem somente quando for necessário no componente.
		
		//Serve para definir o fundo das barras de rolagem, como sendo transparentes...Para exibir apenas o JListBox no fundo.
		rolamentoListaMensagem.setOpaque(false);
		rolamentoListaMensagem.getViewport().setOpaque(false);
		rolamentoListaMensagem.getViewport().setView(lstListaMensagem);
		
		conteudoJanela.add(rolamentoListaMensagem,BorderLayout.CENTER);
				
		painelInferior = new JPanel();
		painelInferior.setLayout(new BorderLayout(0,0));
		painelInferior.setBorder(BorderFactory.createMatteBorder(2,0,0,0,Color.black));
		
		painelMensagem = new JPanel();
		painelMensagem.setLayout(new BorderLayout());
		painelMensagem.setBackground(new Color(2,81,142)); //Los Angeles (secondary)
						
		txtMensagem = new JTextArea();
		txtMensagem.setLineWrap(true); //Define a quebra de texto automática quando ele não couber na mesma linha.
		txtMensagem.setWrapStyleWord(true); //Define a quebre de texto para quebrar palavras inteiras, ao invés de caracteres.
		txtMensagem.addMouseListener
		(
				new  MouseAdapter()
				{
					public void mousePressed(MouseEvent eventoPressionar)
					{
						int x,y;
						
						x = eventoPressionar.getX(); //Pega a posiçao de X em que o mouse foi clicado
						y = eventoPressionar.getY(); //Pega a posição de Y em que o mouse foi clicado.
						
						if(eventoPressionar.isPopupTrigger()) //Verificar se a ação feita no mouse é o padrão daquela plataforma para ativar PopupMenus.
						{
							popupComponenteTexto.show(txtMensagem, x, y); //Exibe o PopupMenu na posição específicada dentro desse componente.
						}
					}
					
					public void mouseReleased(MouseEvent eventoSoltar) //Por padrão na documentação do Java por questões de compatibilidade deve se verificar por popumenus em ambos os eventos.
					{
						int x,y;
						
						x = eventoSoltar.getX(); //Pega a posiçao de X em que o mouse foi clicado
						y = eventoSoltar.getY(); //Pega a posição de Y em que o mouse foi clicado.
						
						if(eventoSoltar.isPopupTrigger()) //Verificar se a ação feita no mouse é o padrão daquela plataforma para ativar PopupMenus.
						{
							popupComponenteTexto.show(txtMensagem, x, y); //Exibe o PopupMenu na posição específicada dentro desse componente.
						}
					}
				}
		);
				
		popupComponenteTexto = new popupMenuClipboard(txtMensagem); //Cria a nosso popup e associa ele ao nosso texto. 
		
		rolamentoTextoUsu = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		rolamentoTextoUsu.getViewport().setView(txtMensagem);
		
		painelMensagem.add(rolamentoTextoUsu,BorderLayout.CENTER);
							
		btnAnexo = new JButton();//68X78
		btnAnexo.setHorizontalAlignment(SwingConstants.CENTER);
		btnAnexo.addMouseListener
		(
				new MouseAdapter()
				{
					public void mouseClicked(MouseEvent e) //Quando o mouse é pressionado e solto sucessivamente, sem diferença de intervalo. (O clique comum)
					{
						int x,y;
						
						x = e.getX();
						y = e.getY();
						
						popupComponenteAnexo.show(btnAnexo,x,y); //popupMenu que realiza o envio de arquivos, semelhante ao menu Enviar. (Tudo que tem no menu enviar tem aqui também)
					}
					
					public void mousePressed(MouseEvent e) //Quando o mouse é pressionado
					{
						//Usado para fazer com o que botão se torne visível e que a sua cor de fundo seja a especificada.
						btnAnexo.setBackground(new Color(24,25,76)); //Chicago
						btnAnexo.setOpaque(true);
					}
					
					public void mouseReleased(MouseEvent e) //Quando o mouse é solto
					{
						//Usado para tornar o botão transparente denovo
						btnAnexo.setBackground(null);
						btnAnexo.setOpaque(false);
					}
				}
		);
		btnAnexo.setContentAreaFilled(false); //Serve para definir o JButton como transparente.
		painelMensagem.add(btnAnexo,BorderLayout.EAST);
		
		popupComponenteAnexo = new popupMenuAnexoEnvio(resolucaoTelaAtual, mensagensAplicacao, lstListaMensagem,this);
				
		painelInferior.add(painelMensagem,BorderLayout.CENTER);
		
		btnEnviar = new JButton("Enviar");
		btnEnviar.setHorizontalAlignment(SwingConstants.CENTER);
		btnEnviar.setForeground(Color.lightGray);
		btnEnviar.setBackground(new Color(14,26,41)); //Philadelphia
		btnEnviar.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent eventoEnviar)
				{
					if ((alvoMensagem == null) || (alvoMensagem.length()==0)) //Caso não possua quem enviar a mensagem ele não reliza nada
					{
						JOptionPane.showMessageDialog(conteudoJanela,"Defina o destinatário da mensagem através do menu configurações!","Alvo da mensagem não definido",JOptionPane.ERROR_MESSAGE);
						return;
					}
						
					
					if(txtMensagem.getText().length()>0) //Só envia mensagens quando o usuário digitou algo. (Caso não tenha nada, não faz nada)
					{
						//"Envia" a mensagem visualmente no aplicativo
						BalaoMensagens novaMensagemTexto;
						
						novaMensagemTexto = new BalaoMensagens(resolucaoTelaAtual[0],resolucaoTelaAtual[1],null,txtMensagem.getText(),(byte) 1);
						
						mensagensAplicacao.add(novaMensagemTexto); //Adiciona a nova mensagem criada a lista de mensagens
						
						//Converte a lista de mensagens para um vetor.
						BalaoMensagens[] vetorMensagensAplicacao = new BalaoMensagens[mensagensAplicacao.size()]; 
						vetorMensagensAplicacao = mensagensAplicacao.toArray(vetorMensagensAplicacao); 
						
						lstListaMensagem.setListData(vetorMensagensAplicacao); //Atualiza a lista de conteúdos daquele JList.
						
						//Realiza a transferência via Sockets
						DadosTransferencia informacoesPassar = new DadosTransferencia(listaConeccoes.get(1).getEnderecoIPCliente(),txtMensagem.getText(),null,(byte)1);
						listaConeccoes.get(0).setDadosParaEnvio(informacoesPassar);
						txtMensagem.setText("");
					}
				}
			}
		);
		painelInferior.add(btnEnviar,BorderLayout.SOUTH);
		
		conteudoJanela.add(painelInferior,BorderLayout.SOUTH);
		
		this.servidorTrocaMensagens = new ServidorPrincipal(); //Inicializa o servidor para que possa intermediar o envio de mensagens.(COs cliente mandam mensagens pro servidor que ele repassa a mensagem, e verifica para o Cliente apropriado. Que recebe e realiza o tratamento dos dados)
		
		this.listaConeccoes= new ArrayList<ConeccaoCliente>(); //Armazena todos os Clientes
		ConeccaoCliente coneccaoDesteCliente = new ConeccaoCliente(ApplicationInfo.enderecoIPUsuario); //Inicializa o Cliente inicial/padrão que é o usuário que do PC que abre o programa.
		this.listaConeccoes.add(coneccaoDesteCliente);
		
		this.setJMenuBar(barraMenus); //Define a barra de menus a ser apresentada nessa janela.
		
		this.setSize(resolucaoTelaInicial[0],resolucaoTelaInicial[1]); //Define o tamanho inicial que a tela  deve ter.
		this.setMinimumSize(new Dimension(resolucaoTelaInicial[0],resolucaoTelaInicial[1]));
		
		this.setLocationRelativeTo(null); //Faz com que essa janela seja alinhada ao centro.
		this.setVisible(true); //Torna a janela visível, pois por padrão elas são invisíveis.
	}
	
	/**
	 * Método usado pela janela TelaSobre para ela poder sinalizar quando ele foi fechada. Assim o programa sabe quando exibir uma janela que ele já tem aberto, ou deve criar uma nova janela pois ele foi fechada.(Previne de múltiplas tela sobre serem criadas)
	 */
	public static void setTelaSobreLivre()
	{
		telaSobre = null;
	}
	
	/**
	 * Função utilizada para obter e carregar um documento do sistema do usuário.
	 * @return null - Caso ocorra um erro, ou a operação seja cancelada pelo usuário.<p>
	 * File - Caso o usuário tenha escolhido uma documento, e o programa carregou esse documento com sucesso.
	 */
	public static File getDocumentoSistemaUsuario()
	{
		File retorno = null; //O arquivo obtido do sistema do usuário, que ele selecionou.
		
		JFileChooser abrirArquivos = new JFileChooser(); //Carrega a janela de abrir diretório e arquivos.
		abrirArquivos.setFileSelectionMode(JFileChooser.FILES_ONLY); //Define que apenas arquivos e não diretórios podem ser selecionados
		abrirArquivos.setAcceptAllFileFilterUsed(false); //Desabilita a opção de escolher qualquer tipo de arquivos nele.
		abrirArquivos.addChoosableFileFilter(new DocumentFileFilter()); //Define que o usuário possa escolher apenas documentos.
		
		int escolhaFeita = abrirArquivos.showOpenDialog(null); //Abre uma outra tela para que o usuário possa escolher e carregar um arquivo. (Não salva, ou sobrescreve arquivos)
		
		if (escolhaFeita == JFileChooser.APPROVE_OPTION) //Somente prossegue caso o usuário tenha escolhido um arquivo com sucesso. Caso ocorra um erro, ou o usuário  cancele a operação, ele não faz nada.
		{
			retorno = abrirArquivos.getSelectedFile(); //Obtém o arquivo que foi escolhido pelo usuário.
		}
				
		return retorno;
	}
	
	/**
	 * Função utilizada para obter e carregar uma imagem do sistema do usuário.
	 * @return null - Caso ocorra um erro, ou a operação seja cancelada pelo usuário.<p>
	 * File - Caso o usuário tenha escolhido uma imagem, e o programa carregou essa imagem com sucesso.
	 */
	public static File getImagemSistemaUsuario()
	{
		File retorno = null; 
		
		JFileChooser abrirArquivos = new JFileChooser();
		abrirArquivos.setFileSelectionMode(JFileChooser.FILES_ONLY); 
		abrirArquivos.setAcceptAllFileFilterUsed(false); 
		abrirArquivos.addChoosableFileFilter(new ImageFileFilter()); 
		
		int escolhaFeita = abrirArquivos.showOpenDialog(null);
		
		if (escolhaFeita == JFileChooser.APPROVE_OPTION) 
		{
			retorno = abrirArquivos.getSelectedFile();
		}
				
		return retorno;
	}
	
	/**
	 * Função utilizada para obter e carregar um aúdio do sistema do usuário.
	 * @return null - Caso ocorra um erro, ou a operação seja cancelada pelo usuário.<p>
	 * File - Caso o usuário tenha escolhido uma aúdio, e o programa carregou esse aúdio com sucesso.
	 */
	public static File getAudioSistemaUsuario()
	{
		File retorno = null; 
		
		JFileChooser abrirArquivos = new JFileChooser();
		abrirArquivos.setFileSelectionMode(JFileChooser.FILES_ONLY); 
		abrirArquivos.setAcceptAllFileFilterUsed(false); 
		abrirArquivos.addChoosableFileFilter(new AudioFileFilter()); 
		
		int escolhaFeita = abrirArquivos.showOpenDialog(null);
		
		if (escolhaFeita == JFileChooser.APPROVE_OPTION) 
		{
			retorno = abrirArquivos.getSelectedFile();
		}
				
		return retorno;
	}
	
	/**
	 * Função utilizada para obter e carregar um vídeo do sistema do usuário.
	 * @return null - Caso ocorra um erro, ou a operação seja cancelada pelo usuário<p>
	 * File - Caso o usuário tenha escolhido uma vídeo, e o programa carregou esse vídeo com sucesso.
	 */
	public static File getVideoSistemaUsuario()
	{
		File retorno = null; 
		
		JFileChooser abrirArquivos = new JFileChooser();
		abrirArquivos.setFileSelectionMode(JFileChooser.FILES_ONLY); 
		abrirArquivos.setAcceptAllFileFilterUsed(false); 
		abrirArquivos.addChoosableFileFilter(new VideoFileFilter()); 
		
		int escolhaFeita = abrirArquivos.showOpenDialog(null);
		
		if (escolhaFeita == JFileChooser.APPROVE_OPTION) 
		{
			retorno = abrirArquivos.getSelectedFile();
		}
				
		return retorno;
	}
	
	/**
	 * Função utilizada para obter e carregar um arquivo qualquer do sistema do usuário.
	 * @return null - Caso ocorra um erro, ou a operação seja cancelada pelo usuário<p>
	 * File - Caso o usuário tenha escolhido um arquivo, e o programa carregou esse vídeo com sucesso.
	 */
	public static File getOutroArquivoSistemaUsuario()
	{
		File retorno = null; 
		
		JFileChooser abrirArquivos = new JFileChooser();
		abrirArquivos.setFileSelectionMode(JFileChooser.FILES_ONLY); 
		abrirArquivos.setAcceptAllFileFilterUsed(true);
				
		int escolhaFeita = abrirArquivos.showOpenDialog(null);
		
		if (escolhaFeita == JFileChooser.APPROVE_OPTION) 
		{
			retorno = abrirArquivos.getSelectedFile();
			
			System.out.print(retorno.getName()+"\n");
		}
				
		return retorno;
	}
}

/**
 * Classe criada para poder exibir e renderizer os componentes JPanel dentro de uma JList. (O que por padrão não é possível, pois ela chama o método .toString() de cada objeto antes de exibi-los)
 * @author Matheus Soares Martins
 *
 */
class PanelRenderer implements ListCellRenderer<BalaoMensagens>
{
	@Override
	public Component getListCellRendererComponent(@SuppressWarnings("rawtypes") JList list, BalaoMensagens value, int index, boolean isSelected,boolean cellHasFocus)
			
	{
		JPanel renderer = (JPanel) value;
		
		renderer.setBackground(isSelected ? Color.cyan : list.getBackground());
		
		return renderer;
	}
}