package com.bank.moneytransferapp.consumer;

import java.time.Instant;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.bank.moneytransferapp.repository.TransactionReportsRepository;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class TransactionConsume {
	@Autowired
    private TransactionReportsRepository validrepo;
    
	@KafkaListener(topics = "bank2")
    public void consume(String message) {
       
        
        
        JSONObject json = new JSONObject(message);
        System.out.println(json.get("AccountNumber").toString());
        
        
        String saccountno = json.get("AccountNumber").toString();

        int validaccno=Integer.parseInt(saccountno);
        
        int searchaccno=0;

        
        String raccountno = json.get("RAccountNumber").toString();

        int validraccno=Integer.parseInt(raccountno);
        
        int searchraccno=0;
        
        
        String jamount = json.get("balance").toString();

        int validamount=Integer.parseInt(jamount);
        
        int samount=0;
        
        
        String report;
        
        try {
        	
        	searchaccno=validrepo.getaccountnumber(validaccno);
        	searchraccno=validrepo.getreceiveraccountnumber(validraccno);
        	samount=validrepo.getdeposit(validaccno);
        }
        catch(Exception e) {
        	
        	System.out.println("Not Matched"+e.getMessage());
        }
   
        System.out.println("Accno"+searchaccno);
        System.out.println("RAccno"+searchraccno);
        System.out.println("deposit"+samount);
        System.out.println("validamount"+validamount);
        
   
        if(validaccno==searchaccno && searchraccno==validraccno && samount >= validamount) {

        	report="valid";
            transactionproducer(String.valueOf(searchaccno),String.valueOf(searchraccno),report,String.valueOf(validamount));
        	
        }
        else {        	
        	report="notvalid";        	
        	transactionproducer(String.valueOf(searchaccno),String.valueOf(searchraccno),report,String.valueOf(validamount));
       	        }
        System.out.println("Message: "+report);
       
	}
	
	public void transactionproducer(String sac,String rac,String message,String validamount) {

		Properties properties = new Properties();

        // kafka bootstrap server
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // producer acks
        properties.setProperty(ProducerConfig.ACKS_CONFIG, "all"); // strongest producing guarantee
        properties.setProperty(ProducerConfig.RETRIES_CONFIG, "3");
        properties.setProperty(ProducerConfig.LINGER_MS_CONFIG, "1");
        // leverage idempotent producer from Kafka 0.11 !
        properties.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true"); // ensure we don't push duplicates

        Producer<String, String> producer = new KafkaProducer<>(properties);


            try {
                producer.send(newRandomTransaction(sac,rac,message,validamount));
                Thread.sleep(100);


            } catch (InterruptedException e) {

            }

        producer.close();
    }

    public static ProducerRecord<String, String> newRandomTransaction(String accno,String rno,String report,String validamount) {
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
