package com.au.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.model.TemporaryBatchConfig;



@Repository
public interface TemporaryBatchConfigRepository extends JpaRepository<TemporaryBatchConfig, Long>{

	@Query(value=" select"
			+ "       * "
			+ "    from "
			+ "        temporary_batch_config t "
			+ "    order by "
			+ "        t.created_date desc limit 1  ",nativeQuery = true)
	TemporaryBatchConfig getLatestBatch();

}
