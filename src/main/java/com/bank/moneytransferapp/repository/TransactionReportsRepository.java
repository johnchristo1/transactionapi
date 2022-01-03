package com.bank.moneytransferapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bank.moneytransferapp.entity.TransactionReport;


public interface TransactionReportsRepository extends JpaRepository <TransactionReport, Long>{
	@Query(value="select accountnumber from bankbalance u where u.accountnumber =:Anumber", nativeQuery=true)
	Integer getaccountnumber(@Param("Anumber") int Anumber);
	

	@Query(value="select senderaccountnumber from bankbalance u where u.senderaccountnumber =:Bnumber", nativeQuery=true)
	Integer getreceiveraccountnumber(@Param("Bnumber") int Bnumber);
	
	@Query(value="select balance from bankbalance u where u.accountnumber =:Cnumber", nativeQuery=true)
	Integer getdeposit(@Param("Cnumber") int Cnumber);

}
