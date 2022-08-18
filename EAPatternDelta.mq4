
//+------------------------------------------------------------------+
//|                                                         frog.mq4 |
//|                        Copyright 2022, MetaQuotes Software Corp. |
//|                                             https://www.mql5.com |
//+------------------------------------------------------------------+
#property copyright "Copyright 2022, MetaQuotes Software Corp."
#property link      "https://www.mql5.com"
#property version   "1.00"
#property strict

extern int checkTimeOnTick = 10000000000000;
extern int checkCountTick = 30;

extern int deltaMaxAsk = 2;
extern int deltaMinAsk = 2;

extern int deltaMaxBid = 2;
extern int deltaMinBid = 2;
extern bool checkRequest = true;

extern bool checkMarket = true;
extern int stopLoss = 300;
extern int takeProfit = 100;
extern double = 0.1;
extern int deltaOrder = 0;
extern int magic = 777;

string URL="http://localhost/api/v1/ea";

int countTick;

//+------------------------------------------------------------------+
//| Expert initialization function                                   |
//+------------------------------------------------------------------+
int OnInit()
{
  deltaMaxAsk = deltaMaxAsk * Point();
  deltaMinAsk = deltaMinAsk * Point();

  deltaMaxBid = deltaMaxBid * Point();
  deltaMinBid = deltaMinBid * Point();
//--- create timer

//---
   return(INIT_SUCCEEDED);
}
//+------------------------------------------------------------------+
//| Expert deinitialization function                                 |
//+------------------------------------------------------------------+
void OnDeinit(const int reason)
{
//--- destroy timer

}
//+------------------------------------------------------------------+
//| Expert tick function                                             |
//+------------------------------------------------------------------+
void OnTick()
{

//  if(checkMarket){
//    //
//  }

  postPrice(Ask, Bid);
  if(countTick < checkCountTick){
    countTick++;
  } else {
    getSignal();
  }

}

//+------------------------------------------------------------------+
void createCross(color colors){

  string nameLine = TimeToString(TimeCurrent(), TIME_DATE | TIME_MINUTES | TIME_SECONDS);
  nameLine += " ";
  nameLine += colors;
  ObjectCreate(nameLine, OBJ_ARROW, 0, TimeCurrent(), Bid);
  ObjectSetInteger(0,nameLine,OBJPROP_ARROWCODE,251);// установим код стрелки
  ObjectSetInteger(0, nameLine, OBJPROP_COLOR, colors);

}
//+------------------------------------------------------------------+
void createLine(color colors){

  string nameLine = TimeToString(TimeCurrent(), TIME_DATE | TIME_MINUTES);
  ObjectCreate(nameLine, OBJ_HLINE, 0, 0, Bid);
  ObjectSetInteger(0, nameLine, OBJPROP_COLOR, colors);

}
//+------------------------------------------------------------------+
void postPrice(double priceAsk, double priceBid){
if (!checkRequest) return;
int res=0;
   char post[];
   char result[];
   string headers;
   string url = URL
   + "?priceAsk=" + DoubleToString(NormalizeDouble(priceAsk, 5), 5)
   + "&priceBid=" + DoubleToString(NormalizeDouble(priceBid, 5), 5);
    for(int i = 0; i < 2; i++){
      int res=WebRequest("POST",url,NULL,NULL,5000,post,1000,result,headers);
      if(res == 200){
         return;
      } else {
         Print("Ошибка в WebRequest. Код ошибки1  =",GetLastError());
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
   + "?time=" + IntegerToString(checkTimeOnTick)
   + "&count=" + IntegerToString(checkCountTick)
   + "&deltaMaxAsk=" + DoubleToString(NormalizeDouble(deltaMaxAsk, 5), 5)
   + "&deltaMinAsk=" + DoubleToString(NormalizeDouble(deltaMinAsk, 5), 5)
   + "&deltaMaxBid=" + DoubleToString(NormalizeDouble(deltaMaxBid, 5), 5)
   + "&deltaMinBid=" + DoubleToString(NormalizeDouble(deltaMinBid, 5), 5);
    for(int i = 0; i < 2; i++){
      int res=WebRequest("GET",url,NULL,NULL,1000,post,1000,result,headers);
      if(res == 200){//если прошла дельта по аску
         openOrder(1);//продажа
         createCross(clrRed);
         return;
      }
      if(res == 201){//если прошла дельта по бид
        openOrder(0);//покупка
        createCross(clrBlue);
        return;
      }
      if(res == 202){//если прошли обе дельты цен
        createCross(clrGold);
        return;
      }
      if(res == -1) {
         Print("Ошибка в WebRequest. Код ошибки2  =",GetLastError());
      }
      if(res == 500){
        Print("Ошибка сервера");
      }
      if(res == 400){
        Print("Выборка слишком мала");
        return;
      }
    }

}
//+------------------------------------------------------------------+
void openOrder(int cmd){// 0 - buy  1 - sell

    if(!checkMarket) return;
    if(!checkDeltaOrder(cmd)) return;

    double price = cmd == 1 ? Bid : Ask;
    color clr = cmd == 1 ? clrRed : clrBlue;
    double sl = cmd == 1 ? Bid + stopLoss * Point : Bid - stopLoss * Point;
    double tp = cmd == 1 ? Bid - takeProfit * Point : Bid + takeProfit * Point;
    OrderSend(Symbol(), cmd, lot, price, 5, sl, tp, "", magic, 0, clr);

}
//+------------------------------------------------------------------+
bool checkDeltaOrder(int cmd){//косяк

        if(OrderSelect(OrdersTotal(), SELECT_BY_POS, MODE_TRADES)){
            if(OrderType() == cmd && MathAbs(OrderOpenPrice() - Bid) < deltaOrder * Point){
                return false;
            } else {
                return true;
            }
        }

        return false;

}