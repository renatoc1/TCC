import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import com.renato.models.Usuario;

public class Teste {

	private static final int TAMANHO_LISTA = 2;

	public static void main(String[] args) throws IOException {
		
		try {
				
		String[] list = new String[TAMANHO_LISTA];
		Usuario usuario = new Usuario();
			
		//Lê arquivo
		FileInputStream fis = new FileInputStream("Teste2.txt");
		InputStreamReader isr = new InputStreamReader(fis);
		BufferedReader br = new BufferedReader(isr);
		
		String linha = br.readLine();
		
		//Percorrer todo o arquivo
		while (linha != null) {
			
			//Linha em branco
			if (!linha.isEmpty()) {
			
				int i;
				String dataFormato = "";
				
				//Percorre os 10 caracteres
				for (i = 0; i < linha.length(); i++) {
					if (i == 10) {
						dataFormato = linha.substring(0, i);
						break;
					}		
				}
				
				String terceiroTermo = linha.substring(2, 3);
				String sextotermo = linha.substring(5, 6);
				
				//Verifica se o terceiro e o sexto termo são "/"
				if (terceiroTermo.equals("/") && sextotermo.equals("/")) {
					
					String[] dt = dataFormato.split("/");
			        
			        String dtDia = dt[0];
			        String dtMes = dt[1];
			        String dtAno = dt[2];
			        
			        //Verifica se o formato da data está correto
		        	if(formatoCorreto(dtDia, dtMes, dtAno)) {
		                	                	
						salvarDados(usuario, linha);
		            		
		            } else {
		                usuario.setMensagem(usuario.getMensagem() + linha);
		            }
		        
				} else {
					usuario.setMensagem(usuario.getMensagem() + linha);
	                System.out.println(usuario.getMensagem());
	            }        	
		                
		        linha = br.readLine();
		        
			} else {
				linha = br.readLine();
			}
	        
		}
	    
		//Fecha o buffer
	    br.close();
	    
		} catch(Exception e) { 
			System.out.println(e.getMessage());
		}	
	}

	private static boolean formatoCorreto(String dtDia, String dtMes, String dtAno) {
		return dtDia.length() == 2 && dtMes.length() == 2 && dtAno.length() == 4;
	}

	private static void salvarDados(Usuario usuario, String linha) {
		String[] list;
		list = linha.split(" - ");
		usuario.setData(list[0]);
		System.out.println("Data: " + list[0]);
		usuario.setPessoa(list[1].split(": ")[0]);
		System.out.println("Pessoa: " + list[1].split(": ")[0]);
		usuario.setMensagem(list[1].split(": ")[1]);
		System.out.println("Mensagem: " + list[1].split(": ")[1]);
	}

}
