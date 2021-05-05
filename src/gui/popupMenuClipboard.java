package gui;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JPopupMenu;
import javax.swing.text.JTextComponent;
import javax.swing.JMenuItem;

/**
 * Um popupMenu responsável por exibir as opções de seleção, cópia e colagem de texto.
 * @author Matheus Soares Martins
 *
 */
@SuppressWarnings("serial")
public class popupMenuClipboard extends JPopupMenu
{
	private JMenuItem itemSelecionarTudo;
	private JMenuItem itemCopiar;
	private JMenuItem itemColar;
	
	/**
	 * Cria um popupMenu (Selecionar tudo, Copiar, Colar) de texto para o componente de texto passado.
	 * @param objetoReferencia O componente de texto a receber o popupMenu de texto.
	 */
	popupMenuClipboard(JTextComponent objetoReferencia) //Foi usado JTextComponente pois ele é a base de todos os componentes de texto. Assim construindo um menu que funcione para ele, funciona para qualquer componente de texto. Ao invés de ter que ir, fazendo pra cada componente.
	{
		itemSelecionarTudo = new  JMenuItem("Selecionar Tudo");
		itemSelecionarTudo.addActionListener
		(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent eventoSelecionar)
					{
						objetoReferencia.selectAll(); //Seleciona todo o texto desse componente de texto.
					}
				}
		);
		this.add(itemSelecionarTudo);
		
		
		itemCopiar = new JMenuItem("Copiar");
		itemCopiar.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent eventoCopiar)
				{
					StringSelection conteudoParaTransferencia; //Converte um valor do tipo String comum em um valor que possa ser transferido na área de transferência do sistema.
					Clipboard areaTransferenciaSistema; //Obtém a área de transferência do sistema.(Onde os itens copiados vão)
					
					if(objetoReferencia.getText().length() != 0)
					{
						areaTransferenciaSistema = Toolkit.getDefaultToolkit().getSystemClipboard();
						
						conteudoParaTransferencia = new StringSelection(objetoReferencia.getText());
						
						areaTransferenciaSistema.setContents(conteudoParaTransferencia,null); //Define conteúdo da área de transferência como sendo esse texto que a gente copiou.
					}
				}
			}
		);
		this.add(itemCopiar);
		
		itemColar = new JMenuItem("Colar");
		itemColar.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent eventoColar)
				{
					Clipboard areaTransferenciaSistema;
					Transferable conteudoParaTransferencia;
										
					areaTransferenciaSistema = Toolkit.getDefaultToolkit().getSystemClipboard();
					conteudoParaTransferencia = areaTransferenciaSistema.getContents(null);//Pega o conteúdo transferível atual da área de transferência do sistema operacional e armazena na variável.(Transferable é um tipo genérico que representa qualquer tipo de valor que pode ser passado para a área de transferência)
					
					if(conteudoParaTransferencia != null)//Quando a área de transferência está sem nada (vazia), ela retorna null no clipboard.getContents(null), então devemos verificar se têm algum conteúdo nela para que possa ser usado antes de qualquer coisa.
					{
						if(conteudoParaTransferencia.isDataFlavorSupported(DataFlavor.stringFlavor))//Após verificar se têm algúm conteúdo na área de transferência, vê se o conteúdo presente atualmente lá é do tipo texto.
						{
							try
							{
								objetoReferencia.setText((String) conteudoParaTransferencia.getTransferData(DataFlavor.stringFlavor));//Define o conteúdo do JTextComponent como sendo o texto presente na área de transferência do sistema.
							}
							
							catch (UnsupportedFlavorException e1)
							{
								
							}
							
							catch (IOException e1)
							{
								
							}
						}
					}
				}
			}
		);
		this.add(itemColar);
		
	}
	
	/**
	 * Serve para definir as fontes do JPopupMenu, para quando a tela aumentar o tamanho da fonte também aumentar.
	 * @param fonteItensMenu A fonte para serem alterados os itens do menu
	 */
	public void setFontesItensMenu(Font fonteItensMenu)
	{
		itemSelecionarTudo.setFont(fonteItensMenu);
		itemCopiar.setFont(fonteItensMenu);
		itemColar.setFont(fonteItensMenu);
	}
}
