package com.bank.moneytransferapp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "transactions")
public class Transactions {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "deposit")
	private int deposit;

	@Column(name = "senderaccountnumber")
	private int senderaccountnumber;

	@Column(name = "receiveraccountnumber")
	private int receiveraccountnumber;

	@Column(name = "status")
	private String status;

	@Column(name = "time")
	private String time;

	public Transactions() {
		super();
		// TODO Auto-generated constructor stub
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getDeposit() {
		return deposit;
	}

	public void setDeposit(int deposit) {
		this.deposit = deposit;
	}

	public int getSenderaccountnumber() {
		return senderaccountnumber;
	}

	public void setSenderaccountnumber(int senderaccountnumber) {
		this.senderaccountnumber = senderaccountnumber;
	}

	public int getReceiveraccountnumber() {
		return receiveraccountnumber;
	}

	public void setReceiveraccountnumber(int receiveraccountnumber) {
		this.receiveraccountnumber = receiveraccountnumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
