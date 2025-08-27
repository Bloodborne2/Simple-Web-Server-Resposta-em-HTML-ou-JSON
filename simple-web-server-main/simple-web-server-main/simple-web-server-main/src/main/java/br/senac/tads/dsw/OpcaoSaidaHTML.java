package br.senac.tads.dsw;

public class OpcaoSaidaHTML implements OpcaoSaida {

	@Override
	public String gerarSaida(String nome, String email) {
		String safeNome = escapeHtml(nome);
		String safeEmail = escapeHtml(email);
		String titulo = "Dados do Usuário";

		return """
           <!DOCTYPE html>
           <html lang="pt-br">
           <head>
             <meta charset="UTF-8" />
             <meta name="viewport" content="width=device-width, initial-scale=1.0" />
             <title>""" + titulo + """
             </title>
             <style>
               body{font-family:Arial, Helvetica, sans-serif; margin:2rem;}
               .card{max-width:520px; border:1px solid #ddd; border-radius:12px; padding:16px;}
               .label{color:#555; font-size:0.9rem;}
               .value{font-size:1.1rem;}
             </style>
           </head>
           <body>
             <h1>""" + titulo + """
             </h1>
             <div class="card">
               <p class="label">Nome completo</p>
               <p class="value">""" + safeNome + """
               </p>
               <p class="label">E-mail</p>
               <p class="value">""" + safeEmail + """
               </p>
             </div>
           </body>
           </html>
           """;
	}


	private String escapeHtml(String s) {
		if (s == null) return "";
		StringBuilder out = new StringBuilder();
		for (char c : s.toCharArray()) {
			switch (c) {
				case '<' -> out.append("&lt;");
				case '>' -> out.append("&gt;");
				case '&' -> out.append("&amp;");
				case '"' -> out.append("&quot;");
				case '\'' -> out.append("&#39;");
				default -> out.append(c);
			}
		}
		return out.toString();
	}
}
