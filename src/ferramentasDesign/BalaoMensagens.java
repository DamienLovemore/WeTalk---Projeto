package ferramentasDesign;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.text.BadLocationException;

import recursos.ApplicationInfo;

/**
 * Classe respons�vel por representar gr�ficamente as mensagens enviadas para o usu�rio nesse aplicativo.
 * @author Matheus Soares Martins
 *
 */
@SuppressWarnings("serial")
public class BalaoMensagens extends JPanel
{
	private int[] resolucaoTelaPrincipal; //Guarda a refer�ncia do tamanho da janela principal, para saber como organizar os seus componentes.
	private File arquivoMensagem; //Caso essa mensagem tenha algum arquivo, e n�o seja apenas uma mensagem de texto.
	private String conteudoMensagem; //Guarda o texto dessa mensagem para ser exibido aqui, caso essa seja uma mensagem de texto.
	private byte tipoBalaoMensagem; //Guarda a informa��o para dizer se essa mensagem � uma mensagem comum de texto, ou de arquivo. E se for arquivo, qual tipo de arquivo est� guardado l�.
	private LocalDateTime dataHoraMensagemCriada; //Guarda a data e o ho�rio em que a mensagem foi criada.
	
	private JLabel lblAutorMensagem;
	private JTextArea txtMensagemBalao;
	private JLabel lblImagemMensagem;
	private ImageIcon iconeImagem;
	private JPanel painelInferior;
	private JLabel lblDescricaoData;
	private JLabel lblDataEnvio;
	private JLabel lblNomeArquivo;
	private JLabel lblTamanhoArquivo;
	
