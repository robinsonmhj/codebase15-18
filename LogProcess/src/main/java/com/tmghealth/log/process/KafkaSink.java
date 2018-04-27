package com.tmghealth.log.process;


import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.flume.Channel;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.Transaction;
import org.apache.flume.conf.Configurable;
import org.apache.flume.sink.AbstractSink;
import org.apache.log4j.Logger;

import com.tmghealth.Util.NetUtil;
import com.tmghealth.monitor.Sender;

public class KafkaSink extends AbstractSink implements Configurable {

	private static Logger log = Logger.getLogger(KafkaSink.class);

	private String hostName;
	private final int transactionThreshold = 5000;
	private Sender sender;

	@Override
	public void configure(Context context) {

		hostName = NetUtil.getHostName();
		sender=Sender.getInstance();

	}

	@Override
	public Status process() throws EventDeliveryException {
		Status status = null;
		String regex = ".*(\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{1,2}:\\d{1,2}.\\d{1,4}).*(TraceExecution|TraceAuthentication|TraceDDLReplay) <DRDAConnThread_([0-9]{1,})> tid=0x([0-9a-f]+).*((executing|Sending|Persisting|Authentication|CALL|alter|truncate|delete|update|insert|drop|select)\\s.*)";
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Channel ch = getChannel();
		Transaction txn = ch.getTransaction();
		Event event = null;
		txn.begin();
		try {
			int eventCount = 0;
			for (eventCount = 0; eventCount < transactionThreshold; eventCount++) {
				event = ch.take();
				if (event != null) {
					byte[] body = event.getBody();
					if (body.length >= 2048) {
						continue;
					}
					String line = new String(body, StandardCharsets.UTF_8);
					Matcher m = p.matcher(line);
					if (m.find()) {
						sender.send(hostName,line);
					}
				} else
					break;
			}

			txn.commit();

			if (eventCount < 1)
				return Status.BACKOFF;
			else
				return Status.READY;

		} catch (Exception e) {
			txn.rollback();
			status = Status.BACKOFF;
			log.info("", e);
		} finally {
			txn.close();
		}
		return status;

	}

	@Override
	public void start() {
		if (StringUtils.isEmpty(hostName)||"unknown".equalsIgnoreCase(hostName)) {
			log.error("hostName cannot be null");
			stop();
		}

		if(sender==null){
			log.error("sender cannot be null");
			stop();
		}
			
		
		final int timerInterval = 5;
		Thread t = new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(timerInterval * 1000);
					} catch (InterruptedException ex) {
						log.info("My god, I cannot sleep", ex);
					}
				}
			}
		});

		t.setDaemon(true);
		t.start();
	}

	@Override
	public void stop() {

	}

}
