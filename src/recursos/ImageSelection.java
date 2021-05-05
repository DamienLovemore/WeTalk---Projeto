package recursos;

import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * Um Transferable que implementa a capacidade requerida para transferir uma Image (java.awt.Image).<p>
 * Esse Transferable apropriadamente dá suporte ao DataFlavor.imageFlavor.<br>
 * Nenhum outro tipo de DataFlavors são suportados.
 *
 * @author Matheus Soares Martins
 *
 */
public class ImageSelection implements Transferable
{
	private Image imagemTransferencia;
	
	/** Cria um Transferable capaz de transferir a imagem específicada.
	 * 
	 * @param imagem - A imagem a ser transferida para a área de transferência (Clipboard).
	 */
	public ImageSelection(Image imagem)
	{
		this.imagemTransferencia = imagem;
	}
	
	
	public DataFlavor[] getTransferDataFlavors()
	{
		DataFlavor[] retorno;
		
		retorno = new DataFlavor[] {DataFlavor.imageFlavor};
		
		return retorno;
	}
		
	public boolean isDataFlavorSupported(DataFlavor flavor)
	{
		boolean retorno;
		
		retorno = flavor.equals(DataFlavor.imageFlavor);
		
		return retorno;
	}
	
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException
	{
		Image retorno;
		
		if (flavor.equals(DataFlavor.imageFlavor) == false)
		{
			throw new UnsupportedFlavorException(flavor);
		}
		
		retorno = this.imagemTransferencia;
		
		return retorno;
	}
}
