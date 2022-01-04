package com.bank.moneytransferapp.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bank.moneytransferapp.entity.TransactionReport;


public interface TransactionReportsRepository extends JpaRepository <TransactionReport, Long>{
	@Query(value="select accountnumber from bankbalace u where u.accountnumber =:Anumber", nativeQuery=true)
	Integer getaccountnumber(@Param("Anumber") int Anumber);
	

	@Query(value="select senderaccountno from addaccounts u where u.senderaccountno =:Bnumber", nativeQuery=true)
	Integer getreceiveraccountnumber(@Param("Bnumber") int Bnumber);
	
	@Transactional
	@Modifying
	@Query(value="update bankbalace u set u.balance=balance-:newamount where u.accountnumber =:Fnumber", nativeQuery=true)
	Integer updatebalance(@Param("newamount") int newamount,@Param("Fnumber") int Fnumber);
	
	@Query(value="select balance from bankbalace u where u.accountnumber =:Cnumber", nativeQuery=true)
	Integer getdeposit(@Param("Cnumber") int Cnumber);

}
