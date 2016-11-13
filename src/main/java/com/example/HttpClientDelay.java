package com.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class HttpClientDelay {

	private String host;
	private Integer port;
	private String path;
	private Integer delayMillis;
	private String json;

	@Autowired
	public HttpClientDelay(HttpClientDelayConfiguration config) {
		host = config.getHost();
		port = config.getPort();
		path = config.getPath();
		delayMillis = config.getDelayMillis();
		json = config.getJson();
	}

	public void run(String... args) throws Exception {

		try (Socket socket = new Socket(host, port);
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(socket.getInputStream()));
				BufferedWriter writer = new WriterWithLog(
						new OutputStreamWriter(socket.getOutputStream()));) {

			log.info("### Request start");
			writer.write("POST " + path + " HTTP/1.1\r\n");
			writer.write("Host: " + host + ":" + port + "\r\n");
			writer.write("Content-Type: application/json\r\n");
			writer.write("Content-Length:" + json.getBytes(StandardCharsets.UTF_8).length
					+ "\r\n");
			writer.write("\r\n");
			writer.flush();

			TimeUnit.MILLISECONDS.sleep(delayMillis);

			writer.write(json +"\r\n");
			writer.write("\r\n");
			writer.flush();
			log.info("### Request end");

			log.info("### Response start");
			while (true) {
				String line = reader.readLine();
				if (line == null) {
					break;
				}
				log.info(line);
				// Transfer-Encoding: chunked の場合、コンテンツの終わりは"0"で示される
				// 本来はコンテンツ内容の"0"かどうかを判断する必要があるが省略
				if (line.equals("0")) {
					break;
				}
			}
			log.info("### Response end");

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Slf4j
	public static class WriterWithLog extends BufferedWriter {

		public WriterWithLog(Writer out) {
			super(out);
		}
		
		@Override
		public void write(String str) throws IOException {
			log.info(str);
			super.write(str);
		}

		
	}
}
