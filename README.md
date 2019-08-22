# Custom-CountDownTimer
Android Custom CountDownTimer

=========================

Based in "CountDownTimerWithPause" created by Gautier Hayoun ( https://github.com/Gautier )

---

Example of usage:

```
private CustomCountDownTimer countDownTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       countDownTimer = new CustomCountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                System.out.println("CountDownTimerWithPause : onTick(" + millisUntilFinished+ ")");
            }

            @Override
            public void onFinish() {
                System.out.println("CountDownTimerWithPause : onFinish()");
             }
        };



        findViewById(R.id.btnStartResume).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(countDownTimer.isPaused()){
                    countDownTimer.resume();
                }else {
                    countDownTimer.create();
                }

            }
        });

        findViewById(R.id.btnPause).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                countDownTimer.pause();

                System.out.println("totalCountdown(): "  + countDownTimer.totalCountdown());
                System.out.println("elapsedTime(): "  + countDownTimer.elapsedTime());
                System.out.println("hasBeenStarted(): "  + countDownTimer.hasBeenStarted());
                System.out.println("timeLeft(): "  + countDownTimer.timeLeft());


            }
        });

}
```
