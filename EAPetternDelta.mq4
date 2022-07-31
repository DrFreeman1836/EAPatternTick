//+------------------------------------------------------------------+
//|                                                         frog.mq4 |
//|                        Copyright 2022, MetaQuotes Software Corp. |
//|                                             https://www.mql5.com |
//+------------------------------------------------------------------+
#property copyright "Copyright 2022, MetaQuotes Software Corp."
#property link      "https://www.mql5.com"
#property version   "1.00"
#property strict
 
input string URL="http://localhost/api/v1/ea";
input int checkTimeOnTick = 1000;
input int checkCountTick = 30;
input int deltaAsk = 2;
input int deltaBid = 2;
input bool checkRequest = true;

int countTick;
 
//+------------------------------------------------------------------+
//| Expert initialization function                                   |
//+------------------------------------------------------------------+
int OnInit()
{
  deltaAsk = deltaAsk * Point();
  deltaBid = deltaBid * Point();
//--- create timer
   EventSetTimer(checkInSecond);
//---
   return(INIT_SUCCEEDED);
}
//+------------------------------------------------------------------+
//| Expert deinitialization function                                 |
//+------------------------------------------------------------------+
void OnDeinit(const int reason)
{
//--- destroy timer
   EventKillTimer();
   
}
//+------------------------------------------------------------------+
//| Expert tick function                                             |
//+------------------------------------------------------------------+
void OnTick()
{

  postPrice(Ask, Bid);
  if(countTick < checkCountTick){
    countTick++;
  } else {
    getSignal();
  }
 
}
 
//+------------------------------------------------------------------+

//+------------------------------------------------------------------+
void createLine(color colors){

  string nameLine = TimeToString(TimeCurrent(), TIME_DATE | TIME_MINUTES);
  ObjectCreate(nameLine, OBJ_HLINE, 0, Bid);
  ObjectSetInteger(0, nameLine, OBJPROP_COLOR, colors);

}
//+------------------------------------------------------------------+
void poctPrice(double priceAsk, double priceBid){
if (!checkRequest) return;
int res=0;
   char post[];
   char result[];
   string headers;
   string url = URL
   + "&priceAsk=" + DoubleToString(NormalizeDouble(priceAsk, 5), 5)
   + "&priceBid=" + DoubleToString(NormalizeDouble(priceBid, 5), 5);
    for(int i = 0; i < 2; i++){
      int res=WebRequest("POST",url,NULL,NULL,5000,post,1000,result,headers);
      if(res == 200){
         return;
      } else () {
         Print("Ошибка в WebRequest. Код ошибки  =",GetLastError());
      }
    }
 
}
//+------------------------------------------------------------------+
void getSignal(){
if (!checkRequest) return;
int res=0;
   char post[];
   char result[];
   string headers;
   string url = URL
   + "/signal"
   + "&time=" + IntegerToString(checkTimeOnTick)
   + "&count=" + IntegerToString(checkCountTick)
   + "&delteAsk=" + DoubleToString(NormalizeDouble(deltaAsk, 5), 5)
   + "&deltaBid=" + DoubleToString(NormalizeDouble(deltaBid, 5), 5);
    for(int i = 0; i < 2; i++){
      int res=WebRequest("GET",url,NULL,NULL,5000,post,1000,result,headers);
      if(res == 200){//если прошла дельта по аску
         createLine(clrRed);
         return;
      }
      if(res == 201){//если прошла дельта по бид
        createLine(clrBlue);
        return;
      }
      if(res == 202){//если прошли обе дельты цен
        createLine(clrGold);
        return;
      }
      if(res == -1 || res == 500) {
         Print("Ошибка в WebRequest. Код ошибки  =",GetLastError());
      }
    }

}
//+------------------------------------------------------------------+