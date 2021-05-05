package recursos;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * Utilizada para definir um filtro que aceita apenas arquivos que sejam um tipo de documento.
 * @author Matheus Soares Martins
 *
 */
public class DocumentFileFilter extends FileFilter //O import correto aqui é javax.swing.filechooser.FileFilter e não java.io.FileFilter;
{
	//Define os formatos de arquivos aceitos
	private final String[] okFileExtensions={"doc","docx","docm","dotx","dotm","dot","pdf","xps","mht","mhtml","htm","html","rtf","txt","xml","odt","xlsx","xlsm","xlsb","pptx","pptm","ppsx","ppsm","rar","zip","gz"};
	
	public boolean accept(File file)
	{
		//Isso apenas define as pastas como visíveis. Não faz com que diretórios e pastas possam ser reconhecidos como arquivos válidos. Para isso  tem o JFileChoose.setFileSelectionMode(***);
		if(file.isDirectory()) //Faz com que seja exibido também pastas, e diretórios além dos arquivos de imagem especificados acima. (Caso contrário não aparecerá nenhuma pasta, ou diretório. Só será possível pegar arquivos da pasta raiz)
		{
			return true;
		}
		
		for (String extension : okFileExtensions) //Cria uma variável local extension e percorre cada valor de okFileExtensions.
		{
			if(file.getName().toLowerCase().endsWith(extension)) //Pega o nome inteiro do arquivo incluido com a extensão (exemplo: mar.txt), e converte isso para tudo minúsculo. Após isso vê se o nome do arquivo termina com uma das extensões válidas.
			{
				return true;
			}
		}
		
		return false;
	}

	public String getDescription() //Pelo fato dele estar extendendo, ele deve implementar esse método obrigatório que retorna a descrição.
	{
		String retorno;
		
		retorno = "Documents"; //O nome do filtro que aparecerá na hora de selecionar os arquivos.
		
		return retorno;
	}
}
