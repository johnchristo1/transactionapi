package com.bank.moneytransferapp.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.bank.moneytransferapp.entity.Transactions;

@Repository
public interface TransactionReportsRepository extends JpaRepository<Transactions, Long> {
	@Query(value = "select accountnumber from accountbalace u where u.accountnumber =:Anumber", nativeQuery = true)
	Integer getaccountnumber(@Param("Anumber") int Anumber);

	@Query(value = "select toaccountnumber from accounts u where u.toaccountnumber =:Bnumber", nativeQuery = true)
	Integer getreceiveraccountnumber(@Param("Bnumber") int Bnumber);

	@Transactional
	@Modifying
	@Query(value = "update accountbalace u set u.deposit=deposit-:newamount where u.accountnumber =:Fnumber", nativeQuery = true)
	Integer updatebalance(@Param("newamount") int newamount, @Param("Fnumber") int Fnumber);

	@Query(value = "select deposit from accountbalace u where u.accountnumber =:Cnumber", nativeQuery = true)
	Integer getdeposit(@Param("Cnumber") int Cnumber);

}
