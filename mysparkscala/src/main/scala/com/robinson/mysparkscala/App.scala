package com.robinson.mysparkscala

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.functions._
import scala.collection.mutable._
import org.apache.spark.sql.Row
import org.apache.spark.sql.Encoders
import scala.{Any=>any}

/**
 * @author ${user.name}
 */
object App {

  def main(args: Array[String]) {
    val spark = SparkSession.builder().appName("The swankiest Spark app ever").master("local[*]").getOrCreate()

    val list= 1 to 10
    list.foreach(println)
    list.filter(_%2==0).map(_*2)
    list.filter(_%2==0).map(x=>x*3)
    println(list.mkString(","))
    val sc = spark.sparkContext
    val map1=spark.conf.getAll
    map1.foreach(x=>println(x._1+"\t"+x._2))
    val l = (0 to 100 by 5).toList
    val col = sc.parallelize(l)
    col.map(a=>Map(a->1))
    val smp = col.sample(true, 2)
    val colCount = col.count
    val smpCount = smp.count
    col.foreach(arg => print(arg + "\t"))
    println("\n============")
    col.map(arg=>arg+1).foreach(arg => print(arg + "\t"))
    println("\norig count = " + colCount)
    smp.foreach(arg => print(arg + "\t"))
    println("\nsampled count = " + smpCount)
    
    /*
    val fileType="com.databricks.spark.csv"
    val path="C:/tmp/school.csv2"
    readFile(fileType,path)
    */
    
    
    val format="jdbc"
    var map= Map[String,String]()
    map.put("url","jdbc:postgresql://gpv2devsegedw1.tmghealth.com:5433/devtmgdw_2")
    map.put("user","hma")
    map.put("password","Monday123456")
    map.put("partitionColumn","client_id")
    map.put("lowerBound","100")
    map.put("upperBound","500")
    map.put("numPartitions","4");
    //map+("dbtable"->"(select client_code, client_id,case when client_id>6020 then 'greater than 6020' else 'less or equals 6020'  end as compare from ws.client_data where client_id>6000) client_data_alias")
    //map.put("dbtable", "(select client_code, client_id from ws.client_data where client_id>6000) client_data_alias")
    map.put("dbtable", "ws.client_data")
    
    readDB(format,map)
    
    System.currentTimeMillis()
    
    /*
    val l1=List(1,2,3,4,5)
    folderTest(l1).foreach(println)
    
    */
  }
  
  
  def readFile(fileType:String,path:String){
    
    val spark=SparkSession.builder().appName("readFile").master("local[*]").getOrCreate()
    
    val df=spark.read.format(fileType).option("header", false).option("inferSchema", "true").option("delimiter","|").load(path)
    
    df.groupBy("_c3","_c2").agg(expr("count(distinct(_c2))"),expr("count(1)")).orderBy("_c3","_c2").show(100)
    
    
    
  }
  
  def g(x:Row)=Map(x->1)
  def mapFun[T, U](xs: List[T])(f: T => U): List[U] = 
    (xs foldRight List[U]())( f(_)::_ )
  
  def readDB(format:String,options:Map[String,String]){
    
    val spark=SparkSession.builder().appName("Spark JDBC reader").master("local[*]").getOrCreate()
    
    val df=spark.read.format(format).options(options).load()
    
    df.groupBy("client_id").agg(count("client_code"),countDistinct("client_code")).orderBy("client_id").show();
   /*
   df.select("client_id").filter("client_id>6020")
   .where("client_code in ('JHH','ULT','CPT')")
    */
    df.show(false)
    mapFun(List(1,2,3))(x => x * x).foreach(println)
    println("I am done")
    
  }
  
  def folderTest(numbers:List[Int])={
  //val numbers = List(5, 4, 8, 6, 2)
  //numbers.fo
  //numbers.fold(0){(z, i) =>  z + i}
  numbers.foldLeft(List[Int]()){(z,i)=>z:+i*2}
  
  }
}
