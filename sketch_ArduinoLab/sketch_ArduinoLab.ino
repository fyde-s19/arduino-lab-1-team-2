// Lab 1 - timed blink
// Sai, Amitesh, Fawaz, Rishabh
// Team 2

void setup() {
  // put your setup code here, to run once:
  pinMode(LED_BUILTIN, OUTPUT);
}

void timedBlink(int interval){
  digitalWrite(LED_BUILTIN, HIGH);
  delay(interval*1000);
  digitalWrite(LED_BUILTIN, LOW);
  delay(1000);
}
void loop() {
  // put your main code here, to run repeatedly:
  timedBlink(1); //varies how long the LED is on in seconds
  //timedBlink(2);
  //timedBlink(3);
}
