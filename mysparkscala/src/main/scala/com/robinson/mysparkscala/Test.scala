package com.robinson.mysparkscala

object Test {
    def main(args:Array[String]){
    
    mapTest
    
    
  }
  
  
  def mapTest(){
    
    var map= Map(1->"abc",2->20,3->(1 to 10 toList))
    
    map.foreach({
      case (a:Int,b:String)=>println("Int,String")
      case (a:Int,b:Int)=>println("Int,Int")
      case (a:Int,b:List[_])=>println("Int,List")
      })
    
  }
  
}