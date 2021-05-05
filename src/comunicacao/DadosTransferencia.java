package comunicacao;

import java.io.File;
import java.io.Serializable;

/**
 * Classe que armazena os dados a serem envios de uma concec��o a outra.
 * @author ???
 *
 */

@SuppressWarnings("serial")
public class DadosTransferencia implements Serializable
{
	
	private String enderecoIPRecebimento;
	private String mensagemUsuario;
	
	private File arquivoTransferenciaUsuario;
	private byte tipoEnvioMensagem=1;
	
	/**
	 * Cria a classe para receber os dados a serem enviados, com as configura��es especificadas
	 * @param alvoMensagem O endere�o IP para recebimento da mensagem
	 * @param mensagemEnvio A mensagem (texto) a ser enviada.
	 * @param arquivoEnvio O arquivo (caso n�o aja, deve ser passado null) para ser enviado.
	 * @param tipoMensagem O tipo de mensagem, se � uma mensagem simples texto, ou � para envio de arquivos.<br>
	 * 1 - Mensagem de texto comum (Apenas String, texto).<br>
	 * 2 - Mensagem para envio de arquivo.
	 */
	public DadosTransferencia(String alvoMensagem, String mensagemEnvio, File arquivoEnvio, byte tipoMensagem)
	{
		this.enderecoIPRecebimento = alvoMensagem;
		this.mensagemUsuario = mensagemEnvio;
		this.arquivoTransferenciaUsuario = arquivoEnvio;
		this.tipoEnvioMensagem = tipoMensagem;
	}
	
	public String getMensagemTexto()
	{
		String retorno = this.mensagemUsuario;
		
		return retorno;
	}
	
	public File getArquivoTransferido()
	{
		File retorno = this.arquivoTransferenciaUsuario;
		
		return retorno;
	}
	
	/**
	 * Obt�m o endere�o IP dessa conec��o. (Utilizado para o servidor saber para qual distinat�rio enviar uma mensagem)
	 * @return O endere�o IP dessa conec��o. (Socket)
	 */
	public String getEnderecoIPConeccao()
	{
		String retorno;
		
		retorno = this.enderecoIPRecebimento;
		
		return retorno;
	}
	
	public byte getTipoMensagemTransferir()
	{
		byte retorno = this.tipoEnvioMensagem;
		
		return retorno;
	}
	
	public String toString()
	{
		String retorno="";
		
		retorno += "Mensagem: "+this.getMensagemTexto()+" ";
		retorno += "Arquivo: "+this.getArquivoTransferido()+" ";
		retorno += "Tipo mensagem: "+this.getTipoMensagemTransferir()+" ";
		retorno += "Alvo da mensagem: "+this.getEnderecoIPConeccao();
		
		return retorno;
	}
}
