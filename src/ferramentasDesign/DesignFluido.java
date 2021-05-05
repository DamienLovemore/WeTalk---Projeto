package ferramentasDesign;
import java.awt.Toolkit;

/**
 * M�todos est�ticos para obter informa��es, e realizar c�lculos sobre a resolu��o/tela atual em que o programa est� sendo exibido.
 * @author Matheus Soares Martins
 *
 */
public class DesignFluido
{
	/**
	 * Esse m�todo retorna a resolu��o da tela atual, em um vetor do tipo inteiro.
	 * @return Um vetor do tipo inteiro de duas posi��es, em que os valores retornados correspondem ao comprimento e a altura respectivamente. (Valores correspondem a pixels)
	 */
	public static int[] getResolucaoTela()
	{
		int[] retorno = new int[2];
		
		retorno[0] = Toolkit.getDefaultToolkit().getScreenSize().width;
		retorno[1] = Toolkit.getDefaultToolkit().getScreenSize().height;
		
		return retorno;
	}
	
	/**
	 * Esse m�todo retorna o comprimento da resolu��o atual.(Ex: Resolu��o 1360/720 retorna->1360)
	 * @return Um valor do tipo inteiro que se refere ao comprimento da resolu��o atual.
	 */
	public static int getComprimentoTela()
	{
		int retorno;
		
		retorno = Toolkit.getDefaultToolkit().getScreenSize().width;
		
		return retorno;
	}
	
	/**
	 * Esse m�todo retorna a altura da resolu��o atual.(Ex: Resolu��o 1360/720 retorna->720)
	 * @return Um valor do tipo inteiro que se refere a altura da resolu��o atual.
	 */
	public static int getAlturaTela()
	{
		int retorno;
		
		retorno = Toolkit.getDefaultToolkit().getScreenSize().height;
		
		return retorno;
	}
	
	/**
	 * Esse m�todo ele pega dois valores do tipo inteiro, que representam o comprimento e a altura da resolu��o espec�ficada, em seguida calcula a porcentagem que esses valores representam em rela��o a resolu��o da tela atual.
	 * @param valorComprimento O comprimento em pixels que se refere ao comprimento da resolu��o.(Aceita apenas valores do tipo inteiro)
	 * @param valorAltura A altura em pixels que se refere a altura da resolu��o.(Aceita apenas valores do tipo inteiro)
	 * @return Um vetor do tipo float de duas posi��es, em que as posi��es se referem a porcentagem do comprimento e da altura respectivamente.(Valores calculados com base nos par�metors informados)
	 */
	public static float[] calcularPorcentagemCorrespondente(int valorComprimento, int valorAltura)
	{
		float[] retorno = new float[2];
		
		retorno[0] = (valorComprimento * 100) / getComprimentoTela();
		retorno[1] = (valorAltura * 100) / getAlturaTela();
		
		return retorno;
	}
	
	/**
	 * Calcula a resolu��o correspondente as porcentagens informadas (Comprimento e Altura), com base na resolu��o da tela atual.
	 * @param porcentagemComprimento Um valor do tipo float que especifica a porcentagem do comprimento da tela que o m�todo dever� retornar.
	 * @param porcentagemAltura Um valor do tipo float que especifica a porcentagem da altura da tela que o m�todo dever� retornar.
	 * @return Retorna um vetor do tipo inteiro (resultados fracion�ros s�o arredondados) de duas posi��es que cont�m o comprimento e altura respectivamente, em rela��o aos valores passados como par�metros.
	 */
	public static int[] calcularResolucaoCorrespondente(float porcentagemComprimento, float porcentagemAltura)
	{
		int[] retorno = new int[2];
		
		retorno[0] = Math.round((porcentagemComprimento/100)*getComprimentoTela());
		retorno[1] = Math.round((porcentagemAltura/100)*getAlturaTela());
		
		return retorno;
	}
}
