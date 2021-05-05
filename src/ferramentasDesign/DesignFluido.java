package ferramentasDesign;
import java.awt.Toolkit;

/**
 * Métodos estáticos para obter informações, e realizar cálculos sobre a resolução/tela atual em que o programa está sendo exibido.
 * @author Matheus Soares Martins
 *
 */
public class DesignFluido
{
	/**
	 * Esse método retorna a resolução da tela atual, em um vetor do tipo inteiro.
	 * @return Um vetor do tipo inteiro de duas posições, em que os valores retornados correspondem ao comprimento e a altura respectivamente. (Valores correspondem a pixels)
	 */
	public static int[] getResolucaoTela()
	{
		int[] retorno = new int[2];
		
		retorno[0] = Toolkit.getDefaultToolkit().getScreenSize().width;
		retorno[1] = Toolkit.getDefaultToolkit().getScreenSize().height;
		
		return retorno;
	}
	
	/**
	 * Esse método retorna o comprimento da resolução atual.(Ex: Resolução 1360/720 retorna->1360)
	 * @return Um valor do tipo inteiro que se refere ao comprimento da resolução atual.
	 */
	public static int getComprimentoTela()
	{
		int retorno;
		
		retorno = Toolkit.getDefaultToolkit().getScreenSize().width;
		
		return retorno;
	}
	
	/**
	 * Esse método retorna a altura da resolução atual.(Ex: Resolução 1360/720 retorna->720)
	 * @return Um valor do tipo inteiro que se refere a altura da resolução atual.
	 */
	public static int getAlturaTela()
	{
		int retorno;
		
		retorno = Toolkit.getDefaultToolkit().getScreenSize().height;
		
		return retorno;
	}
	
	/**
	 * Esse método ele pega dois valores do tipo inteiro, que representam o comprimento e a altura da resolução específicada, em seguida calcula a porcentagem que esses valores representam em relação a resolução da tela atual.
	 * @param valorComprimento O comprimento em pixels que se refere ao comprimento da resolução.(Aceita apenas valores do tipo inteiro)
	 * @param valorAltura A altura em pixels que se refere a altura da resolução.(Aceita apenas valores do tipo inteiro)
	 * @return Um vetor do tipo float de duas posições, em que as posições se referem a porcentagem do comprimento e da altura respectivamente.(Valores calculados com base nos parâmetors informados)
	 */
	public static float[] calcularPorcentagemCorrespondente(int valorComprimento, int valorAltura)
	{
		float[] retorno = new float[2];
		
		retorno[0] = (valorComprimento * 100) / getComprimentoTela();
		retorno[1] = (valorAltura * 100) / getAlturaTela();
		
		return retorno;
	}
	
	/**
	 * Calcula a resolução correspondente as porcentagens informadas (Comprimento e Altura), com base na resolução da tela atual.
	 * @param porcentagemComprimento Um valor do tipo float que especifica a porcentagem do comprimento da tela que o método deverá retornar.
	 * @param porcentagemAltura Um valor do tipo float que especifica a porcentagem da altura da tela que o método deverá retornar.
	 * @return Retorna um vetor do tipo inteiro (resultados fracionáros são arredondados) de duas posições que contém o comprimento e altura respectivamente, em relação aos valores passados como parâmetros.
	 */
	public static int[] calcularResolucaoCorrespondente(float porcentagemComprimento, float porcentagemAltura)
	{
		int[] retorno = new int[2];
		
		retorno[0] = Math.round((porcentagemComprimento/100)*getComprimentoTela());
		retorno[1] = Math.round((porcentagemAltura/100)*getAlturaTela());
		
		return retorno;
	}
}
