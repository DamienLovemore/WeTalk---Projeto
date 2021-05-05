package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import recursos.ApplicationInfo;

/**
 * Classe responsável por exibir informações gerais sobre o programa, e apresentar uma descrição sobre o seu propósito de criação.
 * @author Matheus Soares Martins
 *
 */
@SuppressWarnings("serial")
public class TelaSobre extends JFrame
{
	private int[] resolucaoTelaInicial;
	
	private Container conteudoJanela;
	private JPanel painelSuperior;
	private JLabel lblTituloAplicacao;
	private JTextArea txtTextoAplicacao;
	private JPanel painelInferior;
	private JLabel lblVersaoAtual;
	private JButton btnEntendido;
	
	private ImageIcon iconeLogo;
	
	/**
	 * Cria a janela de descrição/apresentação do programa.
	 * @param comprimentoJanelaAtual Define o comprimento da tela principal que instanciou essa classe.<br>
	 * Isso serve para poder adaptar os componentes internos de acordo com o tamanho da tela.<p>
	 * @param alturaJanelaAtual Define a altura da tela principal que instanciou essa classe.<br>
	 * Isso serve para poder adaptar os componentes internos de acordo com o tamanho da tela.<p>
	 * @param janelaPrincipal A janela principal do programa que criou essa tela.<br>
	 * Para poder posicionar a janela TelaSobre de acordo com a posição da janela principal.
	 */
	TelaSobre(int comprimentoJanelaAtual, int alturaJanelaAtual, Component janelaPrincipal) //Passa os parâmetros do tamanho atual da janela principal que essa janela estará contida.
	{
		//Define o título da janela.
		super("Sobre");
		
		//Usado para calcular a reoslução inicial que a tela deve apresentar, e o tamanho mínimo dessa janela para que o layout funcione adequadamente.
		resolucaoTelaInicial = new int[2];
		resolucaoTelaInicial[0] = Math.round((65.54f/100)*comprimentoJanelaAtual); //400 -> 650. Ou, 61.54% do comprimento atual.
		resolucaoTelaInicial[1] = Math.round((60.9375f/100)*alturaJanelaAtual); //312 -> 512. Ou, 60.9375% da altura atual.
		
		//Obtém o painel de conteúdos dessa janela, para que possamos alterá-lo e definir o seu layout.
		conteudoJanela = this.getContentPane();
		conteudoJanela.setLayout(new BorderLayout());
		
		this.addWindowListener //Utilizado para ouvir e disparar eventos de janela.
		(
			new WindowAdapter()
			{
				public void windowClosing(WindowEvent eventoFecharUsuario) //Quando a janela é fechada através do usuário ter clicado no botão de fechar.
				{
					GUI.setTelaSobreLivre(); //Define que a tela sobre foi fechada e que ela pode ser criada novamente.			
				}
				
				public void windowClosed(WindowEvent eventoFecharComando) //Quando a janela é fechada através de comandos de programação, como o dispose().
				{
					GUI.setTelaSobreLivre();
				}
			}
		);
		
		
		painelSuperior = new JPanel(); //Cria um painel de conteúdo secundário, para gerenciar parte da interface gráfica da Janela. (É mais fácil dividir a interface em blocos, do que tentar acertar o seu layout duma só vez)
		painelSuperior.setLayout(new BorderLayout()); //Define o gerenciador de layout padrão para o container de componentes. (Gerenciadores de layout definem o tamanho dos componentes, bem como o seu alinhamento na interface)
		
		lblTituloAplicacao = new JLabel("WeTalk - "+ApplicationInfo.applicationBuildVersion);
		int tamanhoFonte = Math.round((7.70f/100)*resolucaoTelaInicial[1]); //Calcula o tamanho adequado para fonte de acordo com o tamanho da janela.(Porporção: 24 de uma altura de 312)
		lblTituloAplicacao.setFont(new Font("Bell MT",Font.PLAIN,tamanhoFonte)); //Define a fonte com o nome, estilo e tamanho definios para esse componente.
		lblTituloAplicacao.setForeground(new Color(142,183,255));//Colorado //Define a cor da fonte do texto.
		lblTituloAplicacao.setBackground(new Color(11,77,146));//San Jose //Define a cor de fundo do componente.
		lblTituloAplicacao.setOpaque(true); //Desabilita a transparência padrão de JLabel para que o fundo possa ser visível.
		lblTituloAplicacao.setHorizontalAlignment(SwingConstants.CENTER);
		lblTituloAplicacao.setBorder(BorderFactory.createMatteBorder(0,0,2,0,new Color(0,0,0))); //Cria uma borda apenas na parte inferior na cor preta.
				
		try //Ele só define a logo da janela, caso ele tenha encontrado a imagem. Para evitar de quebrar a aplicação caso não tenha encontrado aquela imagem.
		{
			int[] resolucaoIconeLabel = new int[2];
			
			Image imagemFonte = new ImageIcon(this.getClass().getResource("/gui/imagens/Logo-WeTalk.png")).getImage(); //Carrega a logo dentro dos recursos disponíveis no programa.
			
			resolucaoIconeLabel[0] = Math.round((10f/100)*comprimentoJanelaAtual);
			resolucaoIconeLabel[1] = Math.round((8f/100)*alturaJanelaAtual);
			Image imagemRedimensionada = imagemFonte.getScaledInstance(resolucaoIconeLabel[0],resolucaoIconeLabel[1],Image.SCALE_SMOOTH); //Redimensiona a imagem usando técnicas que preserva a qualidade da imagem, para que ela fique com o tamanho informado.
			
			iconeLogo = new ImageIcon(imagemRedimensionada);
			lblTituloAplicacao.setIcon(iconeLogo); //Define a logo da JLabel que é usada para o título.
			
			this.setIconImage(imagemFonte);
		}
		
		catch(NullPointerException eventoFalhaCarregaLogo)
		{
			//Caso não tenha achado a logo para o programa ele então não faz nada em relação a definir logos.
		}
		painelSuperior.add(lblTituloAplicacao,BorderLayout.NORTH);
				
		txtTextoAplicacao = new JTextArea();
		tamanhoFonte = Math.round((7.051f/100)*resolucaoTelaInicial[1]); //Calcula o tamanho adequado para fonte de acordo com o tamanho da janela.(Porporção: 22 de uma altura de 312)
		txtTextoAplicacao.setFont(new Font("Arial",Font.PLAIN,tamanhoFonte));
		txtTextoAplicacao.setForeground(Color.gray);
		txtTextoAplicacao.setBackground(new Color(28,41,91));//Los Angeles 
		txtTextoAplicacao.setLineWrap(true); //Define a quebra automática de texto caso o conteúdo não caiba na linha.
		txtTextoAplicacao.setWrapStyleWord(true); //Define a quebra automática de texto ocorre por palavras, ao invés de caracteres.
		txtTextoAplicacao.setText(ApplicationInfo.applicationDescription); //Define o texto desse componente de texto para ser exibido.
		txtTextoAplicacao.setCaretPosition(0); //Faz com que seja apresentado o texto em seu começo. (Rola a barra de rolagem do componente para o começo)
		txtTextoAplicacao.setEditable(false); //Desabilita a edição do conteúdo desse componente de texto.
				
		JScrollPane rolamentoTextoDescricao = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		rolamentoTextoDescricao.getViewport().setView(txtTextoAplicacao);
		
		painelSuperior.add(rolamentoTextoDescricao,BorderLayout.CENTER);
		
		conteudoJanela.add(painelSuperior,BorderLayout.CENTER);
		
		painelInferior = new JPanel();
		painelInferior.setLayout(new GridLayout(2,1)); //Cria um layout de grid com uma coluna e duas linhas. Onde cada elemento tem exatamente o mesmo tamanho.
		
		lblVersaoAtual = new JLabel("Versão atual: "+ApplicationInfo.applicationBuildVersion);
		tamanhoFonte = Math.round((7.051f/100)*resolucaoTelaInicial[1]); //Calcula o tamanho adequado para fonte de acordo com o tamanho da janela.(Porporção: 18 de uma altura de 312)
		lblVersaoAtual.setFont(new Font("Bell MT",Font.PLAIN,tamanhoFonte));
		lblVersaoAtual.setForeground(new Color(142,183,255)); //Colorado 
		lblVersaoAtual.setBackground(new Color(11,77,146)); //San Jose
		lblVersaoAtual.setOpaque(true);
		lblVersaoAtual.setBorder(BorderFactory.createMatteBorder(2,0,2,0,new Color(0,0,0)));
		painelInferior.add(lblVersaoAtual);
		
		btnEntendido = new JButton("Entendido");
		btnEntendido.setFont(new Font("Rockwell",Font.PLAIN,tamanhoFonte));
		btnEntendido.setHorizontalAlignment(SwingConstants.CENTER);
		btnEntendido.setBackground(new Color(14,26,41)); //Philadelphia
		btnEntendido.setForeground(Color.lightGray);
		btnEntendido.addActionListener
		(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent eventoExecutar)
					{
						TelaSobre.this.dispose();
					}
				}
		);
		painelInferior.add(btnEntendido);
		
		conteudoJanela.add(painelInferior,BorderLayout.SOUTH);
		
		this.getRootPane().setDefaultButton(btnEntendido); //Define o botão padrão para essa janela. Ou seja, quando o ENTER for pressinado essa janela fechará.
		
		this.setSize(resolucaoTelaInicial[0],resolucaoTelaInicial[1]); //Define o tamanho inicial que a tela  deve ter.
		this.setResizable(false);//Desabilita o redimensionamento da janela;
		
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(janelaPrincipal);
		this.setVisible(true); //Torna a janela visível, pois por padrão elas são invisíveis.
	}
}
