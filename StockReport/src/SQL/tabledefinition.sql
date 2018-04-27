create database finance;





drop table if exists finance.company;
create table finance.company(
	id bigint NOT NULL AUTO_INCREMENT primary key,
	symbol varchar(10) not null,
	company_name varchar(50),
	address1 varchar(100),
	address2 varchar(50),
	country varchar(20),
	phone varchar(20),
	website varchar(50),
	sector varchar(20),
	industry varchar(50)
	);
	

  
  
  drop table if exists finance.history;
  create table finance.history(
  	company_id bigint not null,
  	stock_date date not null,
  	openPrice decimal(10,6),
  	high decimal(10,6),
  	low decimal(10,6),
  	adj decimal(10,6),
  	close decimal(10,6),
  	volume bigint,
  	primary key (company_id,stock_date),
  	foreign key history_fk(company_id) references company(id) on delete cascade
  );
  
  

  
drop table if exists finance.transaction;
create table finance.transaction(
  	company_id bigint not null,
	ask decimal(10,6),
  	ask_size bigint,
  	avgVolume bigint,
  	bid decimal,
  	bid_size bigint,
  	change_price decimal(10,6),
  	changeFromAvg200 decimal(10,6),
  	changeFromAvg200InPercent decimal(10,2),
  	changeFromAvg50 decimal(10,6),
  	changeFromAvg50InPercent decimal(10,2),
  	changeFromYearHigh decimal(10,6),
  	changeFromYearHighInPercent decimal(10,2),
  	changeFromYearLow decimal(10,6),
  	changeFromYearLowInPercent decimal(10,2),
  	changeInPercent decimal(10,2),
  	dayHigh decimal(10,6),
  	dayLow decimal(10,6),
  	lastTradeSize bigint,
  	lastTradeTime datetime,
  	price decimal,
  	priceAvg200 decimal(10,6),
  	priceAvg50 decimal(10,6),
  	insertTime datetime,
  	primary key (company_id,insertTime),
  	foreign key history_fk(company_id) references company(id) on delete cascade
  );
  
  
  
  	
-Djava.util.logging.config.file=logging.properties
	
	

LOAD DATA INFILE 'C://Users//hma//workspace//StockReport//company.sql' INTO TABLE company 
  FIELDS TERMINATED BY '||' 
  (symbol,company_name,address1,address2,country,phone,website,sector,industry);
  
0.

LOAD DATA INFILE 'C://Users//hma//workspace//StockReport//historyData.sql' INTO TABLE history
   FIELDS TERMINATED BY ',' ;
  
  
  /*get the most change prices company*/
  select c.symbol,abs(a.open-a.close) as diff from(
	select company_id,sum(openPrice) as open,sum(close) as close from history 
	where stock_date>='2014-07-01' and stock_date<='2014-09-30' group by company_id
	
	) a join company c on c.id=a.company_id order by diff desc limit 10;
	
	
/*find the top sector*/
	select sector, sum(percent)/datediff('2014-06-30','2014-01-01') as percent from (
	select sector,id,stock_date,abs(openPrice*100-close*100)/(openPrice*100) as percent from (
		select c.id,c.sector,h.openPrice,h.close,h.stock_date from company c join history h on c.id=h.company_id
		where h.stock_date>='2014-01-01' and h.stock_date<='2014-06-30'
		) a
	) b group  by sector having percent>0.3 order by percent;
	
	
	
	
select c.sector, count(1) as count from company c join 
	(
		select company_id,sum(percent) as percent from (
			select company_id,stock_date,abs(openPrice*100-close*100)/(openPrice*100) as percent from history
			where stock_date>='2014-01-01' and stock_date<='2014-06-30'
		) h group by company_id having percent>0.3
	) h2
on c.id=h2.company_id group by c.sector order by count desc limit 3;
*/





find the top 3 companies which has the most transactions
select c.symbol,h.company_id,sum(h.volume) as volume,c.symbol from history h join company c 
on h.company_id=c.id 
group by company_id order by volume desc limit 3;





find the higest price during some time

select c.symbol,h.close from (
select company_id,max(close) close from history group by company_id order by close desc limit 3) h
join company c on c.id=h.company_id;



find the hiest price of a compnay during some time 

select max(h.close) from history h join company c on h.company_id=c.id where symbol='JD';	