	/**
	 * Cria um "bal�o" de mensagens respons�vel por exibir as mensagens enviadas para o usu�rio nesse aplicativo.<p>
	 * Elas podem tanto conter imagens, quano texto, e outros tipos de arquivos. E seus tamanhos se adaptam de acordo com o tamanho de seus componentes.
	 * @param comprimentoJanelaPrincipal Define o comprimento da tela principal que instanciou essa classe.<br>
	 * Isso serve para poder adaptar os componentes internos de acordo com o tamanho da tela.<p>
	 * @param alturaJanelaPrincipal  Define a altura da tela principal que instanciou essa classe.<br>
	 * Isso serve para poder adaptar os componentes internos de acordo com o tamanho da tela.<p>
	 * @param arquivoEnviar O arquivo (caso tenha) que essa mensagem cont�m.<br>
	 * E que ser� utilizado depois para download do mesmo.<p>
	 * @param textoMensagem O texto (caso tenha) que essa mensagem tem.<p>
	 * @param tipoMensagem Define o tipo de mensagem que � essa, e consequentemente o tipo de layout para a mensagem.<br>
	 * 1 - Mensagem de texto. <br>
	 * 2 - Mensagem de arquivo que cont�m um documento.<br>
	 * 3 - Mensagem de arquivo que cont�m uma imagem.<br>
	 * 4 - Mensagem de arquivo que cont�m um a�dio.<br>
	 * 5 - Mensagem de arquivo que cont�m um v�deo.<br>
	 * 6 - Mensagem de arquivo que conte�m outros (qualquer) formato de arquivo.<br>
	 * @see recursos.DocumentFileFilter
	 * @see recursos.ImageFileFilter
	 * @see recursos.AudioFileFilter
	 * @see recursos.VideoFileFilter
	 */
	public BalaoMensagens(int comprimentoJanelaPrincipal, int alturaJanelaPrincipal,File arquivoEnviar,String textoMensagem,byte tipoMensagem)
	{
		//Guarda as vari�veis passadas na constru��o desse componente, pois depois disso essas vari�veis deixam de existir.
		resolucaoTelaPrincipal = new int[2];//618X95 � o tamanho do balao quando a tela princial tem 650X512 de altura.
		resolucaoTelaPrincipal[0] = comprimentoJanelaPrincipal;
		resolucaoTelaPrincipal[1] = alturaJanelaPrincipal;
		
		arquivoMensagem = arquivoEnviar;
		
		conteudoMensagem = textoMensagem;
		
		tipoBalaoMensagem = tipoMensagem;
		
		this.setLayout(new BorderLayout());
		
		this.addComponentListener
		(
			new ComponentAdapter()
			{
				public void componentResized(ComponentEvent e)
				{
					//Calcula o tamanho atual do componente
					int[] resolucaoDesseComponente = new int[2];
					resolucaoDesseComponente[0] = Math.round((95.077f/100)*resolucaoTelaPrincipal[0]);
					resolucaoDesseComponente[1] = Math.round((18.5547f/100)*resolucaoTelaPrincipal[1]);
					
					//Define as fontes e seus tamanhos para cada componente.
					Font fonteComponente;
					int tamanhoFonte;
					
					if (tipoBalaoMensagem == 1)
					{
						fonteComponente = new Font("Dialog",Font.PLAIN,18);
						tamanhoFonte  = Math.round((18.95f/100)*resolucaoDesseComponente[1]);
						txtMensagemBalao.setFont(new Font(fonteComponente.getFontName(),fonteComponente.getStyle(),tamanhoFonte));
						
						int tamanhoComponente = Math.round((24.22f/100) * resolucaoTelaPrincipal[1]); //124 de 512
						Dimension tamanhoMinimo = new Dimension(resolucaoDesseComponente[0],tamanhoComponente); //Utilizado para definir o tamanho min�mo para o componente, para que depois possa calcular o tamanho correto.
						
						//Para o c�lculo do tamanho ideal do JTextArea funcionar ele deve ter o seu tamanho setado primeiro
						txtMensagemBalao.setSize(tamanhoMinimo);
						txtMensagemBalao.setPreferredSize(tamanhoMinimo);
						
						//C�lculo do tamanho adequado que o JTextArea deve ter para pode exibir todo o seu conte�do.
						Rectangle2D r = null;
						try
						{
							r = txtMensagemBalao.modelToView2D(txtMensagemBalao.getDocument().getLength());
							tamanhoComponente = (int) Math.round(r.getY() + r.getHeight());
							
							Dimension tamanhoIdeal = new Dimension(tamanhoMinimo.width,tamanhoComponente);
							txtMensagemBalao.setPreferredSize(tamanhoIdeal);
						}
						
						catch (BadLocationException e1)
						{
							//Caso n�o consiga calcular o tamanho ideal define o tamanho m�nimo no lugar
							txtMensagemBalao.setPreferredSize(tamanhoMinimo);
						}
					}
					
					else
					{
						//184 de altura para a janela de 512 pixels de altura deve ter a imagem.
						
						int comprimentoImagem = Math.round((100f/100) * resolucaoTelaPrincipal[0]);
						int alturaImagem = Math.round((35.94f/100) * resolucaoTelaPrincipal[1]);
						Dimension tamanhoImagem = new Dimension(comprimentoImagem,alturaImagem); //Armazena o tamanho ideal que a imagem deve conter
						
						Image imagemRedimensionada = iconeImagem.getImage(); //Obt�m a imagem em seu tamanho anterior
						imagemRedimensionada = imagemRedimensionada.getScaledInstance(tamanhoImagem.width,tamanhoImagem.height,Image.SCALE_SMOOTH); //Redimensiona a imagem para o tamanho especificado em pixels, utilizando um algoritmo de redimensinamento que prioriza qualidade da imagem, ao inv�s de rapidez de convers�o.
						
						iconeImagem = new ImageIcon(imagemRedimensionada); //Armazena na classe a nova imagem
						
						lblImagemMensagem.setIcon(iconeImagem); //Define a imagem do componente
					}
					
					fonteComponente = new Font("Bell MT",Font.PLAIN,20);
					tamanhoFonte  = Math.round((21.053f/100)*resolucaoDesseComponente[1]);
					
					lblAutorMensagem.setFont(new Font(fonteComponente.getFontName(),fonteComponente.getStyle(),tamanhoFonte));
					lblDescricaoData.setFont(new Font(fonteComponente.getFontName(),fonteComponente.getStyle(),tamanhoFonte));
					lblDataEnvio.setFont(new Font(fonteComponente.getFontName(),fonteComponente.getStyle(),tamanhoFonte));
										
					if (tipoBalaoMensagem!=1) //Todos os formato de mensagens que n�o s�o de texto tem esses componentes
					{
						lblNomeArquivo.setFont(new Font(fonteComponente.getFontName(),fonteComponente.getStyle(),tamanhoFonte));
						lblTamanhoArquivo.setFont(new Font(fonteComponente.getFontName(),fonteComponente.getStyle(),tamanhoFonte));
					}
				}
			}
		);
		
		if (tipoBalaoMensagem == 1) //Monta o design correto para uma bal�o de mensagem de texto
		{
			String nomeUsuario="";
			
			if (ApplicationInfo.applicationUserName.length()<30)
			{
				nomeUsuario = ApplicationInfo.applicationUserName+" diz:";
			}
			
			else
			{
				nomeUsuario = ApplicationInfo.applicationUserName.substring(0,27).toLowerCase(); //Pega os primeiros 27 caracteres da sequ�ncia e os transformam em caracteres min�sculos para ocuparem menos espa�o.
				nomeUsuario = nomeUsuario+"...";
			}
			
			lblAutorMensagem = new JLabel(nomeUsuario);
			lblAutorMensagem.setHorizontalAlignment(SwingConstants.LEFT);
			lblAutorMensagem.setBorder(getBorder());
			lblAutorMensagem.setOpaque(true);
			lblAutorMensagem.setBackground(new Color(165,186,216)); //Kansas City(secondary)
			lblAutorMensagem.setBorder(BorderFactory.createMatteBorder(0,0,2,0,Color.black));
			this.add(lblAutorMensagem,BorderLayout.NORTH);
			
			txtMensagemBalao = new JTextArea();
			txtMensagemBalao.setLineWrap(true); //Define a quebra de texto autom�tica quando ele n�o couber na mesma linha.
			txtMensagemBalao.setWrapStyleWord(true); //Define a quebre de texto para quebrar palavras inteiras, ao inv�s de caracteres.
			txtMensagemBalao.setText(conteudoMensagem); //Define o texto da mensagem.
			txtMensagemBalao.setEditable(false); //Desabilita a edi��o de texto dessa mensagem.
						
			this.add(txtMensagemBalao,BorderLayout.CENTER);
			
			painelInferior = new JPanel();
			painelInferior.setLayout(new GridLayout(1,2)); //Define que o painel inferior ter� uma linha, e duas colunas. E que cada componente adicionado ocopur� uma c�lula, e todos ter�o o mesmo tamanho.
			painelInferior.setBorder(BorderFactory.createMatteBorder(2,0,2,0,Color.black));
			painelInferior.setBackground(new Color(165,186,216));//Kansas City(secondary)
			
			lblDescricaoData = new JLabel("Data de envio: "); //Cria uma JLabel (componente para exibir textos read-only) com o texto que foi passado.
			lblDescricaoData.setHorizontalAlignment(SwingConstants.LEFT); //Define o alinhamento horizontal do texto presente no componente, para que ele seja alinhado a esquerda.
			painelInferior.add(lblDescricaoData);
			
			LocalDateTime dataHoraAtual = LocalDateTime.now(); //Pega a data-hora atual do sistema em que est� rodando, no fuso-hor�rio padr�o daquela m�quina.
			dataHoraMensagemCriada = dataHoraAtual; //Vari�veis locais ap�s o construtor terminar a execu��o elas perdem valor, ent�o armazena em vari�vel global para persist�ncia.
			
			DateTimeFormatter formatoDataHora = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
			
			lblDataEnvio = new JLabel(formatoDataHora.format(dataHoraMensagemCriada));
			lblDataEnvio.setHorizontalAlignment(SwingConstants.CENTER);
			painelInferior.add(lblDataEnvio);
			
			this.add(painelInferior,BorderLayout.SOUTH);
		}
		
		else //Monta o design correto para um bal�o de mensagem que carrega arquivos.(Imagem ou outro tipo de arquivo)
		{
			String nomeUsuario="";
			
			if (ApplicationInfo.applicationUserName.length()<30)
			{
				nomeUsuario = ApplicationInfo.applicationUserName+" diz:";
			}
			
			else
			{
				nomeUsuario = ApplicationInfo.applicationUserName.substring(0,27).toLowerCase(); //Pega os primeiros 27 caracteres da sequ�ncia e os transformam em caracteres min�sculos para ocuparem menos espa�o.
				nomeUsuario = nomeUsuario+"...";
			}
			
			lblAutorMensagem = new JLabel(nomeUsuario);
			lblAutorMensagem.setHorizontalAlignment(SwingConstants.LEFT);
			lblAutorMensagem.setBorder(getBorder());
			lblAutorMensagem.setOpaque(true);
			lblAutorMensagem.setBackground(new Color(165,186,216)); //Kansas City(secondary)
			lblAutorMensagem.setBorder(BorderFactory.createMatteBorder(0,0,2,0,Color.black));
			this.add(lblAutorMensagem,BorderLayout.NORTH);
			
			Image imagemFonte = null;
			
			if (tipoBalaoMensagem == 2) //Para documentos
			{
				imagemFonte = new ImageIcon(BalaoMensagens.this.getClass().getResource("/ferramentasDesign/imagens/DefaultIconDocument.png")).getImage();
			}
					
			else if (tipoBalaoMensagem == 3) //Para imagens
			{
				try //Tenta ler a imagem que est� contida nessa mensagem
				{
					imagemFonte = ImageIO.read(arquivoMensagem);
				}
				
				catch (IOException e1) //Caso n�o consiga carrega o �cone padr�o para a imagem.
				{
					imagemFonte = new ImageIcon(BalaoMensagens.this.getClass().getResource("/ferramentasDesign/imagens/DefaultIconImage.png")).getImage();
				}
			}
			
			else if (tipoBalaoMensagem == 4) //Para a�dios
			{
				imagemFonte = new ImageIcon(BalaoMensagens.this.getClass().getResource("/ferramentasDesign/imagens/DefaultIconAudio.png")).getImage();
			}
			
			else if (tipoBalaoMensagem == 5) //Para v�deos
			{
				imagemFonte = new ImageIcon(BalaoMensagens.this.getClass().getResource("/ferramentasDesign/imagens/DefaultIconVideo.png")).getImage();
			}
			
			else //Para outros tipos de arquivos
			{
				imagemFonte = new ImageIcon(BalaoMensagens.this.getClass().getResource("/ferramentasDesign/imagens/DefaultIconFiles.png")).getImage();
			}
			
			iconeImagem = new ImageIcon(imagemFonte);
			
			lblImagemMensagem = new JLabel(iconeImagem);
						
			this.add(lblImagemMensagem, BorderLayout.CENTER);
			
			painelInferior = new JPanel();
			painelInferior.setLayout(new GridLayout(2,2)); //Define que o painel inferior ter� uma linha, e duas colunas. E que cada componente adicionado ocopur� uma c�lula, e todos ter�o o mesmo tamanho.
			painelInferior.setBorder(BorderFactory.createMatteBorder(2,0,2,0,Color.black));
			painelInferior.setBackground(new Color(165,186,216));//Kansas City(secondary)
			
			String extensaoArquivo = arquivoMensagem.getName(); //Vari�vel usada para armazenar a extens�o do arquivo
			int indexUltimoPonto = extensaoArquivo.lastIndexOf("."); //Vari�vel utilizada pra verificar apatir de qual posi��o da String, � para ele come�ar a pegar as informa��es.
			extensaoArquivo = extensaoArquivo.substring(indexUltimoPonto+1); //Pega apenas a parte da String que se refere ao nome de sua extens�o. (Tudo que est� depois do ponto)
			extensaoArquivo = extensaoArquivo.toUpperCase(); //Converte o texto para mai�sculo
			
			String nomeArquivo = arquivoMensagem.getName();
			nomeArquivo = nomeArquivo.substring(0, indexUltimoPonto); //Pega somente o nome do arquivo. (Sem a extens�o no final)
			
			if (nomeArquivo.length() > 32) //Verifica que se o nome do arquivo � muito grande para caber na interface. Se sim, o formata adequadamente, ao inv�s de coloc�-lo e quebrar o layout do programa.
			{
				String nomeFormatado;
				
				nomeFormatado = nomeArquivo.substring(0,29); //Pega somente os primeiros 32 caraceres do nome do arquivo.
				nomeFormatado+="..."; //Preenche no final com 3 pontos para dar exatamente a quantidade limite de caracteres (35), e para sinalizar que o nome do arquivo continua, por�m foi grande demais para ser exibido.
				nomeFormatado = nomeFormatado.toLowerCase(); //Letras mai�sculas ocupam bem mais espa�o que letras min�sculas. Por isso quando n�o tem espa�o converte para letras min�sculas para exibir o texto adequadamente.
								
				nomeArquivo = nomeFormatado; //Define o valor correto para ser exibido na JLabel.
			}
			
			lblNomeArquivo = new JLabel(nomeArquivo); //Exibe somente o nome do arquivo sem a extens�o no final.
			lblNomeArquivo.setHorizontalAlignment(SwingConstants.CENTER);
			lblNomeArquivo.setBorder(BorderFactory.createMatteBorder(0,0,2,2,Color.black));
						
			painelInferior.add(lblNomeArquivo);
						
			String textoTamanhoArquivo;
			double  tamanhoArquivo = arquivoMensagem.length() / (1e6); //Obt�m o tamanho do arquivo em bytes, e em seguida o converte para MegaBytes.
			tamanhoArquivo = new BigDecimal(tamanhoArquivo).round(new MathContext(4)).doubleValue(); //Arredonda o valor para ter no m�ximo 3 casa decimais.(O valor de MathContext deve ser um a mais da quantidade de casas que voc� quer)
			textoTamanhoArquivo = String.valueOf(tamanhoArquivo)+" MB";
			
			if (tamanhoArquivo < 1.0) //Caso o arquivo seja menor que 1 MB calcula ele para KB no lugar de MB.
			{
				tamanhoArquivo = arquivoMensagem.length() / 1000; //Converte o valor de MB para KB
				tamanhoArquivo = new BigDecimal(tamanhoArquivo).round(new MathContext(4)).doubleValue();
				
				if(tamanhoArquivo < 1) //Para arquivos extremamente pequenos, ocorria dele converter para zero. (Tipo 49 bytes)
					tamanhoArquivo = 1;
				
				textoTamanhoArquivo = String.valueOf(tamanhoArquivo)+" KB";
			}
			
			else if (tamanhoArquivo >= 1000.00) //Caso o arquivo seja maior ou igual a 1000 MB calcula ele para GB no lugar de MB.
			{
				tamanhoArquivo = arquivoMensagem.length() / (1e9); //Converte o valor de MB para GB
				tamanhoArquivo = new BigDecimal(tamanhoArquivo).round(new MathContext(4)).doubleValue();
				
				if(tamanhoArquivo < 1)
					tamanhoArquivo = 1;
					
				textoTamanhoArquivo = String.valueOf(tamanhoArquivo)+" GB";
			}
			
			lblTamanhoArquivo = new JLabel(extensaoArquivo+" "+"("+textoTamanhoArquivo+")");
			lblTamanhoArquivo.setHorizontalAlignment(SwingConstants.CENTER);
			lblTamanhoArquivo.setBorder(BorderFactory.createMatteBorder(0,0,2,2,Color.black));
			painelInferior.add(lblTamanhoArquivo);
			
			lblDescricaoData = new JLabel("Data de envio: "); //Cria uma JLabel (componente para exibir textos read-only) com o texto que foi passado.
			lblDescricaoData.setHorizontalAlignment(SwingConstants.LEFT); //Define o alinhamento horizontal do texto presente no componente, para que ele seja alinhado a esquerda.
			painelInferior.add(lblDescricaoData);
			
			LocalDateTime dataHoraAtual = LocalDateTime.now(); //Pega a data-hora atual do sistema em que est� rodando, no fuso-hor�rio padr�o daquela m�quina.
			dataHoraMensagemCriada = dataHoraAtual; //Vari�veis locais ap�s o construtor terminar a execu��o elas perdem valor, ent�o armazena em vari�vel global para persist�ncia.
			
			DateTimeFormatter formatoDataHora = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
			
			lblDataEnvio = new JLabel(formatoDataHora.format(dataHoraMensagemCriada));
			lblDataEnvio.setHorizontalAlignment(SwingConstants.CENTER);
			painelInferior.add(lblDataEnvio);
			
			this.add(painelInferior,BorderLayout.SOUTH);
		}
		
		//Define o tamanho atual do componente
		int[] resolucaoDesseComponente = new int[2];
		resolucaoDesseComponente[0] = Math.round((95.077f/100)*resolucaoTelaPrincipal[0]);
		resolucaoDesseComponente[1] = Math.round((18.5547f/100)*resolucaoTelaPrincipal[1]);
		this.setSize(resolucaoDesseComponente[0],resolucaoDesseComponente[1]); //Define o tamanho que cada Balao de mensagem deve ter.
		
		//Define a cor de fundo, e em seguida coloca uma borda em volta da mensagem.
		this.setBackground(new Color(165,186,216));//Kansas City(secondary) //Define a cor de fundo do componente.
		this.setBorder(BorderFactory.createLineBorder(Color.white,2)); //Define a borda do componente
	}
	
