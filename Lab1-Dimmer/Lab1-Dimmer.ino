void setup() {
  // put your setup code here, to run once:
  pinMode(LED_BUILTIN, OUTPUT);
  int duty = 5;
  int freq = 100;
  int deltaDuty = -5;
  int deltaFreq = 10;
}
void dimmer(int freq, int duty){
  int period. onTime, offTime;
  period = 1000/freq;               //delay() is in milliseconds
  onTime=period*duty/100;           //how to pretend duty is a fraction
  offTime = period - onTime;        //all time is accounted for(none lost to rounding)
  digitalWrite(LED_BUILTIN, HIGH);  // turn the LED on (HIGH is the voltage level)
  delay(onTime);                    // wait for the interval
  digitalWrite(LED_BUILTIN,LOW);    // turn the LED off by making the voltage LOW
  delay(offTime);                   // wait for 10ms
}
void loop() {
  // put your main code here, to run repeatedly:
  dimmer(freq, duty);
  if(duty==95 || duty==5){
    deltaDuty*=-1;
  }
  duty+=deltaDuty;
  if(freq==100 || freq == 0){
    deltaFreq*=-1;
  }
  freq+=deltaFreq;

}
