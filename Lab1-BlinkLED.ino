void setup() {
  // put your setup code here, to run once:
  pinMode(LED_BUILTIN, OUTPUT);
}
void timedBlink(int interval){
  digitalWrite(LED_BUILTIN, HIGH);
  delay(interval*1000);
  digitalWrite(LED_BUILTIN, LOW);
  delay(interval*1000);
  
}
void loop() {
  // put your main code here, to run repeatedly:
  timedBlink(1);
}