	/**
	 * Esse m�todo � utilizado para que possa ser obtido o texto contido em uma mensagem.
	 * @return String - O texto contido nessa mensagem caso ela tenha.<p>
	 * null - Caso essa mensagem n�o tenha nenhum texto nela.
	 */
	public String getTextoMensagem()
	{
		String retorno;
		
		retorno = this.conteudoMensagem;
		
		return retorno;
	}
	
	/**
	 * Esse m�todo � utilizado para que possa ser obtido o arquivo contido em uma mensagem.
	 * @return File - O arquivo contido nessa mensagem caso ela tenha.<p>
	 * null - Caso essa mensagem n�o tenha nenhum arquivo nela.
	 */
	public File getArquivoMensagem()
	{
		File retorno;
		
		retorno = this.arquivoMensagem;
		
		return retorno;
	}
	
	/**
	 * Conforme a tela principal � redimensionada, tem a necessidade de atualizar o tamanho atual dela.<p>
	 * Para que a classe possa calcular corretamente o tamanho de seus componentes de acordo com a tela.<p>
	 * Para isso esse m�todo foi criado para atualizar o tamanho atual dessa tela dentro da classe.
	 * @param resolucaoTela - Um vetor do tipo inteiro de duas posi��es, em que o index 0 representa o comprimento da tela em pixels.<br>
	 * E o index 1, representa a altura atual da tela em pixels.
	 */
	public void setResolucaoAtualTelaPrincipal(int[] resolucaoTela)
	{
		this.resolucaoTelaPrincipal = resolucaoTela.clone(); //Copia o conte�do do vetor (pois ele � um objeto), ao inv�s de simples usar = e passar a refer�ncia ao objeto.
		this.dispatchEvent(new ComponentEvent(this,ComponentEvent.COMPONENT_RESIZED)); //Automaticamente chama a fun��o de redimensionamento
	}
		
	/**
	 * Obt�m o tipo de mensagem que esse Balao representa.<p>
	 * (Se � uma simples mensagem de texto, ou se tem algum arquivo nela.<p> E se tiver que tipo de arquivo est� guardado nela)
	 * @return 1 - Esse � um balao que cont�m apenas texto, e nenhum tipo de arquivo. <p>
	 * 2 - Esse � um balao que cont�m um tipo de imagem nele. (Qualquer um dos tipo de imagens existentes)
	 */
	public byte getTipoMensagem()
	{
		byte retorno;
		
		retorno = this.tipoBalaoMensagem;
		
		return retorno;
	}
}
