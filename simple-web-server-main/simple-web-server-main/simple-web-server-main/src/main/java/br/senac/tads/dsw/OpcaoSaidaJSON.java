package br.senac.tads.dsw;

public class OpcaoSaidaJSON implements OpcaoSaida {

	@Override
	public String gerarSaida(String nome, String email) {
		String safeNome = escapeJson(nome);
		String safeEmail = escapeJson(email);
		return "{\n" +
			"  \"nomeCompleto\": \"" + safeNome + "\",\n" +
			"  \"email\": \"" + safeEmail + "\"\n" +
			"}";
	}

	private String escapeJson(String s) {
		if (s == null) return "";
		StringBuilder out = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			switch (c) {
				case '\"' -> out.append("\\\"");
				case '\\' -> out.append("\\\\");
				case '\b' -> out.append("\\b");
				case '\f' -> out.append("\\f");
				case '\n' -> out.append("\\n");
				case '\r' -> out.append("\\r");
				case '\t' -> out.append("\\t");
				default -> {
					if (c < 0x20) {
						out.append(String.format("\\u%04x", (int) c));
					} else {
						out.append(c);
					}
				}
			}
		}
		return out.toString();
	}
}
