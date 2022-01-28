package com.bank.moneytransferapp.consumer;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Properties;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.bank.moneytransferapp.entity.Messages;
import com.bank.moneytransferapp.entity.Transactions;
import com.bank.moneytransferapp.repository.TransactionReportsRepository;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class TransactionConsume {

	Logger logger = LoggerFactory.getLogger(TransactionConsume.class);
	@Autowired
	private TransactionReportsRepository validrepo;

	Messages allreports = new Messages();
	
/**
 * In this API, receiver account number, sender account number and requested amount from
 * the request sent by the user are compared to the data stored in the table,
 * if both the account numbers are matched and amount in the table is >= to the requested amount
 * then a response "valid" is sent to the bankAPI, otherwise "notvalid" is sent
 *  
 * */

	@KafkaListener(topics = "bank2")
	public void consume(String message) {

		JSONObject json = new JSONObject(message);
		String saccountno = json.get("AccountNumber").toString();
		int validaccno = Integer.parseInt(saccountno);

		String raccountno = json.get("RAccountNumber").toString();
		int validraccno = Integer.parseInt(raccountno);
		String jamount = json.get("balance").toString();
		int validamount = Integer.parseInt(jamount);

		int searchreceiveraccoutno = 0;
		int searchaccoutno = 0;
		int searchamount = 0;
		String report;

		try {

			searchaccoutno = validrepo.getaccountnumber(validaccno);
			searchreceiveraccoutno = validrepo.getreceiveraccountnumber(validraccno);
			searchamount = validrepo.getdeposit(validaccno);

		} catch (Exception e) {

			logger.error("Exception", e);

		}

		if ((validaccno == searchaccoutno) && (validraccno == searchreceiveraccoutno)
				&& (searchamount >= validamount)) {

			report = allreports.getValid();
			validrepo.updatebalance(validamount, searchaccoutno);
			transactionproducer(String.valueOf(searchaccoutno), String.valueOf(searchreceiveraccoutno), report,
					String.valueOf(validamount));

			Transactions transactions = new Transactions();
			transactions.setDeposit(validamount);
			transactions.setReceiveraccountnumber(validraccno);
			transactions.setSenderaccountnumber(searchaccoutno);
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			String Time = timestamp.toString();
			transactions.setTime(Time);
			transactions.setStatus(allreports.getDebited());
			validrepo.save(transactions);

		} else {
			report = allreports.getNotvalid();
			transactionproducer(String.valueOf(searchaccoutno), String.valueOf(searchreceiveraccoutno), report,
					String.valueOf(validamount));
		}

	}

	public void transactionproducer(String sac, String rac, String message, String validamount) {

		Properties properties = new Properties();

		try {
			properties.load(TransactionConsume.class.getClassLoader().getResourceAsStream("kafka.properties"));
			KafkaConsumer consumer = new KafkaConsumer<>(properties);
		} catch (IOException ex) {
			ex.printStackTrace();
			logger.error("IOException ", ex);
		}
		Producer<String, String> producer = new KafkaProducer<>(properties);

		try {
			producer.send(newRandomTransaction(sac, rac, message, validamount));
			Thread.sleep(100);

		} catch (InterruptedException e) {
			logger.error("InterruptedException", e);
		}

		producer.close();
	}

	public static ProducerRecord<String, String> newRandomTransaction(String accno, String rno, String report,
			String validamount) {
		// creates an empty json {}
		ObjectNode transactionr = JsonNodeFactory.instance.objectNode();

		Instant now = Instant.now();

		// we write the data to the json document
		transactionr.put("SenderAccountnumber", accno);
		transactionr.put("ReceiverAccountnumber", rno);
		transactionr.put("Report", report);
		transactionr.put("Amountb", validamount);
		transactionr.put("time", now.toString());
		return new ProducerRecord<>("bank3", "1", transactionr.toString());
	}

}
