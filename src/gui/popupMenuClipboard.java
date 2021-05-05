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
 * Um popupMenu respons�vel por exibir as op��es de sele��o, c�pia e colagem de texto.
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
	popupMenuClipboard(JTextComponent objetoReferencia) //Foi usado JTextComponente pois ele � a base de todos os componentes de texto. Assim construindo um menu que funcione para ele, funciona para qualquer componente de texto. Ao inv�s de ter que ir, fazendo pra cada componente.
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
					StringSelection conteudoParaTransferencia; //Converte um valor do tipo String comum em um valor que possa ser transferido na �rea de transfer�ncia do sistema.
					Clipboard areaTransferenciaSistema; //Obt�m a �rea de transfer�ncia do sistema.(Onde os itens copiados v�o)
					
					if(objetoReferencia.getText().length() != 0)
					{
						areaTransferenciaSistema = Toolkit.getDefaultToolkit().getSystemClipboard();
						
						conteudoParaTransferencia = new StringSelection(objetoReferencia.getText());
						
						areaTransferenciaSistema.setContents(conteudoParaTransferencia,null); //Define conte�do da �rea de transfer�ncia como sendo esse texto que a gente copiou.
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
					conteudoParaTransferencia = areaTransferenciaSistema.getContents(null);//Pega o conte�do transfer�vel atual da �rea de transfer�ncia do sistema operacional e armazena na vari�vel.(Transferable � um tipo gen�rico que representa qualquer tipo de valor que pode ser passado para a �rea de transfer�ncia)
					
					if(conteudoParaTransferencia != null)//Quando a �rea de transfer�ncia est� sem nada (vazia), ela retorna null no clipboard.getContents(null), ent�o devemos verificar se t�m algum conte�do nela para que possa ser usado antes de qualquer coisa.
					{
						if(conteudoParaTransferencia.isDataFlavorSupported(DataFlavor.stringFlavor))//Ap�s verificar se t�m alg�m conte�do na �rea de transfer�ncia, v� se o conte�do presente atualmente l� � do tipo texto.
						{
							try
							{
								objetoReferencia.setText((String) conteudoParaTransferencia.getTransferData(DataFlavor.stringFlavor));//Define o conte�do do JTextComponent como sendo o texto presente na �rea de transfer�ncia do sistema.
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
	 * Serve para definir as fontes do JPopupMenu, para quando a tela aumentar o tamanho da fonte tamb�m aumentar.
	 * @param fonteItensMenu A fonte para serem alterados os itens do menu
	 */
	public void setFontesItensMenu(Font fonteItensMenu)
	{
		itemSelecionarTudo.setFont(fonteItensMenu);
		itemCopiar.setFont(fonteItensMenu);
		itemColar.setFont(fonteItensMenu);
	}
}
