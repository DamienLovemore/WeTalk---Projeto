package gui;

import java.io.File;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.security.CodeSource;
import javax.swing.JFrame;

import recursos.ApplicationInfo;

public class Aplicacao
{
	public static void main(String[] args)
	{
		GUI aplicacao = new GUI();
				
		
		File jarFile = null;
		try 
		{
			CodeSource codeSource = Aplicacao.class.getProtectionDomain().getCodeSource();
			jarFile = new File(codeSource.getLocation().toURI().getPath());
		}
		
		catch (URISyntaxException e)
		{
			e.printStackTrace();
		}
		String jarDir = jarFile.getParentFile().getPath();
		ApplicationInfo.applicationDownloadsFolder = jarDir+"\\WeTalk - Downloads"; //Define o diret�rio para salvar os arquivos enviados
		System.out.println("Caminho onde os arquivos transferidos ser�o baixados: "+ApplicationInfo.applicationDownloadsFolder);
		
		String enderecoIPPrograma;
		try
		{
			enderecoIPPrograma=InetAddress.getLocalHost().getHostAddress();
		}
		
		catch (UnknownHostException e)
		{
			enderecoIPPrograma = "127.0.0.3";
		}
		ApplicationInfo.enderecoIPUsuario = enderecoIPPrograma;
		System.out.println("Endere�o IP do Cliente que est� executando o programa: "+ApplicationInfo.enderecoIPUsuario);		
				
		aplicacao.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
