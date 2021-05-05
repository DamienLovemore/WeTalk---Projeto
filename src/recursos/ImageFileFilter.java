package recursos;

import java.io.File;

import javax.swing.filechooser.FileFilter;


/**
 * Utilizada para definir um filtro que aceita apenas arquivos que sejam um tipo de imagem.
 * @author Matheus Soares Martins
 *
 */
public class ImageFileFilter extends FileFilter //O import correto aqui � javax.swing.filechooser.FileFilter e n�o java.io.FileFilter;
{
	//Define os formatos de arquivos aceitos
	private final String[] okFileExtensions={"jpg","jpeg","jpe","jfif","png","bmp","dib","rle","gif","ico","tif","tiff","webp","tga"};
	
	public boolean accept(File file)
	{
		//Isso apenas define as pastas como vis�veis. N�o faz com que diret�rios e pastas possam ser reconhecidos como arquivos v�lidos. Para isso  tem o JFileChoose.setFileSelectionMode(***);
		if(file.isDirectory()) //Faz com que seja exibido tamb�m pastas, e diret�rios al�m dos arquivos de imagem especificados acima. (Caso contr�rio n�o aparecer� nenhuma pasta, ou diret�rio. S� ser� poss�vel pegar arquivos da pasta raiz)
		{
			return true;
		}
		
		for (String extension : okFileExtensions) //Cria uma vari�vel local extension e percorre cada valor de okFileExtensions.
		{
			if(file.getName().toLowerCase().endsWith(extension)) //Pega o nome inteiro do arquivo incluido com a extens�o (exemplo: mar.jpg), e converte isso para tudo min�sculo. Ap�s isso v� se o nome do arquivo termina com uma das extens�es v�lidas.
			{
				return true;
			}
		}
		
		return false;
	}
		
	public String getDescription() //Pelo fato dele estar extendendo, ele deve implementar esse m�todo obrigat�rio que retorna a descri��o.
	{
		String retorno;
		
		retorno = "Images"; //O nome do filtro que aparecer� na hora de selecionar os arquivos.
		
		return retorno;
	}
}
