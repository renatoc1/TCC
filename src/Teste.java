import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;

import br.com.renato.models.Usuario;
import br.com.renato.util.JPAUtil;

public class Teste {

	private static final int TAMANHO_LISTA = 2;

	public static void main(String[] args) throws IOException {
		
		try {
						
		Usuario usuario = null;
		EntityManager em = new JPAUtil().getEntityManager();
		String terceiroTermo = "";
		String sextotermo = "";
			
		//Lê arquivo
		FileInputStream fis = new FileInputStream("Conversa1.txt");
		InputStreamReader isr = new InputStreamReader(fis);
		BufferedReader br = new BufferedReader(isr);
		
		String linha = br.readLine();
		
		//Percorrer todo o arquivo
		while (linha != null) {
			
			//Linha em branco
			
			if (validLinha(linha)) {
			
				int i;
				String dataFormato = "";
				
				//Percorre os 10 caracteres
				for (i = 0; i < linha.length(); i++) {
					if (i == 10) {
						dataFormato = linha.substring(0, i);
						break;
					}		
				}
				
				//Verifica se a frase contém mais de 6 caracteres
				if (linha.length() >= 6) {					
					terceiroTermo = linha.substring(2, 3);
					sextotermo = linha.substring(5, 6);
				} else {
					//terceiroTermo = linha.substring(2, 3);
					terceiroTermo = "";
					sextotermo = "";
				}
				
				//Verifica se o terceiro e o sexto termo são "/"
				if (terceiroTermo.equals("/") && sextotermo.equals("/")) {
					
					String[] dt = dataFormato.split("/");
			        
			        String dtDia = dt[0];
			        String dtMes = dt[1];
			        String dtAno = dt[2];
			        
			        //Verifica se o formato da data está correto
		        	if(formatoCorreto(dtDia, dtMes, dtAno)) {
		                	                	
		        		usuario = new Usuario();
						salvarDados(usuario, linha);
		            		
		            } else {
		                usuario.setMensagem(usuario.getMensagem() + linha);
		            }
		        
				} else {
					if (usuario != null) {
						usuario.setMensagem(usuario.getMensagem() + linha);
		                System.out.println(usuario.getMensagem());
					}
	            }        	
		                
		        linha = br.readLine();
		        
			} else {
				linha = br.readLine();
			}
	        
			em.getTransaction().begin();
	        em.persist(usuario);
	        em.getTransaction().commit();
	        
		}
	           
		//Fecha o buffer
	    br.close();
	    em.close();
	    
		} catch(Exception e) { 
			System.out.println(e.getMessage());
		}	
	}

	private static boolean validLinha(String linha) {
		String linhaStr = linha.trim();
		if (linhaStr.isEmpty()) {
			return false;
		}
		return true;
	}

	private static boolean formatoCorreto(String dtDia, String dtMes, String dtAno) {
		return dtDia.length() == 2 && dtMes.length() == 2 && dtAno.length() == 4;
	}

	private static void salvarDados(Usuario usuario, String linha) {
		
		int i = 0, inicio = 0;
		String frase = "", frasePessoa = "", fraseMensagem = "";
		Boolean flag = false;
		String[] list = new String[TAMANHO_LISTA];
		list = linha.split(" - ");
		frase = list[1].toString();
		
		usuario.setData(list[0]);
		System.out.println("Data: " + list[0]);
		
		
		
		if (list[1].contains(": ")) {			
			usuario.setPessoa(list[1].split(": ")[0]);
			System.out.println("Pessoa: " + list[1].split(": ")[0]);			
			usuario.setMensagem(list[1].split(": ")[1]);
			System.out.println("Mensagem: " + list[1].split(": ")[1]);
		} else {					
			Pattern p = Pattern.compile("saiu");
			Matcher m = p.matcher(frase);
			while (m.find()) {
				inicio = m.start();
				System.out.println(m.start() + " " + m.group() +" "+ m.end());
				flag = true;
			}
			if (flag) {			
				//Percorre a String e busca a palavra "saiu"
				if (inicio != 0) {
					for (i = 2; i < inicio - 2; i++) {
						frasePessoa += frase.charAt(i);
						usuario.setPessoa(frasePessoa);
						System.out.println(frasePessoa);
						
					}
					for (i = inicio; i < frase.length(); i++) {
						fraseMensagem += frase.charAt(i);
						usuario.setMensagem(fraseMensagem);
						System.out.println(fraseMensagem);
					}
				}			
			} else {
				Pattern p2 = Pattern.compile("entrou");
				Matcher m2 = p2.matcher(frase);
				while (m2.find()) {
					inicio = m2.start();
					System.out.println(m2.start() + " " + m2.group() +" "+ m2.end());
					flag = false;
				}
				//Percorre a String e busca a palavra "entrou"
				if (inicio != 0) {
					for (i = 2; i < inicio - 1; i++) {
						frasePessoa += frase.charAt(i);
						usuario.setPessoa(frasePessoa);
						System.out.println(frasePessoa);
						
					}
					for (i = inicio; i < frase.length(); i++) {
						fraseMensagem += frase.charAt(i);
						usuario.setMensagem(fraseMensagem);
						System.out.println(fraseMensagem);
					}
				}			
			}
		}		
	}
		
}
