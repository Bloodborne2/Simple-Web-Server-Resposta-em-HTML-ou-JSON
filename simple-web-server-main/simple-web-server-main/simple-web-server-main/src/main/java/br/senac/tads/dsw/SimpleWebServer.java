package br.senac.tads.dsw;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleWebServer {

	private final int port;
	private static final int THREAD_POOL_SIZE = 10;

	public SimpleWebServer(int port) {
		this.port = port;
	}

	public void start() throws IOException {
		ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
		try (ServerSocket serverSocket = new ServerSocket(port)) {
			System.out.println("Server started na porta " + port);
			while (true) {
				Socket clientSocket = serverSocket.accept();
				threadPool.execute(() -> handleClient(clientSocket));
			}
		}
	}

	private void handleClient(Socket clientSocket) {
		try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			 BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {

			String requestLine = in.readLine();
			if (requestLine == null || requestLine.isEmpty()) {
				clientSocket.close();
				return;
			}

			String line;
			String acceptHeader = "text/html"; // padrão
			while ((line = in.readLine()) != null && !line.isEmpty()) {
				if (line.toLowerCase().startsWith("accept:")) {
					acceptHeader = line.substring(7).trim().toLowerCase();
				}
			}

			String[] requestParts = requestLine.split(" ");
			if (requestParts.length < 2) {
				sendError(out, "400 Bad Request", "Request inválida");
				return;
			}

			String path = requestParts[1];
			String query = "";
			if (path.contains("?")) {
				String[] pathParts = path.split("\\?", 2);
				query = pathParts[1];
			}

			String nome = "";
			String email = "";
			if (!query.isEmpty()) {
				String[] params = query.split("&");
				for (String param : params) {
					String[] kv = param.split("=", 2);
					if (kv.length == 2) {
						String key = kv[0].toLowerCase();
						String value = decodeUrl(kv[1]);
						if ("nome".equals(key)) {
							nome = value;
						} else if ("email".equals(key)) {
							email = value;
						}
					}
				}
			}

			OpcaoSaida opcaoSaida;
			String contentType;
			if (acceptHeader.contains("application/json")) {
				opcaoSaida = new OpcaoSaidaJSON();
				contentType = "application/json; charset=UTF-8";
			} else {
				opcaoSaida = new OpcaoSaidaHTML();
				contentType = "text/html; charset=UTF-8";
			}

			String responseBody = opcaoSaida.gerarSaida(nome, email);

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
			ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);

			out.write("HTTP/1.1 200 OK\r\n");
			out.write("Date: " + formatter.format(now) + "\r\n");
			out.write("Server: SimpleWebServer\r\n");
			out.write("Content-Type: " + contentType + "\r\n");
			out.write("Content-Length: " + responseBody.getBytes("UTF-8").length + "\r\n");
			out.write("\r\n");
			out.write(responseBody);

		} catch (IOException e) {
			System.err.println("Error handling client: " + e.getMessage());
		} finally {
			if (clientSocket != null) try {
				clientSocket.close();
			} catch (IOException ignored) {}
		}
	}

	private void sendError(BufferedWriter out, String status, String message) throws IOException {
		String body = "<html><body><h1>" + status + "</h1><p>" + message + "</p></body></html>";
		out.write("HTTP/1.1 " + status + "\r\n");
		out.write("Content-Type: text/html; charset=UTF-8\r\n");
		out.write("Content-Length: " + body.getBytes("UTF-8").length + "\r\n");
		out.write("\r\n");
		out.write(body);
	}

	private String decodeUrl(String s) {
		try {
			return java.net.URLDecoder.decode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return s;
		}
	}

	public static void main(String[] args) throws IOException {
		int port = 8080;
		SimpleWebServer server = new SimpleWebServer(port);
		server.start();
	}
}
