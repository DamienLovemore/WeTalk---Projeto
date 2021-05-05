package gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import comunicacao.ConeccaoCliente;
import comunicacao.DadosTransferencia;
import ferramentasDesign.BalaoMensagens;

/**
 * Classe responsável por apresentar as opções de envios de arquivos, e realizar as suas respectivas ações.<br>
 * Quandoo botão de envio de anexo/arquivo da janela principal for clicado.
 * @author Matheus Soares Martins
 *
 */
@SuppressWarnings("serial")
public class popupMenuAnexoEnvio extends JPopupMenu
{
	private JMenuItem itemEnvioDocumento;
	private JMenuItem itemEnvioImagem;
	private JMenuItem itemEnvioAudio;
	private JMenuItem itemEnvioVideo;
	private JMenuItem itemEnvioOutros;
	
	/**
	 * O popupMenu a ser exibido quando for clicado o botão de anexo.<br>
	 * Ele contém todos os tipos de arquivos possíveis de serem enviados.
	 * @param resolucaoTelaPrincipal Um vetor do tipo inteiro de duas posições, indicando o comprimento e a altura atual da janela principal do programa.
	 * @param mensagensAplicacao A referência a lista que armazena todas as mensagens atuais que estão carregadas na janela do programa.
	 * @param lstListaMensagem  O JList aonde serão apresentados os novos itens enviados.
	 */
	popupMenuAnexoEnvio(int[] resolucaoTelaPrincipal,List<BalaoMensagens> mensagensAplicacao,JList<BalaoMensagens> lstListaMensagem,GUI aplicacaoGrafica)
	{
		itemEnvioDocumento = new  JMenuItem("Documento");
		itemEnvioDocumento.addActionListener
		(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent eventoSelecionar)
					{
						if ((GUI.alvoMensagem == null) || (GUI.alvoMensagem.length()==0))
						{
							JOptionPane.showMessageDialog(null,"Defina o destinatário da mensagem através do menu configurações!","Alvo da mensagem não definido",JOptionPane.ERROR_MESSAGE);
							return;
						}
						
						File documentoEscolhido;
						double tamanhoArquivo; //Usado para calcular e limitar o tamanho do envio de arquivos
						
						documentoEscolhido = GUI.getDocumentoSistemaUsuario();
						
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
							JOptionPane.showMessageDialog(null,"O limite de envio de arquivos é de no máximo 2GB!","Tamanho de arquivos ultrapassado",JOptionPane.ERROR_MESSAGE);
							documentoEscolhido = null; //Invalida a operação de envio
						}
						
						if (documentoEscolhido!=null)
						{
							BalaoMensagens novaMensagemImagem;
							
							novaMensagemImagem = new BalaoMensagens(resolucaoTelaPrincipal[0],resolucaoTelaPrincipal[1],documentoEscolhido,null,(byte) 2);
							
							mensagensAplicacao.add(novaMensagemImagem); //Adiciona a nova mensagem criada a lista de mensagens
							
							//Converte a lista de mensagens para um vetor.
							BalaoMensagens[] vetorMensagensAplicacao = new BalaoMensagens[mensagensAplicacao.size()]; 
							vetorMensagensAplicacao = mensagensAplicacao.toArray(vetorMensagensAplicacao); 
							
							lstListaMensagem.setListData(vetorMensagensAplicacao); //Atualiza a lista de conteúdos daquele JList.
														
							DadosTransferencia informacoesPassar = new DadosTransferencia(aplicacaoGrafica.listaConeccoes.get(1).getEnderecoIPCliente(),"Envio de Documento",documentoEscolhido,(byte)2);
							aplicacaoGrafica.listaConeccoes.get(0).setDadosParaEnvio(informacoesPassar);
						}
					}
				}
		);
		this.add(itemEnvioDocumento);
		
		itemEnvioImagem = new  JMenuItem("Imagem");
		itemEnvioImagem.addActionListener
		(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent eventoSelecionar)
					{
						if ((GUI.alvoMensagem == null) || (GUI.alvoMensagem.length()==0))
						{
							JOptionPane.showMessageDialog(null,"Defina o destinatário da mensagem através do menu configurações!","Alvo da mensagem não definido",JOptionPane.ERROR_MESSAGE);
							return;
						}
						
						File imagemEscolhida;
						double tamanhoArquivo; 
						
						imagemEscolhida = GUI.getImagemSistemaUsuario();
						
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
							JOptionPane.showMessageDialog(null,"O limite de envio de arquivos é de no máximo 2GB!","Tamanho de arquivos ultrapassado",JOptionPane.ERROR_MESSAGE);
							imagemEscolhida = null;
						}
											
						if (imagemEscolhida!=null)
						{
							BalaoMensagens novaMensagemImagem;
							
							novaMensagemImagem = new BalaoMensagens(resolucaoTelaPrincipal[0],resolucaoTelaPrincipal[1],imagemEscolhida,null,(byte) 3);
							
							mensagensAplicacao.add(novaMensagemImagem); //Adiciona a nova mensagem criada a lista de mensagens
							
							//Converte a lista de mensagens para um vetor.
							BalaoMensagens[] vetorMensagensAplicacao = new BalaoMensagens[mensagensAplicacao.size()]; 
							vetorMensagensAplicacao = mensagensAplicacao.toArray(vetorMensagensAplicacao); 
							
							lstListaMensagem.setListData(vetorMensagensAplicacao); //Atualiza a lista de conteúdos daquele JList.
							
							DadosTransferencia informacoesPassar = new DadosTransferencia(aplicacaoGrafica.listaConeccoes.get(1).getEnderecoIPCliente(),"Envio de Imagem",imagemEscolhida,(byte)2);
							aplicacaoGrafica.listaConeccoes.get(0).setDadosParaEnvio(informacoesPassar);
						}
					}
				}
		);
		this.add(itemEnvioImagem);
		
		itemEnvioAudio = new  JMenuItem("Audio");
		itemEnvioAudio.addActionListener
		(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent eventoSelecionar)
					{
						if ((GUI.alvoMensagem == null) || (GUI.alvoMensagem.length()==0))
						{
							JOptionPane.showMessageDialog(null,"Defina o destinatário da mensagem através do menu configurações!","Alvo da mensagem não definido",JOptionPane.ERROR_MESSAGE);
							return;
						}
						
						File audioEscolhido;
						double tamanhoArquivo; 
						
						audioEscolhido = GUI.getAudioSistemaUsuario();
						
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
							JOptionPane.showMessageDialog(null,"O limite de envio de arquivos é de no máximo 2GB!","Tamanho de arquivos ultrapassado",JOptionPane.ERROR_MESSAGE);
							audioEscolhido = null;
						}
						
						if (audioEscolhido!=null)
						{
							BalaoMensagens novaMensagemImagem;
							
							novaMensagemImagem = new BalaoMensagens(resolucaoTelaPrincipal[0],resolucaoTelaPrincipal[1],audioEscolhido,null,(byte) 4);
							
							mensagensAplicacao.add(novaMensagemImagem); //Adiciona a nova mensagem criada a lista de mensagens
							
							//Converte a lista de mensagens para um vetor.
							BalaoMensagens[] vetorMensagensAplicacao = new BalaoMensagens[mensagensAplicacao.size()]; 
							vetorMensagensAplicacao = mensagensAplicacao.toArray(vetorMensagensAplicacao); 
							
							lstListaMensagem.setListData(vetorMensagensAplicacao); //Atualiza a lista de conteúdos daquele JList.
							
							DadosTransferencia informacoesPassar = new DadosTransferencia(aplicacaoGrafica.listaConeccoes.get(1).getEnderecoIPCliente(),"Envio de Audio",audioEscolhido,(byte)2);
							aplicacaoGrafica.listaConeccoes.get(0).setDadosParaEnvio(informacoesPassar);
						}
					}
				}
		);
		this.add(itemEnvioAudio);
		
		itemEnvioVideo = new  JMenuItem("Video");
		itemEnvioVideo.addActionListener
		(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent eventoSelecionar)
					{
						if ((GUI.alvoMensagem == null) || (GUI.alvoMensagem.length()==0))
						{
							JOptionPane.showMessageDialog(null,"Defina o destinatário da mensagem através do menu configurações!","Alvo da mensagem não definido",JOptionPane.ERROR_MESSAGE);
							return;
						}
						
						File videoEscolhido;
						double tamanhoArquivo; 
						
						videoEscolhido = GUI.getVideoSistemaUsuario();
						
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
							JOptionPane.showMessageDialog(null,"O limite de envio de arquivos é de no máximo 2GB!","Tamanho de arquivos ultrapassado",JOptionPane.ERROR_MESSAGE);
							videoEscolhido = null;
						}
						
						if (videoEscolhido!=null)
						{
							BalaoMensagens novaMensagemImagem;
							
							novaMensagemImagem = new BalaoMensagens(resolucaoTelaPrincipal[0],resolucaoTelaPrincipal[1],videoEscolhido,null,(byte) 5);
							
							mensagensAplicacao.add(novaMensagemImagem); //Adiciona a nova mensagem criada a lista de mensagens
							
							//Converte a lista de mensagens para um vetor.
							BalaoMensagens[] vetorMensagensAplicacao = new BalaoMensagens[mensagensAplicacao.size()]; 
							vetorMensagensAplicacao = mensagensAplicacao.toArray(vetorMensagensAplicacao); 
							
							lstListaMensagem.setListData(vetorMensagensAplicacao); //Atualiza a lista de conteúdos daquele JList.
							
							DadosTransferencia informacoesPassar = new DadosTransferencia(aplicacaoGrafica.listaConeccoes.get(1).getEnderecoIPCliente(),"Envio de Video",videoEscolhido,(byte)2);
							aplicacaoGrafica.listaConeccoes.get(0).setDadosParaEnvio(informacoesPassar);
						}
					}
				}
		);
		this.add(itemEnvioVideo);
		
		itemEnvioOutros = new  JMenuItem("Outros");
		itemEnvioOutros.addActionListener
		(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent eventoSelecionar)
					{
						if ((GUI.alvoMensagem == null) || (GUI.alvoMensagem.length()==0))
						{
							JOptionPane.showMessageDialog(null,"Defina o destinatário da mensagem através do menu configurações!","Alvo da mensagem não definido",JOptionPane.ERROR_MESSAGE);
							return;
						}
						
						File arquivoEscolhido;
						double tamanhoArquivo; 
						
						arquivoEscolhido = GUI.getOutroArquivoSistemaUsuario();
						
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
							JOptionPane.showMessageDialog(null,"O limite de envio de arquivos é de no máximo 2GB!","Tamanho de arquivos ultrapassado",JOptionPane.ERROR_MESSAGE);
							arquivoEscolhido = null;
						}
						
						if (arquivoEscolhido!=null)
						{
							BalaoMensagens novaMensagemImagem;
							
							novaMensagemImagem = new BalaoMensagens(resolucaoTelaPrincipal[0],resolucaoTelaPrincipal[1],arquivoEscolhido,null,(byte) 6);
							
							mensagensAplicacao.add(novaMensagemImagem); //Adiciona a nova mensagem criada a lista de mensagens
							
							//Converte a lista de mensagens para um vetor.
							BalaoMensagens[] vetorMensagensAplicacao = new BalaoMensagens[mensagensAplicacao.size()]; 
							vetorMensagensAplicacao = mensagensAplicacao.toArray(vetorMensagensAplicacao); 
							
							lstListaMensagem.setListData(vetorMensagensAplicacao); //Atualiza a lista de conteúdos daquele JList.
							
							DadosTransferencia informacoesPassar = new DadosTransferencia(aplicacaoGrafica.listaConeccoes.get(1).getEnderecoIPCliente(),"Envio de um arquivo qualquer (Outros)",arquivoEscolhido,(byte)2);
							aplicacaoGrafica.listaConeccoes.get(0).setDadosParaEnvio(informacoesPassar);
						}
					}
				}
		);
		this.add(itemEnvioOutros);
				
	}
	
	/**
	 * Serve para definir as fontes do JPopupMenu, para quando a tela aumentar o tamanho da fonte também aumentar.
	 * @param fonteItensMenu A fonte para serem alterados os itens do menu
	 */
	public void setFontesItensMenu(Font fonteItensMenu)
	{
		itemEnvioDocumento.setFont(fonteItensMenu);
		itemEnvioImagem.setFont(fonteItensMenu);
		itemEnvioAudio.setFont(fonteItensMenu);
		itemEnvioVideo.setFont(fonteItensMenu);
		itemEnvioOutros.setFont(fonteItensMenu);
	}
}
